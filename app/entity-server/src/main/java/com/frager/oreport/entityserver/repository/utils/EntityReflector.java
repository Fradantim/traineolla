package com.frager.oreport.entityserver.repository.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.stereotype.Service;

import com.frager.oreport.entityserver.model.Role;
import com.frager.oreport.entityserver.model.User;

/**
 * Clase de utilidad para obtener metadata de las entidades (tablas, columnas,
 * relaciones).
 */
@Service
public class EntityReflector {

	private static final Logger logger = LoggerFactory.getLogger(EntityReflector.class);

	public static final List<Class<? extends Annotation>> TRANSIENT_ANNOTATIONS = Arrays.asList(
			java.beans.Transient.class, javax.persistence.Transient.class,
			org.springframework.data.annotation.Transient.class);

	public static final List<Class<? extends Annotation>> RELATIONAL_ANNOTATIONS = Arrays.asList(
			javax.persistence.ManyToMany.class, javax.persistence.ManyToOne.class, javax.persistence.OneToMany.class,
			javax.persistence.OneToOne.class);

	private Map<Class<?>, Table> tableByClass;

	private Map<Class<?>, Set<String>> columnNamesByClass;
	
	private Map<Class<?>, Map<String, Method>> settersByClass;

	@PostConstruct
	public void loadEntitiesColumns() {
		logger.debug("Cache JPA inicia carga.");
		List<Class<?>> appEntities = Arrays.asList(Role.class, User.class);

		tableByClass = new HashMap<>();
		columnNamesByClass = new HashMap<>();
		settersByClass = new HashMap<>();

		appEntities.forEach(c -> {
			logger.debug("Trabajando clase:{}.", c.getSimpleName());
			tableByClass.put(c, Table.create(buildTableName(c)));
			logger.debug("Trabajando clase:{}, tabla:{}", c.getSimpleName(), tableByClass.get(c));
			columnNamesByClass.put(c, buildColumnNames(c));
			logger.debug("Trabajando clase:{}, columnas:{}", c.getSimpleName(), columnNamesByClass.get(c));
			settersByClass.put(c, buildSettersForColumns(c));			
			logger.debug("Trabajando clase:{}, setters:{}", c.getSimpleName(), settersByClass.get(c));
			
		});

		logger.debug("Cache JPA fin carga.");
	}

	public Boolean fieldIsNotTransient(Field f) {
		return !Modifier.isTransient(f.getModifiers())
				&& TRANSIENT_ANNOTATIONS.stream().noneMatch(c -> f.getAnnotation(c) != null);
	}

	public Boolean fieldDoesNotRefferToAnotherObject(Field f) {
		return RELATIONAL_ANNOTATIONS.stream().noneMatch(c -> f.getAnnotation(c) != null);
	}

	public String getColumnName(Field f) {
		javax.persistence.Column jpaColumn = f.getAnnotation(javax.persistence.Column.class);
		if (jpaColumn != null) {
			return jpaColumn.name();
		}

		org.springframework.data.relational.core.mapping.Column springDataColumn = f
				.getAnnotation(org.springframework.data.relational.core.mapping.Column.class);
		if (springDataColumn != null) {
			return springDataColumn.value();
		}

		return f.getName();
	}

	public String buildTableName(Class<?> aClass) {
		javax.persistence.Table jpaTable = aClass.getAnnotation(javax.persistence.Table.class);
		if (jpaTable != null) {
			return jpaTable.name();
		}

		org.springframework.data.relational.core.mapping.Table springDataTable = aClass
				.getAnnotation(org.springframework.data.relational.core.mapping.Table.class);
		if (springDataTable != null) {
			return springDataTable.value();
		}

		return aClass.getSimpleName();
	}

	public Table getTable(Class<?> aClass) {
		return tableByClass.get(aClass);
	}

	public Set<String> buildColumnNames(Class<?> aClass) {
		return Arrays.stream(aClass.getDeclaredFields()).filter(this::fieldIsNotTransient)
				.filter(this::fieldDoesNotRefferToAnotherObject).map(f -> getColumnName(f)).collect(Collectors.toSet());
	}
	
	public Map<String, Method> buildSettersForColumns(Class<?> aClass) {
		Map<String, Method> columnSetters = new HashMap<>();

		Arrays.stream(aClass.getDeclaredFields()).filter(this::fieldIsNotTransient)
				.filter(this::fieldDoesNotRefferToAnotherObject).forEach(f -> {
					try {
						Method m = aClass.getMethod(
								"set" + f.getName().toUpperCase().charAt(0) + f.getName().substring(1), f.getType());
						columnSetters.put(getColumnName(f), m);
					} catch (Exception e) {
						// no-op
					}
				});
		return columnSetters;
	}
	
	public Set<String> getColumnNames(Class<?> aClass) {
		return columnNamesByClass.get(aClass);
	}
	
	public Method getColumnSetter(Class<?> aClass, String columnName) {
		return settersByClass.get(aClass).get(columnName);
	}

	public List<Expression> buildColumnExpressions(Class<?> aClass, Table tableInstance, String columnPrefix) {
		return columnNamesByClass.get(aClass).stream()
				.map(c -> Column.aliased(c, tableInstance, columnPrefix + "_" + c))
				.collect(Collectors.toList());
	}
}