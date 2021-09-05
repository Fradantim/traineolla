package com.frager.oreport.mailserver.service;

import com.frager.oreport.mailserver.model.Mail;

public interface MailService {

	public Mail send(Mail mail);
}
