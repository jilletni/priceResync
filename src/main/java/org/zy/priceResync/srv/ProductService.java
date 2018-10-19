package org.zy.priceResync.srv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zy.priceResync.beans.Product;
import org.zy.priceResync.db.ResultHandler;
import org.zy.priceResync.db.SqlTemplate;

public class ProductService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProductService.class);
	
	private SqlTemplate sqltmpl = new SqlTemplate();
	
	public void newPTrend(Long prdId, BigDecimal currentP){
		String sql = "insert into ptrend (pid,price) values(?,?)";
		
		this.sqltmpl.update(sql, prdId, currentP);
	}
	
	public void updateLastPrice(Long id, BigDecimal price){
		String sql = "update product set lastprice=? where id=?";
		this.sqltmpl.update(sql, price,id);
	}
	
	public void updateNewPromt(Long id, String newPromt){
		String sql = "update product set str2=? where id=?";
		this.sqltmpl.update(sql, newPromt, id);
	}
	
	public Map<String,Product> loadAllJdProduct(){
		return this.loadProducts("jd");
	}
	
	public Map<String,Product> loadAllYhdProduct(){
		return this.loadProducts("yhd");
	}
	
	public Map<String,Product> loadAllAmzProduct(){
		return this.loadProducts("amz");
	}
	
	public Map<String,Product> loadProducts(String seller){
		final Map<String,Product> rt = new HashMap<String, Product>();
		
		List<Object> ps = new ArrayList<Object>();
		ps.add(seller);
		
		this.sqltmpl.query("select * from product where seller=? and isactive='Y'", ps, new ResultHandler(){
			public void handle(List<Map<String,Object>> rs) {
				for(Map m : rs){
					Product p = new Product();
					p.setId((Long)m.get("ID"));
					p.setExpected((BigDecimal)m.get("EXPECTED"));
					p.setIsActive((String)m.get("ISACTIVE"));
					p.setLastPrice((BigDecimal)m.get("LASTPRICE"));
					p.setModel((String)m.get("MODEL"));
					p.setName((String)m.get("NAME"));
					p.setPrdId((String)m.get("PRDID"));
					p.setSeller((String)m.get("SELLER"));
					p.setSubscribers((String)m.get("SUBSCRIBERS"));
					p.setPrdUrl((String)m.get("PRDURL"));
					p.setExtStr1((String)m.get("STR1"));
					p.setExtStr2((String)m.get("STR2"));
					p.setExtStr3((String)m.get("STR3"));
					p.setExtStr4((String)m.get("STR4"));
					p.setExtInt1((Integer)m.get("INT1"));
					p.setExtInt2((Integer)m.get("INT2"));
					p.setExtInt3((Integer)m.get("INT3"));
					p.setExtInt4((Integer)m.get("INT4"));
					rt.put(p.getPrdId(), p);
				}
			}
			
		});
		
		return rt;
	}
}
