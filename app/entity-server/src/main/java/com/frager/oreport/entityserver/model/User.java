package com.frager.oreport.entityserver.model;

import java.util.Collections;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Table("USER")
public class User {

	@Id
	@Column("ID")
	private Long id;
	
	@Column("LOGIN")
	private String login;
	
	@Column("LEVEL")
	private Integer level;
	
	@JsonProperty("people_lead")
	@Column("PEOPLE_LEAD")
	private String peopleLead;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			  name = "ROLE_USER", 
			  joinColumns = @JoinColumn(name = "ID", referencedColumnName="USER_ID"), 
			  inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName="ID"))
	private Set<Role> roles = Collections.emptySet();

	public User() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getPeopleLead() {
		return peopleLead;
	}

	public void setPeopleLead(String peopleLead) {
		this.peopleLead = peopleLead;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		return 37 + ((login == null) ? 0 : login.hashCode());
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return login != null && login.equals(((User) o).login);
	}
}
