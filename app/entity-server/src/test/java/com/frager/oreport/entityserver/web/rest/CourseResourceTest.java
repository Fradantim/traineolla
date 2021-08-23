package com.frager.oreport.entityserver.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.frager.oreport.entityserver.model.Course;
import com.frager.oreport.entityserver.repository.CourseRepository;

@SpringBootTest
@AutoConfigureWebTestClient
class CourseResourceTest {
	private static final Long DEFAULT_UDEMY_ID = 1243546L;
	private static final String DEFAULT_TITLE = "What a nice title";

	private static final Long UPDATED_UDEMY_ID = DEFAULT_UDEMY_ID * 3;
	private static final String UPDATED_TITLE = DEFAULT_TITLE + ", isn't it?";

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private WebTestClient webTestClient;

	@BeforeEach
	public void cleanup() {
		courseRepository.deleteAll().block();
	}

	private static Course buildDefaultEntity() {
		Course entity = new Course();
		entity.setUdemyId(DEFAULT_UDEMY_ID);
		entity.setTitle(DEFAULT_TITLE);
		return entity;
	}

	@Test
	void create() {
		Long databaseSizeBefore = courseRepository.count().block();

		Course entity = buildDefaultEntity();

		webTestClient.post().uri(CourseResource.PATH).contentType(MediaType.APPLICATION_JSON).bodyValue(entity)
				.exchange().expectStatus().isCreated();

		List<Course> entityList = courseRepository.findAll().collectList().block();
		assertThat(entityList).hasSize(databaseSizeBefore.intValue() + 1);
		Course testEntity = entityList.get(entityList.size() - 1);
		assertThat(testEntity.getUdemyId()).isEqualTo(DEFAULT_UDEMY_ID);
		assertThat(testEntity.getTitle()).isEqualTo(DEFAULT_TITLE);
	}

	@Test
	void createWithExistingId() {
		Course entity = new Course();
		entity.setId(1L);

		Long databaseSizeBefore = courseRepository.count().block();

		webTestClient.post().uri(CourseResource.PATH).contentType(MediaType.APPLICATION_JSON).bodyValue(entity)
				.exchange().expectStatus().isBadRequest();

		assertThat(courseRepository.count().block()).isEqualTo(databaseSizeBefore);
	}

	@Test
	void getAll() {
		Course entity = buildDefaultEntity();
		courseRepository.save(entity).block();

		webTestClient.get().uri(CourseResource.PATH + "?sort=id,desc").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
				.jsonPath("$.[*].id").value(hasItem(entity.getId().intValue())).jsonPath("$.[*].udemyId")
				.value(hasItem(DEFAULT_UDEMY_ID.intValue())).jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE));
	}

	@Test
	void getAllAsStream() {
		Course entity = buildDefaultEntity();
		courseRepository.save(entity).block();

		List<Course> entityList = webTestClient.get().uri(CourseResource.PATH + "?sort=id,desc")
				.accept(MediaType.APPLICATION_NDJSON).exchange().expectStatus().isOk().expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON).returnResult(Course.class).getResponseBody()
				.filter(entity::equals).collectList().block(Duration.ofSeconds(5));

		assertThat(entityList).isNotNull().hasSize(1);
		assertThat(entityList.get(0).getUdemyId()).isEqualTo(DEFAULT_UDEMY_ID);
	}

	@Test
	void getById() {
		Course entity = buildDefaultEntity();
		courseRepository.save(entity).block();

		webTestClient.get().uri(CourseResource.PATH + "/{id}", entity.getId()).accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
				.jsonPath("$.id").value(is(entity.getId().intValue())).jsonPath("$.udemyId")
				.value(is(DEFAULT_UDEMY_ID.intValue())).jsonPath("$.title").value(is(DEFAULT_TITLE));
	}

	@Test
	void getNonExistingEntity() {
		webTestClient.get().uri(CourseResource.PATH + "/{id}", Long.MAX_VALUE).accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isNotFound();
	}

	@Test
	void putNewEntity() {
		Course entity = buildDefaultEntity();
		courseRepository.save(entity).block();

		Long databaseSizeBefore = courseRepository.count().block();

		Course updatedEntity = courseRepository.findById(entity.getId()).block();
		updatedEntity.setUdemyId(UPDATED_UDEMY_ID);
		updatedEntity.setTitle(UPDATED_TITLE);

		webTestClient.put().uri(CourseResource.PATH + "/{id}", updatedEntity.getId())
				.contentType(MediaType.APPLICATION_JSON).bodyValue(updatedEntity).exchange().expectStatus().isOk();

		List<Course> entityList = courseRepository.findAll().collectList().block();
		assertThat(entityList).hasSize(databaseSizeBefore.intValue());
		Course testEntity = entityList.get(entityList.size() - 1);
		assertThat(testEntity.getUdemyId()).isEqualTo(UPDATED_UDEMY_ID);
		assertThat(testEntity.getTitle()).isEqualTo(UPDATED_TITLE);
	}

	@Test
	void putNonExistingJob() {
		Long databaseSizeBefore = courseRepository.count().block();
		Course entity = buildDefaultEntity();
		entity.setId(Long.MIN_VALUE);

		webTestClient.put().uri(CourseResource.PATH + "/{id}", entity.getId()).contentType(MediaType.APPLICATION_JSON)
				.bodyValue(entity).exchange().expectStatus().isNotFound();

		assertThat(courseRepository.count().block()).isEqualTo(databaseSizeBefore);
	}

	@Test
	void putWithMismatchId() throws Exception {
		Long databaseSizeBefore = courseRepository.count().block();
		Course entity = buildDefaultEntity();
		entity.setId(Long.MIN_VALUE);

		webTestClient.put().uri(CourseResource.PATH + "/{id}", Long.MAX_VALUE).contentType(MediaType.APPLICATION_JSON)
				.bodyValue(entity).exchange().expectStatus().isBadRequest();

		assertThat(courseRepository.count().block()).isEqualTo(databaseSizeBefore);
	}

	@Test
	void putWithMissingIdPathParamJob() throws Exception {
		Long databaseSizeBefore = courseRepository.count().block();
		Course entity = buildDefaultEntity();
		entity.setId(Long.MIN_VALUE);

		webTestClient.put().uri(CourseResource.PATH).contentType(MediaType.APPLICATION_JSON).bodyValue(entity)
				.exchange().expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());

		assertThat(courseRepository.count().block()).isEqualTo(databaseSizeBefore);
	}

	@Test
	void deleteJob() {
		Long databaseSizeBefore = courseRepository.count().block();

		Course entity = buildDefaultEntity();
		courseRepository.save(entity).block();

		webTestClient.delete().uri(CourseResource.PATH + "/{id}", entity.getId()).accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isNoContent();

		assertThat(courseRepository.count().block()).isEqualTo(databaseSizeBefore);
	}
}
