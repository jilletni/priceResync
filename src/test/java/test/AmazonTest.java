package test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class AmazonTest {

	public static void handleSinglePrd(String url){
		new PageHandler(url){

			@Override
			public Object handlerPage(Page page) {
				String price = page.getHtml().xpath("//*[@id=\"priceblock_ourprice\"]/text()").toString();
				System.out.println("price:"+price);
			    System.out.println(Pattern.compile("[^\\d,d]").matcher(price).replaceAll(""));
			    
			    return null;
			}
			
		}.process();
	}

	public static void main(String[] args) {
		handleSinglePrd("https://www.amazon.de/dp/B0000A30T3/ref=olp_product_details?_encoding=UTF8&me=");

//	    System.out.println(Pattern.compile("[^\\d(,|.)d]").matcher("EUR 86.89").replaceAll(""));
//		BigDecimal.valueOf(Double.parseDouble("89,44"));
	}

}
