package com.udemy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Instructor extends UdemyObject {
	
	private String title;
	private String name;
	
	@JsonProperty("display_name")
	private String displayName;
	
	@JsonProperty("job_title")
	private String jobTitle;
	
	@JsonProperty("image_50x50")
	private String image50by50;
	
	@JsonProperty("image_100x100")
	private String image100by100;
	private String initials;	
	private String url;
	
	public Instructor() {
		super();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getImage50by50() {
		return image50by50;
	}
	public void setImage50by50(String image50by50) {
		this.image50by50 = image50by50;
	}
	public String getImage100by100() {
		return image100by100;
	}
	public void setImage100by100(String image100by100) {
		this.image100by100 = image100by100;
	}
	public String getInitials() {
		return initials;
	}
	public void setInitials(String initials) {
		this.initials = initials;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
