package test;

import org.zy.priceResync.mail.SimpleMailSender;

public class MailTest {
	public static void main(String[] args) throws Exception {
		SimpleMailSender mailSender = new SimpleMailSender();
		
		mailSender.send("Test for mail sender", "1.for mail sending test<br>2.<br>3.", "244744839@qq.com","244744839@qq.com");
	}
}
