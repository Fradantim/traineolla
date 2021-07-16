package com.udemy.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Course POJO from /organizations/{organization-id}/courses/list Udemy endpoint */
public class ListedCourse extends Course {

	/*
	"estimated_content_length_video": 360,
	*/
	private List<String> categories;
	@JsonProperty("num_videos")
	private Integer numberOfVideos;
	@JsonProperty("promo_video_url")
	private List<Video> promoVideoUrl;
	private List<String> instructors;
	private Map<String, Object> requirements;
	@JsonProperty("what_you_will_learn")
	private Map<String, Object> whatYouWillLearn;
	private Map<String, Object> images;
	@JsonProperty("mobile_native_deeplink")
	private String mobileNativeDeepLink;

	public ListedCourse() {
		super();
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	/** The number of videos contained in this course */
	public Integer getNumberOfVideos() {
		return numberOfVideos;
	}

	public void setNumberOfVideos(Integer numberOfVideos) {
		this.numberOfVideos = numberOfVideos;
	}

	/** URL of the promotional video(s) */
	public List<Video> getPromoVideoUrl() {
		return promoVideoUrl;
	}

	public void setPromoVideoUrl(List<Video> promoVideoUrl) {
		this.promoVideoUrl = promoVideoUrl;
	}

	public List<String> getInstructors() {
		return instructors;
	}

	public void setInstructors(List<String> instructors) {
		this.instructors = instructors;
	}

	/** A description of requirements for this course */
	public Map<String, Object> getRequirements() {
		return requirements;
	}

	public void setRequirements(Map<String, Object> requirements) {
		this.requirements = requirements;
	}

	/**
	 * A description of the skills and knowledge which will be learned on this
	 * course
	 */
	public Map<String, Object> getWhatYouWillLearn() {
		return whatYouWillLearn;
	}

	public void setWhatYouWillLearn(Map<String, Object> whatYouWillLearn) {
		this.whatYouWillLearn = whatYouWillLearn;
	}

	/** A list of sizes and urls pointing to the course image locations */
	public Map<String, Object> getImages() {
		return images;
	}

	public void setImages(Map<String, Object> images) {
		this.images = images;
	}

	/**
	 * This URL will open the course in the Udemy for Business native app for
	 * Android or iOS, if installed
	 */
	public String getMobileNativeDeepLink() {
		return mobileNativeDeepLink;
	}

	public void setMobileNativeDeepLink(String mobileNativeDeepLink) {
		this.mobileNativeDeepLink = mobileNativeDeepLink;
	}
}
