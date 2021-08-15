package com.udemy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCourseAnalytic extends UserAnalytic {

	@JsonProperty("course_id")
	private Integer courseId;

	@JsonProperty("course_title")
	private String courseTitle;

	@JsonProperty("course_category")
	private String courseCategory;

	@JsonProperty("course_subcategory")
	private String courseSubcategory;

	public UserCourseAnalytic() {
		super();
	}

	/** Course Id, to uniquely identify the course */
	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	/** Title of the course */
	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	/**
	 * The first category of the course. <br />
	 * Ordering: content subscription categories before custom categories, more
	 * recently created categories before less recently created categories
	 */
	public String getCourseCategory() {
		return courseCategory;
	}

	public void setCourseCategory(String courseCategory) {
		this.courseCategory = courseCategory;
	}

	/** The subcategory of the {@link #getCourseCategory()} */
	public String getCourseSubcategory() {
		return courseSubcategory;
	}
}
