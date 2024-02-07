package com.javaguy.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;

@Configuration
public class GatewayConfig {
	@Autowired
	@Lazy
	private EurekaClient eurekaClient;
	
	@PostConstruct
	public void identifyServicesDetails() {
		Applications applications =  eurekaClient.getApplications();
		StringBuilder stringBuilderAppInfo = new StringBuilder();
		for(Application application : applications.getRegisteredApplications()) {
			String appName = application.getName();
			stringBuilderAppInfo.append("Application : " + appName );
			List<InstanceInfo> instanceInfos = application.getInstances();
			for(InstanceInfo instanceInfo : instanceInfos) {
				stringBuilderAppInfo.append("\n	# of Instances : " + instanceInfos.size() );
				stringBuilderAppInfo.append("\n	Healthcheck URL  : " + instanceInfo.getHealthCheckUrl() );
				stringBuilderAppInfo.append("\n	Instance Id  : " + instanceInfo.getInstanceId() );
				stringBuilderAppInfo.append("\n	HostName  : " + instanceInfo.getHostName() );
				stringBuilderAppInfo.append("\n	Port  : " + instanceInfo.getPort() );				
				}
			stringBuilderAppInfo.append("\n");
		}
		System.out.println( stringBuilderAppInfo.toString() );
	}
	
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()				
				.route("first-service", r -> r.path("/first/**")
						.and().method("GET")
						.uri("http://localhost:9081"))
				.route("authentication-service",r -> r.path("/api/v1/admin")
						.and()
						.method("GET")
						.uri("http://localhost:9088/authservice/api/v1/admin"))
				.build();
	}
}