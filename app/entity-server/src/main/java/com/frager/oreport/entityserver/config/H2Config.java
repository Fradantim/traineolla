package com.frager.oreport.entityserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;

@ConditionalOnProperty("spring.r2dbc.url")
@ConditionalOnExpression("#{'${spring.r2dbc.url}'.substring(0,12) matches 'r2dbc:h2:tcp'}")
@Configuration
public class H2Config {

	/**
	 * Siendo 2021-08-06 la ultima version de r2dbc-h2 es 0.8.4.RELEASE y, aunque
	 * agregaron conexiones tcp, aun no son auto-configurables. Este Bean ayuda a
	 * resolverlo.
	 * 
	 * @see <a href="https://github.com/r2dbc/r2dbc-h2/issues/86">r2dbc-h2
	 *      Issue#86</a>
	 */
	@Bean
	public H2ConnectionFactory h2ConnectionFactory(@Value("${spring.r2dbc.url}") String url,
			@Value("${spring.r2dbc.username}") String username, @Value("${spring.r2dbc.password}") String password) {
		url = url.substring("r2dbc:h2:".length());
		return new H2ConnectionFactory(
				H2ConnectionConfiguration.builder().url(url).username(username).password(password).build());
	}
}
