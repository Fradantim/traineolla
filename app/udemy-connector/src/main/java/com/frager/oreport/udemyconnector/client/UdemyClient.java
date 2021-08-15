package com.frager.oreport.udemyconnector.client;

import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import com.udemy.model.ListedCourse;
import com.udemy.model.PageResponse;
import com.udemy.model.SingleCourse;
import com.udemy.model.UserCourseActivity;

import reactor.core.publisher.Mono;

public interface UdemyClient {

	public Mono<SingleCourse> getCourseById(Integer id, @Nullable MultiValueMap<String, String> queryParams);

	public Mono<PageResponse<ListedCourse>> getCourses(@Nullable MultiValueMap<String, String> queryParams);

	public Mono<PageResponse<UserCourseActivity>> getUserCourseActivity(@Nullable MultiValueMap<String, String> queryParams);
}
