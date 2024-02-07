package com.nano.custcom.cron;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nano.custcom.service.AWSUserOnboardingMessagesAdapter;

@Component
//@AllArgsConstructor
public class CommerceUserOnBoardingMessageReaderCron {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(CommerceUserOnBoardingMessageReaderCron.class);
	
	@Autowired
	private AWSUserOnboardingMessagesAdapter sendReceiveMessages;
	
	@Scheduled(fixedRate = 5000)
	public void extractUserCreationMessages() {
		sendReceiveMessages.processNewUserCreationMessages();
		logger.debug("The time is now {}", new Date());
	}
}
