package com.frager.oreport.udemyconnector.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.frager.oreport.udemyconnector.client.UdemyClient;
import com.frager.oreport.udemyconnector.service.UserCourseActivityService;
import com.udemy.model.PageResponse;
import com.udemy.model.UserCourseActivity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserCourseActivityServiceImpl extends UdemyService implements UserCourseActivityService {

	private static final Logger logger = LoggerFactory.getLogger(UserCourseActivityServiceImpl.class);

	private UdemyClient udemyClient;

	public UserCourseActivityServiceImpl(@Autowired UdemyClient udemyClient) {
		this.udemyClient = udemyClient;
	}

	@Override
	public Flux<UserCourseActivity> getUserCourseActivity() {
		return getUserCourseActivity(null);
	}

	@Override
	public Flux<UserCourseActivity> getUserCourseActivity(MultiValueMap<String, String> queryParams) {
		Mono<PageResponse<UserCourseActivity>> userCourseActivityPageMono = udemyClient
				.getUserCourseActivity(queryParams);
		Flux<UserCourseActivity> currentFlux = userCourseActivityPageMono.flatMapMany(page -> {
			logger.debug("Transformando pagina de {} elementos", page.getCount());
			return Flux.fromIterable(page.getResults())
					.concatWith(getNextPage(page.getNext(), this::getUserCourseActivity));
		});

		if (logger.isDebugEnabled()) {
			currentFlux.log();
		}

		return currentFlux;
	}

	@Override
	public Mono<PageResponse<UserCourseActivity>> getUserCourseActivityPage(MultiValueMap<String, String> queryParams) {
		return udemyClient.getUserCourseActivity(queryParams).flatMap(this::translateUrls);
	}
}
