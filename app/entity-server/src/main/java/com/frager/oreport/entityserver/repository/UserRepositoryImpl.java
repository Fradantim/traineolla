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
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.query.UpdateMapper;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.AsteriskFromTable;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.OrderByField;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectOn;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectOrdered;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectWhereAndOr;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.core.sql.TrueCondition;
import org.springframework.data.relational.core.sql.render.SqlRenderer;
import org.springframework.r2dbc.core.DatabaseClient;

import com.frager.oreport.entityserver.model.User;
import com.frager.oreport.entityserver.repository.rowmapper.ColumnConverter;
import com.frager.oreport.entityserver.repository.utils.EntityManager;
import com.frager.oreport.entityserver.repository.utils.EntityReflector;
import com.frager.oreport.entityserver.repository.utils.MultiModelRef;
import com.frager.oreport.entityserver.repository.utils.SingleModelRef;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

public class UserRepositoryImpl implements Repository<User> {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

	private DatabaseClient db;
	private R2dbcEntityTemplate r2dbcEntityTemplate;
	private EntityManager entityManager;
	private EntityReflector entityReflector;
	private ColumnConverter converter;
	private UpdateMapper updateMapper;
	private SqlRenderer sqlRenderer;

	private Table entityTable;;
	//private Table roleTable = Table.aliased("role", "role");

	public UserRepositoryImpl(R2dbcEntityTemplate template, EntityManager entityManager,
			EntityReflector entityReflector, ColumnConverter converter, UpdateMapper updateMapper, SqlRenderer sqlRenderer) {
		this.db = template.getDatabaseClient();
		this.r2dbcEntityTemplate = template;
		this.entityManager = entityManager;
		this.entityReflector = entityReflector;
		this.converter = converter;
		this.updateMapper = updateMapper;
		this.sqlRenderer = sqlRenderer;
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
		return createAndExecuteQuery(null, where(entityReflector.getIdColumnName(getMasterClass())).is(id)).next();
	}

	@Override
	public Flux<User> findAllBy(Pageable pageable) {
		return findAllBy(pageable, null);
	}

	@Override
	public Flux<User> findAllBy(Pageable pageable, Criteria criteria) {
		return createAndExecuteQuery(pageable, criteria);
	}

	/**
	 * Retorna una sentencia sql de la forma: <br/>
	 * 
	 * <pre>
	 * SELECT this.col1, this.col2, ...
	 * FROM TABLA this
	 * WHERE (condiciones sobre columnas this.* (opcional segun el atributo criteria) )
	 * ORDER BY (ordenamientos sobre columnas this.* (opcional segun el atributo pageable) )
	 * OFFSET N ROWS FETCH NEXT M ROWS ONLY (opcional segun el atributo pageable)
	 * </pre>
	 */
	private String createTableQuery(Pageable pageable, Criteria criteria) {
		logger.debug("createTableQuery ->");
		List<Expression> columns = new ArrayList<>();

		entityReflector.getColumnNames(getMasterClass()).stream().forEach(c -> {
			// la tabla entidad la usamos sin alias
			columns.add(Column.create(c, entityTable));
		});

		logger.debug("createTableQuery -> SELECT {}", columns);

		SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

		logger.debug("createTableQuery -> SELECT (...) FROM {}", entityTable);

		if (pageable != null) {
			selectFrom = selectFrom.limitOffset(pageable.getPageSize(), pageable.getOffset());
			logger.debug("createTableQuery -> SELECT (...) PAGE {}", pageable);
		}

		SelectWhereAndOr selectFromWhere;
		if (criteria != null) {
			Condition theCondition = Conditions.just(criteria.toString());
			selectFromWhere = selectFrom.where(theCondition);
			logger.debug("createTableQuery -> SELECT (...) WHERE {}", theCondition);
		} else {
			selectFromWhere = selectFrom.where(TrueCondition.INSTANCE);
		}

		SelectOrdered originalSelectFromConditionatedSorted = null;
		if (pageable != null && pageable.getSort() != null && pageable.getSort().isSorted()) {
			RelationalPersistentEntity<?> entity = getPersistentEntity(getMasterClass());
			if (entity != null) {
				Sort sort = updateMapper.getMappedObject(pageable.getSort(), entity);
				Collection<? extends OrderByField> orderBy = createOrderByFields(entityTable, sort);
				originalSelectFromConditionatedSorted = selectFromWhere.orderBy(orderBy);
				logger.debug("createTableQuery -> SELECT (...) ORDER BY {}", orderBy);
			}
		}

		if (originalSelectFromConditionatedSorted == null) {
			originalSelectFromConditionatedSorted = selectFromWhere
					.orderBy(createOrderByField(entityTable, entityReflector.getIdColumnName(getMasterClass())));
		}
		String sqlQuery = sqlRenderer.render(originalSelectFromConditionatedSorted.build());
		logger.debug("createTableQuery -> SELECT STMT: {}", sqlQuery);
		return sqlQuery;
	}

