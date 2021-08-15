package com.frager.oreport.udemyconnector.mapper;

import com.frager.oreport.udemyconnector.model.UserCourseActivity;

public class UserCourseActivityMapper {

	private UserCourseActivityMapper() {
		super();
	}

	public static UserCourseActivity from(com.udemy.model.UserCourseActivity udemyUserCourseActivity) {
		UserCourseActivity ucActivity = new UserCourseActivity();
		ucActivity.setCompletionRatio(udemyUserCourseActivity.getCompletionRatio());
		ucActivity.setCourseCompletionDate(udemyUserCourseActivity.getCourseCompletionDate());
		ucActivity.setCourseEnrollDate(udemyUserCourseActivity.getCourseEnrollDate());
		ucActivity.setCourseFirstCompletionDate(udemyUserCourseActivity.getCourseFirstCompletionDate());
		ucActivity.setCourseId(udemyUserCourseActivity.getCourseId());
		ucActivity.setCourseStartDate(udemyUserCourseActivity.getCourseStartDate());
		ucActivity.setNumberOfVideoConsumedMinutes(udemyUserCourseActivity.getNumberOfVideoConsumedMinutes());
		ucActivity.setUserEmail(udemyUserCourseActivity.getUserEmail());

		return ucActivity;
	}
}
