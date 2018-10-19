package org.zy.priceResync;

import org.h2.tools.Server;
import org.zy.priceResync.scans.AmzScaner;
import org.zy.priceResync.scans.JdPromtScaner;
import org.zy.priceResync.scans.JdScaner;
import org.zy.priceResync.scans.YhdScaner;

public class PReysnc {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PReysnc.class);
	
	public void scanAll(){
		try{
			new Thread(){
				@Override
				public void run(){
					new YhdScaner().scan();
				}
			}.start();
			
			new Thread(){
				@Override
				public void run(){
					new JdScaner().scan();
					new JdPromtScaner().scan();
				}
			}.start();

			new Thread(){
				@Override
				public void run(){
					new AmzScaner().scan();
				}
			}.start();
			
		}catch(Exception e){
			log.error("",e);
		}
	}

	public static void main(String[] args) throws Exception{
		PReysnc p = new PReysnc();
		
		String sPort = Config.get("app.h2.server.port");
		String wPort = Config.get("app.h2.web.port");
		Server webServer = Server.createWebServer("-webAllowOthers","-webPort",wPort).start();
        Server server = Server.createTcpServer("-tcpAllowOthers","-tcpPort",sPort).start();

		while(true){
			p.scanAll();
			try{
				String itv = Config.get("jd.reqInfo.interval", "300");
				int interval = Integer.valueOf(itv);
								
				Thread.currentThread().sleep(interval * 1000L);
			}catch(Exception e){
				log.error("interrupted.", e);
			}
		}

	}

}
