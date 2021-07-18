package com.frager.oreport.udemyconnector.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frager.oreport.udemyconnector.model.Course;
import com.frager.oreport.udemyconnector.service.CourseService;

import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/courses")
public class CourseResource {

	@Autowired
	private CourseService courseService;

	@GetMapping("/{id}")
	public Mono<Course> getById(@Parameter(description = "Id del curso a buscar") @PathVariable("id") Integer id) {
		return courseService.getCourseById(id);
	}

	@GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Object getAll() {
		return courseService.getCourses();
	}
}