	/**
	 * Genera y ejecuta una sentencia sql de la forma: <br/>
	 * 
	 * <pre>
	 * SELECT this.*, attr1.col1, attr1.col2, attr2.col1, ...
	 * FROM (inner-query) this
	 * LEFT OUTER JOIN TABLA_ATRIBUTO_1 attr1 ON this.id = attr1.foreignKey
	 * LEFT OUTER JOIN TABLA_ATRIBUTO_2 attr1 ON this.id = attr2.foreignKey
	 * ...
	 * </pre>
	 * 
	 * Donde <code>(inner-query)</code> es la sentencia generada por {@link #createTableQuery(Pageable, Criteria)}. <br />
	 * 
	 * En caso de que la entidad {@link #getMasterClass()} no indique relaciones eager se ejecuta 
	 * directamente el resultado de {@link #createTableQuery(Pageable, Criteria)}.
	 */
	private Flux<User> createAndExecuteQuery(Pageable pageable, Criteria criteria) {
		String tableQuery = createTableQuery(pageable, criteria);
		
		Map<String, Class<?>> eagerRerences = entityReflector.getEagerRefModels(getMasterClass());
		if (eagerRerences == null || eagerRerences.isEmpty())
			return db.sql(tableQuery).map(this::process).all();
		
		Table innerQuery = Table.create("(" + tableQuery + ")").as(EntityManager.ENTITY_ALIAS);
		List<Expression> columns = new ArrayList<>();
		
		columns.add(AsteriskFromTable.create(innerQuery));

		logger.debug("createAndExecuteQuery -> SELECT {}", columns);

		Map<String, Table> directlyLinkedTables = new HashMap<>();
		eagerRerences.forEach((fieldName, fieldClass) -> {
			Table linkedTable = entityReflector.getTable(fieldClass).as(fieldName);
			entityReflector.getColumnNames(fieldClass).stream().forEach(c -> {
				columns.add(Column.aliased(c, linkedTable, fieldName + "_" + c));
			});

			directlyLinkedTables.put(fieldName, linkedTable);

			logger.debug("createAndExecuteQuery -> SELECT {}", columns);
		});

		SelectFromAndJoin selectFrom = Select.builder().select(columns).from(innerQuery);
		
		SelectFromAndJoinCondition expandedSelect = null;
		
		Boolean needsToGroup = false;
		if (eagerRerences != null && !eagerRerences.isEmpty()) {
			for (Entry<String, Class<?>> entry : eagerRerences.entrySet()) {
				String fieldName = entry.getKey();

				SingleModelRef smr = entityReflector.getEagerSingleModelReferences(getMasterClass(), fieldName);
				if(smr != null) {
					// TODO en algun momento deberia probar este caso
					Table otherTable = directlyLinkedTables.get(fieldName);
					expandedSelect = addFullOuterJoin(getFirstNonNull(expandedSelect, selectFrom), smr, entityTable, otherTable);
				}
				
				MultiModelRef mmr = entityReflector.getEagerMultiModelRef(getMasterClass(), fieldName);
				if(mmr != null) {
					needsToGroup = true;
					
					if (mmr.getJoinColumn() != null) {
						// o2m /m2o
						// TODO en algun momento deberia probar este caso
						Table otherTable = directlyLinkedTables.get(fieldName);
						expandedSelect = addFullOuterJoin(getFirstNonNull(expandedSelect, selectFrom), mmr, entityTable, otherTable);
					}
					
					if (mmr.getJoinTable() != null) {
						// m2m
						Table midleTable = Table.create(mmr.getJoinTable().name()).as(EntityManager.ENTITY_ALIAS+"_"+fieldName);
						expandedSelect = addFullOuterJoin(getFirstNonNull(expandedSelect, selectFrom), mmr.getMiddleModelRef(), entityTable, midleTable);
						
						Table otherTable = directlyLinkedTables.get(fieldName);
						expandedSelect = addFullOuterJoin(getFirstNonNull(expandedSelect, selectFrom), mmr.getEndModelRef(), midleTable, otherTable);
					}
				}
			}
		}

		// a esta altura expandedSelect no deberia ser null
		
		SelectOrdered originalSelectFromConditionatedSorted = null;
		if (pageable != null && pageable.getSort() != null && pageable.getSort().isSorted()) {
			RelationalPersistentEntity<?> entity = getPersistentEntity(getMasterClass());
			if (entity != null) {
				Sort sort = updateMapper.getMappedObject(pageable.getSort(), entity);
				Collection<OrderByField> orderBy = createOrderByFields(entityTable, sort);
				orderBy.add(createOrderByField(entityTable, entityReflector.getIdColumnName(getMasterClass())));
				originalSelectFromConditionatedSorted = expandedSelect.orderBy(orderBy);
				logger.debug("createAndExecuteQuery -> SELECT (...) ORDER BY {}", orderBy);
			}
		}

		if (originalSelectFromConditionatedSorted == null) {
			originalSelectFromConditionatedSorted = expandedSelect
					.orderBy(createOrderByField(entityTable, entityReflector.getIdColumnName(getMasterClass())));
		}
		String querystatement = sqlRenderer.render(originalSelectFromConditionatedSorted.build());
		logger.debug("createAndExecuteQuery -> SELECT STMT: {}", querystatement);

		if(needsToGroup) {
			return db.sql(querystatement).map(this::process).all()
			.groupBy(t -> t)
            .flatMap(g -> {
            	return groupEntity(g.key(), g);
            	// return Mono.empty();
            });	
		}
		
		return db.sql(querystatement).map(this::process).all();		
	}
	
