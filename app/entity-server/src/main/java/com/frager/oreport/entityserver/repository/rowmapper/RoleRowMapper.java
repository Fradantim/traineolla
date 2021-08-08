package com.frager.oreport.entityserver.repository.rowmapper;

import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

import com.frager.oreport.entityserver.model.Role;

import io.r2dbc.spi.Row;

@Service
public class RoleRowMapper implements BiFunction<Row, String, Role> {

    private final ColumnConverter converter;

    public RoleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    @Override
    public Role apply(Row row, String prefix) {
    	Role entity = new Role();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
