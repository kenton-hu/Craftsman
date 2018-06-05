package com.ktn.craftsman.bean;

import java.util.ArrayList;

public class ADResult {

	ArrayList<AD> list;

	public ArrayList<AD> getList() {
		return list;
	}

	public void setList(ArrayList<AD> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ADList [list=" + list + "]";
	}
}
