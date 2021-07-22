package com.frager.oreport.udemyconnector.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

	/**
	 * Construye un {@link WebClient} especificamente para llamados hacia Udemy con
	 * el builder default de Spring. <br />
	 * Esto permite trabajar con <a href=
	 * "https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-web-applications.spring-webflux.auto-configuration">spring-webflux.auto-configuration</a>.
	 */
	@Bean("udemy-webclient")
	public WebClient buildUdemyWebClient(@Autowired WebClient.Builder defaultWebClientBuilder,
			@Value("${udemy.client.id:}") String user, @Value("${udemy.client.secret:}") String pass,
			@Value("${udemy.client.auth-header:}") String authHeader,
			@Nullable Collection<ExchangeFilterFunction> filters) {

		if (authHeader == null || authHeader.isEmpty()) {
			byte[] encodedAuth = Base64.getEncoder().encode((user + ":" + pass).getBytes(StandardCharsets.US_ASCII));
			authHeader = "Basic " + new String(encodedAuth);
		}

		HttpHeaders httpHeadersForUdemy = new HttpHeaders();
		httpHeadersForUdemy.add("Authorization", authHeader);

		WebClient.Builder newBuilder = defaultWebClientBuilder.clone();
		newBuilder.defaultHeaders(defaultHeaders -> defaultHeaders.addAll(httpHeadersForUdemy));

		if (filters != null && !filters.isEmpty()) {
			filters.forEach(newBuilder::filter);
		}

		return newBuilder.build();
	}
}
