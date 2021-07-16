package com.udemy.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Course POJO from /courses/{course-id} Udemy endpoint */
public class SingleCourse extends Course {

	/*
	"image_125_H": "https://img-c.udemycdn.com/course/125_H/1278360_beb4_3.jpg",
    "image_240x135": "https://img-c.udemycdn.com/course/240x135/1278360_beb4_3.jpg",
    "image_480x270": "https://img-c.udemycdn.com/course/480x270/1278360_beb4_3.jpg",
	"published_title": "11-essential-coding-interview-questions",
	"tracking_id": "VWvt9KuQST2rG4EjjM300g",
	"headline": "Learn 3 handy problem solving techniques. Get 11 hand-picked problems. Get ready for your next coding interview!",
	"num_subscribers": 15396,
	
	"avg_rating": 4.5122952,
    "avg_rating_recent": 4.577464,
    "rating": 4.577464,
    "num_reviews": 3534,
    "num_reviews_recent": 120,
    "completion_ratio": 0,
    "num_published_lectures": 35,
    "num_published_quizzes": 22,
    "num_curriculum_items": 57,
    "is_private": false,
    "quality_status": "approved",
    "status_label": "Live",
    "features": { -> Map<String, Object>
        "_class": "course",
        "discussions_create": true,
        "discussions_view": true,
        "discussions_replies_create": true,
        "enroll": true,
        "reviews_create": true,
        "reviews_view": true,
        "reviews_responses_create": true,
        "announcements_comments_view": true,
        "educational_announcements_create": true,
        "promotional_announcements_create": true,
        "promotions_create": true,
        "promotions_view": true,
        "students_view": true
    },
    "is_banned": false,
    "is_published": true,
    "image_48x27": "https://img-c.udemycdn.com/course/48x27/1278360_beb4_3.jpg",
    "image_50x50": "https://img-c.udemycdn.com/course/50x50/1278360_beb4_3.jpg",
    "image_75x75": "https://img-c.udemycdn.com/course/75x75/1278360_beb4_3.jpg",
    "image_96x54": "https://img-c.udemycdn.com/course/96x54/1278360_beb4_3.jpg",
    "image_100x100": "https://img-c.udemycdn.com/course/100x100/1278360_beb4_3.jpg",
    "image_200_H": "https://img-c.udemycdn.com/course/200_H/1278360_beb4_3.jpg",
    "image_304x171": "https://img-c.udemycdn.com/course/304x171/1278360_beb4_3.jpg",
    "image_750x422": "https://img-c.udemycdn.com/course/750x422/1278360_beb4_3.jpg",
    "has_certificate": true,
    "has_closed_caption": true,
    "created": "2017-07-03T18:31:22Z",
    "instructional_level": "Intermediate Level",
    "instructional_level_simple": "Intermediate",
    "content_info": "2 total hours",
    "content_info_short": "2 hours",
    "content_length_practice_test_questions": 0,
    
    los _data son Map<String, Object>
    
    "requirements_data": {
        "items": [
            "You should be familiar with at least one C-like programming language (could be Java, Python, C++, Ruby, JavaScript, etc.)",
            "You should be familiar with basic data structures such as arrays, hash tables, linked lists, trees, and graphs",
            "You should be familiar with the Big O notation"
        ]
    },
    "what_you_will_learn_data": {
        "items": [
            "Learn what a coding interview is like",
            "Learn 3 problem solving techniques you'll be able to use in your next coding interview!",
            "Go through 11 of the most essential coding interview questions, coding exercises and answers!",
            "Overall, feel more confident and be ready for your next coding interview"
        ]
    },
    "who_should_attend_data": {
        "items": [
            "Anyone who is currently preparing for coding interviews / programming interviews",
            "Anyone who wants to improve their problem solving / coding skills in general (coding interviews are a good way to practice these skills)"
        ]
    },
    
    "organization_id": null,
    "is_organization_only": false,
    
    "published_time": "2017-07-29T01:08:59Z",
    
    "content_length_video": 7531,
    "prerequisites": [
        "You should be familiar with at least one C-like programming language (could be Java, Python, C++, Ruby, JavaScript, etc.)",
        "You should be familiar with basic data structures such as arrays, hash tables, linked lists, trees, and graphs",
        "You should be familiar with the Big O notation"
    ],
    "objectives": [
        "Learn what a coding interview is like",
        "Learn 3 problem solving techniques you'll be able to use in your next coding interview!",
        "Go through 11 of the most essential coding interview questions, coding exercises and answers!",
        "Overall, feel more confident and be ready for your next coding interview"
    ],
    "objectives_summary": [
        "Learn what a coding interview is like",
        "Learn 3 problem solving techniques you'll be able to use in your next coding interview!",
        "Go through 11 of the most essential coding interview questions, coding exercises and answers!"
    ],
    "target_audiences": [
        "Anyone who is currently preparing for coding interviews / programming interviews",
        "Anyone who wants to improve their problem solving / coding skills in general (coding interviews are a good way to practice these skills)"
    ],
    
    "course_has_labels": [
        {
            "_class": "course_has_label",
            "id": 1131147,
            "label": {
                "_class": "course_label",
                "id": 155842,
                "title": "Coding Interview",
                "url": "/topic/coding-interview/",
                "icon_class": "udi udi-line-star",
                "type": "topic",
                "display_name": "Coding Interview"
            },
            "is_primary": true
        }
    ],
    "last_update_date": "2019-08-14",
    "num_article_assets": 1,
    "num_coding_exercises": 22,
    "num_assignments": 0,
    "num_additional_assets": 24,
    
    "custom_category_ids": [], -> INTEGER
    "alternate_redirect_course_id": 0,
    "is_approved": true,
    "is_organization_eligible": true,
    "instructor_status": null,
    "available_features": [
        "q_and_a_enabled",
        "certificate"
    ],
	*/
	private Integer id;
	private String title;
	private String description;
	private String url;
	@JsonProperty("estimated_content_length")
	private String estimatedContentLength;
	@JsonProperty("num_lectures")
	private Integer numberOfLectures;
	private Map<String, Object> locale;
	@JsonProperty("num_quizzes")
	private Integer numberOfQuizzes;
	@JsonProperty("num_practice_tests")
	private Integer numberOfPracticeTests;
	
