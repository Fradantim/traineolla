package com.frager.oreport.udemyconnector.web.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frager.oreport.udemyconnector.model.Course;
import com.frager.oreport.udemyconnector.service.CourseService;

import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/courses")
public class CourseResource {

	private static final Logger logger = LoggerFactory.getLogger(CourseResource.class);

	@Value("${request.udemy.params.identifier}")
	private String udemyRequestParamIdentifier;

	@Autowired
	private CourseService courseService;

	// @RequestParam(defaultValue="") Para que springdocs-openapi lo incluya como
	// argumento
	@GetMapping("/{id}")
	public Mono<Course> getById(@Parameter(description = "Id del curso a buscar") @PathVariable("id") Integer id,
			@Parameter(description = "${springdoc.api-docs.request.udemy.params.description}") @RequestParam(defaultValue = "") Map<String, String> requestParams) {
		return courseService.getCourseById(id, getUdemySpecificRequestParams(requestParams));
	}

	// @RequestParam(defaultValue="") Para que springdocs-openapi lo incluya como
	// argumento
	@GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Object getAll(
			@Parameter(description = "${springdoc.api-docs.request.udemy.params.description}") @RequestParam(defaultValue = "") Map<String, String> requestParams) {
		return courseService.getCourses(getUdemySpecificRequestParams(requestParams));
	}

	/**
	 * Retorna los request params que seran incluidos en los siguientes llamados a
	 * Udemy. Puede retornar null, un mapa vacio o uno cargado los los items que
	 * cuya key con {@value #UDEMY_REQUEST_PARAM_IDENTIFIER}, estos resultados se
	 * transformaran para que su key inicie sin
	 * {@value #UDEMY_REQUEST_PARAM_IDENTIFIER}.
	 */
	private <T> MultiValueMap<String, T> getUdemySpecificRequestParams(Map<String, T> requestParams) {
		if (requestParams == null || requestParams.isEmpty()) {
			logger.debug("No se incluyeron argumentos extra de busqueda");
			return null;
		}

		logger.debug("Argumentos extra de busqueda: {}", requestParams);
		Map<String, List<T>> udemyRequestParams = requestParams.entrySet().stream()
				.filter(e -> e.getKey().startsWith(udemyRequestParamIdentifier))
				.collect(Collectors.toMap(e -> e.getKey().substring(udemyRequestParamIdentifier.length()),
						e -> Arrays.asList(e.getValue())));

		if (udemyRequestParams.isEmpty()) {
			logger.debug("No hubieron argumentos especificos de Udemy");
			return null;
		}

		MultiValueMap<String, T> udemyRequestParamsAsMultiValueMap = new MultiValueMapAdapter<>(udemyRequestParams);
		logger.debug("Argumentos especificos de Udemy: {}", udemyRequestParamsAsMultiValueMap);
		return udemyRequestParamsAsMultiValueMap;
	}
}
