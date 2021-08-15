package com.udemy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAnalytic extends UdemyObject {

	@JsonProperty("user_email")
	private String userEmail;

	public UserAnalytic() {
		super();
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
