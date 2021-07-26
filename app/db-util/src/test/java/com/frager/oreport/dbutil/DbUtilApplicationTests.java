package com.frager.oreport.dbutil;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:secrets.mock.properties")
class DbUtilApplicationTests {

	@Autowired
	private Environment env;
	
	@Test
	void contextLoads() {
		assertNotNull(env);
	}

}
