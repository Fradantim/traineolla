package com.frager.oreport.batchcoursesnapshot.service.impl;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frager.oreport.batchcoursesnapshot.entity.AccountCourseSnapshot;
import com.frager.oreport.batchcoursesnapshot.repository.AccountCourseSnapshotRepository;
import com.frager.oreport.batchcoursesnapshot.service.AccountCourseSnapshotService;

@Service
public class AccountCourseSnapshotServiceImpl implements AccountCourseSnapshotService {

	private static final Logger logger = LoggerFactory.getLogger(AccountCourseSnapshotServiceImpl.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AccountCourseSnapshotRepository snapshotRepository;

	public Boolean isApplicableEntity(AccountCourseSnapshot snapshot) {
		if (logger.isTraceEnabled()) {
			try {
				logger.trace("Procesando item: {}", objectMapper.writeValueAsString(snapshot));
			} catch (JsonProcessingException e) {
				logger.error("Error al transformar objeto snapshot a json para debug.", e);
			}
		}

		Optional<AccountCourseSnapshot> previousSnapshot = snapshotRepository
				.findFirstByAccountEnterpriseIdAndUdemyCourseIdOrderBySnapshotDateTimeDesc(
						snapshot.getAccountEnterpriseId(), snapshot.getUdemyCourseId());

		if (!previousSnapshot.isPresent() || hasDifferentInfo(previousSnapshot.get(), snapshot)) {
			// no tengo info previa, o tengo nueva info que agregar
			if (previousSnapshot.isPresent()) {
				snapshot.setPreviousSnapId(previousSnapshot.get().getId());
			}

			return true;
		}

		return false;
	}

	@Override
	public Collection<AccountCourseSnapshot> saveAll(Collection<AccountCourseSnapshot> buffer) {
		if (buffer != null && !buffer.isEmpty()) {
			logger.info("Persistiendo {} snapshots nuevos...", buffer.size());
			snapshotRepository.saveAll(buffer);
			logger.debug("Persistidos OK!");
		}

		return buffer;
	}

	private static Boolean hasDifferentInfo(AccountCourseSnapshot snapA, AccountCourseSnapshot snapB) {
		if (!offsetDateTimeEquals(snapA.getCourseCompletionDate(), snapB.getCourseCompletionDate())
				|| !offsetDateTimeEquals(snapA.getCourseFirstCompletionDate(), snapB.getCourseFirstCompletionDate())
				|| !Objects.equals(snapA.getNumVideoConsumedMinutes(), snapB.getNumVideoConsumedMinutes())
				|| !Objects.equals(snapA.getCompletionRatio(), snapB.getCompletionRatio()))
			return true;

		return false;
	}

	/**
	 * Si las dos fechas son las mismas, pero indicadas en distintas fechas horarias
	 * el {@link OffsetDateTime#equals(Object)} retorna false, con esto se
	 * salvaguarda un poco eso.
	 */
	private static Boolean offsetDateTimeEquals(OffsetDateTime a, OffsetDateTime b) {
		return Objects.equals(a, b) || (a != null && a.isEqual(b));
	}
}
