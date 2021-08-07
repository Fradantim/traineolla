package com.frager.oreport.entityserver.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table("ROLE")
public class Role implements Persistable<String> {

	@NotNull
	@Id
	@Column("NAME")
	private String name;

	public Role() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	@Override
	public String getId() {
		return getName();
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return this.name.equals(other.name);
	}
}
