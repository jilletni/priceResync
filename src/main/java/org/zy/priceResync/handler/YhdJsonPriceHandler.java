package org.zy.priceResync.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zy.priceResync.Config;
import org.zy.priceResync.PageHandler;
import org.zy.priceResync.beans.Notification;
import org.zy.priceResync.beans.Product;
import org.zy.priceResync.srv.NotificationService;
import org.zy.priceResync.srv.ProductService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import us.codecraft.webmagic.Page;

public class YhdJsonPriceHandler extends PageHandler {
	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(YhdJsonPriceHandler.class);
	private Map<String,Product> allPd;
	private ProductService pSrv = new ProductService();
	
	public YhdJsonPriceHandler(){
		this.allPd = this.pSrv.loadAllYhdProduct();
		String ids = this.getPrdIdsQryStr();
		super.url = java.text.MessageFormat.format(Config.get("yhd.url.priceQry"), new Object[]{ids});
	}
	
	@Override
	public Object handlerPage(Page page) {
		String json = page.getRawText();
		
		Map allData = JSON.parseObject(json, Map.class);
		
		List<Map> data = (List<Map>)((Map)allData.get("data")).get("values");

		List<Product> rt = new ArrayList<Product>();
		
		for(Map map : data){
			String id = map.get("pmId").toString();
			String priceStr = map.get("currentPrice").toString();
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
