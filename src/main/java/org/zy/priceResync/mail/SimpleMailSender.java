package org.zy.priceResync.mail;

import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.zy.priceResync.Config;

/**
 * 简单邮件发送器，可单发，群发。
 * 
 * @author
 * 
 */
public class SimpleMailSender {
	private transient MailAuthenticator authenticator;
	private Session session;
	
	public SimpleMailSender() {
		init();

	}

	private void init() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", Config.get("app.email.isAuth"));
		props.put("mail.smtp.host", Config.get("app.email.smtp.host"));
		props.put("mail.smtp.port", Config.get("app.email.smtp.port"));
		props.put("mail.smtp.starttls.enable",Config.get("app.email.smtp.starttls.enable"));		
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", "app.email.smtp.port");
		authenticator = new MailAuthenticator();
		session = Session.getInstance(props, authenticator);
	}

	public void send(String subject, Object content, String...recipients)
			throws AddressException, MessagingException {
		final MimeMessage message = new MimeMessage(session);
		System.out.println(session.getProperties());
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		final int num = recipients.length;
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipients[i]);
		}
		message.setRecipients(RecipientType.TO, addresses);
		message.setSubject(subject);
		message.setContent(content.toString(), "text/html;charset=utf-8");
		Transport.send(message);
	}

}