package com.frager.oreport.batchcoursesnapshot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.frager.oreport")
@SpringBootApplication
public class BatchCourseSnapshotsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchCourseSnapshotsApplication.class, args);
	}

}