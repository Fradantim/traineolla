package com.frager.oreport.entityserver.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Table("USER")
public class AdministratedUser extends User {

	@Column("PASSWORD")
	private String password;
	
	@Column("LOGIN_ENABLED")
	private Boolean loginEnabled;
	
	@JsonProperty("reset_password")
	@Column("RESET_PASSWORD")
	private String resetPassword;

	public AdministratedUser() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getLoginEnabled() {
		return loginEnabled;
	}

	public void setLoginEnabled(Boolean loginEnabled) {
		this.loginEnabled = loginEnabled;
	}

	public String getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(String resetPassword) {
		this.resetPassword = resetPassword;
	}
}
