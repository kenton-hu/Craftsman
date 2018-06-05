package com.ktn.craftsman.bean;

import java.security.KeyStore.PrivateKeyEntry;

public class Notice {

	private String name;
	private int id;
	private int order;
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
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	@Override
	public String toString() {
		return "Notice [name=" + name + ", id=" + id + ", order=" + order + "]";
	}
	
}
