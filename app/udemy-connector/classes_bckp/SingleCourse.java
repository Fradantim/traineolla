package com.udemy.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO del endpoint <code>/courses/{course-id}</code> de Udemy. <br />
 * Otros elementos interesantes disponibles: <br />
 * <code>
 * {"published_title": "11-essential-coding-interview-questions", "headline":
 * "Learn 3 handy problem solving techniques. Get 11 hand-picked problems. Get
 * ready for your next coding interview!", "num_subscribers": 15396,
 * "avg_rating": 4.5122952, "avg_rating_recent": 4.577464, "rating": 4.577464,
 * "num_reviews": 3534, "num_reviews_recent": 120, "completion_ratio": 0,
 * "num_published_lectures": 35, "num_published_quizzes": 22,
 * "num_curriculum_items": 57, "is_private": false, "quality_status":
 * "approved", "is_banned": false, "is_published": true,"has_certificate": true,
 * "created": "2017-07-03T18:31:22Z", "published_time": "2017-07-29T01:08:59Z",
 * "instructional_level": "Intermediate Level", "instructional_level_simple":
 * "Intermediate", "content_info": "2 total hours", "organization_id": null,
 * "is_organization_only": false }
 * </code>
 */
public class SingleCourse extends Course {

	@JsonProperty("visible_instructors")
	private List<Instructor> visibleInstructors;

	public SingleCourse() {
		super();
	}

	public List<Instructor> getVisibleInstructors() {
		return visibleInstructors;
	}

	public void setVisibleInstructors(List<Instructor> visibleInstructors) {
		this.visibleInstructors = visibleInstructors;
	}
}
