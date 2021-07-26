package com.frager.oreport.dbutil.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class LiquibaseConfig {

	private static final Logger logger = LoggerFactory.getLogger(LiquibaseConfig.class);

	@ConditionalOnProperty("spring.liquibase.enabled")
	@Bean
	public SpringLiquibase liquibase(@Autowired DataSource dataSource,
			@Value("${spring.liquibase.changelog}") String liquibaseChangelog) {
		logger.info("Iniciando liquibase.");
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(liquibaseChangelog);
		liquibase.setDataSource(dataSource);
		logger.info("Liquibase configurado!.");
		return liquibase;
	}
}
