package com.frager.oreport.udemyconnector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.frager.oreport")
@SpringBootApplication
public class UdemyConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(UdemyConnectorApplication.class, args);
	}

}
