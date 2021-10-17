package com.frager.oreport.batchcoursesnapshot.service.impl;

import java.time.Duration;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.frager.oreport.batchcoursesnapshot.dto.UserCourseActivity;
import com.frager.oreport.batchcoursesnapshot.runner.AppRunner;
import com.frager.oreport.batchcoursesnapshot.service.UserCourseActivityService;

import reactor.core.publisher.Flux;

@Service
public class UserCourseActivityServiceImpl implements UserCourseActivityService {

	private final static Logger logger = LoggerFactory.getLogger(AppRunner.class);

	@Autowired
	private WebClient webClient;

	@Value("${udemy-connector.user-account-activity.url}")
	private String udemyConnectorUrl;

	@Value("#{${udemy-connector.query-params}}")
	private MultiValueMap<String, String> queryArguments;

	@Value("${udemy-connector.query-param.to-date}")
	private String toDateParam;

	@Value("${udemy-connector.query-param.from-date}")
	private String fromDateParam;

	@Value("${udemy-connector.query-param.from-date.months-ago}")
	private Integer fromDateParamMonthsAgo;

	public Flux<UserCourseActivity> getActivitiesBefore(String toDate) {
		MultiValueMap<String, String> allQueryArguments = new MultiValueMapAdapter<>(queryArguments);
		allQueryArguments.add(toDateParam, toDate);
		allQueryArguments.add(fromDateParam, LocalDate.parse(toDate).atStartOfDay()
				.minus(Duration.ofDays(fromDateParamMonthsAgo * 30)).toLocalDate().toString());

		String finalUrl = UriComponentsBuilder.fromHttpUrl(udemyConnectorUrl).queryParams(allQueryArguments).build()
				.toUriString();

		logger.debug("Llamando al servicio: " + finalUrl);

		return webClient.get().uri(finalUrl).accept(MediaType.APPLICATION_NDJSON).retrieve()
				.bodyToFlux(UserCourseActivity.class);
	}
}
