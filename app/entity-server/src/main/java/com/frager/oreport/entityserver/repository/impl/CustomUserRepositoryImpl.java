package com.frager.oreport.entityserver.repository.impl;

import java.util.Collections;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.r2dbc.core.DatabaseClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.frager.oreport.entityserver.model.Role;
import com.frager.oreport.entityserver.model.User;
import com.frager.oreport.entityserver.repository.CustomUserRepository;

import io.r2dbc.spi.Row;
import reactor.core.publisher.Flux;

public class CustomUserRepositoryImpl implements CustomUserRepository {

	private DatabaseClient client;

	public CustomUserRepositoryImpl(DatabaseClient client) {
		System.out.println("INSTANCIADO!");
		this.client = client;
	}

	@Override
	public Flux<User> findByLogin(String login) {
		System.out.println("IN!");
		String query = "SELECT * "
				+ " FROM USER INNER JOIN ROLE_USER ON USER_ID = ID WHERE LOGIN = :login";
		return client.sql(query).bind("login", login).map(this::apply).all();
	}

	public User apply(Row row, Object o) {
		User u = new User();
		u.setId(row.get("ID", Long.class));
		u.setLogin(row.get("LOGIN", String.class));

		Long roleId = row.get("ROLE_ID", Long.class);

		Role r = new Role();
		r.setId(roleId);
		r.setName(String.valueOf(roleId));

		u.setRoles(Collections.singleton(r));
		return u;

	}
}
