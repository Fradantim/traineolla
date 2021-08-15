package com.frager.oreport.udemyconnector.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.frager.oreport.udemyconnector.model.Course;
import com.frager.oreport.udemyconnector.service.CourseService;
import com.frager.oreport.udemyconnector.service.impl.CourseServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("CourseResourceTest")
@TestConfiguration
class CourseServiceTestConfiguration {

	@Value("${request.udemy.params.identifier}")
	private String udemyRequestParamIdentifier;

	@Value("#{${test.web.courses.url.query-params}}")
	private MultiValueMap<String, String> coursesUrlQueryParams;

	@Primary
	@Bean
	public CourseService buildFakeService() {
		return new CourseServiceImpl(null) {
			@Override
			public Mono<Course> getCourseById(Integer id, MultiValueMap<String, String> queryParams) {

				if (queryParams != null) {
					assertMapHasCorrectValues(coursesUrlQueryParams, queryParams);
				}

				return Mono.just(new Course());
			}

			@Override
			public Flux<Course> getCourses(MultiValueMap<String, String> queryParams) {
				if (queryParams != null) {
					assertMapHasCorrectValues(coursesUrlQueryParams, queryParams);
				}

				return Flux.just(new Course(), new Course(), new Course());
			}

			/**
			 * Evalua que haya recibido todos los atributos marcados por el identificador
			 * {@link ServiceTestConfiguration#coursesUrlQueryParams}.
			 */
			private <T> void assertMapHasCorrectValues(MultiValueMap<String, T> sentMap,
					MultiValueMap<String, T> receivedMap) {
				Integer udemyElements = 0;
				for (Entry<String, List<T>> entry : sentMap.entrySet()) {
					if (entry.getKey().startsWith(udemyRequestParamIdentifier)) {
						assertEquals(entry.getValue(),
								receivedMap.get(entry.getKey().substring(udemyRequestParamIdentifier.length())));
						udemyElements++;
					}
				}

				assertEquals(udemyElements, receivedMap.size());
			}
		};
	}
}

@ActiveProfiles("CourseResourceTest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:secrets.mock.properties")
@TestPropertySource("classpath:test.mock.properties")
class CourseResourceTest {

	@LocalServerPort
	private int port;

	@Value("#{${test.web.courses.url.query-params}}")
	private MultiValueMap<String, String> coursesUrlQueryParams;

	@Test
	void getCourseByIdWithNoExtraParamsTest() {
		Mono<Course> courseMono = WebClient.create().get()
				.uri("http://localhost:" + port + "/courses/", uriF -> uriF.path(String.valueOf(177013)).build())
				.retrieve().bodyToMono(Course.class);

		Course course = courseMono.block();

		assertNotNull(course);
	}

	@Test
	void getCourseByIdWithExtraParamsTest() {
		Mono<Course> courseMono = WebClient.create().get()
				.uri("http://localhost:" + port + "/courses/",
						uriF -> uriF.queryParams(coursesUrlQueryParams).path(String.valueOf(177013)).build())
				.retrieve().bodyToMono(Course.class);

		Course course = courseMono.block();

		assertNotNull(course);
	}

	@Test
	void getCoursesWithNoExtraParamsTest() {
		Flux<Course> courseFlux = WebClient.create().get().uri("http://localhost:" + port + "/courses/").retrieve()
				.bodyToFlux(Course.class);

		List<Course> courses = courseFlux.collect(Collectors.toList()).block();

		assertFalse(courses.isEmpty());
	}

	@Test
	void getCoursesWithExtraParamsTest() {
		Flux<Course> courseFlux = WebClient.create().get()
				.uri("http://localhost:" + port + "/courses/", uriF -> uriF.queryParams(coursesUrlQueryParams).build())
				.retrieve().bodyToFlux(Course.class);

		List<Course> courses = courseFlux.collect(Collectors.toList()).block();

		assertFalse(courses.isEmpty());
	}
}
