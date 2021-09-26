package com.frager.oreport.dbutil.config;

import java.sql.SQLException;

import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2Config {

	@ConditionalOnProperty("h2.enabled")
	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2DataBaseServer(@Value("${h2.port}") String h2port) throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", h2port);
	}

	@ConditionalOnProperty("h2.console.enabled")
	@Bean
	public ServletRegistrationBean<WebServlet> h2ServletRegistration() {
		ServletRegistrationBean<WebServlet> registration = new ServletRegistrationBean<>(new WebServlet());
		registration.addUrlMappings("/console/*");
		registration.addInitParameter("-webAllowOthers", "true");
		return registration;
	}
}
