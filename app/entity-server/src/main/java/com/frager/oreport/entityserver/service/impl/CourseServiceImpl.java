package com.frager.oreport.entityserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.frager.oreport.entityserver.model.Course;
import com.frager.oreport.entityserver.repository.CourseRepository;
import com.frager.oreport.entityserver.service.CourseService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseRepository courseRepository;
	
	@Override
	public Mono<Long> count(){
		return courseRepository.count();
	}

	@Override
	public Flux<Course> findAll(Pageable pageable) {
		return courseRepository.findBy(pageable);
	}

	@Override
	public Mono<Course> findById(Long id) {
		return courseRepository.findById(id);
	}

	@Override
	public Mono<Course> findByUdemyId(Long udemyId) {
		return courseRepository.findOneByUdemyId(udemyId);
	}

	@Override
	public Mono<Void> deleteById(Long id) {
		return courseRepository.deleteById(id);
	}

	@Override
	public Mono<Boolean> existsById(Long id) {
		return courseRepository.existsById(id);
	}

	@Override
	public Mono<Course> save(Course course) {
		return courseRepository.save(course);
	}
}
