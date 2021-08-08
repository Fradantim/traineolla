package com.frager.oreport.entityserver.repository;

import com.frager.oreport.entityserver.model.User;

import reactor.core.publisher.Flux;

public interface CustomUserRepository {

	Flux<User> findByLogin (String login);    
}