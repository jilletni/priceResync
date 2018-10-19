package org.zy.priceResync;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Config.class);
	private static Map<String,String> cfg = new HashMap<String,String>();
	
	static{
		Properties prop = new Properties();
		try{
			prop.load(PReysnc.class.getResourceAsStream("/app.properties"));
		}catch(IOException ioe){
			log.error("cannot read config file.",ioe);
			throw new RuntimeException(ioe);
		}
	
		cfg = new HashMap(prop);
	}
	
	public static String get(String key){
		return cfg.get(key);
	}
	
	public static String get(String key, String def){
		String rt = cfg.get(key);
		if(rt == null){
			return def;
		}
		
		return cfg.get(key);
	}
}
