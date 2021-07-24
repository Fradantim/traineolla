package com.frager.oreport.udemyconnector.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frager.oreport.udemyconnector.client.UdemyClient;
import com.frager.oreport.udemyconnector.model.Course;
import com.udemy.model.ListedCourse;
import com.udemy.model.PageResponse;
import com.udemy.model.SingleCourse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
@TestPropertySource("classpath:secrets.mock.properties")
@TestPropertySource("classpath:test.mock.properties")
class CourseServiceImplTest {

	private static final TypeReference<PageResponse<ListedCourse>> PAGE_OF_COURSE_TYPE_REF = new TypeReference<PageResponse<ListedCourse>>() {
	};

	@MockBean
	private UdemyClient udemyClient;

	@Autowired
	private ObjectMapper defaultObjectMapper;

	private static final Integer SPECIFIC_COURSE_ID = 2360128;

	@Value("${courses.2360128}")
	private String specificCourseParsed;

	@Value("#{'${courses.page.1}SPLITTER${courses.page.2}SPLITTER${courses.page.3.no-next}'.split('SPLITTER')}")
	private List<String> coursePages;

	@Test
	void getCourseByIdTest() {
		assertDoesNotThrow(this::getCourseByIdOperation);
	}

	void getCourseByIdOperation() throws JsonMappingException, JsonProcessingException {
		SingleCourse singleCourse = defaultObjectMapper.readValue(specificCourseParsed, SingleCourse.class);

		Mockito.when(udemyClient.getCourseById(SPECIFIC_COURSE_ID, null)).thenReturn(Mono.just(singleCourse));
		Course course = new CourseServiceImpl(udemyClient).getCourseById(SPECIFIC_COURSE_ID).block();

		assertNotNull(course);
		// mapeos ya asserteados en su tester especifico
	}

	@Test
	void getCoursesTest() {
		assertDoesNotThrow(this::getCoursesOperation);
	}

	/**
	 * Prueba que el flux recorra toda la paginacion de elementos hasta la ultima
	 * que no indica next.
	 */
	@SuppressWarnings("unchecked")
	void getCoursesOperation() throws JsonMappingException, JsonProcessingException {
		Mockito.when(udemyClient.getCourses(null)).thenAnswer(mockedCoursePageAnswer);
		Mockito.when(udemyClient.getCourses(Mockito.any(MultiValueMap.class))).thenAnswer(mockedCoursePageAnswer);

		Flux<Course> coursesFlux = new CourseServiceImpl(udemyClient).getCourses(null);

		assertEquals(coursesFlux.collectList().block().size(), calculatePagesContents(coursePages));
	}

	private Answer<Mono<PageResponse<ListedCourse>>> mockedCoursePageAnswer = new Answer<Mono<PageResponse<ListedCourse>>>() {
		@SuppressWarnings("unchecked")
		public Mono<PageResponse<ListedCourse>> answer(InvocationOnMock invocation) {
			Integer thePageToReturn = 0;
			Object[] args = invocation.getArguments();
			if (args.length > 0 && args[0] != null && args[0] instanceof MultiValueMap) {
				MultiValueMap<String, String> paramsMap = (MultiValueMap<String, String>) args[0];
				if (paramsMap.containsKey("page") && !paramsMap.get("page").isEmpty()) {
					thePageToReturn = Integer.parseInt(paramsMap.get("page").get(0)) - 1;
				}
			}
			try {
				return Mono
						.just(defaultObjectMapper.readValue(coursePages.get(thePageToReturn), PAGE_OF_COURSE_TYPE_REF));
			} catch (JsonProcessingException e) {
				throw new RuntimeException("Error al serializar JSON", e);
			}
		}
	};

	private Integer calculatePagesContents(List<String> coursePages) {
		try {
			Integer result = 0;
			for (String coursePage : coursePages) {
				result += defaultObjectMapper.readValue(coursePage, PAGE_OF_COURSE_TYPE_REF).getResults().size();
			}

			return result;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error al serializar JSON", e);
		}
	}
}
