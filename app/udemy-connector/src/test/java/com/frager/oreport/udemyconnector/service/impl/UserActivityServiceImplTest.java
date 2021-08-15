package com.frager.oreport.udemyconnector.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import com.udemy.model.PageResponse;
import com.udemy.model.UserActivity;

import reactor.core.publisher.Flux;

@SpringBootTest
@TestPropertySource("classpath:secrets.mock.properties")
@TestPropertySource("classpath:test.mock.properties")
class UserActivityServiceImplTest extends PagingTester {

	private static final TypeReference<PageResponse<UserActivity>> PAGE_OF_UA_TYPE_REF = new TypeReference<PageResponse<UserActivity>>() {
	};

	@MockBean
	private UdemyClient udemyClient;

	@Autowired
	private ObjectMapper defaultObjectMapper;

	@Value("#{'${user-activity.page.1.no-next}'.split('SPLITTER')}")
	private List<String> userActivityPages;

	@Test
	void getCoursesTest() {
		assertDoesNotThrow(this::getUserActivity);
	}

	/**
	 * Prueba que el flux recorra toda la paginacion de elementos hasta la ultima
	 * que no indica next.
	 */
	@SuppressWarnings("unchecked")
	void getUserActivity() throws JsonMappingException, JsonProcessingException {
		Answer<?> mockedAnswer = buildPagedResponse(PAGE_OF_UA_TYPE_REF, userActivityPages);
		Mockito.when(udemyClient.getUserActivity(null)).thenAnswer(mockedAnswer);
		Mockito.when(udemyClient.getUserActivity(Mockito.any(MultiValueMap.class))).thenAnswer(mockedAnswer);

		Flux<UserActivity> userActivityFlux = new UserActivityServiceImpl(udemyClient).getUserActivity();

		assertEquals(userActivityFlux.collectList().block().size(), calculatePagesContents(userActivityPages));
	}

	private Integer calculatePagesContents(List<String> coursePages) {
		try {
			Integer result = 0;
			for (String coursePage : coursePages) {
				result += defaultObjectMapper.readValue(coursePage, PAGE_OF_UA_TYPE_REF).getResults().size();
			}

			return result;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error al serializar JSON", e);
		}
	}
}
