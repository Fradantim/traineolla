package com.frager.oreport.entityserver.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.frager.oreport.entityserver.model.Course;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CourseRepository extends R2dbcRepository<Course, Long>{

	Flux<Course> findBy(Pageable pageable);
	
	Mono<Course> findOneByUdemyId(Long udemyId);
}
