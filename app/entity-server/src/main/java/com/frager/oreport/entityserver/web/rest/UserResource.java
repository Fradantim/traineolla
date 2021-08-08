package com.frager.oreport.entityserver.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.frager.oreport.entityserver.model.User;
import com.frager.oreport.entityserver.service.HeaderService;
import com.frager.oreport.entityserver.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.error.BadRequestAlertException;
import tech.jhipster.web.error.LoginAlreadyUsedException;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping(UserResource.PATH)
public class UserResource {

	public static final String PATH = "/users";

	private final Logger logger = LoggerFactory.getLogger(UserResource.class);

	@Value("#{'${user.resource.allowed.roles}'.split(',')}")
	private List<String> allowedRoles;

	@Autowired
	private UserService userService;

	@Autowired
	private HeaderService headerService;

	/** Los login siempre en minuscula */
	private void parseUser(User user) {
		if (user != null) {
			if (user.getLevel() != null)
				user.setLogin(user.getLogin().toLowerCase());
			if (user.getPeopleLead() != null) {
				user.setPeopleLead(user.getPeopleLead().toLowerCase());
			}
		}
	}
	
	@GetMapping()
    public Mono<ResponseEntity<Flux<User>>> getAllUsers(ServerHttpRequest request, Pageable pageable) {
        logger.debug("REST request para consultar todos los user");

        return userService
            .count()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(userService.findAll(pageable)));
    }

	@Operation(description = "${api-docs.request.user.create.description}")
	@PostMapping()
	public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
		logger.debug("REST request para crear user : {}", user);
		if (user == null || user.getLogin() == null) {
			throw new BadRequestAlertException("Un nuevo usuario debe contener atributo login.", User.class,
					"idexists");
		}

		if (user.getRoles() != null && !user.getRoles().isEmpty()) {
			if (!user.getRoles().stream().allMatch(r -> allowedRoles.contains(r.getName()))) {
				throw new BadRequestAlertException(
						"Un nuevo usuario solo puede contener roles " + Arrays.toString(allowedRoles.toArray()) + ".",
						User.class, "idexists");
			}
		}

		parseUser(user);

		return userService.findById(user.getLogin()).hasElement().flatMap(loginExists -> {
			if (loginExists) {
				return Mono.error(new LoginAlreadyUsedException());
			}

			return userService.save(user);
		}).map(persistedUser -> {
			return ResponseEntity
					.created(UriComponentsBuilder.fromUriString(PATH).path(persistedUser.getLogin()).build().toUri())
					.headers(headerService.createAlert("userManagement.created", persistedUser.getLogin()))
					.body(persistedUser);

		});
	}
/*
@PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Mono<ResponseEntity<AdminUserDTO>> updateUser(@Valid @RequestBody AdminUserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        return userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .filter(user -> !user.getId().equals(userDTO.getId()))
            .hasElement()
            .flatMap(
                emailExists -> {
                    if (Boolean.TRUE.equals(emailExists)) {
                        return Mono.error(new EmailAlreadyUsedException());
                    }
                    return userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
                }
            )
            .filter(user -> !user.getId().equals(userDTO.getId()))
            .hasElement()
            .flatMap(
                loginExists -> {
                    if (Boolean.TRUE.equals(loginExists)) {
                        return Mono.error(new LoginAlreadyUsedException());
                    }
                    return userService.updateUser(userDTO);
                }
            )
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(
                user ->
                    ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin()))
                        .body(user)
            );
    }

  s
    

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

s
    @GetMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Mono<AdminUserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userService
            .getUserWithAuthoritiesByLogin(login)
            .map(AdminUserDTO::new)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

 
    @DeleteMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        log.debug("REST request to delete User: {}", login);
        return userService
            .deleteUser(login)
            .map(
                it -> ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "userManagement.deleted", login)).build()
            );
    }
 */
}
