package com.frager.oreport.udemyconnector.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.frager.oreport.udemyconnector.client.UdemyClient;
import com.frager.oreport.udemyconnector.mapper.CourseMapper;
import com.frager.oreport.udemyconnector.model.Course;
import com.frager.oreport.udemyconnector.service.CourseService;
import com.udemy.model.SingleCourse;

import reactor.core.publisher.Mono;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private UdemyClient udemyClient;
	
	@Override
	public Mono<Course> getCourseById(Integer id) {
		return getCourseById(id, null);
	}

	@Override
	public Mono<Course> getCourseById(Integer id, MultiValueMap<String, String> queryParams) {
		Mono<SingleCourse> singleCourseMono = udemyClient.getCourseById(id, queryParams);
		Mono<Course> courseMono = singleCourseMono.map(CourseMapper::fromSingleCourse);
		return courseMono;
	}

}
