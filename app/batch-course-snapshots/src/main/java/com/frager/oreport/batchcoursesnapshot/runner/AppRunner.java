package com.frager.oreport.batchcoursesnapshot.runner;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.frager.oreport.batchcoursesnapshot.dto.UserCourseActivity;
import com.frager.oreport.batchcoursesnapshot.mapper.ActivityToSnapshotMapper;
import com.frager.oreport.batchcoursesnapshot.service.AccountCourseSnapshotService;
import com.frager.oreport.batchcoursesnapshot.service.UserCourseActivityService;

import reactor.core.publisher.Flux;

@Component
public class AppRunner implements CommandLineRunner {

	private final static Logger logger = LoggerFactory.getLogger(AppRunner.class);

	private static final String ARGUMENT_ERROR = "Se requiere exactamente un argumento para iniciar, la fecha hasta para la consulta de actividades en formato YYYY-MM-DD.";

	@Autowired
	private AccountCourseSnapshotService snapshotService;

	@Autowired
	private UserCourseActivityService activityService;

	@Autowired
	private ActivityToSnapshotMapper mapper;

	@Value("#{${transaction.size}}")
	private Integer transactionSize;

	@Override
	public void run(String... args) throws Exception {
		executeOperation();
	}

	private void executeOperation() {
		logger.info("Iniciando proceso de actualizacion de snapshots.");
		Flux<UserCourseActivity> activitiesFlux = activityService.getActivitiesBefore();

		activitiesFlux.doOnComplete(this::end).filter(mapper::isValid).map(mapper::buildFromActivity)
				.filter(snapshotService::isApplicableEntity).buffer(transactionSize).map(snapshotService::saveAll)
				.blockLast();
	}

	private void end() {
		logger.info("FIN OK!");
	}

	private void validateArgs(String... args) {
		if (args == null || args.length == 0) {
			throw new IllegalArgumentException(ARGUMENT_ERROR);
		}

		try {
			LocalDate.parse(args[0]);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(ARGUMENT_ERROR, e);
		}
	}
}