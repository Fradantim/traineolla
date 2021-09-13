package com.frager.oreport.udemyconnector.web.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * Al 2021-08-21 spring-boot-starter-webflux:2.5.2 no tiene un contexto del cual
 * recuperar informacion del request que ingreso, como si lo tiene
 * spring-boot-starter-web. Este filtro ayuda a cargar dicha instancia,
 * recuperable a traves de {@link ReactiveRequestContextHolder#getRequest()}.
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveRequestContextFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		return chain.filter(exchange).contextWrite(ctx -> ctx.put(ReactiveRequestContextHolder.CONTEXT_KEY, request));
	}
}
