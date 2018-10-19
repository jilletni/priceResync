package test;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
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

public class JdTest {
	public static Log log = org.apache.commons.logging.LogFactory.getLog(JdTest.class);
	public static void handleListTable(String url){
		new PageHandler(url){

			@Override
			public Object handlerPage(Page page) {
				String[] targets = new String[]{"G502","G602","G403"};
				
				Html html = page.getHtml();
				System.out.println(html.get());
				List<Selectable> items = html.xpath("//*[@id=\"plist\"]/ul/li").nodes();
				for(Selectable item : items){
					String key = contains(targets, item.xpath("//div/div/a/em").get());
					if(key == null){
						continue;
					}
					
//					String price = item.xpath("//*[@id="plist"]/ul/li[2]/div").toString();
					//System.out.println(key +"," + price);
				}
				
				return null;
			}
			
			public String contains(String[] targets, String str){
				for(String keyw : targets){
					if(str.contains(keyw)){
						return keyw;
					}
				}
				
				return null;
			}
			
		};
	}
	
	public static void handleJson(String url){
		new PageHandler(url){

			@Override
			public Object handlerPage(Page page) {
				String[] targets = new String[]{"G502","G602","G403"};
				
				
				String json = page.getRawText();
				json = json.substring(0, json.length() - 3).substring(4);
				
				System.out.println(json);

				List<Map> data = JSON.parseArray(json, Map.class);
				for(Map map : data){
					System.out.println(map.get("id") + "," + map.get("p"));
				}
				return null;								
			}
			
		};
	}
	
	public static void handleSinglePrd(String url){
		new PageHandler(url){

			@Override
			public Object handlerPage(Page page) {
				System.out.println(page.getHtml().xpath("//*[@id=\"jdPrice-copy\"]/span[1]/text()").toString());
				return null;
			}
			
		}.process();
	}
	
	public static void handlejProm(String url){
		new PageHandler(url){

			@Override
			public Object handlerPage(Page page) {
				String json = page.getRawText();
				
				System.out.println(json);
				log.info(json);

				Map data = JSON.parseObject(json, Map.class);
				Map promt = (Map)data.get("prom");
				List skuCoupons = (List)data.get("skuCoupon");
				
				System.out.println(getPromtAsString(promt));				
				System.out.println(getskucouponsAsString(skuCoupons));
				
				return null;
			}
			
		}.process();
	}
	
	public static String getPromtAsString(Map promt){
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
	
	public static String getskucouponsAsString(List coupons){
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
	
	public static void main(String[] args) {
//		handleListTable("https://list.jd.com/list.html?cat=670,686,690&ev=exbrand_11821&delivery=1&trans=1&JL=3_%E5%93%81%E7%89%8C_%E7%BD%97%E6%8A%80%EF%BC%88Logitech%EF%BC%89#J_crumbsBar");
		//handlejProm("https://cd.jd.com/promotion/v2?skuId=532637&area=2_2813_51976_0&shopId=0&venderId=0&cat=1320%2C5019%2C5021");
		handlejProm("https://cd.jd.com/promotion/v2?skuId=5822919&area=2_2813_51976_0&shopId=1000094141&venderId=1000094141&cat=9987%2C653%2C655");
		//handleJson("http://p.3.cn/prices/mgets?type=1&area=2_2820_51972&pdtk=&pduid=2042879616&pdpin=&pdbp=0&skuIds=J_3777958,J_3777926,J_2269354,J_958838&callback=cnp");
//		System.out.println("\u2191");
	}

}
