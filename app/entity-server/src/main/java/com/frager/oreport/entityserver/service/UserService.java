package com.frager.oreport.entityserver.service;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import com.frager.oreport.entityserver.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

	public Mono<User> save(User user);
	
	public Mono<User> partialUpdate(User user);

	public Mono<User> findById(String id);

	public Flux<User> findAll(@Nullable Pageable pageable);
	
	public Mono<Void> delete(String id);
	
	public Mono<Long> count();
}
