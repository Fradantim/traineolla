package com.frager.oreport.udemyconnector.web.util;

import java.text.MessageFormat;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.frager.oreport.udemyconnector.utils.URLUtils;
import com.frager.oreport.udemyconnector.web.filter.ReactiveRequestContextFilter;
import com.frager.oreport.udemyconnector.web.filter.ReactiveRequestContextHolder;
import com.udemy.model.PageResponse;

import reactor.core.publisher.Mono;

/**
 * Clase de utilidad para armar informacion de paginacion en base a los
 * principios de <a href="https://developer.github.com/v3/#pagination">GitHub
 * API</a>, y la <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link
 * header)</a>.
 */
public final class PaginationUtil {

	private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";
	private static final String HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\"";

	private PaginationUtil() {
		super();
	}

	/**
	 * <b>IMPORTANTE</b>: Este metodo solo funciona en contexto web-reactivos que
	 * hayan pasado por el filtro {@link ReactiveRequestContextFilter}.
	 */
	public static Mono<HttpHeaders> generatePaginationHttpHeadersInReactiveWebContext(String preAppend,
			PageResponse<?> page) {
		return ReactiveRequestContextHolder.getRequest()
				.map(serverHttpRequest -> generatePaginationHttpHeaders(preAppend, serverHttpRequest, page));
	}

	public static HttpHeaders generatePaginationHttpHeaders(String preAppend, HttpRequest request,
			PageResponse<?> page) {
		return generatePaginationHttpHeaders(preAppend, UriComponentsBuilder.fromHttpRequest(request), page);
	}

	public static HttpHeaders generatePaginationHttpHeaders(String preAppend, UriComponentsBuilder uriBuilder,
			PageResponse<?> page) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HEADER_X_TOTAL_COUNT, Long.toString(page.getCount()));
		StringBuilder link = new StringBuilder();
		if (page.getNext() != null) {
			link.append(prepareLink(page.getNext(), "next")).append(",");
		}
		if (page.getPrevious() != null) {
			link.append(prepareLink(page.getPrevious(), "prev")).append(",");
		}
		link.append(prepareLink(getFirstPage(preAppend, uriBuilder, page), "first"));

		headers.add(HttpHeaders.LINK, link.toString());
		return headers;
	}

	private static String prepareLink(String url, String relType) {
		return MessageFormat.format(HEADER_LINK_FORMAT, preparePageUri(url), relType);
	}

	private static String preparePageUri(String url) {
		return url.replace(",", "%2C").replace(";", "%3B");
	}

	private static String getFirstPage(String preAppend, UriComponentsBuilder uriBuilder, PageResponse<?> page) {
		if (page.getPrevious() == null && page.getNext() == null)
			return uriBuilder.toUriString();

		String anyPage = page.getPrevious() != null ? page.getPrevious() : page.getNext();

		if (!anyPage.contains("?"))
			return anyPage;

		MultiValueMap<String, String> queryParams = URLUtils.getQueryParamsFromURL(anyPage);
		queryParams.remove(preAppend + "page");

		return UriComponentsBuilder.fromHttpUrl(anyPage.split("\\?")[0]).queryParams(queryParams).toUriString();
	}
}
