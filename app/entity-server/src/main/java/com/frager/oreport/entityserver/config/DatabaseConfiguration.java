package com.frager.oreport.entityserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.query.UpdateMapper;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.relational.core.dialect.RenderContextFactory;
import org.springframework.data.relational.core.sql.render.SqlRenderer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
public class DatabaseConfiguration {

	@Bean
    public R2dbcDialect dialect(ConnectionFactory connectionFactory) {
        return DialectResolver.getDialect(connectionFactory);
    }
	
	@Bean
    public UpdateMapper updateMapper(R2dbcDialect dialect, MappingR2dbcConverter mappingR2dbcConverter) {
        return new UpdateMapper(dialect, mappingR2dbcConverter);
    }
	
	@Bean
    public SqlRenderer sqlRenderer(R2dbcDialect dialect) {
        RenderContextFactory factory = new RenderContextFactory(dialect);
        return SqlRenderer.create(factory.createRenderContext());
    }
}
