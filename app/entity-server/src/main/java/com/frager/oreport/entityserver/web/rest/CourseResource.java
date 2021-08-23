package com.frager.oreport.entityserver.web.rest;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.frager.oreport.entityserver.error.BadRequestException;
import com.frager.oreport.entityserver.error.NotFoundException;
import com.frager.oreport.entityserver.model.Course;
import com.frager.oreport.entityserver.service.CourseService;
import com.frager.oreport.entityserver.web.util.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CourseResource.PATH)
public class CourseResource {

	public static final String PATH = "/courses";

	private final Logger logger = LoggerFactory.getLogger(CourseResource.class);

	@Autowired
	private CourseService courseService;

	@PostMapping()
	public Mono<ResponseEntity<Course>> createCourse(@RequestBody Course course) {
		logger.debug("REST request para crear un curso : {}", course);
		if (course.getId() != null) {
			throw new BadRequestException("Un nuevo curso no puede tener id.");
		}

		if (course.getUdemyId() == null) {
			throw new BadRequestException("Un nuevo curso no puede tener id de udemy nulo.");
		}

		return courseService.save(course).map(result -> {
			return ResponseEntityUtil.created(result, PATH, result.getId());
		});
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Course>> updateCourse(@PathVariable Long id, @RequestBody Course course) {
		logger.debug("REST request para actualizar un curso : {}, {}", id, course);
		if (course.getId() == null) {
			throw new BadRequestException("Un curso existente no puede carecer de id.");
		}
		if (!Objects.equals(id, course.getId())) {
			throw new BadRequestException("Esta operacion no puede cambiar el id del curso.");
		}

		return courseService.existsById(id).flatMap(exists -> {
			if (!exists) {
				return Mono.error(new NotFoundException("No se encontro un curso con id " + id + "."));
			}

			return courseService.save(course).map(result -> ResponseEntity.ok().body(result));
		});
	}

	@PageableAsQueryParam
	@GetMapping()
	public Mono<ResponseEntity<Mono<List<Course>>>> getAllCourses(@Parameter(hidden=true) Pageable pageable) {
		logger.debug("REST request para consultar todos los cursos como lista");

		return getAllCourseAsStream(pageable).map(responeSentity -> {
			return new ResponseEntity<>(responeSentity.getBody().collectList(), responeSentity.getHeaders(),
					responeSentity.getStatusCode());
		});
	}

	@PageableAsQueryParam
	@GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Mono<ResponseEntity<Flux<Course>>> getAllCourseAsStream(@Parameter(hidden=true) Pageable pageable) {
		logger.debug("REST request para consultar todos los cursos como stream");

		return ResponseEntityUtil.okPagedStream(pageable, courseService.count(), courseService::findAll);
	}

	public enum CourseQueryType {
		ID, UDEMY_ID;
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Course>> getCourse(@PathVariable Long id,
			@RequestParam(defaultValue = "ID") CourseQueryType queryType) {
		logger.debug("REST request para consultar curso : {} {}", queryType, id);
		Mono<Course> course;

		switch (queryType) {
		case ID: {
			course = courseService.findById(id);
			break;
		}
		case UDEMY_ID: {
			course = courseService.findByUdemyId(id);
			break;
		}
		default: {
			throw new BadRequestException("No hay un comportamiento definido para el "
					+ CourseQueryType.class.getSimpleName() + " " + queryType + ".");
		}
		}

		return ResponseEntityUtil.wrapOrNotFound("Error, no se conoce un curso con " + queryType + "=" + id + ".",
				course);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public Mono<ResponseEntity<Void>> deleteTask(@PathVariable Long id) {
		logger.debug("REST request para borrar curso : {}", id);
		return courseService.deleteById(id).then(Mono.just(0)).map(result -> ResponseEntity.noContent().build());
	}
}
