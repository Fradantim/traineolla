package com.frager.oreport.batchcoursesnapshot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

	@Bean
	public WebClient buildUdemyWebClient(@Autowired WebClient.Builder defaultWebClientBuilder) {
		return defaultWebClientBuilder.build();
	}
}
