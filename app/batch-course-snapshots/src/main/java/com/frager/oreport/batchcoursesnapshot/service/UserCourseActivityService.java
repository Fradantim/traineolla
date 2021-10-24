package com.frager.oreport.batchcoursesnapshot.service;

import com.frager.oreport.batchcoursesnapshot.dto.UserCourseActivity;

import reactor.core.publisher.Flux;

public interface UserCourseActivityService {

	public Flux<UserCourseActivity> getActivitiesBefore();
}
