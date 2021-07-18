package com.frager.oreport.udemyconnector.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frager.oreport.udemyconnector.model.Course;
import com.frager.oreport.udemyconnector.service.CourseService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/courses")
public class CourseResource {

	@Autowired
	private CourseService courseService;
	
	@GetMapping("/{id}")
	public Mono<Course> getById(@PathVariable("id") Integer id) {
		return courseService.getCourseById(id);
	}
	
	@GetMapping("")
	public Object getAll() {
		// TODO Flux paginado?
		return null;
	}
}
