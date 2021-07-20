package com.frager.oreport.udemyconnector.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

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
			@Value("${udemy.client.auth-header:}") String authHeader) {

		if (authHeader == null || authHeader.isEmpty()) {
			byte[] encodedAuth = Base64.getEncoder().encode((user + ":" + pass).getBytes(StandardCharsets.US_ASCII));
			authHeader = "Basic " + new String(encodedAuth);
		}

		HttpHeaders httpHeadersForUdemy = new HttpHeaders();
		httpHeadersForUdemy.add("Authorization", authHeader);
		return defaultWebClientBuilder.clone()
				.defaultHeaders(defaultHeaders -> defaultHeaders.addAll(httpHeadersForUdemy)).build();
	}

	@ConditionalOnProperty("springdoc.api-docs.enabled")
	@Bean
	public OpenAPI springShopOpenAPI(@Value("${application.name:missingAppName}") String appName,
			@Value("${build.version:?.?.?}") String buildVer,
			@Value("${springdoc.api-docs.title:missingTitle}") String title,
			@Value("#{${springdoc.api-docs.licence:{}}}") Map<String, String> license,
			@Value("#{${springdoc.api-docs.ext-docs:{}}}") Map<String, String> externalDocs) {

		Info info = new Info().title(appName).description(title).version("v" + buildVer);
		if (license != null && !license.isEmpty()) {
			info.setLicense(new License().name(license.get("name")).url(license.get("url")));
		}
		OpenAPI def = new OpenAPI().info(info);
		if (externalDocs != null && !externalDocs.isEmpty()) {
			def.externalDocs(new ExternalDocumentation().description(externalDocs.get("description"))
					.url(externalDocs.get("url")));
		}
		return def;
	}
}
