package com.frager.oreport.mailserver;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:secrets.mock.properties")
class MailServerApplicationTests {

	@Autowired
	private Environment env;
	
	@Test
	void contextLoads() {
		assertNotNull(env);
	}

}
