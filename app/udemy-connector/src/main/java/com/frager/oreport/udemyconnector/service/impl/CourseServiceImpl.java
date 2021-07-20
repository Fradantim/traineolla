package com.frager.oreport.udemyconnector.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.frager.oreport.udemyconnector.client.UdemyClient;
import com.frager.oreport.udemyconnector.mapper.CourseMapper;
import com.frager.oreport.udemyconnector.model.Course;
import com.frager.oreport.udemyconnector.service.CourseService;
import com.frager.oreport.udemyconnector.utils.URLUtils;
import com.udemy.model.ListedCourse;
import com.udemy.model.PageResponse;
import com.udemy.model.SingleCourse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseServiceImpl implements CourseService {

	private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

	@Autowired
	private UdemyClient udemyClient;

	@Override
	public Mono<Course> getCourseById(Integer id) {
		return getCourseById(id, null);
	}

	@Override
	public Mono<Course> getCourseById(Integer id, MultiValueMap<String, String> queryParams) {
		Mono<SingleCourse> singleCourseMono = udemyClient.getCourseById(id, queryParams);
		return singleCourseMono.map(CourseMapper::fromSingleCourse);
	}

	public Flux<Course> getCourses() {
		return getCourses(null);
	}

	public Flux<Course> getCourses(MultiValueMap<String, String> queryParams) {
		Mono<PageResponse<ListedCourse>> listedCoursePageMono = udemyClient.getCourses(queryParams);
		Flux<Course> currentFlux = listedCoursePageMono.flatMapMany(page -> {
			MultiValueMap<String, String> nextPageInfo = URLUtils.getQueryParamsFromURL(page.getNext());
			if (nextPageInfo == null || nextPageInfo.isEmpty()) {
				return Flux.fromIterable(page.getResults()).map(CourseMapper::fromListedCourse);
			} else {
				logger.debug("La consulta previa informa de una siguiente pagina. count:{}, query-params:{}",
						page.getCount(), nextPageInfo);
				return Flux.fromIterable(page.getResults()).map(CourseMapper::fromListedCourse)
						.concatWith(getCourses(nextPageInfo));
			}
		});

		if (logger.isDebugEnabled()) {
			currentFlux.log();
		}

		return currentFlux;
	}
}
