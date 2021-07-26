package com.frager.oreport.dbutil.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:secrets.mock.properties")
class AutoShutDownConfigTest {

	@Test
	void keepAliveTest() {
		ConfigurableApplicationContext appCtx = Mockito.mock(ConfigurableApplicationContext.class);
		Mockito.when(appCtx.isActive()).thenReturn(true);
		AutoShutDownConfig config = new AutoShutDownConfig(appCtx, true);
		config.doSomethingAfterStartup();
		assertTrue(appCtx.isActive());
	}

	@Test
	void shutdownTest() {
		ConfigurableApplicationContext appCtx = Mockito.mock(ConfigurableApplicationContext.class);
		Mockito.when(appCtx.isActive()).thenReturn(false);
		AutoShutDownConfig config = new AutoShutDownConfig(appCtx, false);
		config.doSomethingAfterStartup();
		assertFalse(appCtx.isActive());
	}
}
