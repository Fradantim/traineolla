package com.frager.oreport.batchcoursesnapshot.mapper;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Component;

import com.frager.oreport.batchcoursesnapshot.dto.UserCourseActivity;
import com.frager.oreport.batchcoursesnapshot.entity.AccountCourseSnapshot;

@Component
public class ActivityToSnapshotMapper {

	/** Hay casos "user_email": "Anonymized User" */
	public Boolean isValid(UserCourseActivity activity) {
		return activity != null && activity.getUserEmail() != null && activity.getUserEmail().contains("@");
	}

	public AccountCourseSnapshot buildFromActivity(UserCourseActivity activity) {
		AccountCourseSnapshot snapshot = new AccountCourseSnapshot();
		snapshot.setAccountEnterpriseId(activity.getUserEmail().substring(0, activity.getUserEmail().indexOf("@")));
		if (activity.getCompletionRatio() != null)
			snapshot.setCompletionRatio(activity.getCompletionRatio().intValue());
		snapshot.setCourseCompletionDate(activity.getCourseCompletionDate());
		snapshot.setCourseEnrollDate(activity.getCourseEnrollDate());
		snapshot.setCourseFirstCompletionDate(activity.getCourseFirstCompletionDate());
		if (activity.getNumberOfVideoConsumedMinutes() != null)
			snapshot.setNumVideoConsumedMinutes(activity.getNumberOfVideoConsumedMinutes().intValue());
		snapshot.setSnapshotDateTime(OffsetDateTime.now());
		snapshot.setUdemyAccount(activity.getUserEmail());
		snapshot.setUdemyCourseId(activity.getCourseId());
		return snapshot;
	}
}
