package com.Insightgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(servers = { @Server(url = "/", description = "Default Server URL") })
@EnableDiscoveryClient
public class InsightgramMessagingService {

	public static void main(String[] args) {
		SpringApplication.run(InsightgramMessagingService.class, args);
	}

}
