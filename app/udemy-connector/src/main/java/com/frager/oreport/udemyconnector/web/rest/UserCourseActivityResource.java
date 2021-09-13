package com.frager.oreport.udemyconnector.web.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frager.oreport.udemyconnector.service.UserCourseActivityService;
import com.udemy.model.PageResponse;
import com.udemy.model.UserCourseActivity;

import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user-course-activity")
public class UserCourseActivityResource extends UdemyResource {

	@Autowired
	private UserCourseActivityService userCourseActivityService;

	@GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<UserCourseActivity> getAll(
			@Parameter(description = "${api-docs.user-course-activity.description}") @RequestParam(defaultValue = "{}") Map<String, String> requestParams) {
		return userCourseActivityService.getUserCourseActivity(getUdemySpecificRequestParams(requestParams));
	}

	@GetMapping()
	public Mono<ResponseEntity<List<UserCourseActivity>>> getPage(
			@Parameter(description = "${api-docs.user-course-activity.description}") @RequestParam(defaultValue = "{}") Map<String, String> requestParams) {
		Mono<PageResponse<UserCourseActivity>> activities = userCourseActivityService
				.getUserCourseActivityPage(getUdemySpecificRequestParams(requestParams));

		return activities.flatMap(page -> buildPagedResponse(page));
	}
}
