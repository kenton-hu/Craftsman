package com.ktn.craftsman.bean;

public class NoticeDetail {

	private String name;
	private int id;
	private String content;
	private long createDate;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "NoticeDetail [name=" + name + ", id=" + id + ", content=" + content + ", createDate=" + createDate
				+ "]";
	}
	
}
