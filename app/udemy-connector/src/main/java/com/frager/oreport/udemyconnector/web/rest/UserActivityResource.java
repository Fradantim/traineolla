package com.frager.oreport.udemyconnector.web.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frager.oreport.udemyconnector.service.UserActivityService;
import com.udemy.model.UserActivity;

import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user-activity")
public class UserActivityResource extends UdemyResource {

	@Autowired
	private UserActivityService userActivityService;

	@GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<UserActivity> getAll(
			@Parameter(description = "${api-docs.user-activity.description}") @RequestParam(defaultValue = "{}") Map<String, String> requestParams) {
		return userActivityService.getUserActivity(getUdemySpecificRequestParams(requestParams));
	}
}
