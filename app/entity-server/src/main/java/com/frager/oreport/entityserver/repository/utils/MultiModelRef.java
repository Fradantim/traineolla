package com.frager.oreport.entityserver.repository.utils;

import javax.annotation.Nullable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

public interface MultiModelRef extends SingleModelRef {
	public @Nullable JoinTable getJoinTable();
	
	default public SingleModelRef getMiddleModelRef() {
		return null;
	}
	
	default public SingleModelRef getEndModelRef() {
		return null;
	}

	public static MultiModelRef fromAnotation(javax.persistence.OneToMany o2m, JoinColumn jc) {
		return new MultiModelRefFromO2M(o2m, jc);
	}

	public static MultiModelRef fromAnotation(javax.persistence.ManyToOne m2o, JoinColumn jc) {
		return new MultiModelRefFromM2O(m2o, jc);
	}

	public static MultiModelRef fromAnotation(javax.persistence.ManyToMany m2m, @NotNull JoinTable jt) {
		return new MultiModelRefFromM2M(m2m, jt);
	}
	
	public static MultiModelRef fromAnotation(javax.persistence.ManyToMany m2m, @NotNull JoinTable jt, Boolean reversed) {
		return new MultiModelRefFromM2M(m2m, jt, reversed);
	}
}

final class MultiModelRefFromO2M implements MultiModelRef {
	OneToMany o2m;
	JoinColumn jc;

	protected MultiModelRefFromO2M(OneToMany o2m, JoinColumn jc) {
		this.o2m = o2m;
		this.jc = jc;
	}
	
	@Override
	public Boolean mapsExternalAttribute() {
		return o2m.mappedBy() != null && !o2m.mappedBy().isEmpty();
	}

	@Override
	public String getMappedBy() {
		return o2m.mappedBy();
	}

	@Override
	public JoinColumn getJoinColumn() {
		return jc;
	}

	@Override
	public JoinTable getJoinTable() {
		return null;
	}
	
	@Override
	public Boolean isReversed() {
		return false;
	}
}

final class MultiModelRefFromM2O implements MultiModelRef {
	ManyToOne m2o;
	JoinColumn jc;

	protected MultiModelRefFromM2O(ManyToOne m2o, JoinColumn jc) {
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
	public JoinTable getJoinTable() {
		return null;
	}
	
	@Override
	public Boolean isReversed() {
		return true;
	}
}

final class MultiModelRefFromM2M implements MultiModelRef {
	ManyToMany m2m;
	JoinTable jt;
	Boolean reversed = false;

	protected MultiModelRefFromM2M(ManyToMany m2m, JoinTable jt) {
		this.m2m = m2m;
		this.jt = jt;
	}
	
	protected MultiModelRefFromM2M(ManyToMany m2m, JoinTable jt, Boolean reversed) {
		this(m2m, jt);
		this.reversed = reversed;
	}

	@Override
	public Boolean mapsExternalAttribute() {
		return m2m.mappedBy() != null && !m2m.mappedBy().isEmpty();
	}

	@Override
	public String getMappedBy() {
		return m2m.mappedBy();
	}

	@Override
	public JoinColumn getJoinColumn() {
		return null;
	}

	@Override
	public JoinTable getJoinTable() {
		return jt;
	}
	
	@Override
	public Boolean isReversed() {
		return reversed;
	}
	
	public void ee() {
		
	}
	
	@Override
	public SingleModelRef getMiddleModelRef() {
		MultiModelRefFromM2M ref = this;
		return new SingleModelRef() {
			@Override
			public Boolean mapsExternalAttribute() {
				return ref.mapsExternalAttribute();
			}
			
			@Override
			public Boolean isReversed() {
				return false;
			}
			
			@Override
			public String getMappedBy() {
				return ref.getMappedBy();
			}
			
			@Override
			public JoinColumn getJoinColumn() {
				return ref.isReversed() ?  ref.getJoinTable().inverseJoinColumns()[0]: ref.getJoinTable().joinColumns()[0];
			}
		};
	}
	
	@Override
	public SingleModelRef getEndModelRef() {
		MultiModelRefFromM2M ref = this;
		return new SingleModelRef() {
			@Override
			public Boolean mapsExternalAttribute() {
				return ref.mapsExternalAttribute();
			}
			
			@Override
			public Boolean isReversed() {
				return false;
			}
			
			@Override
			public String getMappedBy() {
				return ref.getMappedBy();
			}
			
			@Override
			public JoinColumn getJoinColumn() {
				return ref.isReversed() ?  ref.getJoinTable().joinColumns()[0] : ref.getJoinTable().inverseJoinColumns()[0];
			}
		};
	}
}