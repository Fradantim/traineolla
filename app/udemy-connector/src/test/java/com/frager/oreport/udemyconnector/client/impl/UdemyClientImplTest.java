package com.frager.oreport.udemyconnector.client.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.udemy.model.Course;
import com.udemy.model.PageResponse;
import com.udemy.model.SingleCourse;
import com.udemy.model.UdemyObject;
import com.udemy.model.UserCourseActivity;

import reactor.core.publisher.Mono;

@SpringBootTest
@TestPropertySource("classpath:secrets.mock.properties")
@TestPropertySource("classpath:test.mock.properties")
class UdemyClientImplTest {

	@Value("${courses.2360128}")
	private String singleCourse;

	@Value("${courses.page.1}")
	private String coursesPage1;
	
	@Value("${user-course-activity.page.1.no-next}")
	private String userCourseActivityPage1;
	
	@Value("${user-activity.page.1.no-next}")
	private String userActivityPage1;

	@Value("#{${test.udemy.courses.url.query-params}}")
	private MultiValueMap<String, String> testCoursesUrlQueryParams;

	private ExchangeFunction getExchangeFunction(final String returnedBody) {
		return r -> Mono.just(ClientResponse.create(HttpStatus.OK)
				.header("content-type", MediaType.APPLICATION_JSON_VALUE).body(returnedBody).build());
	}

	private WebClient singleCourseWebClient;
	private WebClient coursePagesWebClient;
	private WebClient userCourseAcitvityPagesWebClient;
	private WebClient userAcitvityPagesWebClient;

	@PostConstruct
	private void postConstruct() {
		singleCourseWebClient = WebClient.builder().exchangeFunction(getExchangeFunction(singleCourse)).build();
		coursePagesWebClient = WebClient.builder().exchangeFunction(getExchangeFunction(coursesPage1)).build();
		userCourseAcitvityPagesWebClient = WebClient.builder().exchangeFunction(getExchangeFunction(userCourseActivityPage1)).build();
		userAcitvityPagesWebClient = WebClient.builder().exchangeFunction(getExchangeFunction(userActivityPage1)).build();
	}

	@Test
	void getCourseTest() {
		new UdemyClientImpl(singleCourseWebClient).getCourseById(0, null).subscribe(this::assertSingleCourse);
	}

	void assertSingleCourse(SingleCourse course) {
		assertNotNull(course);
		assertNotNull(course.getId());
		assertNotNull(course.getClazz());
	}

	private <S extends UdemyObject> void assertPagedUdemyObject(PageResponse<S> page, Function<S, ? extends Object> nonNullSuplier) {
		assertNotNull(page);
		assertNotNull(page.getCount());
		assertNotNull(page.getResults());
		assertFalse(page.getResults().isEmpty());
		assertNotNull(page.getResults().get(0));
		assertNotNull(nonNullSuplier.apply(page.getResults().get(0)));
	}
	
	@Test
	void getCoursesTest() {
		new UdemyClientImpl(coursePagesWebClient).getCourses(testCoursesUrlQueryParams)
				.subscribe(page -> assertPagedUdemyObject(page, Course::getId));
	}

	@Test
	void getUserCourseActivityTest() {
		new UdemyClientImpl(userCourseAcitvityPagesWebClient).getUserCourseActivity(null)
				.subscribe(page -> assertPagedUdemyObject(page, UserCourseActivity::getUserEmail));
	}
	
	@Test
	void getUserActivityTest() {
		new UdemyClientImpl(userCourseAcitvityPagesWebClient).getUserCourseActivity(null)
				.subscribe(page -> assertPagedUdemyObject(page, UserCourseActivity::getUserEmail));
	}
}