	@JsonProperty("visible_instructors")
	private List<Instructor> visibleInstructors;

	public SingleCourse() {
		super();
	}

	/** Course Id, this is a unique identifier for the course */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/** HTML Field Course Description, this is the description of the course */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/** This is an estimated length of time the content would take to be consumed */
	public String getEstimatedContentLength() {
		return estimatedContentLength;
	}

	public void setEstimatedContentLength(String estimatedContentLength) {
		this.estimatedContentLength = estimatedContentLength;
	}

	/** The number of lectures this course comprises of */
	public Integer getNumberOfLectures() {
		return numberOfLectures;
	}

	public void setNumberOfLectures(Integer numberOfLectures) {
		this.numberOfLectures = numberOfLectures;
	}

	/** Indicates the language of the course (e.g. es_ES) **/
	public Map<String, Object> getLocale() {
		return locale;
	}

	public void setLocale(Map<String, Object> locale) {
		this.locale = locale;
	}

	/** Number of quizzes contained in the course */
	public Integer getNumberOfQuizzes() {
		return numberOfQuizzes;
	}

	public void setNumberOfQuizzes(Integer numberOfQuizzes) {
		this.numberOfQuizzes = numberOfQuizzes;
	}

	/** Number of practice tests contained in the course */
	public Integer getNumberOfPracticeTests() {
		return numberOfPracticeTests;
	}

	public void setNumberOfPracticeTests(Integer numberOfPracticeTests) {
		this.numberOfPracticeTests = numberOfPracticeTests;
	}

	public List<Instructor> getVisibleInstructors() {
		return visibleInstructors;
	}

	public void setVisibleInstructors(List<Instructor> visibleInstructors) {
		this.visibleInstructors = visibleInstructors;
	}
}
