package com.frager.oreport.entityserver.service;

import org.springframework.http.HttpHeaders;

public interface HeaderService {

	public HttpHeaders createEntityCreationAlert(Class<?> entityClass, String param);

	public HttpHeaders createEntityCreationAlert(String entityName, String param);
	
	public HttpHeaders createEntityDeletionAlert(Class<?> entityClass, String param);
	
	public HttpHeaders createEntityDeletionAlert(String entityName, String param);
}