	private Mono<User> groupEntity(User key, GroupedFlux<User, User> g) {
		return g.collect(Collectors.reducing((entityA, entityB) -> {
			if (entityA.getRoles() == null)
				entityA.setRoles(new HashSet<>());
			
			if(entityA.getRoles() != null)
				entityA.getRoles().addAll(entityB.getRoles());

			return entityA;
		})).map(op -> op.get());
	}

	private SelectJoin getFirstNonNull(SelectJoin... selects) {
		for(SelectJoin select : selects)
			if (select != null)
				return select;
		return null;
	}
	
	

	private RelationalPersistentEntity<?> getPersistentEntity(Class<?> entityType) {
        return r2dbcEntityTemplate.getConverter().getMappingContext().getPersistentEntity(entityType);
    }
	
	private SelectFromAndJoinCondition addFullOuterJoin(SelectJoin select, SingleModelRef smr, Table thisTable, Table otherTable) {
		logger.debug("addFullOuterJoin -> SELECT (...) LEFT OUTER JOIN {}", otherTable);
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

		logger.debug("addFullOuterJoin -> SELECT (...) LEFT OUTER JOIN {} ON {}={}", otherTable, thisColumn, otherColumn);
		return selectOn.on(thisColumn).equals(otherColumn);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private User process(Row row, RowMetadata metadata) {
		logger.trace("Parsing row {}", row);
		Object entity = instanceAndFillFromColumns(getMasterClass(), row); // la tabla entidad la usamos sin alias
		
		// Atributos relacionados a entity cargados de forma eager
		entityReflector.getEagerRefModels(getMasterClass()).forEach( (fieldName, fieldClass) -> {
			Object entityEagerAtribute = instanceAndFillFromColumns(fieldClass, row, fieldName);
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
	 * Metodo para llamar a {@link #instanceAndFillFromColumns(Class, Row, String)}
	 * sin un prefijo.
	 */
	private <S> S instanceAndFillFromColumns(@NotNull Class<S> newInstanceClass, Row row) {
		return instanceAndFillFromColumns(newInstanceClass, row, null);
	}
	
	/**
	 * Instancia un objeto de tipo S y le asigna los atributos que pueda conseguir a
	 * traves de los nombres de columnas en {@link #entityReflector}.
	 */
	private <S> S instanceAndFillFromColumns(@NotNull Class<S> newInstanceClass, Row row, String prefix) {
		S entity;
		try {
			entity = newInstanceClass.getConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error al invocar constructor sin argumentos por reflexion, clase:"
					+ newInstanceClass.getSimpleName() + ".", e);
		}

		final String truePrefix = prefix != null ? prefix+"_" : "";
		
		entityReflector.getColumnNames(entity.getClass()).forEach(c -> {
			Method m = entityReflector.getColumnSetter(entity.getClass(), c);

			try {
				m.invoke(entity, converter.fromRow(row, truePrefix + c,
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
				.then(r2dbcEntityTemplate.delete(getMasterClass()).matching(query(where(entityReflector.getIdColumnName(getMasterClass())).is(entityId))).all().then());
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
	
    private static Collection<OrderByField> createOrderByFields(Table table, Sort sortToUse) {
        List<OrderByField> fields = new ArrayList<>();

        for (Sort.Order order : sortToUse) {
            String propertyName = order.getProperty();
            OrderByField orderByField = createOrderByField(table, propertyName);

            fields.add(order.isAscending() ? orderByField.asc() : orderByField.desc());
        }

        return fields;
    }
    
    private static OrderByField createOrderByField(Table table, String propertyName) {
        return OrderByField.from(table.column(propertyName));
    }
}