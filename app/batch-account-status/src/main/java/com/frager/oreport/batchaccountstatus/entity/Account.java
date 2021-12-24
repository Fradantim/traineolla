package com.frager.oreport.batchaccountstatus.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ACCOUNT")
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ENTERPRISE_ID")
  private String enterpriseId;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "PEOPLE_LEAD_EID")
  private String peopleLeadEid;

  @Column(name = "PEOPLE_LEAD_EMAIL")
  private String peopleLeadEmail;

  @Column(name = "LEVEL")
  private Integer level;

  @Column(name = "TECHNOLOGY_ID")
  private Long technologyId;

  public Account() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEnterpriseId() {return enterpriseId;}

  public void setEnterpriseId(String enterpriseId) {this.enterpriseId = enterpriseId;}

  public String getEmail() {return email;}

  public void setEmail(String email) {this.email = email;}

  public String getPeopleLeadEid() {return peopleLeadEid;}

  public void setPeopleLeadEid(String peopleLeadEid) {this.peopleLeadEid = peopleLeadEid;}

  public String getPeopleLeadEmail() {return peopleLeadEmail;}

  public void setPeopleLeadEmail(String peopleLeadEmail) {this.peopleLeadEmail = peopleLeadEmail;}

  public Integer getLevel() {return level;}

  public void setLevel(Integer level) {this.level = level;}

  public Long getTechnologyId() {return technologyId;}

  public void setTechnologyId(Long technologyId) {this.technologyId = technologyId;}
}
