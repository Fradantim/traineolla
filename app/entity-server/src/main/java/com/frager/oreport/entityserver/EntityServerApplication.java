package com.frager.oreport.entityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.frager.oreport")
@SpringBootApplication
public class EntityServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntityServerApplication.class, args);
	}
}
