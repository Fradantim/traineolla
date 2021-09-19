package com.frager.oreport.udemyconnector.service.impl;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import com.frager.oreport.udemyconnector.utils.URLUtils;
import com.udemy.model.PageResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class UdemyService {

	private static final Logger logger = LoggerFactory.getLogger(UdemyService.class);

	@Value("${request.udemy.params.identifier}")
	private String udemyRequestParamIdentifier;

	/**
	 * En caso de que <code>nextPageInfo</code> indique que hay una siguiente pagina
	 * este metodo retornar un {@link Flux} con dichos elementos, o un {@link Flux}
	 * vacio en caso contrario.
	 */
	protected <S> Flux<S> getNextPage(@Nullable String nextPageUrl,
			Function<MultiValueMap<String, String>, Flux<S>> pageQueryFunction) {
		MultiValueMap<String, String> nextPageInfo = URLUtils.getQueryParamsFromURL(nextPageUrl);

		if (nextPageInfo != null && !nextPageInfo.isEmpty()) {
			logger.debug("La consulta previa informa de una siguiente pagina: query-params:{}", nextPageInfo);
			return pageQueryFunction.apply(nextPageInfo);
		}

		return Flux.empty();
	}

	/**
	 * Transforma las url de la respuesta paginada en una url de este mismo
	 * servicio.
	 */
	protected <S> Mono<PageResponse<S>> translateUrls(PageResponse<S> pageResponse) {
		return Mono.just(pageResponse.getPrevious() != null)
				.flatMap(hasPrevious -> hasPrevious
						? URLUtils.getURLFromQueryParams(udemyRequestParamIdentifier, pageResponse.getPrevious())
						: Mono.empty())
				.defaultIfEmpty("").map(previous -> {
					if (!previous.isEmpty())
						pageResponse.setPrevious(previous);
					return pageResponse.getNext() != null;
				})
				.flatMap(hasNext -> hasNext
						? URLUtils.getURLFromQueryParams(udemyRequestParamIdentifier, pageResponse.getNext())
						: Mono.empty())
				.defaultIfEmpty("").map(next -> {
					if (!next.isEmpty())
						pageResponse.setNext(next);
					return pageResponse;
				});
	}
}
