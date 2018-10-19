package test;

import java.util.List;
import java.util.Map;

import org.zy.priceResync.PageHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.minidev.json.JSONValue;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.selector.Selectable;

public class YhdTest {
	public static void singleHandleJson(String url){
		new PageHandler(url){

			@Override
			public Object handlerPage(Page page) {
				String json = page.getRawText();
				
				System.out.println(json);
				
				//json = "[" + json + "]";

//				List<Map> data = JSON.parseArray(json, Map.class);
				Map map = JSON.parseObject(json, Map.class);
//				for(Map map : data){
//					System.out.println(map.get("id") + "," + map.get("p"));
//				}
				System.out.println(map.get("currentPrice"));
				return null;					
			}
			
		}.process();
	}
	
	public static void handleJson(String url){
		new PageHandler(url){

			@Override
			public Object handlerPage(Page page) {
				String json = page.getRawText();
				
				System.out.println(json);
				
				//json = "[" + json + "]";

				Map map = JSON.parseObject(json, Map.class);
				List<Map> data = (List<Map>)((Map)map.get("data")).get("values");
				
				System.out.println(">>>>" + data.get(0).get("currentPrice"));
				return null;					
			}
			
		}.process();
	}

	public static void main(String[] args) {
		//singleHandleJson("http://gps.yhd.com/restful/detail?mcsite=1&provinceId=1&pmId=67800529");
		handleJson("http://item.yhd.com/item/ajax/initOptionCollectPrice.do?pminfoIds=12417418,956818,7490163,101192,1129351,1129342,11557257&mainPmInfoId=11557257&_=1481614570781");
		
	}

}
