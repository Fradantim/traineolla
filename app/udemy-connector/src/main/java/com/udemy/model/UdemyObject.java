package com.udemy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UdemyObject {
	@JsonProperty("_class")
	private String clazz;
	
	public UdemyObject() {
	}

	/** Udemy class name */
	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
}
