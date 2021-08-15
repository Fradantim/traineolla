package com.frager.oreport.udemyconnector.service.impl;

import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

public abstract class PagingTester {
	
	@Autowired
	private ObjectMapper defaultObjectMapper;

	protected <S> Answer<Mono<S>> buildPagedResponse(TypeReference<S> typeReference, List<String> pages ){
		return new Answer<Mono<S>>() {
			@SuppressWarnings("unchecked")
			public Mono<S> answer(InvocationOnMock invocation) {
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
							.just(defaultObjectMapper.readValue(pages.get(thePageToReturn), typeReference));
				} catch (JsonProcessingException e) {
					throw new RuntimeException("Error al serializar JSON", e);
				}
			}
		};
	}
}
