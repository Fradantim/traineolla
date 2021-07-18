package com.frager.oreport.udemyconnector.client.impl;

import java.util.List;
import java.util.Map.Entry;

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
import com.udemy.model.UserActivity;
import com.udemy.model.UserCourseActivity;
import com.udemy.model.UserProgress;

import reactor.core.publisher.Mono;

@Component
public class UdemyClientImpl implements UdemyClient {

	private static final ParameterizedTypeReference<PageResponse<ListedCourse>> PAGE_OF_COURSE_TYPE_REF = new ParameterizedTypeReference<PageResponse<ListedCourse>>() {
	};

	@Value("${udemy.course.url}")
	private String courseUrl;

	@Value("#{${udemy.course.url.query-params}}")
	private MultiValueMap<String, String> courseUrlQueryParams;

	@Value("${udemy.courses.url}")
	private String coursesUrl;

	@Value("#{${udemy.courses.url.query-params}}")
	private MultiValueMap<String, String> coursesUrlQueryParams;

	@Value("${udemy.user-activity.url}")
	private String userActivityUrl;

	@Value("#{${udemy.user-activity.url.query-params}}")
	private MultiValueMap<String, String> userActivityUrlQueryParams;

	@Value("${udemy.user-course-activity.url}")
	private String userCourseActivityUrl;

	@Value("#{${udemy.user-course-activity.url.query-params}}")
	private MultiValueMap<String, String> userCourseActivityUrlQueryParams;

	@Value("${udemy.user-progress.url}")
	private String userProgressUrl;

	@Value("#{${udemy.user-progress.url.query-params}}")
	private MultiValueMap<String, String> userProgressUrlQueryParams;

	public UdemyClientImpl() {
		super();
	}

	/**
	 * Retorna un nuevo {@link MultiValueMap} con los elementos de specificValues y
	 * defaultValues.
	 */
	@SuppressWarnings("unchecked")
	private <S, T> MultiValueMap<S, T> mixMaps(@Nullable MultiValueMap<S, T> specificValues,
			@NonNull MultiValueMap<S, T> defaultValues) {
		if (specificValues == null || specificValues.isEmpty())
			return defaultValues;

		MultiValueMap<S, T> finalValues = new MultiValueMapAdapter<>(specificValues);
		for (Entry<S, List<T>> entry : specificValues.entrySet()) {
			finalValues.addIfAbsent(entry.getKey(), (T) entry.getValue());
		}
		return finalValues;
	}

	@Autowired
	@Qualifier("udemy-webclient")
	private WebClient webClient;

	@Override
	public Mono<SingleCourse> getCourseById(Integer id, MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, courseUrlQueryParams);
		return webClient.get()
				.uri(courseUrl, uriF -> uriF.queryParams(finalQueryParams).path(String.valueOf(id)).build()).retrieve()
				.bodyToMono(SingleCourse.class);
	}

	@Override
	public Mono<PageResponse<ListedCourse>> getCourses(MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, coursesUrlQueryParams);
		return webClient.get().uri(coursesUrl, uriF -> uriF.queryParams(finalQueryParams).build()).retrieve()
				.bodyToMono(PAGE_OF_COURSE_TYPE_REF);
	}

	@Override
	public PageResponse<UserActivity> getUserActivity(MultiValueMap<String, String> queryParams) {
		// MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams,
		// userActivityUrlQueryParams);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResponse<UserCourseActivity> getUserCourseActivity(MultiValueMap<String, String> queryParams) {
		// MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams,
		// userCourseActivityUrlQueryParams);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResponse<UserProgress> getUserProgress(MultiValueMap<String, String> queryParams) {
		// MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams,
		// userProgressUrlQueryParams);
		// TODO Auto-generated method stub
		return null;
	}
}
