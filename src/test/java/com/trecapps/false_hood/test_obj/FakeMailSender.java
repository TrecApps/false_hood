package com.trecapps.false_hood.test_obj;

import java.io.InputStream;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class FakeMailSender implements JavaMailSender {
	
	FalsehoodApp app;
	
	FakeMailSender(FalsehoodApp app)
	{
		if(app == null)
			throw new NullPointerException();
		this.app = app;
	}
	

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		System.out.println("Fake Mail Sender send method called!");
		app.sendEmail(simpleMessage);

	}

	@Override
	public void send(SimpleMailMessage... simpleMessages) throws MailException {
		for(SimpleMailMessage message: simpleMessages)
		{
			send(message);
		}

	}

	@Override
	public MimeMessage createMimeMessage() {
		return new MimeMessage((Session)null);
	}

	@Override
	public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void send(MimeMessage mimeMessage) throws MailException {
		System.out.println("Fake Mail Sender send method called!");
		app.sendEmail(mimeMessage);
	}

	@Override
	public void send(MimeMessage... mimeMessages) throws MailException {
		for(MimeMessage message: mimeMessages)
			send(message);
	}

	@Override
	public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
		// TODO Auto-generated method stub

	}

}
