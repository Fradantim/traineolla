package com.udemy.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Course extends UdemyObject {
	
	private Integer id;
	private String title;
	private String description;
	private String url;
	@JsonProperty("estimated_content_length")
	private Integer estimatedContentLength;
	@JsonProperty("num_lectures")
	private Integer numberOfLectures;
	private Map<String, Object> locale;
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
	public Integer getEstimatedContentLength() {
		return estimatedContentLength;
	}

	public void setEstimatedContentLength(Integer estimatedContentLength) {
		this.estimatedContentLength = estimatedContentLength;
	}

	/** The number of lectures this course comprises of */
	public Integer getNumberOfLectures() {
		return numberOfLectures;
	}

	public void setNumberOfLectures(Integer numberOfLectures) {
		this.numberOfLectures = numberOfLectures;
	}

	/** Indicates the language of the course (e.g. es_ES) **/
	public Map<String, Object> getLocale() {
		return locale;
	}

	public void setLocale(Map<String, Object> locale) {
		this.locale = locale;
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
