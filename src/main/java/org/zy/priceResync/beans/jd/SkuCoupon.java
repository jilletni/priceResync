package org.zy.priceResync.beans.jd;

public class SkuCoupon {
	private String id;
	private String couponType;
	private String trueDiscount;
	private String beginTime;
	private String endTime;
	private String discount;
	private String limitType;
	private String quota;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getTrueDiscount() {
		return trueDiscount;
	}
	public void setTrueDiscount(String trueDiscount) {
		this.trueDiscount = trueDiscount;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getLimitType() {
		return limitType;
	}
	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}
	public String getQuota() {
		return quota;
	}
	public void setQuota(String quota) {
		this.quota = quota;
	}
}
