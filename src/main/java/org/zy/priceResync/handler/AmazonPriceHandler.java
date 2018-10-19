package org.zy.priceResync.handler;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.zy.priceResync.Config;
import org.zy.priceResync.PageHandler;
import org.zy.priceResync.srv.ProductService;

import us.codecraft.webmagic.Page;

public class AmazonPriceHandler extends PageHandler {
	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AmazonPriceHandler.class);
	private static String priceIdInHtml = Config.get("amz.html.priceId");
	private static String regexForPrice = Config.get("amz.html.regex");
	
	
	private ProductService pSrv = new ProductService();

	@Override
	public Object handlerPage(Page page) {
		String format = page.getHtml().xpath("//*[@id=\"" + this.priceIdInHtml + "\"]/text()").toString();
		
		String price = Pattern.compile(regexForPrice).matcher(format).replaceAll("");
		if(price.contains(",")){
			price = price.replaceFirst(",", ".");
		}
		
		return BigDecimal.valueOf(Double.parseDouble(price));
	}

}
