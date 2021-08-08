package com.frager.oreport.entityserver.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.frager.oreport.entityserver.model.User;

public interface UserRepository extends R2dbcRepository<User, String>, UserRelationalRepository {

}

interface UserRelationalRepository extends Repository<User> {
	
}