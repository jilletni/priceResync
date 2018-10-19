package org.zy.priceResync.handler;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zy.priceResync.Config;
import org.zy.priceResync.PageHandler;
import org.zy.priceResync.beans.Notification;
import org.zy.priceResync.beans.Product;
import org.zy.priceResync.db.ResultHandler;
import org.zy.priceResync.db.SqlTemplate;
import org.zy.priceResync.mail.SimpleMailSender;
import org.zy.priceResync.srv.NotificationService;
import org.zy.priceResync.srv.ProductService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import us.codecraft.webmagic.Page;

public class JdJsonPriceHandler extends PageHandler {
	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JdJsonPriceHandler.class);
	private Map<String,Product> allPd;
	private ProductService pSrv = new ProductService();
	
	@Override
	protected void preProcess(){
		this.allPd = this.pSrv.loadAllJdProduct();
		String ids = this.getPrdIdsQryStr();
		super.url = java.text.MessageFormat.format(Config.get("jd.url.priceQry"), new Object[]{ids});
	}

	@Override
	public Object handlerPage(Page page) {		
		String json = page.getRawText();
		json = json.substring(0, json.length() - 3).substring(4);
		List<Map> data = (List<Map>)super.parseJson(json, Map.class);
		
		List<Product> rt = new ArrayList<Product>();
		
		for(Map map : data){
			String id = (String)map.get("id");
			String priceStr = (String)map.get("p");
			BigDecimal currPrice = BigDecimal.valueOf(Double.parseDouble(priceStr));
			
			Product p = this.allPd.get(id);
			if(p == null){
				log.warn("cannot find product in database:" + id);
				continue;
			}
			
			pSrv.newPTrend(p.getId(), currPrice);
			
			BigDecimal lstPrice = p.getLastPrice();
			lstPrice = lstPrice == null ? BigDecimal.ZERO : lstPrice;
			
			int rst = currPrice.compareTo(lstPrice);
			if(rst == 0){
				continue;
			}
			
			p.addAttribute("currentPrice", currPrice);
			rt.add(p);
				
			this.pSrv.updateLastPrice(p.getId(), currPrice);
		}
		return rt;
	}
	
	private String getPrdIdsQryStr(){
		String ids = "";
		for(String id : this.allPd.keySet()){
			ids += (id + ",");
		}
	
		return ids.length() == 0 ? ids : ids.substring(0, ids.length() - 1);
	}

}
