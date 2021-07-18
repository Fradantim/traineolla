package com.frager.oreport.udemyconnector.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Course {

	private Integer id;
	private String title;
	private String description;
	private String url;
	@JsonProperty("estimated_content_length")
	private Integer estimatedContentLength;
	@JsonProperty("num_lectures")
	private Integer numberOfLectures;
	@JsonProperty("num_quizzes")
	private Integer numberOfQuizzes;
	@JsonProperty("num_practice_tests")
	private Integer numberOfPracticeTests;
	
	public Course() {
		super();
	}

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

	public Integer getEstimatedContentLength() {
		return estimatedContentLength;
	}

	public void setEstimatedContentLength(Integer estimatedContentLength) {
		this.estimatedContentLength = estimatedContentLength;
	}

	public Integer getNumberOfLectures() {
		return numberOfLectures;
	}

	public void setNumberOfLectures(Integer numberOfLectures) {
		this.numberOfLectures = numberOfLectures;
	}

	public Integer getNumberOfQuizzes() {
		return numberOfQuizzes;
	}

	public void setNumberOfQuizzes(Integer numberOfQuizzes) {
		this.numberOfQuizzes = numberOfQuizzes;
	}

	public Integer getNumberOfPracticeTests() {
		return numberOfPracticeTests;
	}

	public void setNumberOfPracticeTests(Integer numberOfPracticeTests) {
		this.numberOfPracticeTests = numberOfPracticeTests;
	}
}
