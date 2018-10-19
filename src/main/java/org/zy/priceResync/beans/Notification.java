package org.zy.priceResync.beans;

public class Notification {
	private String title;
	private String body = "";
	private String receivers;
	public String getReceivers() {
		return receivers;
	}
	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Notification append(String msg){
		this.body += (msg + "</br>");
		return this;
	}
}
