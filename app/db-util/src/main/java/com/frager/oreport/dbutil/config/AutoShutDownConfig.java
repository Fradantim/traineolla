package com.frager.oreport.dbutil.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AutoShutDownConfig {

	private static final Logger logger = LoggerFactory.getLogger(AutoShutDownConfig.class);

	public AutoShutDownConfig(@Autowired ConfigurableApplicationContext appCtx,
			@Value("${h2.enabled:false}") Boolean h2Enabled) {
		this.h2Enabled = h2Enabled;
		this.appCtx = appCtx;
	}

	private Boolean h2Enabled;

	private ConfigurableApplicationContext appCtx;

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		if (h2Enabled == null || !h2Enabled) {
			logger.info("No quedan operaciones por realizar, se procede a apagar.");
			appCtx.close();
		}
	}
}
