package com.frager.oreport.batchcoursesnapshot.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ACCOUNT_COURSE_SNAPSHOT")
@Entity
public class AccountCourseSnapshot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ACCOUNT_EID")
	private String accountEnterpriseId;

	@Column(name = "UDEMY_COURSE_ID")
	private Long udemyCourseId;

	@Column(name = "SNAPSHOT_DATETIME")
	private OffsetDateTime snapshotDateTime;

	@Column(name = "PREVIOUS_SNAP")
	private Long previousSnapId;

	@Column(name = "UDEMY_ACCOUNT")
	private String udemyAccount;

	@Column(name = "COMPLETION_RATIO")
	private Integer completionRatio;

	@Column(name = "NUM_VIDEO_CONSUMED_MINUTES")
	private Integer numVideoConsumedMinutes;

	@Column(name = "COURSE_ENROLL_DATE")
	private OffsetDateTime courseEnrollDate;

	@Column(name = "COURSE_START_DATE")
	private OffsetDateTime courseStartDate;

	@Column(name = "COURSE_FIRST_COMPLETION_DATE")
	private OffsetDateTime courseFirstCompletionDate;

	@Column(name = "COURSE_COMPLETION_DATE")
	private OffsetDateTime courseCompletionDate;

	public AccountCourseSnapshot() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountEnterpriseId() {
		return accountEnterpriseId;
	}

	public void setAccountEnterpriseId(String accountEnterpriseId) {
		this.accountEnterpriseId = accountEnterpriseId;
	}

	public Long getUdemyCourseId() {
		return udemyCourseId;
	}

	public void setUdemyCourseId(Long udemyCourseId) {
		this.udemyCourseId = udemyCourseId;
	}

	public OffsetDateTime getSnapshotDateTime() {
		return snapshotDateTime;
	}

	public void setSnapshotDateTime(OffsetDateTime snapshotDateTime) {
		this.snapshotDateTime = snapshotDateTime;
	}

	public Long getPreviousSnapId() {
		return previousSnapId;
	}

	public void setPreviousSnapId(Long previousSnapId) {
		this.previousSnapId = previousSnapId;
	}

	public String getUdemyAccount() {
		return udemyAccount;
	}

	public void setUdemyAccount(String udemyAccount) {
		this.udemyAccount = udemyAccount;
	}

	public Integer getCompletionRatio() {
		return completionRatio;
	}

	public void setCompletionRatio(Integer completionRatio) {
		this.completionRatio = completionRatio;
	}

	public Integer getNumVideoConsumedMinutes() {
		return numVideoConsumedMinutes;
	}

	public void setNumVideoConsumedMinutes(Integer numVideoConsumedMinutes) {
		this.numVideoConsumedMinutes = numVideoConsumedMinutes;
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

	public OffsetDateTime getCourseFirstCompletionDate() {
		return courseFirstCompletionDate;
	}

	public void setCourseFirstCompletionDate(OffsetDateTime courseFirstCompletionDate) {
		this.courseFirstCompletionDate = courseFirstCompletionDate;
	}

	public OffsetDateTime getCourseCompletionDate() {
		return courseCompletionDate;
	}

	public void setCourseCompletionDate(OffsetDateTime courseCompletionDate) {
		this.courseCompletionDate = courseCompletionDate;
	}
}
