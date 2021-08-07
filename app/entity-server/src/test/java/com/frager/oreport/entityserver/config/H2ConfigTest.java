package com.frager.oreport.entityserver.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import io.r2dbc.h2.H2ConnectionFactory;

class H2ConfigTest {

	@Test
	void h2ConnectionFactory() {
		H2ConnectionFactory h2ConFact = new H2Config().h2ConnectionFactory("r2dbc:h2:tcp://localhost:9090/file", "sa", "");
		assertThat(h2ConFact).isNotNull();
	}
}
