package com.frager.oreport.entityserver.repository.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.FetchType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private Map<Class<?>, Map<String, Method>> columnSettersByClass;
	
	private Map<Class<?>, Map<String, Class<?>>> eagerRefModelsByClass;
	
	private Map<Class<?>, Map<String, Method>> eagerRefModelSettersByClass;

	private Map<Class<?>, Map<String, SingleModelRef>> eagerSingleModelRefByClass;

	private Map<Class<?>, Map<String, MultiModelRef>> eagerMultiModelRefByClass;

	@PostConstruct
	public void loadEntitiesColumns() {
		logger.debug("Cache JPA inicia carga.");
		List<Class<?>> appEntities = Arrays.asList(Role.class, User.class);

		tableByClass = new HashMap<>();
		columnNamesByClass = new HashMap<>();
		columnSettersByClass = new HashMap<>();
		eagerRefModelsByClass = new HashMap<>();
		eagerRefModelSettersByClass = new HashMap<>();
		eagerSingleModelRefByClass = new HashMap<>();
		eagerMultiModelRefByClass =  new HashMap<>();

		appEntities.forEach(c -> {
			logger.debug("Trabajando clase:{}.", c.getSimpleName());
			tableByClass.put(c, Table.create(buildTableName(c)));
			logger.debug("Trabajando clase:{}, tabla:{}", c.getSimpleName(), tableByClass.get(c));
			columnNamesByClass.put(c, buildColumnNames(c));
			logger.debug("Trabajando clase:{}, columnas:{}", c.getSimpleName(), columnNamesByClass.get(c));
			columnSettersByClass.put(c, buildSettersForColumns(c));
			logger.debug("Trabajando clase:{}, setters4Columns:{}", c.getSimpleName(), columnSettersByClass.get(c));
			eagerRefModelsByClass.put(c, buildEagerRelatedObjects(c));
			logger.debug("Trabajando clase:{}, eagerRefModels:{}", c.getSimpleName(), eagerRefModelsByClass.get(c));
			eagerRefModelSettersByClass.put(c, buildSettersForEagerRelatedObjects(c));
			logger.debug("Trabajando clase:{}, setters4EagerRefModels:{}", c.getSimpleName(), eagerRefModelSettersByClass.get(c));
			eagerSingleModelRefByClass.put(c, buildEagerSingleModelReferencesByClass(c));
			logger.debug("Trabajando clase:{}, eagerSingleModelRefs:{}", c.getSimpleName(), eagerSingleModelRefByClass.get(c));
			eagerMultiModelRefByClass.put(c, buildEagerMultiModelReferencesByClass(c));
			logger.debug("Trabajando clase:{}, eagerMultiModelRefs:{}", c.getSimpleName(), eagerMultiModelRefByClass.get(c));
		});

		logger.debug("Cache JPA fin carga.");
	}

	public Boolean fieldIsNotTransient(Field f) {
		return !Modifier.isTransient(f.getModifiers())
				&& TRANSIENT_ANNOTATIONS.stream().noneMatch(c -> f.getAnnotation(c) != null);
	}

	public Boolean fieldReffersToAnotherModel(Field f) {
		return RELATIONAL_ANNOTATIONS.stream().anyMatch(c -> f.getAnnotation(c) != null);
	}

	public Boolean fieldDoesNotRefferToAnotherModel(Field f) {
		return !fieldReffersToAnotherModel(f);
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
				.filter(this::fieldDoesNotRefferToAnotherModel).map(f -> getColumnName(f)).collect(Collectors.toSet());
	}

	private String getSetterName(Field f) {
		return "set" + f.getName().toUpperCase().charAt(0) + f.getName().substring(1);
	}

	public Map<String, Method> buildSettersForColumns(Class<?> aClass) {
		Map<String, Method> columnSetters = new HashMap<>();

		Arrays.stream(aClass.getDeclaredFields()).filter(this::fieldIsNotTransient)
				.filter(this::fieldDoesNotRefferToAnotherModel).forEach(f -> {
					try {
						Method m = aClass.getMethod(getSetterName(f), f.getType());
						columnSetters.put(getColumnName(f), m);
					} catch (Exception e) {
						// no-op
					}
				});
		return columnSetters;
	}
	
	public Map<String, Class<?>> buildEagerRelatedObjects(Class<?> aClass) {
		Map<String, Class<?>> relatedModels = new HashMap<>();

		Arrays.stream(aClass.getDeclaredFields()).filter(this::fieldIsNotTransient)
				.filter(this::fieldReffersToAnotherModel).filter(this::modelReferenceIsEager).forEach(f -> {
					try {
						relatedModels.put(f.getName(), getRealType(f));
					} catch (Exception e) {
						// no-op
					}
				});
		return relatedModels;
	}
	
	/**
	 * En caso de que f sea de tipo generico retorna el tipo de dato escondido, caso
	 * contrario retorna el tipo de dato de f.
	 */
	private Class<?> getRealType(Field f) throws ClassNotFoundException {
		Type type = f.getGenericType();
		if (type instanceof ParameterizedType) {
			return Class.forName(((ParameterizedType) type).getActualTypeArguments()[0].getTypeName());
		}
		return f.getType();
	}

	public Map<String, Method> buildSettersForEagerRelatedObjects(Class<?> aClass) {
		Map<String, Method> relatedModelSetters = new HashMap<>();

		Arrays.stream(aClass.getDeclaredFields()).filter(this::fieldIsNotTransient)
				.filter(this::fieldReffersToAnotherModel).filter(this::modelReferenceIsEager).forEach(f -> {
					try {
						Method m = aClass.getMethod(getSetterName(f), f.getType());
						relatedModelSetters.put(f.getName(), m);
					} catch (Exception e) {
						// no-op
					}
				});
		return relatedModelSetters;
	}
	
	private Map<String, SingleModelRef> buildEagerSingleModelReferencesByClass(Class<?> aClass) {
		Map<String, SingleModelRef> eagerSingleModelReferences = new HashMap<>();

		Arrays.stream(aClass.getDeclaredFields()).filter(this::fieldIsNotTransient)
				.filter(this::fieldReffersToAnotherModel).filter(this::modelReferenceIsEager).forEach(f -> {
					try {
						SingleModelRef smr = null;
						javax.persistence.JoinColumn jc = f.getAnnotation(javax.persistence.JoinColumn.class);

						javax.persistence.OneToOne o2o = f.getAnnotation(javax.persistence.OneToOne.class);

						if (o2o != null) {
							smr = SingleModelRef.fromAnotation(o2o, jc);
							if (smr.mapsExternalAttribute()) {
								// tengo que buscar la informacion referenciada
								Field otherClassField = getRealType(f).getField(smr.getMappedBy());
								o2o = otherClassField.getAnnotation(javax.persistence.OneToOne.class);
								jc = otherClassField.getAnnotation(javax.persistence.JoinColumn.class);
								smr = SingleModelRef.fromAnotation(o2o, jc, true);
							}
						} else {
							javax.persistence.ManyToOne m2o = f.getAnnotation(javax.persistence.ManyToOne.class);
							if (m2o != null)
								smr = SingleModelRef.fromAnotation(m2o, jc);
						}
						if (smr != null)
							eagerSingleModelReferences.put(f.getName(), smr);
					} catch (Exception e) {
						// no-op
					}
				});

		return eagerSingleModelReferences;
	}

	private Map<String, MultiModelRef> buildEagerMultiModelReferencesByClass(Class<?> aClass) {
		Map<String, MultiModelRef> eagerMultiModelReferences = new HashMap<>();

		Arrays.stream(aClass.getDeclaredFields()).filter(this::fieldIsNotTransient)
				.filter(this::fieldReffersToAnotherModel).filter(this::modelReferenceIsEager).forEach(f -> {
					try {
						MultiModelRef mmr = null;
						javax.persistence.OneToMany o2m = f.getAnnotation(javax.persistence.OneToMany.class);

						if (o2m != null) {
							javax.persistence.JoinColumn jc = f.getAnnotation(javax.persistence.JoinColumn.class);
							mmr = MultiModelRef.fromAnotation(o2m, jc);
							if (mmr.mapsExternalAttribute()) {
								// tengo que buscar la informacion referenciada
								Field otherClassField = getRealType(f).getField(mmr.getMappedBy());
								javax.persistence.ManyToOne m2o = otherClassField
										.getAnnotation(javax.persistence.ManyToOne.class);
								jc = otherClassField.getAnnotation(javax.persistence.JoinColumn.class);
								mmr = MultiModelRef.fromAnotation(m2o, jc);
							}
						} else {
							javax.persistence.ManyToMany m2m = f.getAnnotation(javax.persistence.ManyToMany.class);
							if (m2m != null) {
								javax.persistence.JoinTable jt = f.getAnnotation(javax.persistence.JoinTable.class);
								mmr = MultiModelRef.fromAnotation(m2m, jt);
								if (mmr.mapsExternalAttribute()) {
									// tengo que buscar la informacion referenciada
									Field otherClassField = getRealType(f).getField(mmr.getMappedBy());
									m2m = otherClassField.getAnnotation(javax.persistence.ManyToMany.class);
									jt = otherClassField.getAnnotation(javax.persistence.JoinTable.class);
									mmr = MultiModelRef.fromAnotation(m2m, jt, true);
								}
							}
						}

						if (mmr != null)
							eagerMultiModelReferences.put(f.getName(), mmr);
					} catch (Exception e) {
						// no-op
					}
				});

		return eagerMultiModelReferences;
	}

	private Boolean modelReferenceIsEager(Field f) {
		javax.persistence.ManyToMany m2m = f.getAnnotation(javax.persistence.ManyToMany.class);

		if (m2m != null) {
			return m2m.fetch() == FetchType.EAGER;
		}

		javax.persistence.ManyToOne m2o = f.getAnnotation(javax.persistence.ManyToOne.class);

		if (m2o != null) {
			return m2o.fetch() == FetchType.EAGER;
		}

		javax.persistence.OneToMany o2m = f.getAnnotation(javax.persistence.OneToMany.class);

		if (o2m != null) {
			return o2m.fetch() == FetchType.EAGER;
		}

		javax.persistence.OneToOne o2o = f.getAnnotation(javax.persistence.OneToOne.class);

		if (o2o != null) {
			return o2o.fetch() == FetchType.EAGER;
		}

		return false;
	}

	public Set<String> getColumnNames(Class<?> aClass) {
		return columnNamesByClass.get(aClass);
	}

	public Method getColumnSetter(Class<?> aClass, String columnName) {
		return columnSettersByClass.get(aClass).get(columnName);
	}
	
	public Method getEagerReferencedModelSetter(Class<?> aClass, Field field) {
		return getEagerReferencedModelSetter(aClass, field.getName());
	}
	
	public Map<String, Class<?>> getEagerRefModels(Class<?> aClass) {
		return eagerRefModelsByClass.get(aClass);
	}
	
	public Method getEagerReferencedModelSetter(Class<?> aClass, String fieldName) {
		return eagerRefModelSettersByClass.get(aClass).get(fieldName);
	}
	
	public SingleModelRef getEagerSingleModelReferences(Class<?> aClass, Field field) {
		return getEagerSingleModelReferences(aClass, field.getName());
	}
	
	public SingleModelRef getEagerSingleModelReferences(Class<?> aClass, String fieldName) {
		return eagerSingleModelRefByClass.get(aClass).get(fieldName);
	}
	
	public MultiModelRef getEagerMultiModelRef(Class<?> aClass, Field field) {
		return getEagerMultiModelRef(aClass, field.getName());
	}
	
	public MultiModelRef getEagerMultiModelRef(Class<?> aClass, String fieldName) {
		return eagerMultiModelRefByClass.get(aClass).get(fieldName);
	}	
}