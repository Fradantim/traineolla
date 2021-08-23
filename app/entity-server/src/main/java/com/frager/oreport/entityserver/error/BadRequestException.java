package com.frager.oreport.entityserver.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}