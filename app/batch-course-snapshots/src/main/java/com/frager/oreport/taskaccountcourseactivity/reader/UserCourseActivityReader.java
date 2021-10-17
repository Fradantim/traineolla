//package com.frager.oreport.taskaccountcourseactivity.reader;
//
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Queue;
//import java.util.stream.Collectors;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//import org.springframework.util.MultiValueMapAdapter;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.util.UriComponentsBuilder;
//
//@Component
//public class UserCourseActivityReader implements ItemReader<Map<String, Object>> {
//
//	private final static Logger logger = LoggerFactory.getLogger(UserCourseActivityReader.class);
//
//	private final WebClient webClient;
//
//	private final Queue<Map<String, Object>> activities;
//
//	private String nextPageUrl;
//
//	public UserCourseActivityReader(@Autowired WebClient webClient,
//			@Value("${udemy-connector.user-account-activity.url}") String udemyConnectorUrl,
//			@Value("#{${udemy-connector.query-params}}") MultiValueMap<String, String> queryArguments,
//			@Autowired ApplicationArguments applicationArguments,
//			@Value("${udemy-connector.query-param.to-date}") String toDateParam) {
//		this.webClient = webClient;
//		this.activities = new LinkedList<>();
//
//		if (applicationArguments.getNonOptionArgs().isEmpty()) {
//			throw new IllegalArgumentException("Error, es mandatorio al menos un argumento.");
//		}
//
//		MultiValueMap<String, String> allQueryArguments = new MultiValueMapAdapter<>(queryArguments);
//		allQueryArguments.add(toDateParam, applicationArguments.getNonOptionArgs().get(0));
//		this.nextPageUrl = UriComponentsBuilder.fromHttpUrl(udemyConnectorUrl).queryParams(allQueryArguments).build()
//				.toUriString();
//	}
//
//	@Override
//	public Map<String, Object> read() throws Exception {
//		if (activities.isEmpty() && nextPageUrl == null) {
//			logger.debug("No hay quedan mas items que entregar.");
//			return null;
//		}
//
//		if (activities.isEmpty()) {
//			getNextPageInfo();
//			return read();
//		}
//
//		return activities.poll();
//	}
//
//	private void getNextPageInfo() {
//		logger.debug("Consultando url {}", nextPageUrl);
//		@SuppressWarnings("unchecked")
//		Map<String, Object>[] newActivities = webClient.get().uri(nextPageUrl).accept(MediaType.APPLICATION_JSON)
//				.exchangeToMono(clientResponse -> {
//					retrieveNextPage(clientResponse.headers().asHttpHeaders());
//					return clientResponse.bodyToMono(Map[].class);
//				}).block();
//		activities.addAll(Arrays.asList(newActivities));
//	}
//
//	/**
//	 * Recuperar la informacion de la siguiente pagina en base a los principios de
//	 * <a href="https://developer.github.com/v3/#pagination">GitHub API</a>, y la
//	 * <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
//	 */
//	private void retrieveNextPage(HttpHeaders headers) {
//		nextPageUrl = null;
//		if (!headers.containsKey("link"))
//			return;
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("Link headers recuperados: {}", headers.get("link"));
//			logger.debug("Count headers recuperados: {}", headers.get("x-total-count"));
//		}
//
//		List<String> linkHeaders = headers.get("link").stream().map(l -> Arrays.asList(l.split(",")))
//				.flatMap(List::stream).collect(Collectors.toList());
//		Optional<String> nextPage = linkHeaders.stream().filter(link -> link.endsWith("rel=\"next\"")).findFirst()
//				.map(UserCourseActivityReader::retrieveLink);
//		if (nextPage.isPresent()) {
//			nextPageUrl = nextPage.get();
//		}
//	}
//
//	/**
//	 * Transforma <br />
//	 * <br />
//	 * <code>&lthttps://my-super-duper/url&gt&semi; rel="next"</code> <br />
//	 * <br />
//	 * en <br />
//	 * <br />
//	 * <code>https://my-super-duper/url</code>
//	 */
//	private static String retrieveLink(String headerPageLink) {
//		String res = headerPageLink.split(";")[0].substring(1);
//		res = res.substring(0, res.length() - 1);
//		logger.debug("Link a siguiente pagina: {}", res);
//
//		return res;
//	}
//}
