package com.udemy.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO del endpoint
 * <code>/organizations/{organization-id}/analytics/user-progress/</code> de
 * Udemy. <br />
 * Otros elementos interesantes disponibles: <br />
 * <code>
 * {"course_category": "", "course_subcategory":
 * "", "num_subscribers": 15396,
 * "avg_rating": 4.5122952, "avg_rating_recent": 4.577464, "rating": 4.577464,
 * "num_reviews": 3534, "num_reviews_recent": 120, "completion_ratio": 0,
 * "num_published_lectures": 35, "num_published_quizzes": 22,
 * "num_curriculum_items": 57, "is_private": false, "quality_status":
 * "approved", "is_banned": false, "is_published": true,"has_certificate": true,
 * "created": "2017-07-03T18:31:22Z", "published_time": "2017-07-29T01:08:59Z",
 * "instructional_level": "Intermediate Level", "instructional_level_simple":
 * "Intermediate", "content_info": "2 total hours", "organization_id": null,
 * "is_organization_only": false }
 * </code>
 */
public class UserProgress extends UserCourseAnalytic {

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
