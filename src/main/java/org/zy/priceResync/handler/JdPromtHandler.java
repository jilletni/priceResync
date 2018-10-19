package org.zy.priceResync.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.zy.priceResync.Config;
import org.zy.priceResync.PageHandler;
import org.zy.priceResync.srv.ProductService;

import com.alibaba.fastjson.JSON;

import us.codecraft.webmagic.Page;

public class JdPromtHandler extends PageHandler {
	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JdPromtHandler.class);
	
	public JdPromtHandler(){
		this.charset = Config.get("app.http.charset.jd");
	}

	private ProductService pSrv = new ProductService();

	@Override
	public Object handlerPage(Page page) {
		String json = page.getRawText();
		
		Map data = JSON.parseObject(json, Map.class);
		Map promtMap = (Map)data.get("prom");
		List skuCoupons = (List)data.get("skuCoupon");
		
		String promt = this.getPromtAsString(promtMap);				
		String coupons = this.getskucouponsAsString(skuCoupons);
		
		if(promt == null && coupons == null){
			return null;
		}else{
			return promt + "," + coupons; 
		}
	}
	
	public String getPromtAsString(Map promt){
		List pickOneTags = (List)promt.get("pickOneTag");
		if(pickOneTags == null || pickOneTags.size() == 0){
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < pickOneTags.size(); i++){
			Map m = (Map)pickOneTags.get(i);
			String cnt = (String)m.get("content");
			sb.append(cnt).append(" ");
			
		}
		return sb.toString();
	}
	
	public String getskucouponsAsString(List coupons){
		if(coupons == null || coupons.size() == 0){
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < coupons.size(); i++){
			Map m = (Map)coupons.get(i);
			String quota = m.get("quota").toString();
			String trueDiscount = m.get("trueDiscount").toString();
			sb.append(String.format("quota[%s]discount[%s]", quota, trueDiscount)).append(" ");
		}
		
		return sb.toString();
	}

}
