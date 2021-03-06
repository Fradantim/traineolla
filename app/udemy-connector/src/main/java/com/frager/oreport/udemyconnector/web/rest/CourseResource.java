package com.frager.oreport.udemyconnector.web.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frager.oreport.udemyconnector.service.CourseService;
import com.udemy.model.Course;

import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/courses")
public class CourseResource extends UdemyResource {

	@Autowired
	private CourseService courseService;

	@GetMapping("/{id}")
	public Mono<Course> getById(@Parameter(description = "Id del curso a buscar") @PathVariable("id") Integer id,
			@Parameter(description = "${api-docs.courses.id.description}") @RequestParam(defaultValue = "{}") Map<String, String> requestParams) {
		return courseService.getCourseById(id, getUdemySpecificRequestParams(requestParams));
	}

	@GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<Course> getAll(
			@Parameter(description = "${api-docs.courses.description}") @RequestParam(defaultValue = "{}") Map<String, String> requestParams) {
		return courseService.getCourses(getUdemySpecificRequestParams(requestParams));
	}
}
