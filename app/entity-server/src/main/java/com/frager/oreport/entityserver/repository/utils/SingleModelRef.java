package com.frager.oreport.entityserver.repository.utils;

import javax.annotation.Nullable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

public interface SingleModelRef {
	public Boolean mapsExternalAttribute();

	public Boolean isReversed();

	public String getMappedBy();

	public @Nullable JoinColumn getJoinColumn();

	public static SingleModelRef fromAnotation(javax.persistence.OneToOne o2o, JoinColumn jc) {
		return new SingleModelRefFromO2O(o2o, jc);
	}

	public static SingleModelRef fromAnotation(javax.persistence.OneToOne o2o, JoinColumn jc, Boolean reversed) {
		return new SingleModelRefFromO2O(o2o, jc, reversed);
	}

	public static SingleModelRef fromAnotation(javax.persistence.ManyToOne m2o, @NotNull JoinColumn jc) {
		return new SingleModelRefFromM2O(m2o, jc);
	}
}

final class SingleModelRefFromO2O implements SingleModelRef {
	OneToOne o2o;
	JoinColumn jc;
	Boolean isReversed = false;

	protected SingleModelRefFromO2O(OneToOne o2o, JoinColumn jc) {
		this.o2o = o2o;
		this.jc = jc;
	}

	protected SingleModelRefFromO2O(OneToOne o2o, JoinColumn jc, Boolean reversed) {
		this(o2o, jc);
		this.isReversed = true;
	}

	@Override
	public Boolean mapsExternalAttribute() {
		return o2o.mappedBy() != null && !o2o.mappedBy().isEmpty();
	}

	@Override
	public String getMappedBy() {
		return o2o.mappedBy();
	}

	@Override
	public JoinColumn getJoinColumn() {
		return jc;
	}

	@Override
	public Boolean isReversed() {
		return isReversed;
	}
}

final class SingleModelRefFromM2O implements SingleModelRef {
	ManyToOne m2o;
	JoinColumn jc;

	protected SingleModelRefFromM2O(ManyToOne m2o, JoinColumn jc) {
		this.m2o = m2o;
		this.jc = jc;
	}

	@Override
	public Boolean mapsExternalAttribute() {
		return false;
	}

	@Override
	public String getMappedBy() {
		return null;
	}

	@Override
	public JoinColumn getJoinColumn() {
		return jc;
	}

	@Override
	public Boolean isReversed() {
		return false;
	}
}