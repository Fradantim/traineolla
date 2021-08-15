package com.udemy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCourseAnalytic extends UserAnalytic {

	@JsonProperty("course_id")
	private Integer courseId;

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
}
