package com.frager.oreport.mailserver.service.impl;

import java.util.Arrays;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.frager.oreport.mailserver.model.Mail;
import com.frager.oreport.mailserver.model.SendStatus;
import com.frager.oreport.mailserver.service.MailService;

import reactor.core.publisher.Mono;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	private String from;

	private JavaMailSender javaMailSender;

	public MailServiceImpl(@Autowired JavaMailSender javaMailSender, @Value("${spring.mail.username}") String from) {
		this.javaMailSender = javaMailSender;
		this.from = from;
	}

	@Override
	public Mono<Mail> send(Mail mail) {
		return Mono.fromCallable(() -> sendMailOperation(mail));
	}

	private Mail sendMailOperation(Mail mail) {
		try {
			if (logger.isDebugEnabled())
				logger.debug("Enviando correo a {}.", Arrays.toString(mail.getTo()));

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false); // true indicates multipart message
			helper.setFrom(from);
			helper.setSubject(mail.getSubject());
			helper.setTo(mail.getTo());
			helper.setText(mail.getText(), true);// true indicates body is html
			javaMailSender.send(message);

			if (logger.isDebugEnabled())
				logger.debug("Correo a {} correctamente.", Arrays.toString(mail.getTo()));

			mail.setSendStatus(SendStatus.ok());
		} catch (Exception e) {
			logger.error("Error al enviar correo.", e);
			mail.setSendStatus(SendStatus.error(ExceptionUtils.getStackTrace(e)));
		}

		return mail;
	}
}
