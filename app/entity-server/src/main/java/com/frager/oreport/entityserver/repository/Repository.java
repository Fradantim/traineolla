package com.frager.oreport.entityserver.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Repository<T> {
	<S extends T> Mono<S> insert(S entity);
    <S extends T> Mono<S> save(S entity);

	Flux<T> findAll();

	Mono<T> findById(Long id);

	Flux<T> findAllBy(Pageable pageable);

	Flux<T> findAllBy(Pageable pageable, Criteria criteria);
	
	Mono<Void> deleteById(Long id);
}
