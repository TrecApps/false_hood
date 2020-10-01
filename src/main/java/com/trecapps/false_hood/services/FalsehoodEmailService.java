package com.trecapps.false_hood.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class FalsehoodEmailService
{
	@Autowired
	JavaMailSender mailSender;
	
	void sendValidationEmail(String to,
            String subject,
            String code, String appealId) throws MessagingException
	{
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		
		String url = code;
		
		String htmlEmailContent = "<h1>Please use this code to verify your Appeal Signature for appeal "+ appealId +"</h1><br><h2>"+ url +"</h2>";
		
		helper.setSubject(subject);
		helper.setText(htmlEmailContent, true);
		helper.setTo(to);
		mailSender.send(mimeMessage);
	}
}
