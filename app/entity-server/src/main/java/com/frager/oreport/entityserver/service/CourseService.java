package com.frager.oreport.entityserver.service;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.frager.oreport.entityserver.model.Course;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseService {

	public Mono<Long> count();
	
	public Flux<Course> findAll(@NonNull Pageable pageable);
	
	public Mono<Course> findById(@NonNull Long id);
	
	public Mono<Course> findByUdemyId(@NonNull Long udemyId);
	
	public Mono<Void> deleteById(@NonNull Long id);
	
	public Mono<Boolean> existsById(Long id);
	
	public Mono<Course> save(Course course);
}
