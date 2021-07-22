package com.frager.oreport.udemyconnector.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import reactor.core.publisher.Mono;

@SpringBootTest
@TestPropertySource("classpath:secrets.mock.properties")
class WebConfigTest {

	private ExchangeFilterFunction assertHasAuthHeader = (clientRequest, nextFilter) -> {
		// evaluo que el header "Authorization" se encuentre cargado
		assertNotNull(clientRequest.headers().get("Authorization"));
		assertFalse(clientRequest.headers().get("Authorization").isEmpty());
		return nextFilter.exchange(clientRequest);
	};

	@Test
	void checkUdemyWebClientHasAuthHeaders(@Autowired WebClient.Builder defaultWebClientBuilder,
			@Value("${udemy.client.id:}") String user, @Value("${udemy.client.secret:}") String pass,
			@Value("${udemy.client.auth-header:}") String authHeader) {
		WebClient wc = new WebConfig().buildUdemyWebClient(defaultWebClientBuilder, user, pass, authHeader,
				Arrays.asList(assertHasAuthHeader));
		Mono<Object> resMono=wc.get().uri("0.0.0.0").retrieve().bodyToMono(Object.class);
		assertThrows(WebClientRequestException.class, () -> {
			resMono.block();
		});
	}
}
