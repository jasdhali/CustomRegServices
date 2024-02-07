package com.nano.custcom.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nano.custcom.AuditActions;
import com.nano.custcom.dto.AppUserDTO;
import com.nano.custcom.dto.AuditLogRequestDTO;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Component
@Profile(value = {"aws","nondocker"})
public class AWSUserOnboardingMessagesAdapter {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(AWSUserOnboardingMessagesAdapter.class);
	@Value("${aws.commerce.entiry.create.queuename:Message.fifo}")
	private String queueName;

	@Value("${aws.commerce.entiry.create.queueurl}")
	private String queueurl;
	
	@Value("${aws.commerce.queue.audit}")
	private String auditQueueName;	

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private AccountService accountService;
	
	private SqsClient getClient() {
		return SqsClient.builder().region(Region.US_EAST_1).credentialsProvider(ProfileCredentialsProvider.create())
				.build();
	}
	
	/**
	 * Processes all messages that have been created by new user creation API/UI
	 * Using that data this CRON reader creates downstream data.  
	*/	
	public void processNewUserCreationMessages() {
		ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder().queueUrl(queueurl)
				.maxNumberOfMessages(10).build();
		ReceiveMessageResponse messageResponse = getClient().receiveMessage(receiveMessageRequest);

		List<Message> sqsMessages = messageResponse.messages();

		sqsMessages.forEach(message -> {
			Map<MessageSystemAttributeName, String> attributesMap = message.attributes();
			AppUserDTO appUserDTO = null;
			try {
				
				appUserDTO = objectMapper.readValue(message.body(), AppUserDTO.class);
				List<AuditLogRequestDTO> auditLogRequestDTOs = accountService.createAccountProfile(appUserDTO);
				
				auditLogRequestDTOs.stream().forEachOrdered(
						auditLogIns -> createAuditMessage(auditLogIns)
						);
				
				DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder().queueUrl(queueurl)
						.receiptHandle(message.receiptHandle()).build();
				getClient().deleteMessage(deleteMessageRequest);
			} catch (JsonProcessingException e) {
				logger.error("Error while converting sqs message to object {}", e.getMessage());
			} catch (Exception e) {
				logger.error("Error while either saving or deleting sqs message : {}", e.getMessage());
			}

			System.out.println("       Attribute Size  : " + attributesMap.size());
			if (attributesMap.size() > 0) {
				attributesMap.forEach((k, v) -> {
					logger.info("   Attribute Name : " + k + " - Attribute Value : " + v);
				});
			}
		});
	}
	
	public void createAuditMessage(AuditLogRequestDTO auditLogRequestDTO) {
		 
		SqsClient sqsClient = getClient();
		try {
			Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
			MessageAttributeValue attributeValue = MessageAttributeValue.builder().stringValue(""+UUID.randomUUID())
					.dataType("String").build();			
			messageAttributes.put("Id", attributeValue);
			
			GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder().queueName(auditQueueName).build();
			String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

			SendMessageRequest messageRequest = 
						SendMessageRequest.builder()
						.messageAttributes(messageAttributes)
						.queueUrl(queueUrl)
					.messageGroupId("audit-message-group")
					.messageBody(objectMapper.writeValueAsString(auditLogRequestDTO))
					.messageDeduplicationId(""+UUID.randomUUID())
					.build();

			sqsClient.sendMessage(messageRequest);

		} 
		catch (SqsException | JsonProcessingException sqsException) {
			logger.error("Error encountered while building. Message {}", sqsException.getMessage() );
		}
	}
	
	private AuditLogRequestDTO getAuditlogInstance(String artifactName,
			AuditActions action, int userId,int artifactId) {
		AuditLogRequestDTO auditLogRequest = AuditLogRequestDTO.builder()				
				.userId(userId)
				.accountId(null)
				.artifactId( artifactId )
				.artifactName( artifactName )
				.actionPerformed(action.name())
				.build();
		return auditLogRequest;
	}
}
