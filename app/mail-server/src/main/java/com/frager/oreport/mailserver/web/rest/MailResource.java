package com.frager.oreport.mailserver.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frager.oreport.mailserver.model.Mail;
import com.frager.oreport.mailserver.service.MailService;

import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mail")
public class MailResource {

	@Autowired
	private MailService mailService;

	@Operation(description="${api-docs.request.mail.description}")
	@PostMapping()
	public Mono<Mail> send(@RequestBody Mail mail) {
		return mailService.send(mail);
	}
}
