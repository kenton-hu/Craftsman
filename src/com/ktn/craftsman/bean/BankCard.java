package com.ktn.craftsman.bean;

public class BankCard {
	private String idName;
	private String bankName;
	private int id;
	private String cardNo;
	private String idNo;
	private String bankImage;
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getBankImage() {
		return bankImage;
	}
	public void setBankImage(String bankImage) {
		this.bankImage = bankImage;
	}
	@Override
	public String toString() {
		return "BankCard [idName=" + idName + ", bankName=" + bankName + ", id=" + id + ", cardNo=" + cardNo + ", idNo="
				+ idNo + ", bankImage=" + bankImage + "]";
	}
	
}
