package com.udemy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAnalytic extends UdemyObject {

	@JsonProperty("user_name")
	private String username;

	@JsonProperty("user_surname")
	private String userSurname;

	@JsonProperty("user_email")
	private String userEmail;

	@JsonProperty("user_role")
	private String userRole;

	@JsonProperty("user_is_deactivated")
	private Boolean deactivated;

	@JsonProperty("user_external_id")
	private String userExternaId;

	public UserAnalytic() {
		super();
	}

	/** User First Name, this is the first name of the user */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/** User Last Name, this is the last name of the user */
	public String getUserSurname() {
		return userSurname;
	}

	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/** This is the user’s role. Possible values are student, admin or owner. */
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * Indicates if user’s account has been deactivated in the organization site.
	 */
	public Boolean isDeactivated() {
		return deactivated;
	}

	public void setDeactivated(Boolean deactivated) {
		this.deactivated = deactivated;
	}

	/**
	 * This is the ID specific to the organization to make it easier to identify
	 * users in reports
	 */
	public String getUserExternaId() {
		return userExternaId;
	}

	public void setUserExternaId(String userExternaId) {
		this.userExternaId = userExternaId;
	}
}
