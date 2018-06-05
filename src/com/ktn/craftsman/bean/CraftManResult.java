package com.ktn.craftsman.bean;

import java.util.ArrayList;

public class CraftManResult {

	private ArrayList<CraftMan> list;
	private int total;
	public ArrayList<CraftMan> getList() {
		return list;
	}
	public void setList(ArrayList<CraftMan> list) {
		this.list = list;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "CraftManResult [list=" + list + ", total=" + total + "]";
	}
	
}
