package org.zy.priceResync.scans;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zy.priceResync.Config;
import org.zy.priceResync.beans.Notification;
import org.zy.priceResync.beans.Product;
import org.zy.priceResync.handler.YhdJsonPriceHandler;
import org.zy.priceResync.srv.NotificationService;

public class YhdScaner {
	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(YhdScaner.class);
	private YhdJsonPriceHandler handler = new YhdJsonPriceHandler();
	private NotificationService notiSrv = new NotificationService();
	
	public Object scan() {
		List<Product> ps = (List<Product>) this.handler.process();

		if (ps.size() == 0) {
			return null;
		}
		
		Map<String,Notification> notiMap = new HashMap<String,Notification>();

		for (Product p : ps) {
			String name = p.getName();
			String model = p.getModel();

			BigDecimal currPrice = (BigDecimal) p.getAttribute("currentPrice");
			BigDecimal lstPrice = p.getLastPrice();
			lstPrice = lstPrice == null ? BigDecimal.ZERO : lstPrice;

			int rst = currPrice.compareTo(lstPrice);
			
			String receStr = p.getSubscribers();
			if(receStr == null){
				receStr = Config.get("app.recipients");
			}
			
			String[] reces = receStr.split(",");
			
			for(String rece : reces){
				Notification noti = notiMap.get(rece);
				if(noti == null){
					noti = new Notification();
					noti.setTitle("Notifications: yhd prices changed");
					notiMap.put(rece, noti);
				}
						
				if (rst > 0) {
					noti.append(model + " \u2191 " + currPrice + "(" + lstPrice + ")");
					log.info(model + " \u2191 " + currPrice + "(" + lstPrice + ")");
				} else if (rst < 0) {
					noti.append(model + " \u2193 " + currPrice + "(" + lstPrice + ")");
					log.info(model + " \u2193 " + currPrice + "(" + lstPrice + ")");
				}
				
			}			
		}

		this.notiSrv.sendNotification(notiMap);

		return null;
	}
}
