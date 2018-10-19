package org.zy.priceResync.beans;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Product {
	private Long id;
	private String model;
	private String seller;
	private String prdId;
	private String name;
	private BigDecimal expected;
	private BigDecimal lastPrice;
	private String isActive;
	private String subscribers;
	private String prdUrl;
	private String extStr1;
	private String extStr2;
	private String extStr3;
	private String extStr4;
	private int extInt1;
	private int extInt2;
	private int extInt3;
	private int extInt4;
	
	private Map attribute = new HashMap();
	
	public String getPrdUrl() {
		return prdUrl;
	}
	public void setPrdUrl(String prdUrl) {
		this.prdUrl = prdUrl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getPrdId() {
		return prdId;
	}
	public void setPrdId(String prdId) {
		this.prdId = prdId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getExpected() {
		return expected;
	}
	public void setExpected(BigDecimal expected) {
		this.expected = expected;
	}
	public BigDecimal getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(String subscribers) {
		this.subscribers = subscribers;
	}
	
	public void addAttribute(Object key, Object value){
		this.attribute.put(key, value);
	}
	
	public Object getAttribute(Object key){
		return this.attribute.get(key);
	}
	public String getExtStr1() {
		return extStr1;
	}
	public void setExtStr1(String extStr1) {
		this.extStr1 = extStr1;
	}
	public String getExtStr2() {
		return extStr2;
	}
	public void setExtStr2(String extStr2) {
		this.extStr2 = extStr2;
	}
	public String getExtStr3() {
		return extStr3;
	}
	public void setExtStr3(String extStr3) {
		this.extStr3 = extStr3;
	}
	public String getExtStr4() {
		return extStr4;
	}
	public void setExtStr4(String extStr4) {
		this.extStr4 = extStr4;
	}
	public int getExtInt1() {
		return extInt1;
	}
	public void setExtInt1(int extInt1) {
		this.extInt1 = extInt1;
	}
	public int getExtInt2() {
		return extInt2;
	}
	public void setExtInt2(int extInt2) {
		this.extInt2 = extInt2;
	}
	public int getExtInt3() {
		return extInt3;
	}
	public void setExtInt3(int extInt3) {
		this.extInt3 = extInt3;
	}
	public int getExtInt4() {
		return extInt4;
	}
	public void setExtInt4(int extInt4) {
		this.extInt4 = extInt4;
	}
}
