package com.frager.oreport.udemyconnector.web.rest;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.frager.oreport.udemyconnector.service.UserActivityService;
import com.frager.oreport.udemyconnector.service.impl.UserActivityServiceImpl;
import com.udemy.model.UserActivity;
import com.udemy.model.UserCourseActivity;

import reactor.core.publisher.Flux;

@Profile("UserActivityResourceTest")
@TestConfiguration
class UserActivityServiceTestConfiguration {

	@Primary
	@Bean
	public UserActivityService buildFakeService() {
		return new UserActivityServiceImpl(null) {
			@Override
			public Flux<UserActivity> getUserActivity() {
				return getUserActivity(null);
			}
			
			@Override
			public Flux<UserActivity> getUserActivity(MultiValueMap<String, String> queryParams) {
				return Flux.just(new UserActivity(), new UserActivity(), new UserActivity());
			}
		};
	}
}

@ActiveProfiles("UserActivityResourceTest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:secrets.mock.properties")
@TestPropertySource("classpath:test.mock.properties")
class UserActivityResourceTest {

	@LocalServerPort
	private int port;

	@Test
	void getCoursesWithNoExtraParamsTest() {
		Flux<UserCourseActivity> courseFlux = WebClient.create().get()
				.uri("http://localhost:" + port + "/user-activity/").retrieve()
				.bodyToFlux(UserCourseActivity.class);

		List<UserCourseActivity> activities = courseFlux.collect(Collectors.toList()).block();

		assertFalse(activities.isEmpty());
	}

	@Test
	void getCoursesWithExtraParamsTest() {
		Flux<UserCourseActivity> courseFlux = WebClient.create().get()
				.uri("http://localhost:" + port + "/user-activity/",
						uriF -> uriF.queryParams(new LinkedMultiValueMap<>()).build())
				.retrieve().bodyToFlux(UserCourseActivity.class);

		List<UserCourseActivity> activities = courseFlux.collect(Collectors.toList()).block();

		assertFalse(activities.isEmpty());
	}
}
