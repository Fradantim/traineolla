package com.udemy.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCourseActivity extends User {

	@JsonProperty("user_surname")
	private Integer courseId;

	@JsonProperty("course_title")
	private String courseTitle;

	@JsonProperty("course_category")
	private String courseCategory;

	@JsonProperty("course_subcategory")
	private String courseSubcategory;

	@JsonProperty("course_duration")
	private Double courseDuration;

	@JsonProperty("completion_ratio")
	private Double completionRatio;

	@JsonProperty("course_enroll_date")
	private LocalDateTime courseEnrollDate;

	@JsonProperty("course_start_date")
	private LocalDateTime courseStartDate;

	@JsonProperty("course_completion_date")
	private LocalDateTime courseCompletionDate;

	@JsonProperty("course_first_completion_date")
	private LocalDateTime courseFirstCompletionDate;

	@JsonProperty("course_last_accessed_date")
	private LocalDateTime courselastAccessedDate;

	@JsonProperty("is_assigned")
	private String isAssigned;

	@JsonProperty("assigned_by")
	private String assignedBy;

	public UserCourseActivity() {
		super();
	}

	/** The Course Id to uniquely identify the course */
	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	/** The title of the course */
	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	/**
	 * Indicates the first category of the course. Ordering: content subscription
	 * categories before custom categories, more recently created categories before
	 * less recently created categories
	 */
	public String getCourseCategory() {
		return courseCategory;
	}

	public void setCourseCategory(String courseCategory) {
		this.courseCategory = courseCategory;
	}

	/** Indicates the subcategory of the {@link #getCourseCategory()} */
	public String getCourseSubcategory() {
		return courseSubcategory;
	}

	public void setCourseSubcategory(String courseSubcategory) {
		this.courseSubcategory = courseSubcategory;
	}

	/** Total duration of the video content of the course in minutes */
	public Double getCourseDuration() {
		return courseDuration;
	}

	public void setCourseDuration(Double courseDuration) {
		this.courseDuration = courseDuration;
	}

	/**
	 * The % of modules in the course that have been marked completed by the user.
	 * This includes non video lectures and quizzes
	 */
	public Double getCompletionRatio() {
		return completionRatio;
	}

	public void setCompletionRatio(Double completionRatio) {
		this.completionRatio = completionRatio;
	}

	/** The date/time the user enrolled in the course */
	public LocalDateTime getCourseEnrollDate() {
		return courseEnrollDate;
	}

	public void setCourseEnrollDate(LocalDateTime courseEnrollDate) {
		this.courseEnrollDate = courseEnrollDate;
	}

	/** The date/time the user started the course */
	public LocalDateTime getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(LocalDateTime courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	/** The date/time that the course was completed by the user */
	public LocalDateTime getCourseCompletionDate() {
		return courseCompletionDate;
	}

	public void setCourseCompletionDate(LocalDateTime courseCompletionDate) {
		this.courseCompletionDate = courseCompletionDate;
	}

	/** The first date/time that the course was completed by the user */
	public LocalDateTime getCourseFirstCompletionDate() {
		return courseFirstCompletionDate;
	}

	public void setCourseFirstCompletionDate(LocalDateTime courseFirstCompletionDate) {
		this.courseFirstCompletionDate = courseFirstCompletionDate;
	}

	/** The date/time the user last accessed the course */
	public LocalDateTime getCourselastAccessedDate() {
		return courselastAccessedDate;
	}

	public void setCourselastAccessedDate(LocalDateTime courselastAccessedDate) {
		this.courselastAccessedDate = courselastAccessedDate;
	}

	/**
	 * "Yes" indicates that the user was assigned the course. "No" indicates the
	 * user enrolled in the course themselves. Courses that were assigned before the
	 * 19th March 2015 will not be recorded as assigned.
	 */
	public String getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(String isAssigned) {
		this.isAssigned = isAssigned;
	}

	/**
	 * Provides the email address of the admin user who assigned the course to the
	 * user, if it was Assigned. It will be empty if the user enrolled in the course
	 * themselves.
	 */
	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}
}
