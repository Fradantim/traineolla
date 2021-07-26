package com.frager.oreport.dbutil.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import liquibase.integration.spring.SpringLiquibase;

class LiquibaseConfigTest {

	@Autowired 
	private DataSource dataSource;
	
	@Test
	void loadLiquibaseTest() {
		SpringLiquibase sLiquibase = new LiquibaseConfig().liquibase(dataSource, "");
		assertNotNull(sLiquibase);
	}
}
