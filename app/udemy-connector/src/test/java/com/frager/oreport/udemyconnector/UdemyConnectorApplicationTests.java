package com.frager.oreport.udemyconnector;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:secrets.mock.properties")
class UdemyConnectorApplicationTests {

	@Test
	void contextLoads() {
	}

}
