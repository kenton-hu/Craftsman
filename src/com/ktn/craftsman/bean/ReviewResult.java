package com.ktn.craftsman.bean;

import java.util.ArrayList;

public class ReviewResult {

	private int total;
	private ArrayList<Review> list;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<Review> getList() {
		return list;
	}
	public void setList(ArrayList<Review> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "ReviewResult [total=" + total + ", list=" + list + "]";
	}
	
}
