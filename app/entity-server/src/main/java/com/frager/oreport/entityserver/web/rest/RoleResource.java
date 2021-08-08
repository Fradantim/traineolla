package com.frager.oreport.entityserver.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.frager.oreport.entityserver.model.Role;
import com.frager.oreport.entityserver.service.HeaderService;
import com.frager.oreport.entityserver.service.RoleService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.reactive.ResponseUtil;

@RestController
@RequestMapping(RoleResource.PATH)
public class RoleResource {

	public static final String PATH = "/roles";

	private final Logger logger = LoggerFactory.getLogger(RoleResource.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private HeaderService headerService;

	@PostMapping()
	public Mono<ResponseEntity<Role>> createRole(@RequestBody Role role) {
		logger.debug("REST request para crear Role : {}", role);
		return roleService.save(role)
				.map(result -> ResponseEntity
						.created(UriComponentsBuilder.fromUriString(PATH).path(role.getName()).build().toUri())
						.headers(headerService.createEntityCreationAlert(Role.class, result.getId())).body(result));
	}

	@GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<Role> getAllRolesAsStream(@RequestParam(required = false) String name) {
		if (name != null) {
			logger.debug("REST request consultando todos los roles name={}", name);
			return roleService.findOneByName(name).flux();
		}
		logger.debug("REST request consultando todos los roles");
		return roleService.findAll();
	}

	@GetMapping()
	public Mono<List<Role>> getAllRoles(@RequestParam(required = false) String name) {
		return getAllRolesAsStream(name).collectList();
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Role>> getRole(@PathVariable Long id) {
		logger.debug("REST request consultando rol : {}", id);
		Mono<Role> role = roleService.findById(id);
		return ResponseUtil.wrapOrNotFound(role);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public Mono<ResponseEntity<Void>> deleteRole(@PathVariable Long id) {
		logger.debug("REST request borrar rol : {}", id);
		return roleService.delete(id).then(Mono.just(0)).map(result -> ResponseEntity.noContent()
				.headers(headerService.createEntityDeletionAlert(Role.class, id)).build());
	}
}
