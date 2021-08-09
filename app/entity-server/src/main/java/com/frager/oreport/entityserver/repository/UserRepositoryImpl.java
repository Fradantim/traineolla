package com.frager.oreport.entityserver.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectOn;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;

import com.frager.oreport.entityserver.model.User;
import com.frager.oreport.entityserver.repository.rowmapper.ColumnConverter;
import com.frager.oreport.entityserver.repository.utils.EntityManager;
import com.frager.oreport.entityserver.repository.utils.EntityReflector;
import com.frager.oreport.entityserver.repository.utils.MultiModelRef;
import com.frager.oreport.entityserver.repository.utils.SingleModelRef;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserRepositoryImpl implements Repository<User> {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

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
		entityTable = entityReflector.getTable(getMasterClass()).as(EntityManager.ENTITY_ALIAS);
	}
	
	public Class<?> getMasterClass() {
		return User.class;
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
		try {
			logger.debug("createQuery -> SELECT (...)");
			List<Expression> columns = new ArrayList<>();
			
			entityReflector.getColumnNames(getMasterClass()).stream().forEach(c -> {
				columns.add(Column.aliased(c, entityTable, EntityManager.ENTITY_ALIAS + "_" + c));
			});

			logger.debug("createQuery -> SELECT {}", columns);
			
			Map<String, Class<?>> eagerRerences = entityReflector.getEagerRefModels(getMasterClass());
			Map<String, Table> directlyLinkedTables = new HashMap<>();
			
			if (eagerRerences != null && !eagerRerences.isEmpty()) {
				eagerRerences.forEach( (fieldName, fieldClass) -> {
					Table linkedTable = entityReflector.getTable(fieldClass).as(fieldName);
					entityReflector.getColumnNames(fieldClass).stream().forEach(c -> {
						columns.add(Column.aliased(c, linkedTable, fieldName + "_" + c));
					});
					
					directlyLinkedTables.put(fieldName, linkedTable);
					
					logger.debug("createQuery -> SELECT {}", columns);
				});
			}
			
			AtomicBoolean needsToGroup = new AtomicBoolean(false);
			
			// SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable); ORIGINAL
			
			SelectFromAndJoin originalSelectFrom = Select.builder().select(columns).from(entityTable);
			SelectFromAndJoinCondition expandedSelect = null;
			// SelectJoin selectFrom = Select.builder().select(Expressions.asterisk()).from(entityTable);
			// Select.builder().select(columns).from(entityTable).where(TrueCondition.INSTANCE);
			
			logger.debug("createQuery -> SELECT {} FROM {}", columns, entityTable);
			
			if (eagerRerences != null && !eagerRerences.isEmpty()) {
				for (Entry<String, Class<?>> entry : eagerRerences.entrySet()) {
					String fieldName = entry.getKey();
					Class<?> fieldClass = entry.getValue();

					SingleModelRef smr = entityReflector.getEagerSingleModelReferences(getMasterClass(), fieldName);
					if(smr != null) {
						// TODO en algun momento deberia probar este caso
						Table otherTable = directlyLinkedTables.get(fieldName);
						expandedSelect = addFullOuterJoin(expandedSelect != null ? expandedSelect : originalSelectFrom, smr, entityTable, otherTable);
					}
					
					MultiModelRef mmr = entityReflector.getEagerMultiModelRef(getMasterClass(), fieldName);
					if(mmr != null) {
						needsToGroup.set(true);
						
						if (mmr.getJoinColumn() != null) {
							// o2m /m2o
							// TODO en algun momento deberia probar este caso
							Table otherTable = directlyLinkedTables.get(fieldName);
							expandedSelect = addFullOuterJoin(expandedSelect != null ? expandedSelect : originalSelectFrom, mmr, entityTable, otherTable);
						}
						
						if (mmr.getJoinTable() != null) {
							// m2m
							Table midleTable = Table.create(mmr.getJoinTable().name()).as(EntityManager.ENTITY_ALIAS+"_"+fieldName);
							expandedSelect = addFullOuterJoin(expandedSelect != null ? expandedSelect : originalSelectFrom, mmr.getMiddleModelRef(), entityTable, midleTable);
							
							Table otherTable = directlyLinkedTables.get(fieldName);
							expandedSelect = addFullOuterJoin(expandedSelect != null ? expandedSelect : originalSelectFrom, mmr.getEndModelRef(), midleTable, otherTable);
						}
					}
				}
			}

			String select;
			if (expandedSelect != null) {
				select = entityManager.createSelect(expandedSelect, getMasterClass(), pageable, criteria);
			} else {
				select = entityManager.createSelect(originalSelectFrom, getMasterClass(), pageable, criteria);
			}
			
			logger.debug("SELECT STMT: {}", select);
			
			String alias = entityTable.getReferenceName().getReference();

			String selectWhere = Optional.ofNullable(criteria).map(crit -> new StringBuilder(select).append(" ")
					.append("WHERE").append(" ").append(alias).append(".").append(crit.toString()).toString())
					.orElse(select); // TODO remove once
										// https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
			return db.sql(selectWhere).map(this::process);
		} catch (Exception e) {
			logger.error("Oops " + e.getMessage());
			return new RowsFetchSpec<User>() {

				@Override
				public Mono<User> one() { return null; }

				@Override
				public Mono<User> first() { return null; }

				@Override
				public Flux<User> all() { return Flux.empty(); } 
			};
		}
	}
	
	private SelectFromAndJoinCondition addFullOuterJoin(SelectJoin select, SingleModelRef smr, Table thisTable, Table otherTable) {
		logger.debug("createQuery -> SELECT (...) LEFT OUTER JOIN {}", otherTable);
		SelectOn selectOn = select.leftOuterJoin(otherTable);
		Column thisColumn;
		Column otherColumn;

		if (smr.isReversed()) {
			thisColumn = Column.create(smr.getJoinColumn().referencedColumnName(), thisTable);
			otherColumn = Column.create(smr.getJoinColumn().name(), otherTable);
		} else {
			thisColumn = Column.create(smr.getJoinColumn().name(), thisTable);
			otherColumn = Column.create(smr.getJoinColumn().referencedColumnName(), otherTable);
		}

		logger.debug("createQuery -> SELECT (...) LEFT OUTER JOIN {} ON {}={}", otherTable, thisColumn, otherColumn);
		return selectOn.on(thisColumn).equals(otherColumn);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private User process(Row row, RowMetadata metadata) {
		logger.trace("Parsing row {}", row);
		Object entity = instanceAndFillFromColumns(getMasterClass(), EntityManager.ENTITY_ALIAS, row);

		// Atributos relacionados a entity cargados de forma eager
		entityReflector.getEagerRefModels(getMasterClass()).forEach( (fieldName, fieldClass) -> {
			Object entityEagerAtribute = instanceAndFillFromColumns(fieldClass, fieldName, row);
			Object setterArgument = null;		
			Method setter = entityReflector.getEagerReferencedModelSetter(getMasterClass(), fieldName);
			
			if(setter.getParameterTypes()[0].equals(fieldClass)) {
				setterArgument = entityEagerAtribute;
			} else if (Collection.class.isAssignableFrom(setter.getParameters()[0].getType())) {
				// collections...
				Collection collection;
				if (List.class.isAssignableFrom(setter.getParameters()[0].getType())) {
					// list
					collection = new ArrayList<>();
				} else if (Set.class.isAssignableFrom(setter.getParameters()[0].getType())) {
					// set
					collection = new HashSet<>();
				} else {
					throw new RuntimeException("Error al invocar setter por reflexion Collection desconocida, clase:"
							+ getMasterClass() + ", metodo:" + setter + ", argumento:" + setterArgument
							+ ", Collection:" + setter.getParameters()[0].getType().getSimpleName() + ".");
				}
				collection.add(entityEagerAtribute);
				setterArgument = collection;
			}
			
			if (setterArgument != null) {
				try {
					setter.invoke(entity, setterArgument);
				} catch (Exception e) {
					throw new RuntimeException("Error al invocar setter por reflexion, clase:" + getMasterClass()
							+ ", metodo:" + setter + ", argumento:" + setterArgument + ".", e);
				}
			}
		});

		return (User) entity;
	}
	
	/**
	 * Instancia un objeto de tipo S y le asigna los atributos que pueda conseguir a
	 * traves de los nombres de columnas en {@link #entityReflector}.
	 */
	private <S> S instanceAndFillFromColumns(@NotNull Class<S> newInstanceClass, String prefix, Row row) {
		S entity;
		try {
			entity = newInstanceClass.getConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error al invocar constructor sin argumentos por reflexion, clase:"
					+ newInstanceClass.getSimpleName() + ".", e);
		}

		entityReflector.getColumnNames(entity.getClass()).forEach(c -> {
			Method m = entityReflector.getColumnSetter(entity.getClass(), c);

			try {
				m.invoke(entity, converter.fromRow(row, prefix + "_" + c,
						(Class<?>) m.getParameters()[0].getParameterizedType()));
			} catch (Exception e) {
				throw new RuntimeException("Error al invocar setter por reflexion, clase:"
						+ newInstanceClass.getSimpleName() + ", metodo:" + m + ".", e);
			}
		});
		return entity;
	}

	@Override
	public Mono<Void> deleteById(Long entityId) {
		return deleteRelations(entityId)
				.then(r2dbcEntityTemplate.delete(getMasterClass()).matching(query(where("id").is(entityId))).all().then());
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