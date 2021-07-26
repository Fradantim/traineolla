package com.frager.oreport.dbutil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.frager.oreport")
@SpringBootApplication
public class DbUtilApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbUtilApplication.class, args);
	}
}
