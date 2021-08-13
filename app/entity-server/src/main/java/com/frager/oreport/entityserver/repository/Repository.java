package com.frager.oreport.entityserver.repository;

import javax.annotation.Nullable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Repository<T> {
	<S extends T> Mono<S> insert(S entity);
    <S extends T> Mono<S> save(S entity);

	Flux<T> findAll();

	Mono<T> findById(Long id);

	Flux<T> findAllBy(@Nullable Pageable pageable);

	Flux<T> findAllBy(@Nullable Pageable pageable, @Nullable Criteria criteria);
	
	Mono<Void> deleteById(Long id);
}
