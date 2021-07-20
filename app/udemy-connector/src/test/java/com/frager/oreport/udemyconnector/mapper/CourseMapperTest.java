package com.frager.oreport.udemyconnector.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.frager.oreport.udemyconnector.model.Course;
import com.udemy.model.ListedCourse;
import com.udemy.model.SingleCourse;

class CourseMapperTest {

	private void fillSampleCourse(com.udemy.model.Course c) {
		c.setDescription("Una descripcion");
		c.setEstimatedContentLength(177013);
		c.setId(1);
		c.setNumberOfLectures(123);
		c.setNumberOfPracticeTests(456);
		c.setNumberOfQuizzes(789);
		c.setTitle("Un titulo");
		c.setUrl("Una url");
	}

	private void assertCourseKeepsContents(com.udemy.model.Course udemyCourse, Course oreportCourse) {
		assertEquals(udemyCourse.getDescription(), oreportCourse.getDescription());
		assertEquals(udemyCourse.getEstimatedContentLength(), oreportCourse.getEstimatedContentLength());
		assertEquals(udemyCourse.getId(), oreportCourse.getId());
		assertEquals(udemyCourse.getNumberOfLectures(), oreportCourse.getNumberOfLectures());
		assertEquals(udemyCourse.getNumberOfPracticeTests(), oreportCourse.getNumberOfPracticeTests());
		assertEquals(udemyCourse.getNumberOfQuizzes(), oreportCourse.getNumberOfQuizzes());
		assertEquals(udemyCourse.getTitle(), oreportCourse.getTitle());
		assertEquals(udemyCourse.getUrl(), oreportCourse.getUrl());
	}

	@Test
	void testMapperFromSingleCourse() {
		SingleCourse sc = new SingleCourse();
		fillSampleCourse(sc);

		// reservado para mas atributos de SingleCourse

		Course c = CourseMapper.fromSingleCourse(sc);
		assertCourseKeepsContents(sc, c);
	}

	@Test
	void testMapperFromListedCourse() {
		ListedCourse lc = new ListedCourse();
		fillSampleCourse(lc);

		// reservado para mas atributos de ListedCourse

		Course c = CourseMapper.fromListedCourse(lc);
		assertCourseKeepsContents(lc, c);
	}
}
