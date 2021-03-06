package com.udemy.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO del endpoint
 * <code>/organizations/{organization-id}/analytics/user-course-activity</code>
 * de Udemy.
 */
public class UserCourseActivity extends UserCourseAnalytic {

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
	public OffsetDateTime getCourseEnrollDate() {
		return courseEnrollDate;
	}

	public void setCourseEnrollDate(OffsetDateTime courseEnrollDate) {
		this.courseEnrollDate = courseEnrollDate;
	}

	/** The date/time the user started the course */
	public OffsetDateTime getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(OffsetDateTime courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	/** The date/time that the course was completed by the user */
	public OffsetDateTime getCourseCompletionDate() {
		return courseCompletionDate;
	}

	public void setCourseCompletionDate(OffsetDateTime courseCompletionDate) {
		this.courseCompletionDate = courseCompletionDate;
	}

	/** The first date/time that the course was completed by the user */
	public OffsetDateTime getCourseFirstCompletionDate() {
		return courseFirstCompletionDate;
	}

	public void setCourseFirstCompletionDate(OffsetDateTime courseFirstCompletionDate) {
		this.courseFirstCompletionDate = courseFirstCompletionDate;
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
