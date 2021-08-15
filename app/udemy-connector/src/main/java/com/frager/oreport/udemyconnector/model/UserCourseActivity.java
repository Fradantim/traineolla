package com.frager.oreport.udemyconnector.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCourseActivity {
	
	@JsonProperty("course_id")
	private Integer courseId;
	
	@JsonProperty("user_email")
	private String userEmail;
	
	@JsonProperty("completion_ratio")
	private Double completionRatio;
	
	@JsonProperty("course_enroll_date")
	private OffsetDateTime courseEnrollDate;

	@JsonProperty("course_start_date")
	private OffsetDateTime courseStartDate;

	@JsonProperty("course_completion_date")
	private OffsetDateTime courseCompletionDate;

	@JsonProperty("course_first_completion_date")
	private OffsetDateTime courseFirstCompletionDate;
	
	@JsonProperty("num_video_consumed_minutes")
	private Double numberOfVideoConsumedMinutes;
	
	public UserCourseActivity() {
		super();
	}

	public Double getCompletionRatio() {
		return completionRatio;
	}

	public void setCompletionRatio(Double completionRatio) {
		this.completionRatio = completionRatio;
	}

	public OffsetDateTime getCourseEnrollDate() {
		return courseEnrollDate;
	}

	public void setCourseEnrollDate(OffsetDateTime courseEnrollDate) {
		this.courseEnrollDate = courseEnrollDate;
	}

	public OffsetDateTime getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(OffsetDateTime courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public OffsetDateTime getCourseCompletionDate() {
		return courseCompletionDate;
	}

	public void setCourseCompletionDate(OffsetDateTime courseCompletionDate) {
		this.courseCompletionDate = courseCompletionDate;
	}

	public OffsetDateTime getCourseFirstCompletionDate() {
		return courseFirstCompletionDate;
	}

	public void setCourseFirstCompletionDate(OffsetDateTime courseFirstCompletionDate) {
		this.courseFirstCompletionDate = courseFirstCompletionDate;
	}

	public Double getNumberOfVideoConsumedMinutes() {
		return numberOfVideoConsumedMinutes;
	}

	public void setNumberOfVideoConsumedMinutes(Double numberOfVideoConsumedMinutes) {
		this.numberOfVideoConsumedMinutes = numberOfVideoConsumedMinutes;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
