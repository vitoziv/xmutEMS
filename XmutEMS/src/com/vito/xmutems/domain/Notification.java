package com.vito.xmutems.domain;

import java.io.Serializable;

import org.json.JSONObject;

public class Notification implements Serializable {
	/**
	 * Default generate ID
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String publisher;
	private String publishDate;
	private String valid;
	private String url;
	
	
	public Notification(String title, String publisher, String publishDate,
			String valid, String url) {
		super();
		this.title = title;
		this.publisher = publisher;
		this.publishDate = publishDate;
		this.valid = valid;
		this.url = url;
	}

	//解析json数据
	public Notification(JSONObject jsonNotification) {
		title = jsonNotification.optString("title");
		publisher = jsonNotification.optString("publisher");
		publishDate = jsonNotification.optString("publishDate");
		valid = jsonNotification.optString("valid");
		url = jsonNotification.optString("url");
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
