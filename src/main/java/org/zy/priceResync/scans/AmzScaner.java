package org.zy.priceResync.scans;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.zy.priceResync.Config;
import org.zy.priceResync.beans.Notification;
import org.zy.priceResync.beans.Product;
import org.zy.priceResync.handler.AmazonPriceHandler;
import org.zy.priceResync.srv.NotificationService;
import org.zy.priceResync.srv.ProductService;

public class AmzScaner {
	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AmzScaner.class);
	private AmazonPriceHandler handler = new AmazonPriceHandler();
	private NotificationService notiSrv = new NotificationService();
	private ProductService pSrv = new ProductService();
	
	public Object scan() {
		Map<String,Product> allPd = this.pSrv.loadAllAmzProduct();
		Map<String,Notification> notiMap = new HashMap<String,Notification>();
		
		for(Iterator<String> it = allPd.keySet().iterator(); it.hasNext();){
			String pId = it.next();
			Product p = allPd.get(pId);
			
			this.handler.setUrl(p.getPrdUrl());
			BigDecimal currPrice = null;
			try{
				currPrice = (BigDecimal)this.handler.process();
			}catch(Exception e){
				log.error(e.getMessage(),e);
				continue;
			}

			pSrv.newPTrend(p.getId(), currPrice);
			BigDecimal lstPrice = p.getLastPrice();
			lstPrice = lstPrice == null ? BigDecimal.ZERO : lstPrice;
			
			int rst = currPrice.compareTo(lstPrice);
			if(rst != 0){
				this.pSrv.updateLastPrice(p.getId(), currPrice);
			}else{
				continue;
			}
			
			String receStr = p.getSubscribers();
			if(receStr == null){
				receStr = Config.get("app.recipients");
			}
			
			String[] reces = receStr.split(",");
			
			for(String rece : reces){
				Notification noti = notiMap.get(rece);
				if(noti == null){
					noti = new Notification();
					noti.setTitle("Notifications: amazon prices changed");
					notiMap.put(rece, noti);
				}
						
				if (rst > 0) {
					noti.append(p.getModel() + " \u2191 " + currPrice + "(" + lstPrice + ")");
					log.info(p.getModel() + " \u2191 " + currPrice + "(" + lstPrice + ")");
				} else if (rst < 0) {
					noti.append(p.getModel() + " \u2193 " + currPrice + "(" + lstPrice + ")");
					log.info(p.getModel() + " \u2193 " + currPrice + "(" + lstPrice + ")");
				}
				
			}		
		}
		
		this.notiSrv.sendNotification(notiMap);
		
		return null;
	}
}
