package com.frager.oreport.entityserver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

public class Course {

	@Id
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Long id;

	@NonNull
	@Column("UDEMY_ID")
	private Long udemyId;

	/**
	 * Atributo ignorado hasta que el driver actual pueda traducir CLOB -> String
	 * 
	 * @see <a href="https://github.com/r2dbc/r2dbc-h2/issues/202">r2dbc-h2
	 *      Issue#202</a>
	 */
	@JsonIgnore
	@Transient
	private String description;

	private String title;

	@Column("ESTIMATED_CONTENT_LENGTH")
	private Integer estimatedContentLength;

	@Column("NUM_LECTURES")
	private Integer numLectures;

	@Column("NUM_QUIZZES")
	private Integer numQuizzes;

	@Column("NUM_PRACTICE_TESTS")
	private Integer numPracticeTests;

	public Course() {
		super();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Course)) {
			return false;
		}
		return id != null && id.equals(((Course) o).id);
	}

	@Override
	public int hashCode() {
		return 31 * 1 + ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", udemyId=" + udemyId + ", description=" + description + ", title=" + title
				+ ", (...) ]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUdemyId() {
		return udemyId;
	}

	public void setUdemyId(Long udemyId) {
		this.udemyId = udemyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getEstimatedContentLength() {
		return estimatedContentLength;
	}

	public void setEstimatedContentLength(Integer estimatedContentLength) {
		this.estimatedContentLength = estimatedContentLength;
	}

	public Integer getNumLectures() {
		return numLectures;
	}

	public void setNumLectures(Integer numLectures) {
		this.numLectures = numLectures;
	}

	public Integer getNumQuizzes() {
		return numQuizzes;
	}

	public void setNumQuizzes(Integer numQuizzes) {
		this.numQuizzes = numQuizzes;
	}

	public Integer getNumPracticeTests() {
		return numPracticeTests;
	}

	public void setNumPracticeTests(Integer numPracticeTests) {
		this.numPracticeTests = numPracticeTests;
	}
}