package com.frager.oreport.udemyconnector.service;

import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import com.udemy.model.UserActivity;

import reactor.core.publisher.Flux;

public interface UserActivityService {

	public Flux<UserActivity> getUserActivity();
	
	public Flux<UserActivity> getUserActivity(@Nullable  MultiValueMap<String, String> queryParams);
}
