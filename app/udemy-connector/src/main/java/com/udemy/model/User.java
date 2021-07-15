package com.udemy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User extends UdemyObject {

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

	@JsonProperty("num_video_consumed_minutes")
	private Double numberOfVideoConsumedMinutes;

	public User() {
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

	/**
	 * Minutes Video Consumed, this is the total number of minutes of video lectures
	 * the user has consumed. It does not include any estimation of time spent on
	 * other materials such as slides or ebooks. If the user watches some videos
	 * multiple times then each time will contribute to the total in, this report
	 */
	public Double getNumberOfVideoConsumedMinutes() {
		return numberOfVideoConsumedMinutes;
	}

	public void setNumberOfVideoConsumedMinutes(Double numberOfVideoConsumedMinutes) {
		this.numberOfVideoConsumedMinutes = numberOfVideoConsumedMinutes;
	}
}
