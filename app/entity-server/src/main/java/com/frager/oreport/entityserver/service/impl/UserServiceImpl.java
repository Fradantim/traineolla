package com.frager.oreport.entityserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.frager.oreport.entityserver.model.User;
import com.frager.oreport.entityserver.repository.UserRepository;
import com.frager.oreport.entityserver.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Mono<User> save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public Mono<User> partialUpdate(User user) {
		return userRepository.findById(user.getLogin()).map(existingUser -> {
			if (user.getLevel() != null) {
				existingUser.setLevel(user.getLevel());
			}
			
			if (user.getLogin() != null) {
				existingUser.setLogin(user.getLogin());
			}
			
			if (user.getPeopleLead() != null) {
				existingUser.setPeopleLead(user.getPeopleLead());
			}
			
			if (user.getRoles() != null) {
				existingUser.setRoles(user.getRoles());
			}

			return existingUser;
		}).flatMap(userRepository::save);
	}

	@Override
	public Mono<User> findById(String id) {
		return userRepository.findById(id);
	}

	@Override
	public Flux<User> findAll(Pageable pageable) {
		return userRepository.findAllBy(pageable);
	}

	@Override
	public Mono<Void> delete(String id) {
		return userRepository.deleteById(id);
	}
	
	@Override
	public Mono<Long> count() {
		return userRepository.count();
	}
}
