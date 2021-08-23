package com.frager.oreport.entityserver.util;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtil {

	public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
		T domainObject1 = clazz.getConstructor().newInstance();
		assertThat(domainObject1.toString()).isNotNull();
		assertThat(domainObject1).isEqualTo(domainObject1).hasSameHashCodeAs(domainObject1);
		// Test con una instancia de otra clase
		Object testOtherObject = new Object();
		assertThat(domainObject1).isNotEqualTo(testOtherObject).isNotEqualTo(null);
		// Test con una instancia de la misma clase
		T domainObject2 = clazz.getConstructor().newInstance();
		assertThat(domainObject1).isNotEqualTo(domainObject2);
		// Los hashCode son el mismo por que la entidades aun no se persistieron
		assertThat(domainObject1).hasSameHashCodeAs(domainObject2);
	}
}
