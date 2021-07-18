package com.frager.oreport.udemyconnector.utls;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class URLUtils {

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
		try {
			for (String queryArg : url.getQuery().split("&")) {
				String[] queryKeyVal = URLDecoder.decode(queryArg, Charset.defaultCharset().name()).split("=");

				if (queryKeyVal.length == 2) {
					queryParams.add(queryKeyVal[0], queryKeyVal[1]);
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Se intento decodear una url-query con charset erroneo?: {}", uri);
			return null;
		}

		return queryParams;
	}
}
