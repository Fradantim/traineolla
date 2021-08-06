package com.frager.oreport.entityserver.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.frager.oreport.entityserver.model.Role;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, String> {

}