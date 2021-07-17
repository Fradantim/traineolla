package com.frager.oreport.udemyconnector.config;

import java.nio.charset.Charset;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

	@Bean("udemy-client-webclient-builder")
	public WebClient.Builder getWebClientBuilder(@Value("${udemy.client.id:}") String user,
			@Value("${udemy.client.secret:}") String pass, @Value("${udemy.client.auth-header:}") String authHeader) {
		
		if (authHeader == null || authHeader.isEmpty()) {
			byte[] encodedAuth = Base64.getEncoder().encode((user + ":" + pass).getBytes(Charset.forName("US-ASCII")));
			authHeader = "Basic " + new String(encodedAuth);
		}
		
		HttpHeaders httpHeadersForUdemy = new HttpHeaders();
		httpHeadersForUdemy.add("Authorization", authHeader);
		return WebClient.builder().defaultHeaders(defaultHeaders -> defaultHeaders.addAll(httpHeadersForUdemy));
	}
}
