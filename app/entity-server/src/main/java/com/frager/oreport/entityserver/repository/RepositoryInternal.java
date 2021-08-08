package com.frager.oreport.entityserver.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepositoryInternal<T> {
//	public <S extends T> Mono<S> insert(S entity);
//
//	public <S extends T> Mono<S> save(S entity);
//
//	public Mono<Integer> update(T entity);

	public Flux<T> findAll();

	public Mono<T> findById(Long id);

	public Flux<T> findAllBy(Pageable pageable);

	public Flux<T> findAllBy(Pageable pageable, Criteria criteria);

//	public Mono<T> findOneWithEagerRelationships(Long id);
//
//	public Flux<T> findAllWithEagerRelationships();
//
//	public Flux<T> findAllWithEagerRelationships(Pageable page);

	public Mono<Void> deleteById(Long id);
}
