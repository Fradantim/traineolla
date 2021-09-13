package com.frager.oreport.udemyconnector.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.frager.oreport.udemyconnector.web.filter.ReactiveRequestContextFilter;
import com.frager.oreport.udemyconnector.web.filter.ReactiveRequestContextHolder;

import reactor.core.publisher.Mono;

public class URLUtils {

	private URLUtils() {
		super();
	}

	private static final Logger logger = LoggerFactory.getLogger(URLUtils.class);

	/**
	 * Retorna un mapa multi valor con los argumentos de consulta de la url. En caso
	 * de error retorna null.
	 */
	public static MultiValueMap<String, String> getQueryParamsFromURL(@Nullable String uri) {
		if (uri == null)
			return null;

		URL url = null;
		try {
			url = new URL(uri);
		} catch (MalformedURLException e) {
			logger.error("Se intento obtener url-query de una url malformada: {}", uri);
			return null;
		}

		if (url.getQuery() == null || url.getQuery().isEmpty())
			return null;

		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		for (String queryArg : url.getQuery().split("&")) {
			List<String> queryKeyVal = Arrays.asList(queryArg.split("=")).stream().map(kv -> {
				try {
					return URLDecoder.decode(kv, Charset.defaultCharset().name());
				} catch (UnsupportedEncodingException e) {
					// no deberia suceder
					logger.error("Se intento decodear una url-query con charset erroneo?: {}", uri);
				}
				return "";
			}).collect(Collectors.toList());

			if (queryKeyVal.size() == 2) {
				queryParams.add(queryKeyVal.get(0), queryKeyVal.get(1));
			}
		}

		return queryParams;
	}

	
	/**
	 * Retorna una url basada en la misma url con la que se ingreso al servicio y
	 * con los query args ingresados por parametro. <br/>
	 * <b>IMPORTANTE</b>: Este metodo solo funciona en contexto web-reactivos que
	 * hayan pasado por el filtro {@link ReactiveRequestContextFilter}.
	 */
	public static Mono<String> getURLFromQueryParams(String preAppend, String url){
		return getURLFromQueryParams(preAppend, getQueryParamsFromURL(url));
	}
	
	/**
	 * Retorna una url basada en la misma url con la que se ingreso al servicio y
	 * con los query args ingresados por parametro. <br/>
	 * <b>IMPORTANTE</b>: Este metodo solo funciona en contexto web-reactivos que
	 * hayan pasado por el filtro {@link ReactiveRequestContextFilter}.
	 */
	public static Mono<String> getURLFromQueryParams(String preAppend,
			@Nullable MultiValueMap<String, String> queryArgs) {
		return ReactiveRequestContextHolder.getRequest().map(serverHttpRequest -> {
			String url = serverHttpRequest.getURI().toString().split("\\?")[0];
			if (queryArgs == null || queryArgs.isEmpty())
				return url;

			MultiValueMap<String, String> newQueryArgs = new LinkedMultiValueMap<>();
			queryArgs.forEach((k, v) -> newQueryArgs.put(preAppend + k, v));

			return UriComponentsBuilder.fromUriString(url).queryParams(newQueryArgs).toUriString();
		});
	}
}
