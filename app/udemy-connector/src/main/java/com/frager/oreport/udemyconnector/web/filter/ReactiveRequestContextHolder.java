package com.frager.oreport.udemyconnector.web.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;

import reactor.core.publisher.Mono;

/**
 * Al 2021-08-21 spring-boot-starter-webflux:2.5.2 no tiene un contexto del cual
 * recuperar informacion del request que ingreso, como si lo tiene
 * spring-boot-starter-web. Esta clase Holder intenta recueperarlo desde el
 * contexto cargado por el filtro {@link ReactiveRequestContextFilter}.
 */
public class ReactiveRequestContextHolder {
	static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;

	private ReactiveRequestContextHolder() {
		super();
	}

	public static Mono<ServerHttpRequest> getRequest() {
		return Mono.deferContextual(Mono::just).map(ctx -> ctx.get(CONTEXT_KEY));
	}
}
