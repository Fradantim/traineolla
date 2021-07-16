package com.udemy.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserActivity extends User {

	@JsonProperty("report_date")
	private OffsetDateTime reportDate;

	@JsonProperty("user_joined_date")
	private OffsetDateTime joinDate;

	@JsonProperty("num_new_enrolled_courses")
	private Integer numberOfNewEnrolledCourses;

	@JsonProperty("num_new_assigned_courses")
	private Integer numberOfNewAssignedCourses;

	@JsonProperty("num_new_started_courses")
	private Integer numberOfNewStartedCourses;

	@JsonProperty("num_completed_courses")
	private Integer numberOfCompletedCourses;

	@JsonProperty("num_completed_lectures")
	private Integer numberOfCompletedLectures;

	@JsonProperty("num_web_visited_days")
	private Double numberOfWebVisitedDays;

	@JsonProperty("last_date_visit")
	private OffsetDateTime lastVisitDate;
	
	@JsonProperty("num_video_consumed_minutes")
	private Double numberOfVideoConsumedMinutes;

	public UserActivity() {
	}

	public OffsetDateTime getReportDate() {
		return reportDate;
	}

	public void setReportDate(OffsetDateTime reportDate) {
		this.reportDate = reportDate;
	}

	/** This is the date the user joined the portal */
	public OffsetDateTime getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(OffsetDateTime joinDate) {
		this.joinDate = joinDate;
	}

	/**
	 * No. of New Courses Enrolled, this is the number of courses the user has
	 * enrolled in (during the timeframe specified)
	 */
	public Integer getNumberOfNewEnrolledCourses() {
		return numberOfNewEnrolledCourses;
	}

	public void setNumberOfNewEnrolledCourses(Integer numberOfNewEnrolledCourses) {
		this.numberOfNewEnrolledCourses = numberOfNewEnrolledCourses;
	}

	/**
	 * No. of Assigned Courses - This indicates the number of courses that were
	 * assigned. Courses that were assigned before 19th March 2015 are not included
	 * in this count
	 */
	public Integer getNumberOfNewAssignedCourses() {
		return numberOfNewAssignedCourses;
	}

	public void setNumberOfNewAssignedCourses(Integer numberOfNewAssignedCourses) {
		this.numberOfNewAssignedCourses = numberOfNewAssignedCourses;
	}

	/**
	 * No. of Courses Started, this is the number of courses the user has started
	 * (during the timeframe specified)
	 */
	public Integer getNumberOfNewStartedCourses() {
		return numberOfNewStartedCourses;
	}

	public void setNumberOfNewStartedCourses(Integer numberOfNewStartedCourses) {
		this.numberOfNewStartedCourses = numberOfNewStartedCourses;
	}

	/**
	 * No. of Courses Completed, this is the number of courses the user has
	 * completed (during the timeframe specified)
	 */
	public Integer getNumberOfCompletedCourses() {
		return numberOfCompletedCourses;
	}

	public void setNumberOfCompletedCourses(Integer numberOfCompletedCourses) {
		this.numberOfCompletedCourses = numberOfCompletedCourses;
	}

	/**
	 * No. of Lectures Completed, this is the number of lectures the user has
	 * completed (during the timeframe specified)
	 */
	public Integer getNumberOfCompletedLectures() {
		return numberOfCompletedLectures;
	}

	public void setNumberOfCompletedLectures(Integer numberOfCompletedLectures) {
		this.numberOfCompletedLectures = numberOfCompletedLectures;
	}

	/**
	 * No. of Days visited (via Web), this is the number of days that the user
	 * visited the UfB portal via the Web
	 */
	public Double getNumberOfWebVisitedDays() {
		return numberOfWebVisitedDays;
	}

	public void setNumberOfWebVisitedDays(Double numberOfWebVisitedDays) {
		this.numberOfWebVisitedDays = numberOfWebVisitedDays;
	}

	/**
	 * Date of Last Visit (UTC), this is the date that the user last visited the UfB
	 * portal
	 */
	public OffsetDateTime getLastVisitDate() {
		return lastVisitDate;
	}

	public void setLastVisitDate(OffsetDateTime lastVisitDate) {
		this.lastVisitDate = lastVisitDate;
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
