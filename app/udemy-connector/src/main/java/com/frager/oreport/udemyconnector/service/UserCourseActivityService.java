package com.frager.oreport.udemyconnector.service;

import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import com.udemy.model.UserCourseActivity;

import reactor.core.publisher.Flux;

public interface UserCourseActivityService {

	public Flux<UserCourseActivity> getUserCourseActivity();
	
	public Flux<UserCourseActivity> getUserCourseActivity(@Nullable  MultiValueMap<String, String> queryParams);
}
