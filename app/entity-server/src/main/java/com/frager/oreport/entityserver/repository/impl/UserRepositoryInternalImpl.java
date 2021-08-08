package com.frager.oreport.entityserver.repository.impl;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import org.springframework.stereotype.Service;

import com.frager.oreport.entityserver.model.User;
import com.frager.oreport.entityserver.repository.RepositoryInternal;
import com.frager.oreport.entityserver.repository.rowmapper.RoleRowMapper;
import com.frager.oreport.entityserver.repository.rowmapper.UserRowMapper;
import com.frager.oreport.entityserver.repository.utils.EntityManager;
import com.frager.oreport.entityserver.repository.utils.EntityManager.LinkTable;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// @Service
public class UserRepositoryInternalImpl implements RepositoryInternal<User> {
	private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final RoleRowMapper roleMapper;

    private static final Table entityTable = Table.aliased("job", EntityManager.ENTITY_ALIAS);
    private static final Table roleTable = Table.aliased("role", "role");
    private static final Table roleUserTable = Table.aliased("role_user", "role_user");

    private static final LinkTable roleLink = new LinkTable("role_user", "user_id", "role_id");
    
    public UserRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        RoleRowMapper roleMapper
    ) {
    	System.out.println(this.getClass().getSimpleName()+" INSTANCIADO!");
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public Flux<User> findAllBy(Pageable pageable) {
    	System.out.println("IN");
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<User> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<User> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = UserSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(RoleSqlHelper.getColumns(roleTable, "role"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(roleUserTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("role_id", roleUserTable));

        String select = entityManager.createSelect(selectFrom, User.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(
                crit ->
                    new StringBuilder(select)
                        .append(" ")
                        .append("WHERE")
                        .append(" ")
                        .append(alias)
                        .append(".")
                        .append(crit.toString())
                        .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<User> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<User> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

//    @Override
//    public Mono<User> findOneWithEagerRelationships(Long id) {
//        return findById(id);
//    }
//
//    @Override
//    public Flux<User> findAllWithEagerRelationships() {
//        return findAll();
//    }
//
//    @Override
//    public Flux<User> findAllWithEagerRelationships(Pageable page) {
//        return findAllBy(page);
//    }

    private User process(Row row, RowMetadata metadata) {
    	User entity = userMapper.apply(row, "e");
        // entity.setEmployee(roleMapper.apply(row, "employee"));
        return entity;
    }

//    @Override
    public <S extends User> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

//    @Override
    public <S extends User> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity).flatMap(savedEntity -> updateRelations(savedEntity));
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Job with id = " + entity.getId());
                        }
                        return entity;
                    }
                )
                .then(updateRelations(entity));
        }
    }

//    @Override
    public Mono<Integer> update(User entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(r2dbcEntityTemplate.delete(User.class).matching(query(where("id").is(entityId))).all().then());
    }

    protected <S extends User> Mono<S> updateRelations(S entity) {
        // Mono<Void> result = entityManager.updateLinkTable(taskLink, entity.getId(), entity.getTasks().stream().map(Task::getId)).then();
    	// return result.thenReturn(entity);
    	// throw new UnsupportedOperationException();
    	// TODO destrabar
    	return Mono.just(entity);
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        //return entityManager.deleteFromLinkTable(taskLink, entityId);
    	// throw new UnsupportedOperationException();
    	// TODO destrabar
    	return Mono.empty();
    }
}

class UserSqlHelper {
    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("login", table, columnPrefix + "_login"));
        columns.add(Column.aliased("level", table, columnPrefix + "_level"));
        columns.add(Column.aliased("people_lead", table, columnPrefix + "_people_lead"));
        return columns;
    }
}
