package org.zy.priceResync;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

abstract public class PageHandler {
	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PageHandler.class);

	protected String url;
	
	protected String charset;
	
	public PageHandler(){
		
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public PageHandler(String url){
		this.url = url;
	}
	
	
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Object process(){
		Object rt;
		preProcess();
		
		PageProcessorImpl pp = new PageProcessorImpl();
		Spider.create(pp).addUrl(url).run();
		
		postProcess();
		
		return pp.getResult();
	}
	
	abstract public Object handlerPage(Page page);
	
	protected void preProcess(){
	}
	
	protected void postProcess(){
	}	
	
	protected Object parseJson(String json, Class claz){
		if(log.isDebugEnabled()){
			log.debug(json);
		}
		
		try{
			return JSON.parseArray(json, claz);
		}catch(JSONException e){
			log.error(json);
			throw e;
		}
	}
	
	private class PageProcessorImpl implements PageProcessor{
		private Object result = null;

		@Override
		public void process(Page page) {
			this.result = PageHandler.this.handlerPage(page);
			
		}

		@Override
		public Site getSite() {
			Site rt = Site.me();
			if(PageHandler.this.charset != null){
				rt.setCharset(PageHandler.this.charset);
			}else{
				rt.setCharset(Config.get("app.http.charset"));
			}
			
			//rt.setHttpProxy(new HttpHost("web-proxy.atl.hp.com",8080));
			return rt;
		}
		
		public Object getResult(){
			return this.result;
		}
		
	}
}
