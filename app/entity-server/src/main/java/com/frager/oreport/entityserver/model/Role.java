package com.frager.oreport.entityserver.model;

import org.springframework.data.annotation.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ROLE")
public class Role {
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME")
	private String name;

	public Role() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return 31 + ((name == null) ? 0 : name.hashCode());
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return name != null && name.equals(((Role) o).name);
	}
}
