package com.frager.oreport.entityserver.service;

import org.springframework.http.HttpHeaders;

public interface HeaderService {

	public HttpHeaders createAlert(String message, Object param);
	
	public HttpHeaders createEntityCreationAlert(Class<?> entityClass, Object param);

	public HttpHeaders createEntityCreationAlert(String entityName, Object param);
	
	public HttpHeaders createEntityDeletionAlert(Class<?> entityClass, Object param);
	
	public HttpHeaders createEntityDeletionAlert(String entityName, Object param);
}
