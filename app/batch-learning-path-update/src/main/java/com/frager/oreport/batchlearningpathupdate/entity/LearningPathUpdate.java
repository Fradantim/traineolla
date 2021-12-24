package com.frager.oreport.batchlearningpathupdate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "LEARNING_PATH")
@Entity
public class LearningPathUpdate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "LEVEL")
  private Integer level;

  @Column(name = "TECHNOLOGY_ID")
  private Long technologyId;

  public LearningPathUpdate() {super();}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {return name;}

  public void setName(String name) {this.name = name;}

  public String getDescription() {return description;}

  public void setDescription(String description) {this.description = description;}

  public Integer getLevel() {return level;}

  public void setLevel(Integer level) {this.level = level;}

  public Long getTechnologyId() {return technologyId;}

  public void setTechnologyId(Long technologyId) {this.technologyId = technologyId;}
}
