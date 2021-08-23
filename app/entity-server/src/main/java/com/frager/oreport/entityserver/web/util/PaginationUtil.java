package com.frager.oreport.entityserver.web.util;

import java.text.MessageFormat;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.web.util.UriComponentsBuilder;

import com.frager.oreport.entityserver.web.filter.ReactiveRequestContextFilter;
import com.frager.oreport.entityserver.web.filter.ReactiveRequestContextHolder;

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
	public static Mono<HttpHeaders> generatePaginationHttpHeadersInReactiveWebContext(Page<?> page) {
		return ReactiveRequestContextHolder.getRequest()
				.map(serverHttpRequest -> generatePaginationHttpHeaders(serverHttpRequest, page));
	}

	public static HttpHeaders generatePaginationHttpHeaders(HttpRequest request, Page<?> page) {
		return generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page);
	}

	public static HttpHeaders generatePaginationHttpHeaders(UriComponentsBuilder uriBuilder, Page<?> page) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HEADER_X_TOTAL_COUNT, Long.toString(page.getTotalElements()));
		int pageNumber = page.getNumber();
		int pageSize = page.getSize();
		StringBuilder link = new StringBuilder();
		if (pageNumber < page.getTotalPages() - 1) {
			link.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next")).append(",");
		}
		if (pageNumber > 0) {
			link.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev")).append(",");
		}
		link.append(prepareLink(uriBuilder, page.getTotalPages() - 1, pageSize, "last")).append(",")
				.append(prepareLink(uriBuilder, 0, pageSize, "first"));
		headers.add(HttpHeaders.LINK, link.toString());
		return headers;
	}

	private static String prepareLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
		return MessageFormat.format(HEADER_LINK_FORMAT, preparePageUri(uriBuilder, pageNumber, pageSize), relType);
	}

	private static String preparePageUri(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize) {
		return uriBuilder.replaceQueryParam("page", Integer.toString(pageNumber))
				.replaceQueryParam("size", Integer.toString(pageSize)).toUriString().replace(",", "%2C")
				.replace(";", "%3B");
	}
}
