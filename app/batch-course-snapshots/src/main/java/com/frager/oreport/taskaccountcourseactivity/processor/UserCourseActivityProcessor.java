//package com.frager.oreport.taskaccountcourseactivity.processor;
//
//import java.time.OffsetDateTime;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Optional;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.frager.oreport.batchcoursesnapshot.entity.AccountCourseSnapshot;
//import com.frager.oreport.taskaccountcourseactivity.repository.AccountCourseSnapshotRepository;
//
//@Component
//public class UserCourseActivityProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>> {
//
//	private final static Logger logger = LoggerFactory.getLogger(UserCourseActivityProcessor.class);
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@Autowired
//	private AccountCourseSnapshotRepository snapshotRepository;
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public Map<String, Object> process(Map<String, Object> item) throws Exception {
//		if (logger.isDebugEnabled()) {
//			logger.debug("Procesando item: {}", objectMapper.writeValueAsString(item));
//		}
//
//		if (!isValid(item))
//			return null;
//
//		AccountCourseSnapshot newSnapshot = map(item);
//
//		Optional<AccountCourseSnapshot> previousSnapshot = snapshotRepository
//				.findFirstByAccountEnterpriseIdAndUdemyCourseIdOrderBySnapshotDateTimeDesc(
//						newSnapshot.getAccountEnterpriseId(), newSnapshot.getUdemyCourseId());
//
//		if (!previousSnapshot.isPresent() || activityHasDifferentInfo(previousSnapshot.get(), newSnapshot)) {
//			// no tengo info previa, o tengo nueva info que agregar
//			if (previousSnapshot.isPresent()) {
//				newSnapshot.setPreviousSnapId(previousSnapshot.get().getId());
//			}
//
//			return objectMapper.convertValue(newSnapshot, Map.class);
//		}
//
//		// descartar item
//		return null;
//	}
//
//	private AccountCourseSnapshot map(Map<String, Object> item) {
//		AccountCourseSnapshot result = new AccountCourseSnapshot();
//		result.setSnapshotDateTime(OffsetDateTime.now());
//		if (item.get("user_email") instanceof String) {
//			result.setUdemyAccount((String) item.get("user_email"));
//			result.setAccountEnterpriseId(result.getUdemyAccount().split("@")[0]);
//		}
//
//		if (item.get("course_id") instanceof Number) {
//			result.setUdemyCourseId(((Number) item.get("course_id")).longValue());
//		}
//
//		if (item.get("completion_ratio") instanceof Number) {
//			result.setCompletionRatio(((Number) item.get("completion_ratio")).intValue());
//		}
//
//		result.setCourseEnrollDate(toOffsetDateTime(item.get("course_enroll_date")));
//		result.setCourseStartDate(toOffsetDateTime(item.get("course_start_date")));
//		result.setCourseCompletionDate(toOffsetDateTime(item.get("course_completion_date")));
//		result.setCourseFirstCompletionDate(toOffsetDateTime(item.get("course_first_completion_date")));
//
//		if (item.get("num_video_consumed_minutes") instanceof Number) {
//			result.setNumVideoConsumedMinutes(((Number) item.get("num_video_consumed_minutes")).intValue());
//		}
//
//		return result;
//	}
//
//	private OffsetDateTime toOffsetDateTime(Object value) {
//		if (value instanceof String) {
//			return OffsetDateTime.parse((String) value);
//		}
//
//		if (value instanceof OffsetDateTime) {
//			return (OffsetDateTime) value;
//		}
//
//		return null;
//	}
//
//	/** Hay casos "user_email": "Anonymized User" */
//	private static Boolean isValid(Map<String, Object> item) {
//		return item.get("user_email") instanceof String && ((String) item.get("user_email")).contains("@");
//	}
//
//	/*
//	 * private static AccountCourseSnapshot buildFromActivity(UserCourseActivity
//	 * activity, String enterpriseId) { AccountCourseSnapshot snapshot = new
//	 * AccountCourseSnapshot(); snapshot.setAccountEnterpriseId(enterpriseId); if
//	 * (activity.getCompletionRatio() != null)
//	 * snapshot.setCompletionRatio(activity.getCompletionRatio().intValue());
//	 * snapshot.setCourseCompletionDate(activity.getCourseCompletionDate());
//	 * snapshot.setCourseEnrollDate(activity.getCourseEnrollDate());
//	 * snapshot.setCourseFirstCompletionDate(activity.getCourseFirstCompletionDate()
//	 * ); if (activity.getNumberOfVideoConsumedMinutes() != null)
//	 * snapshot.setNumVideoConsumedMinutes(activity.getNumberOfVideoConsumedMinutes(
//	 * ).intValue()); snapshot.setSnapshotDateTime(OffsetDateTime.now());
//	 * snapshot.setUdemyAccount(activity.getUserEmail());
//	 * snapshot.setUdemyCourseId(activity.getCourseId()); return snapshot; }
//	 */
//
//	private static Boolean activityHasDifferentInfo(AccountCourseSnapshot snapshot, AccountCourseSnapshot activity) {
//		if (!Objects.equals(snapshot.getCourseCompletionDate(), activity.getCourseCompletionDate())
//				|| !Objects.equals(snapshot.getCourseFirstCompletionDate(), activity.getCourseFirstCompletionDate())
//				|| !Objects.equals(snapshot.getNumVideoConsumedMinutes(), activity.getNumVideoConsumedMinutes())
//				|| !Objects.equals(snapshot.getCompletionRatio(), activity.getCompletionRatio()))
//			return true;
//
//		return false;
//	}
//}
