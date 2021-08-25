package com.frager.oreport.entityserver.web.util;

import java.util.Collections;
import java.util.function.Function;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.frager.oreport.entityserver.error.NotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ResponseEntityUtil {

	private ResponseEntityUtil() {
		super();
	}

	/**
	 * <b>IMPORTANTE</b>: Este metodo solo funciona en contexto web-reactivos.
	 * 
	 * @see PaginationUtil#generatePaginationHttpHeadersInReactiveWebContext(org.springframework.data.domain.Page)
	 */
	public static <S> Mono<ResponseEntity<Flux<S>>> okPagedStream(Pageable pageable, Mono<Long> countPublisher,
			Function<Pageable, Flux<S>> pagedElementsPublisher) {
		return countPublisher.map(total -> new PageImpl<>(Collections.emptyList(), pageable, total))
				.flatMap(PaginationUtil::generatePaginationHttpHeadersInReactiveWebContext)
				.map(headers -> ResponseEntity.ok().headers(headers).body(pagedElementsPublisher.apply(pageable)));
	}

	public static <S> ResponseEntity<S> created(S body, String url, Object path) {
		if (url.charAt(url.length() - 1) != '/')
			url = url + '/';

		return ResponseEntity.created(UriComponentsBuilder.fromUriString(url).path(path.toString()).build().toUri())
				.body(body);
	}

	public static <S> Mono<ResponseEntity<S>> wrapOrNotFound(String maybeErrorMessage, Mono<S> maybeResponse) {
		return wrapOrNotFound(maybeErrorMessage, maybeResponse, null);
	}

	public static <S> Mono<ResponseEntity<S>> wrapOrNotFound(String maybeErrorMessage, Mono<S> maybeResponse,
			HttpHeaders headers) {
		return maybeResponse.switchIfEmpty(Mono.error(new NotFoundException(maybeErrorMessage)))
				.map(response -> ResponseEntity.ok().headers(headers).body(response));
	}
}
