package com.frager.oreport.entityserver.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

class RoleTest {

	@Test
	void equalsVerifier() throws Exception {
		Role role1 = new Role();
		role1.setName("new-role" + LocalDateTime.now());

		assertThat(role1).isEqualTo(role1).hasSameHashCodeAs(role1).isNotEqualTo(new Object());

		Role role2 = new Role();
		role2.setName(role1.getName());
		assertThat(role1).isEqualTo(role2).hasSameHashCodeAs(role2);

		role2.setName("another-role");
		assertThat(role1).isNotEqualTo(role2);

		role2.setName(null);
		assertThat(role1).isNotEqualTo(role2);

		role1.setName(null);
		assertThat(role1).isNotEqualTo(role2);
	}
}
