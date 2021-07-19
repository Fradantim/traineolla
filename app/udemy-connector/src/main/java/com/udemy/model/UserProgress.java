package com.udemy.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO del endpoint
 * <code>/organizations/{organization-id}/analytics/user-progress/</code> de
 * Udemy.
 */
public class UserProgress extends UserAnalytic {

	@JsonProperty("course_id")
	private Integer courseId;

	@JsonProperty("course_title")
	private String courseTitle;

	@JsonProperty("course_category")
	private String courseCategory;

	@JsonProperty("course_subcategory")
	private String courseSubcategory;

	@JsonProperty("item_id")
	private Integer itemId;

	@JsonProperty("item_type")
	private String itemType;

	@JsonProperty("item_title")
	private String itemTitle;

	@JsonProperty("item_start_time")
	private OffsetDateTime itemStartTime;

	@JsonProperty("item_completion_time")
	private OffsetDateTime itemCompletionTime;

	@JsonProperty("item_completion_ratio")
	private Double itemCompletionRatio;

	@JsonProperty("Item_final_result")
	private Double itemFinalResult;

	public UserProgress() {
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

	public void setCourseSubcategory(String courseSubcategory) {
		this.courseSubcategory = courseSubcategory;
	}

	/** Unique identifier for the lecture/quiz */
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/** Indicates if the module is a video lecture, nonvideo lecture or quiz */
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/** The title of the lecture or quiz */
	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	/**
	 * The date the user started the lecture or quiz. If the user has not started a
	 * particular lecture then there will be no row for that lecture in this report.
	 * The start date may sometimes be blank if we were not able to determine the
	 * start date for various reasons.
	 */
	public OffsetDateTime getItemStartTime() {
		return itemStartTime;
	}

	public void setItemStartTime(OffsetDateTime itemStartTime) {
		this.itemStartTime = itemStartTime;
	}

	/**
	 * The date the user completed the lecture or quiz. It will be empty if the user
	 * has not completed the quiz. If the user has viewed the lecture or taken the
	 * quiz multiple times this will be the date it was first completed. Users can
	 * mark lectures as complete if they wish. For example they may already be
	 * familiar with some of the material or instructions in a tutorial and may not
	 * need to watch all of it.
	 */
	public OffsetDateTime getItemCompletionTime() {
		return itemCompletionTime;
	}

	public void setItemCompletionTime(OffsetDateTime itemCompletionTime) {
		this.itemCompletionTime = itemCompletionTime;
	}

	/**
	 * the % of the lecture or quiz that has been completed by the user. If the user
	 * has skipped questions in a quiz or parts of a video it will be reflected
	 * here. If the user watches some parts of the video multiple times it will not
	 * impact the % complete. Occasionally the % Complete reported may be less than
	 * the actual % completed by the user. This can occur if large amounts of video
	 * were downloaded and watched offline via mobile for example as in this case
	 * not all updates may be sent.
	 */
	public Double getItemCompletionRatio() {
		return itemCompletionRatio;
	}

	public void setItemCompletionRatio(Double itemCompletionRatio) {
		this.itemCompletionRatio = itemCompletionRatio;
	}

	/**
	 * The final score of the quiz if this item is a quiz. It will be empty for
	 * lectures.
	 */
	public Double getItemFinalResult() {
		return itemFinalResult;
	}

	public void setItemFinalResult(Double itemFinalResult) {
		this.itemFinalResult = itemFinalResult;
	}
}
