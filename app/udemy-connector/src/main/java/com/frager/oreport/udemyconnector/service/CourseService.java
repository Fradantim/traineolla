package com.frager.oreport.udemyconnector.service;

import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import com.udemy.model.Course;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseService {

	public Mono<Course> getCourseById(Integer id);

	public Mono<Course> getCourseById(Integer id, @Nullable MultiValueMap<String, String> queryParams);
	
	public Flux<Course> getCourses(@Nullable  MultiValueMap<String, String> queryParams);
}
