package com.frager.oreport.core.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@PropertySource("classpath:core.properties")
@Configuration
public class OpenApiConfig {

	@ConditionalOnProperty("springdoc.api-docs.enabled")
	@Bean
	public OpenAPI openApiDef(@Value("${application.name:missingAppName}") String appName,
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
