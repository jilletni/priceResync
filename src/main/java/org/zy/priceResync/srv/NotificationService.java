package org.zy.priceResync.srv;

import java.util.Iterator;
import java.util.Map;

import org.zy.priceResync.Config;
import org.zy.priceResync.beans.Notification;
import org.zy.priceResync.mail.SimpleMailSender;

public class NotificationService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NotificationService.class);
	
	public void sendNotification(Map<String,Notification> notificationMap){
		SimpleMailSender mailSender = new SimpleMailSender();
		
		for(Iterator<String> it = notificationMap.keySet().iterator(); it.hasNext();){
			String rece = it.next();
			Notification noti = notificationMap.get(rece);
			try{
				mailSender.send(noti.getTitle(), noti.getBody(), rece);
			}catch(Exception e){
				log.error("fail to send email", e);
			}
		}		
	}
}
