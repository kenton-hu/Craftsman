package com.ktn.craftsman.bean;

import java.util.ArrayList;

public class NoticeResult {

	private int total;
	private ArrayList<Notice> list;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<Notice> getList() {
		return list;
	}
	public void setList(ArrayList<Notice> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "NoticeResult [total=" + total + ", list=" + list + "]";
	}
	
}
