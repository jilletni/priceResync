package org.zy.priceResync.scans;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.zy.priceResync.Config;
import org.zy.priceResync.beans.Notification;
import org.zy.priceResync.beans.Product;
import org.zy.priceResync.handler.AmazonPriceHandler;
import org.zy.priceResync.handler.JdPromtHandler;
import org.zy.priceResync.srv.NotificationService;
import org.zy.priceResync.srv.ProductService;

public class JdPromtScaner {
	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JdPromtScaner.class);
	private JdPromtHandler handler = new JdPromtHandler();
	private NotificationService notiSrv = new NotificationService();
	private ProductService pSrv = new ProductService();
	
	public Object scan() {
		Map<String,Product> allPd = this.pSrv.loadAllJdProduct();
		Map<String,Notification> notiMap = new HashMap<String,Notification>();
		
		for(Iterator<String> it = allPd.keySet().iterator(); it.hasNext();){
			String pId = it.next();
			Product p = allPd.get(pId);
			String url = p.getExtStr1();
			if(url == null || url.isEmpty()){
				continue;
			}
			
			this.handler.setUrl(url);
			String currPromt = null;
			try{
				currPromt = (String)this.handler.process();
			}catch(Exception e){
				log.error(e.getMessage(),e);
				continue;
			}

			//pSrv.newPTrend(p.getId(), currPrice);
			String lastPromt = p.getExtStr2();
			
			if(this.isNullOrEmpty(lastPromt, currPromt)){
				continue;
			}
			
			if(this.isEqual(lastPromt, currPromt)){
				continue;
			}

			this.pSrv.updateNewPromt(p.getId(), currPromt);
			
			String receStr = p.getSubscribers();
			if(receStr == null){
				receStr = Config.get("app.recipients");
			}
			
			String[] reces = receStr.split(",");
			
			for(String rece : reces){
				Notification noti = notiMap.get(rece);
				if(noti == null){
					noti = new Notification();
					noti.setTitle("Notifications: jd promt changed");
					notiMap.put(rece, noti);
				}
						
				noti.append(String.format("%s last[%s] curr[%s]", p.getModel(), lastPromt, currPromt));
				log.info(String.format("%s last[%s] curr[%s]", p.getModel(), lastPromt, currPromt));
				
			}		
		}
		
		this.notiSrv.sendNotification(notiMap);
		
		return null;
	}
	
	private boolean isNullOrEmpty(String str){
		return str == null ? true : str.isEmpty();
	}
	
	private boolean isNullOrEmpty(String...strs){
		for(String str : strs){
			if(!this.isNullOrEmpty(str)){
				return false;
			}
		}
		
		return true;
	}
	
	private boolean isEqual(String str1, String str2){
		if(str1 == null && str2 == null){
			return true;
		}
		
		if(str1 != null){
			return str1.equals(str2);
		}else{
			return str2.equals(str1);
		}
	}
}
