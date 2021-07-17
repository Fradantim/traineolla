package com.frager.oreport.udemyconnector.client;

import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import com.udemy.model.ListedCourse;
import com.udemy.model.PageResponse;
import com.udemy.model.SingleCourse;
import com.udemy.model.UserActivity;
import com.udemy.model.UserCourseActivity;
import com.udemy.model.UserProgress;

import reactor.core.publisher.Mono;

public interface UdemyClient {

	public Mono<SingleCourse> getCourseById(Integer id);
	
	public Mono<SingleCourse> getCourseById(Integer id, @Nullable MultiValueMap<String, String> queryParams);
	
	public PageResponse<ListedCourse> getCourses();
	
	public PageResponse<ListedCourse> getCourses(@Nullable MultiValueMap<String, String> queryParams);
	
	public PageResponse<UserActivity> getUserActivity();
	
	public PageResponse<UserActivity> getUserActivity(@Nullable MultiValueMap<String, String> queryParams);
	
	public PageResponse<UserCourseActivity> getUserCourseActivity();
	
	public PageResponse<UserCourseActivity> getUserCourseActivity(@Nullable MultiValueMap<String, String> queryParams);
	
	public PageResponse<UserProgress> getUserProgress();
	
	public PageResponse<UserProgress> getUserProgress(@Nullable MultiValueMap<String, String> queryParams);	
}
