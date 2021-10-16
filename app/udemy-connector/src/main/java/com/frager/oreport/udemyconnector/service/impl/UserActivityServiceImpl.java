package com.frager.oreport.udemyconnector.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.frager.oreport.udemyconnector.client.UdemyClient;
import com.frager.oreport.udemyconnector.service.UserActivityService;
import com.udemy.model.PageResponse;
import com.udemy.model.UserActivity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserActivityServiceImpl extends UdemyService implements UserActivityService {

	private static final Logger logger = LoggerFactory.getLogger(UserActivityServiceImpl.class);

	private UdemyClient udemyClient;

	public UserActivityServiceImpl(@Autowired UdemyClient udemyClient) {
		this.udemyClient = udemyClient;
	}

	@Override
	public Flux<UserActivity> getUserActivity() {
		return getUserActivity(null);
	}

	@Override
	public Flux<UserActivity> getUserActivity(MultiValueMap<String, String> queryParams) {
		Mono<PageResponse<UserActivity>> userActivityPageMono = udemyClient.getUserActivity(queryParams);
		Flux<UserActivity> currentFlux = userActivityPageMono.flatMapMany(page -> {
			if (logger.isDebugEnabled()) {
				Integer pageSize = (page.getResults() != null) ? page.getResults().size() : 0;
				logger.debug("Transformando pagina de {} elementos de un total de {}", pageSize, page.getCount());
			}
			return Flux.fromIterable(page.getResults()).concatWith(getNextPage(page.getNext(), this::getUserActivity));
		});

		if (logger.isDebugEnabled()) {
			currentFlux.log();
		}

		return currentFlux;
	}
}
