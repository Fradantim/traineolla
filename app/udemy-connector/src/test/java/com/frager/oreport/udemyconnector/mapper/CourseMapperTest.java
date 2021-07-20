package com.frager.oreport.udemyconnector.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.frager.oreport.udemyconnector.model.Course;
import com.udemy.model.ListedCourse;
import com.udemy.model.SingleCourse;

class CourseMapperTest {

	@Test
	void testMapperFromSingle() {
		SingleCourse sc = new SingleCourse();
		sc.setId(1);
		
		Course c = CourseMapper.fromSingleCourse(sc);
		assertEquals(sc.getId(), c.getId());
	}
	
	@Test
	void testMapperFromListed() {
		ListedCourse lc = new ListedCourse();
		lc.setId(1);
		
		Course c = CourseMapper.fromListedCourse(lc);
		assertEquals(lc.getId(), c.getId());
	}
}
