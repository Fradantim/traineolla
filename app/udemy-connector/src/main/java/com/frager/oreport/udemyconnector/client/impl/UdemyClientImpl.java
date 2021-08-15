package com.frager.oreport.udemyconnector.client.impl;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.client.WebClient;

import com.frager.oreport.udemyconnector.client.UdemyClient;
import com.udemy.model.ListedCourse;
import com.udemy.model.PageResponse;
import com.udemy.model.SingleCourse;
import com.udemy.model.UserCourseActivity;

import reactor.core.publisher.Mono;

@Component
public class UdemyClientImpl implements UdemyClient {

	private static final Logger logger = LoggerFactory.getLogger(UdemyClientImpl.class);

	private static final ParameterizedTypeReference<PageResponse<ListedCourse>> PAGE_OF_COURSE_TYPE_REF = new ParameterizedTypeReference<PageResponse<ListedCourse>>() {
	};

	private static final ParameterizedTypeReference<PageResponse<UserCourseActivity>> PAGE_OF_UCA_TYPE_REF = new ParameterizedTypeReference<PageResponse<UserCourseActivity>>() {
	};

	@Value("${udemy.course.url}")
	private String courseUrl;

	@Value("#{${udemy.course.url.query-params}}")
	private MultiValueMap<String, String> courseUrlQueryParams;

	@Value("${udemy.courses.url}")
	private String coursesUrl;

	@Value("#{${udemy.courses.url.query-params}}")
	private MultiValueMap<String, String> coursesUrlQueryParams;

	@Value("${udemy.user-course-activity.url}")
	private String userCourseActivityUrl;

	@Value("#{${udemy.user-course-activity.url.query-params}}")
	private MultiValueMap<String, String> userCourseActivityUrlQueryParams;

	private WebClient udemyWebClient;

	public UdemyClientImpl(@Autowired @Qualifier("udemy-webclient") WebClient udemyWebClient) {
		super();
		this.udemyWebClient = udemyWebClient;
	}

	/**
	 * Retorna un nuevo {@link MultiValueMap} con los elementos de specificValues y
	 * defaultValues.
	 */
	@SuppressWarnings("unchecked")
	private <S, T> MultiValueMap<S, T> mixMaps(@Nullable MultiValueMap<S, T> specificValues,
			@NonNull MultiValueMap<S, T> defaultValues) {
		if (specificValues == null || specificValues.isEmpty()) {
			logger.debug("No se indicaron parametros especificos, se retornan los default: {}", defaultValues);
			return defaultValues;
		}

		MultiValueMap<S, T> finalValues = new MultiValueMapAdapter<>(specificValues);
		for (Entry<S, List<T>> entry : specificValues.entrySet()) {
			finalValues.addIfAbsent(entry.getKey(), (T) entry.getValue());
		}

		logger.debug("Se indicaron parametros especificos, se retornan los mezclados: {}", finalValues);
		return finalValues;
	}

	@Override
	public Mono<SingleCourse> getCourseById(Integer id, MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, courseUrlQueryParams);
		return udemyWebClient.get()
				.uri(courseUrl, uriF -> uriF.queryParams(finalQueryParams).path(String.valueOf(id)).build()).retrieve()
				.bodyToMono(SingleCourse.class);
	}

	@Override
	public Mono<PageResponse<ListedCourse>> getCourses(MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, coursesUrlQueryParams);
		return udemyWebClient.get().uri(coursesUrl, uriF -> uriF.queryParams(finalQueryParams).build()).retrieve()
				.bodyToMono(PAGE_OF_COURSE_TYPE_REF);
	}

	@Override
	public Mono<PageResponse<UserCourseActivity>> getUserCourseActivity(MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, userCourseActivityUrlQueryParams);
		return udemyWebClient.get().uri(userCourseActivityUrl, uriF -> uriF.queryParams(finalQueryParams).build()).retrieve()
				.bodyToMono(PAGE_OF_UCA_TYPE_REF);
	}
}
