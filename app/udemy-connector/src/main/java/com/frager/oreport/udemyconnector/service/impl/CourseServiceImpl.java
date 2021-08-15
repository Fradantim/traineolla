package com.frager.oreport.udemyconnector.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.frager.oreport.udemyconnector.client.UdemyClient;
import com.frager.oreport.udemyconnector.service.CourseService;
import com.udemy.model.Course;
import com.udemy.model.ListedCourse;
import com.udemy.model.PageResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseServiceImpl extends UdemyService implements CourseService {

	private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

	private UdemyClient udemyClient;

	public CourseServiceImpl(@Autowired UdemyClient udemyClient) {
		this.udemyClient = udemyClient;
	}

	@Override
	public Mono<Course> getCourseById(Integer id) {
		return getCourseById(id, null);
	}

	@Override
	public Mono<Course> getCourseById(Integer id, MultiValueMap<String, String> queryParams) {
		return udemyClient.getCourseById(id, queryParams).map(this::foolishMapper);
	}

	@Override
	public Flux<Course> getCourses() {
		return getCourses(null);
	}

	@Override
	public Flux<Course> getCourses(MultiValueMap<String, String> queryParams) {
		Mono<PageResponse<ListedCourse>> listedCoursePageMono = udemyClient.getCourses(queryParams);
		Flux<Course> currentFlux = listedCoursePageMono.flatMapMany(page -> 
			return Flux.fromIterable(page.getResults()).map(this::foolishMapper)
					.concatWith(getNextPage(page.getNext(), this::getCourses));
		);

		if (logger.isDebugEnabled()) {
			currentFlux.log();
		}

		return currentFlux;
	}

	/** Es un poco tonto, pero sirve para up-castear a {@link Course} */
	private Course foolishMapper(Course c) {
		return c;
	}
}
