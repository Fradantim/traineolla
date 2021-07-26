package com.frager.oreport.dbutil.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.h2.server.web.WebServlet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest()
@TestPropertySource("classpath:secrets.mock.properties")
class H2ConfigTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void assertConnects() {
		assertNotNull(jdbcTemplate.queryForObject("SELECT 'A' FROM DUAL", Object.class));
	}

	@Test
	void getH2ConsoleTest() {
		// responde text / html, que RestTemplate no sabe leer, pero responde y no es
		// NotFoundException. TODO corregir en algun momento...
		// assertThrows(UnknownContentTypeException.class,
		// 		() -> new RestTemplate().getForEntity("http://localhost:" + 8080 + "/console", Object.class));
		ServletRegistrationBean<WebServlet> srBean = new H2Config().h2ServletRegistration();
		assertNotNull(srBean);
	}
}