package com.frager.oreport.batchcourseupdate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "COURSE")
@Entity
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "UDEMY_ID")
  private Long udemyId;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ESTIMATED_CONTENT_LENGTH")
  private Integer estimatedContentLength;

  @Column(name = "NUM_LECTURES")
  private Integer numLectures;

  @Column(name = "NUM_QUIZZES")
  private Integer numQuizzes;

  @Column(name = "NUM_PRACTICE_TESTS")
  private Integer numPracticeTests;

  @Column(name = "EXPECTED_SCORE")
  private Integer expectedScore;

  public Course() {super();}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUdemyId() {return udemyId;}

  public void setUdemyId(Long udemyId) {this.udemyId = udemyId;}

  public String getTitle() {return title;}

  public void setTitle(String title) {this.title = title;}

  public String getDescription() {return description;}

  public void setDescription(String description) {this.description = description;}

  public Integer getEstimatedContentLength() {return estimatedContentLength;}

  public void setEstimatedContentLength(Integer estimatedContentLength) {this.estimatedContentLength = estimatedContentLength;}

  public Integer getNumLectures() {return numLectures;}

  public void setNumLectures(Integer numLectures) {this.numLectures = numLectures;}

  public Integer getNumQuizzes() {return numQuizzes;}

  public void setNumQuizzes(Integer numQuizzes) {this.numQuizzes = numQuizzes;}

  public Integer getNumPracticeTests() {return numPracticeTests;}

  public void setNumPracticeTests(Integer numPracticeTests) {this.numPracticeTests = numPracticeTests;}

  public Integer getExpectedScore() {return expectedScore;}

  public void setExpectedScore(Integer expectedScore) {this.expectedScore = expectedScore;}
}
