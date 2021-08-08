package com.frager.oreport.entityserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frager.oreport.entityserver.model.Role;
import com.frager.oreport.entityserver.repository.RoleRepository;
import com.frager.oreport.entityserver.service.RoleService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RoleServiceImpl implements RoleService {

	private RoleRepository roleRepository;

	public RoleServiceImpl(@Autowired RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Mono<Role> save(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Mono<Role> findById(Long id) {
		return roleRepository.findById(id);
	}

	@Override
	public Flux<Role> findAll() {
		return roleRepository.findAll();
	}
	
	@Override
	public Mono<Void> delete(Long id) {
		return roleRepository.deleteById(id);
	}
}
