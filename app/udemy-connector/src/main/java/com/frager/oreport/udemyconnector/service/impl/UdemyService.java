package com.frager.oreport.udemyconnector.service.impl;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import com.frager.oreport.udemyconnector.utils.URLUtils;

import reactor.core.publisher.Flux;

public abstract class UdemyService {

	private static final Logger logger = LoggerFactory.getLogger(UdemyService.class);

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
}
