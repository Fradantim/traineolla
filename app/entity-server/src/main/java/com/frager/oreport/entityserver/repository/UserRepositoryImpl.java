package com.frager.oreport.entityserver.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;

import com.frager.oreport.entityserver.model.User;
import com.frager.oreport.entityserver.repository.rowmapper.ColumnConverter;
import com.frager.oreport.entityserver.repository.utils.EntityManager;
import com.frager.oreport.entityserver.repository.utils.EntityReflector;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserRepositoryImpl implements Repository<User> {

	private DatabaseClient db;
	private R2dbcEntityTemplate r2dbcEntityTemplate;
	private EntityManager entityManager;
	private EntityReflector entityReflector;
	private ColumnConverter converter;

	private Table entityTable;;
	//private Table roleTable = Table.aliased("role", "role");

	public UserRepositoryImpl(R2dbcEntityTemplate template, EntityManager entityManager,
			EntityReflector entityReflector, ColumnConverter converter) {
		this.db = template.getDatabaseClient();
		this.r2dbcEntityTemplate = template;
		this.entityManager = entityManager;
		this.entityReflector = entityReflector;
		this.converter = converter;
	}
	
	@PostConstruct
	void fillComputedValues() {
		entityTable = entityReflector.getTable(User.class).as(EntityManager.ENTITY_ALIAS);
	}

	@Override
	public <S extends User> Mono<S> insert(S entity) {
		return entityManager.insert(entity);
	}

	@Override
	public <S extends User> Mono<S> save(S entity) {
		if (entity.getId() == null) {
			return insert(entity);
		} else {
			return update(entity).map(numberOfUpdates -> {
				if (numberOfUpdates.intValue() <= 0) {
					throw new IllegalStateException("Unable to update User with id = " + entity.getId());
				}
				return entity;
			});
		}
	}

	private Mono<Integer> update(User entity) {
		return r2dbcEntityTemplate.update(entity).thenReturn(1);
	}

	@Override
	public Flux<User> findAll() {
		return findAllBy(null, null);
	}

	@Override
	public Mono<User> findById(Long id) {
		return createQuery(null, where("id").is(id)).one();
	}

	@Override
	public Flux<User> findAllBy(Pageable pageable) {
		return findAllBy(pageable, null);
	}

	@Override
	public Flux<User> findAllBy(Pageable pageable, Criteria criteria) {
		return createQuery(pageable, criteria).all();
	}

	private RowsFetchSpec<User> createQuery(Pageable pageable, Criteria criteria) {
		List<Expression> columns = new ArrayList<>();
		columns.addAll(entityReflector.buildColumnExpressions(User.class, entityTable, EntityManager.ENTITY_ALIAS));
		SelectFromAndJoin selectFrom = Select
	            .builder()
	            .select(columns)
	            .from(entityTable);
	            /*
	            TODO objetos relacionados
	            .leftOuterJoin(roleUserTable)
	            .on(Column.create("user_id", entityTable))
	            .equals(Column.create("role_id", roleUserTable)); */

		String select = entityManager.createSelect(selectFrom, User.class, pageable, criteria);
		String alias = entityTable.getReferenceName().getReference();

		String selectWhere = Optional.ofNullable(criteria).map(crit -> new StringBuilder(select).append(" ")
				.append("WHERE").append(" ").append(alias).append(".").append(crit.toString()).toString())
				.orElse(select); // TODO remove once
									// https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
		return db.sql(selectWhere).map(this::process);
	}
	
	private User process(Row row, RowMetadata metadata) {
		String prefix = EntityManager.ENTITY_ALIAS;
		User entity;
		try {
			entity = User.class.getConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error al invocar constructor sin argumentos por reflexion, clase:"
					+ User.class.getSimpleName() + ".", e);
		}

		entityReflector.getColumnNames(entity.getClass()).forEach(c -> {
			Method m = entityReflector.getColumnSetter(entity.getClass(), c);

			try {
				m.invoke(entity, converter.fromRow(row, prefix + "_" + c,
						(Class<?>) m.getParameters()[0].getParameterizedType()));
			} catch (Exception e) {
				throw new RuntimeException("Error al invocar setter por reflexion, clase:"
						+ entity.getClass().getSimpleName() + ", metodo:" + m + ".", e);
			}
		});

		// TODO mapear objetos relacionados
		return entity;
	}

	@Override
	public Mono<Void> deleteById(Long entityId) {
		return deleteRelations(entityId)
				.then(r2dbcEntityTemplate.delete(User.class).matching(query(where("id").is(entityId))).all().then());
	}

	protected <S extends User> Mono<S> updateRelations(S entity) {
		// Mono<Void> result = entityManager.updateLinkTable(taskLink, entity.getId(),
		// entity.getTasks().stream().map(Task::getId)).then();
		// return result.thenReturn(entity);
		// TODO destrabar
		return Mono.just(entity);
	}

	protected Mono<Void> deleteRelations(Long entityId) {
		// return entityManager.deleteFromLinkTable(taskLink, entityId);
		// throw new UnsupportedOperationException();
		// TODO destrabar
		return Mono.empty();
	    }
}