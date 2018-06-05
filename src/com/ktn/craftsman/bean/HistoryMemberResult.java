package com.ktn.craftsman.bean;

import java.util.ArrayList;

public class HistoryMemberResult {

	private int total;
	private ArrayList<HistoryMember> list;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<HistoryMember> getList() {
		return list;
	}
	public void setList(ArrayList<HistoryMember> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "HistoryMemberResult [total=" + total + ", list=" + list + "]";
	}
	
}
