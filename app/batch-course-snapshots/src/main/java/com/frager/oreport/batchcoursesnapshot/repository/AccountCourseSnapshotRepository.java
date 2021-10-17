package com.frager.oreport.batchcoursesnapshot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.frager.oreport.batchcoursesnapshot.entity.AccountCourseSnapshot;

@Repository
public interface AccountCourseSnapshotRepository extends JpaRepository<AccountCourseSnapshot, Long> {

	/** Consulta indexada por <b>ACC_COURSE_SNAP_IDX_ACC_COURSE_DATE</b> */
	public Optional<AccountCourseSnapshot> findFirstByAccountEnterpriseIdAndUdemyCourseIdOrderBySnapshotDateTimeDesc(
			String accountEnterpriseId, Long udemyCourseId);

}
