package com.frager.oreport.udemyconnector.mapper;

import com.frager.oreport.udemyconnector.model.Course;
import com.udemy.model.ListedCourse;
import com.udemy.model.SingleCourse;

public class CourseMapper {

	private static Course baseCourse(com.udemy.model.Course udemyCourse) {
		Course course = new Course();
		course.setId(udemyCourse.getId());
		course.setDescription(udemyCourse.getDescription());
		try {
			// TODO reemplazar por set-get cuando udemyCourse.getEstimatedContentLength()
			// sea integer
			course.setEstimatedContentLength(new Integer(udemyCourse.getEstimatedContentLength()));
		} catch (NumberFormatException e) {
			// no-op
		}

		course.setNumberOfLectures(udemyCourse.getNumberOfLectures());
		course.setNumberOfPracticeTests(udemyCourse.getNumberOfPracticeTests());
		course.setNumberOfQuizzes(udemyCourse.getNumberOfQuizzes());
		course.setTitle(udemyCourse.getTitle());
		course.setUrl(udemyCourse.getUrl());

		return course;
	}

	public static Course fromSingleCourse(SingleCourse udemyCourse) {
		Course course = baseCourse(udemyCourse);
		// reservado para atributos especificos de SingleCourse
		return course;
	}

	public static Course fromListedCourse(ListedCourse udemyCourse) {
		Course course = baseCourse(udemyCourse);
		// reservado para atributos especificos de ListedCourse
		return course;
	}
}
