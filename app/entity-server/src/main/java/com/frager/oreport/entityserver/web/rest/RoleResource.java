package com.frager.oreport.entityserver.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frager.oreport.entityserver.model.Role;
import com.frager.oreport.entityserver.service.RoleService;
import com.frager.oreport.entityserver.web.util.ResponseUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(RoleResource.PATH)
public class RoleResource {
	
	public static final String PATH = "/role";
	
	private final Logger logger = LoggerFactory.getLogger(RoleResource.class);
	
	private RoleService roleService;
	
	public RoleResource(@Autowired RoleService roleService) {
		this.roleService = roleService;
	}	

	@PostMapping()
	public Mono<ResponseEntity<Role>> createRole(@RequestBody Role role) throws URISyntaxException {
		logger.debug("REST request para crear Role : {}", role);
		return roleService.save(role).map(result -> {
			try {
				return ResponseEntity.created(new URI(PATH + "/" + role.getName()))/*.headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))*/
						.body(result);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Role> getAllCountriesAsStream() {
    	logger.debug("REST request consultando todos los roles");
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Role>> getCountry(@PathVariable String id) {
    	logger.debug("REST request consultando rol : {}", id);
        Mono<Role> country = roleService.findById(id);
        return ResponseUtil.wrapOrNotFound(country);
    }
}
