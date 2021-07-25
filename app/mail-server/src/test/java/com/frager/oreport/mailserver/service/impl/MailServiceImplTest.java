package com.frager.oreport.mailserver.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

import com.frager.oreport.mailserver.model.Mail;
import com.frager.oreport.mailserver.model.Status;

import reactor.core.publisher.Mono;

@SpringBootTest
@TestPropertySource("classpath:secrets.mock.properties")
class MailServiceImplTest {
	
	private MimeMessage mimeMessageExample;
	
	private MailServiceImplTest(@Autowired JavaMailSender javaMailSender){
		this.mimeMessageExample = javaMailSender.createMimeMessage();
	}

	@Test
	void mailSendsOkTest() {
		JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);

		Mail originalMail = new Mail();

		originalMail.setTo(new String[] { "micho@gmail.com" });
		originalMail.setCc(new String[] { "tito@outlook.com", "grande@yahoo.com" });
		originalMail.setBcc(new String[] { "cabezon@hotmail.com" });

		originalMail.setSubject("Nigerian princes in your area!");
		originalMail.setText("No body, only krab kalash.");
		
		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageExample);
		
		Mono<Mail> returnedMailMono = new MailServiceImpl(javaMailSender, "slim@shady.com").send(originalMail);
		Mail returnedMail = returnedMailMono.block();

		assertNotNull(returnedMail);
		assertNotNull(returnedMail.getSendStatus());
		assertEquals(Status.OK, returnedMail.getSendStatus().getStatus());
	}

	@Test 
	void mailSendsErrorTest() {
		JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);

		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageExample);
		Mockito.doThrow(new RuntimeException("Some unexpected error.")).when(javaMailSender)
				.send(Mockito.any(MimeMessage.class));

		Mail originalMail = new Mail();
		Mono<Mail> returnedMailMono = new MailServiceImpl(javaMailSender, "slim@shady.com").send(originalMail);
		Mail returnedMail = returnedMailMono.block();

		assertNotNull(returnedMail);
		assertNotNull(returnedMail.getSendStatus());
		assertEquals(Status.ERROR, returnedMail.getSendStatus().getStatus());
		assertNotNull(returnedMail.getSendStatus().getText());
	}
}
