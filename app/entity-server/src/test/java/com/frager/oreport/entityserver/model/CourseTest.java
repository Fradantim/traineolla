package com.frager.oreport.entityserver.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.frager.oreport.entityserver.util.TestUtil;

class CourseTest {
	@Test
	void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Course.class);
		Course entity1 = new Course();
		entity1.setId(1L);
		Course entity2 = new Course();
		entity2.setId(entity1.getId());
		assertThat(entity1).isEqualTo(entity2);
		entity2.setId(2L);
		assertThat(entity1).isNotEqualTo(entity2);
		entity1.setId(null);
		assertThat(entity1).isNotEqualTo(entity2);
	}
}