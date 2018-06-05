package com.ktn.craftsman.bean;

public class Case {

	private String image;
	private String name;
	private long createDate;
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
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "Case [image=" + image + ", name=" + name + ", createDate=" + createDate + "]";
	}
	
}
