package com.ktn.craftsman.bean;

import java.util.ArrayList;

public class BankListResult {

	private int total;
	private ArrayList<BankCard> list;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<BankCard> getList() {
		return list;
	}
	public void setList(ArrayList<BankCard> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "BankListResult [total=" + total + ", list=" + list + "]";
	}
}
