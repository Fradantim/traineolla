package com.frager.oreport.mailserver.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import com.frager.oreport.mailserver.model.Mail;
import com.frager.oreport.mailserver.model.SendStatus;
import com.frager.oreport.mailserver.model.Status;
import com.frager.oreport.mailserver.service.MailService;
import com.frager.oreport.mailserver.service.impl.MailServiceImpl;

import reactor.core.publisher.Mono;

@Profile("MailResourceTest")
@TestConfiguration
class ServiceTestConfiguration {

	@Primary
	@Bean
	public MailService myMailServer() {
		return new MailServiceImpl(null, null) {
			@Override
			public Mono<Mail> send(Mail mail) {
				mail.setSendStatus(SendStatus.ok());
				return Mono.just(mail);
			}
		};
	}
}

@ActiveProfiles("MailResourceTest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:secrets.mock.properties")
class MailResourceTest {

	@LocalServerPort
	private int port;

	@Test
	void sendMailTest() {
		Mail originalMail = new Mail();

		originalMail.setTo(new String[] { "micho@gmail.com" });
		originalMail.setCc(new String[] { "tito@outlook.com", "grande@yahoo.com" });
		originalMail.setBcc(new String[] { "cabezon@hotmail.com" });

		originalMail.setSubject("Nigerian princes in your area!");
		originalMail.setText("No body, only krab kalash.");

		Mono<Mail> mailMono = WebClient.create().post().uri("http://localhost:" + port + "/mail/")
				.bodyValue(originalMail).retrieve().bodyToMono(Mail.class);

		Mail mailRetrieved = mailMono.block();

		assertNotNull(mailRetrieved);
		assertNotChanged(originalMail, mailRetrieved);
		assertNotNull(mailRetrieved.getSendStatus());
		assertEquals(Status.OK, mailRetrieved.getSendStatus().getStatus());
	}

	private void assertNotChanged(Mail mailA, Mail mailB) {
		assertEquals(mailA.getBcc(), mailA.getBcc());
		assertEquals(mailA.getCc(), mailA.getCc());
		assertEquals(mailA.getSubject(), mailA.getSubject());
		assertEquals(mailA.getText(), mailA.getText());
		assertEquals(mailA.getTo(), mailA.getTo());
	}
}
