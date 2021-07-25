package com.frager.oreport.mailserver.service;

import com.frager.oreport.mailserver.model.Mail;

import reactor.core.publisher.Mono;

public interface MailService {

	public Mono<Mail> send(Mail mail);
}
