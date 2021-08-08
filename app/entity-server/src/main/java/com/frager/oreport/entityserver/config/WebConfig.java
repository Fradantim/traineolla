package com.frager.oreport.entityserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;

@Configuration
public class WebConfig {

	/** Bean que me ayuda a traducir nro pagina y cantidad en {@link Pageable} */
	@Bean
	public HandlerMethodArgumentResolver reactivePageableHandlerMethodArgumentResolver() {
		return new ReactivePageableHandlerMethodArgumentResolver();
	}

	/** Bean que me ayuda a traducir columna y ordenamiento en {@link Pageable} */
	@Bean
	public HandlerMethodArgumentResolver reactiveSortHandlerMethodArgumentResolver() {
		return new ReactiveSortHandlerMethodArgumentResolver();
	}
}
