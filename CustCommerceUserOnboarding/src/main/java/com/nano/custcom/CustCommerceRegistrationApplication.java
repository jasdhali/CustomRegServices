package com.nano.custcom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
public class CustCommerceRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustCommerceRegistrationApplication.class, args);
	}

}
