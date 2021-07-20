package com.frager.oreport.udemyconnector.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

class URLUtilsTest {

	@Test
	void getQueryParamsFromURLTest_withContent() {
		MultiValueMap<String, String> originalRequestParams = new LinkedMultiValueMap<String, String>();

		// 10 elementos simples
		for (int i = 0; i < 10; i++) {
			originalRequestParams.add("key_" + originalRequestParams.size(), "value_" + i);
		}

		// algunos caracteres especiales
		for (String specialChar : new String[] { "!", "\"", "%", "[", "]", "*", "(", ")", "[", "#", "$", "=" }) {
			originalRequestParams.add("key_" + originalRequestParams.size(), specialChar);
		}

		String builtUrl = UriComponentsBuilder.fromHttpUrl("http://url.com").queryParams(originalRequestParams)
				.toUriString();

		MultiValueMap<String, String> retrievedRequestParams = URLUtils.getQueryParamsFromURL(builtUrl);
		assertMultiValueMapEquals(originalRequestParams, retrievedRequestParams);
	}

	@Test
	void getQueryParamsFromURLTest_withNoContent() {
		MultiValueMap<String, String> originalRequestParams = new LinkedMultiValueMap<String, String>();

		String builtUrl = UriComponentsBuilder.fromHttpUrl("http://url.com").queryParams(originalRequestParams)
				.toUriString();

		MultiValueMap<String, String> retrievedRequestParams = URLUtils.getQueryParamsFromURL(builtUrl);
		assertMultiValueMapEquals(originalRequestParams, retrievedRequestParams);
	}

	@Test
	void getQueryParamsFromURLTest_withNullContent() {
		MultiValueMap<String, String> originalRequestParams = null;

		String builtUrl = UriComponentsBuilder.fromHttpUrl("http://url.com").queryParams(originalRequestParams)
				.toUriString();

		MultiValueMap<String, String> retrievedRequestParams = URLUtils.getQueryParamsFromURL(builtUrl);
		assertMultiValueMapEquals(originalRequestParams, retrievedRequestParams);
	}

	private <S, T> void assertMultiValueMapEquals(@NonNull MultiValueMap<S, T> mapA,
			@NonNull MultiValueMap<S, T> mapB) {
		if (mapA == null || mapA.isEmpty()) {
			assertTrue(mapB == null || mapB.isEmpty());
			return;
		}

		assertEquals(mapA.size(), mapB.size());

		for (Entry<S, List<T>> aEntry : mapA.entrySet()) {
			assertEquals(aEntry.getValue(), mapB.get(aEntry.getKey()));
		}
	}
}
