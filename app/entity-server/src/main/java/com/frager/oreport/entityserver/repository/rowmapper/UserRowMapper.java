package com.frager.oreport.entityserver.repository.rowmapper;

import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

import com.frager.oreport.entityserver.model.User;

import io.r2dbc.spi.Row;

@Service
public class UserRowMapper implements BiFunction<Row, String, User> {

    private final ColumnConverter converter;
    
    public UserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    @Override
    public User apply(Row row, String prefix) {
    	User entity = new User();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLogin(converter.fromRow(row, prefix + "_login", String.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", Integer.class));
        entity.setPeopleLead(converter.fromRow(row, prefix + "_people_lead", String.class));
        return entity;
    }
}
