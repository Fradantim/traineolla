package com.frager.oreport.entityserver.service;

import com.frager.oreport.entityserver.model.Role;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleService {

	public Mono<Role> save(Role role);

	public Mono<Role> findById(String id);

	public Flux<Role> findAll();
	
	public Mono<Void> delete(String id);
}
