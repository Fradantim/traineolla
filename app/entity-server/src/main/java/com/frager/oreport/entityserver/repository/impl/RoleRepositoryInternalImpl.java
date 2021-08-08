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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;

import com.frager.oreport.entityserver.model.Role;
import com.frager.oreport.entityserver.model.User;
import com.frager.oreport.entityserver.repository.RepositoryInternal;
import com.frager.oreport.entityserver.repository.rowmapper.RoleRowMapper;
import com.frager.oreport.entityserver.repository.utils.EntityManager.LinkTable;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/* public class RoleRepositoryInternalImpl implements RepositoryInternal<Role> {
	private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RoleRowMapper roleMapper;

    private static final Table entityTable = Table.aliased("job", EntityManager.ENTITY_ALIAS);
    private static final Table employeeTable = Table.aliased("employee", "employee");

    private static final LinkTable taskLink = new LinkTable("rel_job__task", "job_id", "task_id");
    
    public RoleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RoleRowMapper roleMapper
    ) {
    	System.out.println(this.getClass().getSimpleName()+" INSTANCIADO!");
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.roleMapper = roleMapper;
    }

    @Override
    public Flux<Role> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Role> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Role> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = RoleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        // columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        SelectFromAndJoin selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable);

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
    public Flux<Role> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Role> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

//    @Override
//    public Mono<Role> findOneWithEagerRelationships(Long id) {
//        return findById(id);
//    }
//
//    @Override
//    public Flux<Role> findAllWithEagerRelationships() {
//        return findAll();
//    }
//
//    @Override
//    public Flux<Role> findAllWithEagerRelationships(Pageable page) {
//        return findAllBy(page);
//    }

    private Role process(Row row, RowMetadata metadata) {
    	Role entity = roleMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Role> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Role> Mono<S> save(S entity) {
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

    @Override
    public Mono<Integer> update(Role entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(r2dbcEntityTemplate.delete(User.class).matching(query(where("id").is(entityId))).all().then());
    }

    protected <S extends Role> Mono<S> updateRelations(S entity) {
        // Mono<Void> result = entityManager.updateLinkTable(taskLink, entity.getId(), entity.getTasks().stream().map(Task::getId)).then();
    	// return result.thenReturn(entity);
    	return Mono.just(entity);
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        // return entityManager.deleteFromLinkTable(taskLink, entityId);
    	return Mono.empty();
    }
} */

class RoleSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        return columns;
    }
}
