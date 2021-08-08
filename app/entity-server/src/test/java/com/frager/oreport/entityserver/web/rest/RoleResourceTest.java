package com.frager.oreport.entityserver.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.frager.oreport.entityserver.model.Role;
import com.frager.oreport.entityserver.repository.RoleRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest()
@AutoConfigureWebTestClient
class RoleResourceTest {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private WebTestClient webTestClient;

	@BeforeEach
	public void initTest() {
		roleRepository.deleteAll().block();
	}

	@Test
	void createRole() throws Exception {
		Role newRole = new Role();
		newRole.setName("new-role-" + LocalDateTime.now());
		Long databaseSizeBeforeCreate = roleRepository.count().block();
		// Crear nuevo rol
		webTestClient.post().uri(RoleResource.PATH).contentType(MediaType.APPLICATION_JSON).bodyValue(newRole)
				.exchange().expectStatus().isCreated();

		// Validar que el nuevo rol existe en la BBDD
		roleRepository.findOneByName(newRole.getName()).switchIfEmpty(Mono.just(new Role())).subscribe(c -> {
			assertThat(c).isNotNull();
			assertThat(newRole.getName()).isEqualTo(c.getName());
		});

		// Validar que los numeros cierren
		assertThat(roleRepository.count().block()).isEqualTo(databaseSizeBeforeCreate + 1);
	}

	@Test
	void getAllRolesAsStream() {
		// Inicializo
		Role newRole = new Role();
		newRole.setName("new-role-" + LocalDateTime.now());
		roleRepository.save(newRole).block();

		List<Role> roleList = webTestClient.get().uri(RoleResource.PATH).accept(MediaType.APPLICATION_NDJSON).exchange()
				.expectStatus().isOk().expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
				.returnResult(Role.class).getResponseBody().filter(newRole::equals).collectList().block();

		assertThat(roleList).isNotNull().hasSize(1);
		assertThat(roleList.get(0)).isEqualTo(newRole);
	}

	@Test
	void getAllRoles() {
		// Inicializo
		Role newRole = new Role();
		newRole.setName("new-role-" + LocalDateTime.now());
		roleRepository.save(newRole).block();

		// get all
		webTestClient.get().uri(RoleResource.PATH).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.[*].name")
				.value(hasItem(newRole.getName()));
	}

	@Test
	void getAllRolesByName() {
		// Inicializo
		Role newRole = new Role();
		newRole.setName("new-role-" + LocalDateTime.now());
		roleRepository.save(newRole).block();

		// get all
		webTestClient.get().uri(RoleResource.PATH + "?name=" + newRole.getName()).accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
				.jsonPath("$.[*].name").value(hasItem(newRole.getName()));
	}

	@Test
	void getRole() {
		// Inicializo
		Role newRole = new Role();
		newRole.setName("new-role-" + LocalDateTime.now());
		newRole = roleRepository.save(newRole).block();

		webTestClient.get().uri(RoleResource.PATH + "/{id}", newRole.getId()).accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
				.jsonPath("$.name").value(is(newRole.getName()));
	}

	@Test
	void getNonExistingRole() {
		webTestClient.get().uri(RoleResource.PATH + "/{id}", Long.MAX_VALUE).accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isNotFound();
	}

	@Test
	void deleteRole() {
		// Inicializo
		Role newRole = new Role();
		newRole.setName("new-role-" + LocalDateTime.now());
		newRole = roleRepository.save(newRole).block();

		Long databaseSizeBeforeDelete = roleRepository.count().block();

		webTestClient.delete().uri(RoleResource.PATH + "/{id}", newRole.getId()).accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isNoContent();

		// Validar que los numeros cierren
		StepVerifier.create(roleRepository.findById(newRole.getId())).expectComplete();
		assertThat(roleRepository.count().block()).isEqualTo(databaseSizeBeforeDelete - 1);
	}
}
