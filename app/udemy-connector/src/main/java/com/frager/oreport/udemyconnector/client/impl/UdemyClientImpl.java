package com.frager.oreport.udemyconnector.client.impl;

import java.nio.charset.Charset;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import com.frager.oreport.udemyconnector.client.UdemyClient;
import com.udemy.model.ListedCourse;
import com.udemy.model.PageResponse;
import com.udemy.model.SingleCourse;
import com.udemy.model.UserActivity;
import com.udemy.model.UserCourseActivity;
import com.udemy.model.UserProgress;

@Component
public class UdemyClientImpl implements UdemyClient {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private HttpHeaders httpHeaders;
	
	@Value("${udemy.course.url}")
	private String courseURL;
	
	@Value("#{${udemy.course.url.query-params}}")
	private MultiValueMap<String, String> courseURLQueryParams;
	
	@Value("${udemy.courses.url}")
	private String coursesURL;
	
	@Value("#{${udemy.courses.url.query-params}}")
	private MultiValueMap<String, String> coursesURLQueryParams;
	
	@Value("${udemy.user-activity.url}")
	private String userActivityURL;
	
	@Value("#{${udemy.user-activity.url.query-params}}")
	private MultiValueMap<String, String> userActivityURLQueryParams;
	
	@Value("${udemy.user-course-activity.url}")
	private String userCourseActivityURL;
	
	@Value("#{${udemy.user-course-activity.url.query-params}}")
	private MultiValueMap<String, String> userCourseActivityURLQueryParams;
	
	@Value("${udemy.user-progress.url}")
	private String UserProgressURL;
	
	@Value("#{${udemy.user-progress.url.query-params}}")
	private MultiValueMap<String, String> UserProgressURLQueryParams;
	
	public UdemyClientImpl(@Value("${udemy.client.id:}") String user, @Value("${udemy.client.secret:}") String pass, @Value("${udemy.client.auth-header:}") String authHeader) {
		super();
		httpHeaders = new HttpHeaders();
		if (authHeader == null || authHeader.isEmpty()) {
			byte[] encodedAuth = Base64.getEncoder().encode((user + ":" + pass).getBytes(Charset.forName("US-ASCII")) );
	        authHeader = "Basic " + new String( encodedAuth );
		}		
		httpHeaders.add("Authorization", authHeader);
	}
	
	private <S,T> MultiValueMap<S, T> mixMaps(@Nullable MultiValueMap<S, T> specificValues, @NonNull MultiValueMap<S, T> defaultValues) {
		if (specificValues == null || specificValues.isEmpty())
			return defaultValues;
		
		MultiValueMap<S, T> finalValues = new MultiValueMapAdapter<>(specificValues);
		finalValues.addAll(defaultValues);
		
		return finalValues;
	}
	

	@Override
	public SingleCourse getCourseById(Integer id, MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, courseURLQueryParams);
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PageResponse<ListedCourse> getCourses(MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, coursesURLQueryParams);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResponse<UserActivity> getUserActivity(MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, userActivityURLQueryParams);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResponse<UserCourseActivity> getUserCourseActivity(MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, userCourseActivityURLQueryParams);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResponse<UserProgress> getUserProgress(MultiValueMap<String, String> queryParams) {
		MultiValueMap<String, String> finalQueryParams = mixMaps(queryParams, UserProgressURLQueryParams);
		// TODO Auto-generated method stub
		return null;
	}
	
}
