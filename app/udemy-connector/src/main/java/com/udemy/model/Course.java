package com.udemy.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Course extends UdemyObject {

	private Integer id;
	private String title;
	private String description;
	private String url;
	@JsonProperty("estimated_content_length")
	private String estimatedContentLength;
	private List<String> categories;
	@JsonProperty("num_lectures")
	private Integer numberOfLectures;
	@JsonProperty("num_videos")
	private Integer numberOfVideos;
	@JsonProperty("promo_video_url")
	private List<Video> promoVideoUrl;
	private Map<String, Object> locale;
	private List<String> instructors;
	private Map<String, Object> requirements;
	@JsonProperty("what_you_will_learn")
	private Map<String, Object> whatYouWillLearn;
	private Map<String, Object> images;
	@JsonProperty("mobile_native_deeplink")
	private String mobileNativeDeepLink;
	@JsonProperty("num_quizzes")
	private Integer numberOfQuizzes;
	@JsonProperty("num_practice_tests")
	private Integer numberOfPracticeTests;

	public Course() {
		super();
	}

	/** Course Id, this is a unique identifier for the course */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/** HTML Field Course Description, this is the description of the course */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/** This is an estimated length of time the content would take to be consumed */
	public String getEstimatedContentLength() {
		return estimatedContentLength;
	}

	public void setEstimatedContentLength(String estimatedContentLength) {
		this.estimatedContentLength = estimatedContentLength;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	/** The number of lectures this course comprises of */
	public Integer getNumberOfLectures() {
		return numberOfLectures;
	}

	public void setNumberOfLectures(Integer numberOfLectures) {
		this.numberOfLectures = numberOfLectures;
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

	/** Indicates the language of the course (e.g. es_ES) **/
	public Map<String, Object> getLocale() {
		return locale;
	}

	public void setLocale(Map<String, Object> locale) {
		this.locale = locale;
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

	/** Number of quizzes contained in the course */
	public Integer getNumberOfQuizzes() {
		return numberOfQuizzes;
	}

	public void setNumberOfQuizzes(Integer numberOfQuizzes) {
		this.numberOfQuizzes = numberOfQuizzes;
	}

	/** Number of practice tests contained in the course */
	public Integer getNumberOfPracticeTests() {
		return numberOfPracticeTests;
	}

	public void setNumberOfPracticeTests(Integer numberOfPracticeTests) {
		this.numberOfPracticeTests = numberOfPracticeTests;
	}
}
