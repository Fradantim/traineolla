package com.frager.oreport.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConditionalOnProperty("env.prop.file")
@PropertySource("file:${env.prop.file}")
public class PropertiesConfig {
}
