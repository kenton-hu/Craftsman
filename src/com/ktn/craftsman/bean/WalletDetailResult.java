package com.ktn.craftsman.bean;

import java.util.ArrayList;

public class WalletDetailResult {

	private int total;
	private ArrayList<WalletDetail> list;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<WalletDetail> getList() {
		return list;
	}
	public void setList(ArrayList<WalletDetail> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "WalletDetailResult [total=" + total + ", list=" + list + "]";
	}
	
}
