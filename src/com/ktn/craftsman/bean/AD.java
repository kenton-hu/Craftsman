package com.ktn.craftsman.bean;

import com.ktn.craftsman.R.id;

public class AD {

	private String image;
	private String name;
	private int id;
	private String url;
	private int order;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	@Override
	public String toString() {
		return "AD [image=" + image + ", name=" + name + ", id=" + id + ", url=" + url + ", order=" + order + "]";
	}
	
}
