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

public class KaolaTest {
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

	public static void main(String[] args) {
//		handleListTable("https://list.jd.com/list.html?cat=670,686,690&ev=exbrand_11821&delivery=1&trans=1&JL=3_%E5%93%81%E7%89%8C_%E7%BD%97%E6%8A%80%EF%BC%88Logitech%EF%BC%89#J_crumbsBar");
//		handleSinglePrd("https://item.m.jd.com/product/2269354.html");
		//handleJson("http://p.3.cn/prices/mgets?type=1&area=2_2820_51972&pdtk=&pduid=2042879616&pdpin=&pdbp=0&skuIds=J_3777958,J_3777926,J_2269354,J_958838&callback=cnp");
//		System.out.println("\u2191");
		handleSinglePrd("http://www.kaola.com/product/1444642.html?referPage=searchPage&referId=%E9%9B%85%E8%AF%97%E5%85%B0%E9%BB%9B&from=page1&position=1&istext=0&srId=f89e48239a24c0f0fab9356792d2b454&zn=result&zp=page1-1&ri=%E9%9B%85%E8%AF%97%E5%85%B0%E9%BB%9B&rp=search");
	}

}
