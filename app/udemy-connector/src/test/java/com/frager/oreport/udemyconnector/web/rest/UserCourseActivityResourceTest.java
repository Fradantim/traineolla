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

import com.frager.oreport.udemyconnector.model.UserCourseActivity;
import com.frager.oreport.udemyconnector.service.UserCourseActivityService;
import com.frager.oreport.udemyconnector.service.impl.UserCourseActivityServiceImpl;

import reactor.core.publisher.Flux;

@Profile("UserCourseActivityResourceTest")
@TestConfiguration
class UserCourseActivityServiceTestConfiguration {

	@Primary
	@Bean
	public UserCourseActivityService buildFakeService() {
		return new UserCourseActivityServiceImpl(null) {
			@Override
			public Flux<UserCourseActivity> getUserCourseActivity() {
				return getUserCourseActivity(null);
			}

			@Override
			public Flux<UserCourseActivity> getUserCourseActivity(MultiValueMap<String, String> queryParams) {
				return Flux.just(new UserCourseActivity(), new UserCourseActivity(), new UserCourseActivity());
			}
		};
	}
}

@ActiveProfiles("UserCourseActivityResourceTest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:secrets.mock.properties")
@TestPropertySource("classpath:test.mock.properties")
class UserCourseActivityResourceTest {

	@LocalServerPort
	private int port;

	@Test
	void getCoursesWithNoExtraParamsTest() {
		Flux<UserCourseActivity> courseFlux = WebClient.create().get()
				.uri("http://localhost:" + port + "/user-course-activity/").retrieve()
				.bodyToFlux(UserCourseActivity.class);

		List<UserCourseActivity> activities = courseFlux.collect(Collectors.toList()).block();

		assertFalse(activities.isEmpty());
	}

	@Test
	void getCoursesWithExtraParamsTest() {
		Flux<UserCourseActivity> courseFlux = WebClient.create().get()
				.uri("http://localhost:" + port + "/user-course-activity/",
						uriF -> uriF.queryParams(new LinkedMultiValueMap<>()).build())
				.retrieve().bodyToFlux(UserCourseActivity.class);

		List<UserCourseActivity> activities = courseFlux.collect(Collectors.toList()).block();

		assertFalse(activities.isEmpty());
	}
}
