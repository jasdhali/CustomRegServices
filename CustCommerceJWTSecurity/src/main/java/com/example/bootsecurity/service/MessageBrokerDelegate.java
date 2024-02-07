package com.example.bootsecurity.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.bootsecurity.dto.AuditLogRequestDTO;
import com.example.bootsecurity.model.ApplicationUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Component
public class MessageBrokerDelegate {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(MessageBrokerDelegate.class);

	@Value("${aws.commerce.entiry.create.queuename:Message.fifo}")
	private String queueName;

	@Value("${aws.commerce.queue.audit}")
	private String auditQueue;

	@Value("${aws.commerce.entiry.create.queueurl}")
	private String queueurl;

	@Autowired
	private ObjectMapper objectMapper;

	private SqsClient getClient() {
		return SqsClient.builder().region(Region.US_EAST_1).credentialsProvider(ProfileCredentialsProvider.create())
				.build();
	}

	public void userSignedUp(ApplicationUser user) {

		Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
		MessageAttributeValue attributeValue = MessageAttributeValue.builder().stringValue("" + user.getId())
				.dataType("String").build();
		messageAttributes.put("Id", attributeValue);

		SendMessageRequest sendMsgRequest = null;
		try {
			sendMsgRequest = SendMessageRequest.builder().queueUrl(queueurl).messageAttributes(messageAttributes)
					.messageGroupId("baeldung-group-1").messageDeduplicationId("" + user.getId())
					.messageBody(objectMapper.writeValueAsString(user)).build();
		} catch (JsonProcessingException sqsException) {			 
			logger.error("Error encountered while processing object to json conversion. Message {}", sqsException.getMessage() );
		}
		catch (SqsException sqsException) {
			logger.error("Error encountered while building. Message {}", sqsException.getMessage() );
		}
		getClient().sendMessage(sendMsgRequest);
	}

	public void createAuditMessage(AuditLogRequestDTO auditLogRequestDTO) {
		 
		SqsClient sqsClient = getClient();
		try {
			Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
			MessageAttributeValue attributeValue = MessageAttributeValue.builder().stringValue(""+UUID.randomUUID())
					.dataType("String").build();			
			messageAttributes.put("Id", attributeValue);
			
			GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder().queueName(auditQueue).build();
			String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

			SendMessageRequest messageRequest = SendMessageRequest.builder().messageAttributes(messageAttributes).queueUrl(queueUrl)
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
}
