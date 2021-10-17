package com.frager.oreport.batchcoursesnapshot.service;

import java.util.Collection;

import com.frager.oreport.batchcoursesnapshot.entity.AccountCourseSnapshot;

public interface AccountCourseSnapshotService {

	/**
	 * Retorna verdadero si la entidad es candidata a ser persitida, puede alterar
	 * el estado de la entidad.
	 */
	public Boolean isApplicableEntity(AccountCourseSnapshot snapshot);

	public Collection<AccountCourseSnapshot> saveAll(Collection<AccountCourseSnapshot> buffer);
}
