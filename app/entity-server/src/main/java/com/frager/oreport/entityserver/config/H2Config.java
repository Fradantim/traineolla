package com.frager.oreport.entityserver.config;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.relational.core.conversion.BasicRelationalConverter;

import com.frager.oreport.core.config.PropertiesConfig;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.Clob;
import reactor.core.publisher.Flux;

@AutoConfigureAfter(PropertiesConfig.class)
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
	@ConditionalOnProperty("spring.r2dbc.url")
	@ConditionalOnExpression("#{'${spring.r2dbc.url}'.substring(0,12) matches 'r2dbc:h2:tcp'}")
	@Bean
	public H2ConnectionFactory h2ConnectionFactory(@Value("${spring.r2dbc.url}") String url,
			@Value("${spring.r2dbc.username}") String username, @Value("${spring.r2dbc.password}") String password) {
		url = url.substring("r2dbc:h2:".length());
		return new H2ConnectionFactory(
				H2ConnectionConfiguration.builder().url(url).username(username).password(password).build());
	}

	@Autowired
	private BasicRelationalConverter basicRelationalConverter;

	/**
	 * Agregar transformador CLOB a String.
	 * 
	 * @see <a href="https://github.com/r2dbc/r2dbc-h2/issues/202">r2dbc-h2
	 *      Issue#202</a>
	 */
	@PostConstruct
	void addClobConverter() {
		((ConfigurableConversionService) basicRelationalConverter.getConversionService())
				.addConverter(new ClobConverter());
	}

	class ClobConverter implements Converter<Clob, String> {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public String convert(Clob source) {
			List asImperative = null;
			try {
				asImperative = (List) ((Flux) source.stream()).collectList().toFuture().get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException("Error while converting CLOB to String.", e);
			}
			return Objects.isNull(asImperative) ? "" : String.join("", asImperative);
		}
	}
}
