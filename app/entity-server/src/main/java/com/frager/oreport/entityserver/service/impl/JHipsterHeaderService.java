package com.frager.oreport.entityserver.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.frager.oreport.entityserver.service.HeaderService;

import tech.jhipster.web.util.HeaderUtil;

/**
 * Servicio para integrar algunas funciones de front de JHipster con el servicio
 * de entity-server.
 */
@Service
public class JHipsterHeaderService implements HeaderService {

	@Value("${application.name:missingAppName}")
	private String applicationName;

	@Override
	public HttpHeaders createAlert(String message, Object param) {
		return HeaderUtil.createAlert(applicationName, message, String.valueOf(param));
	}

	@Override
	public HttpHeaders createEntityCreationAlert(Class<?> entityClass, Object param) {
		return createEntityCreationAlert(entityClass.getSimpleName(), param);
	}

	@Override
	public HttpHeaders createEntityCreationAlert(String entityName, Object param) {
		String message = "Un nuevo '" + entityName + "' fue creado con id '" + param + "'.";
		return createAlert(message, String.valueOf(param));
	}

	@Override
	public HttpHeaders createEntityDeletionAlert(Class<?> entityClass, Object param) {
		return createEntityDeletionAlert(entityClass.getSimpleName(), param);
	}

	@Override
	public HttpHeaders createEntityDeletionAlert(String entityName, Object param) {
		String message = "Un '" + entityName + "' con id '" + param + "' fue borrado.";
		return createAlert(message, String.valueOf(param));
	}
}
