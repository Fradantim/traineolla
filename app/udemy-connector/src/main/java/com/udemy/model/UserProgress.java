package com.udemy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProgress extends User {

	/*
	report_date Datetime
	
	user_joined_date Datetime
	Date Joined (UTC), this is the date the user joined the portal
	
	num_new_enrolled_courses Integer
	No. of New Courses Enrolled, this is the number of courses the user has enrolled
	in (during the timeframe specified)
	
	num_new_assigned_courses Integer
	No. of Assigned Courses - This indicates the number of courses that were
	assigned. Courses that were assigned before 19th March 2015 are not included in
	this count
	
	num_new_started_courses Integer
	No. of Courses Started, this is the number of courses the user has started (during
	the timeframe specified)
	
	num_completed_courses Integer
	No. of Courses Completed, this is the number of courses the user has completed
	(during the timeframe specified)
	
	num_completed_lectures Integer
	No. of Lectures Completed, this is the number of lectures the user has completed
	(during the timeframe specified)
	
	num_video_consumed_minutes
	Decimal
	Minutes Video Consumed, this is the total number of minutes of video lectures
	the user has consumed. It does not include any estimation of time spent on other
	materials such as slides or ebooks. If the user watches some videos multiple
	times then each time will contribute to the total in, this report
	
	num_web_visited_days Integer
	No. of Days visited (via Web), this is the number of days that the user visited the
	UfB portal via the Web
	last_date_visit Datetime 
	
	 */
}
