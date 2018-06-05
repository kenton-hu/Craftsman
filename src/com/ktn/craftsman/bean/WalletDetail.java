package com.ktn.craftsman.bean;

public class WalletDetail {

	private int balance;
	private String memo;
	private int id;
	private String credit;
	private String debit;
	private String name;
	private long createDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getDebit() {
		return debit;
	}
	public void setDebit(String debit) {
		this.debit = debit;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "WalletDetail [balance=" + balance + ", memo=" + memo + ", id=" + id + ", credit=" + credit + ", debit="
				+ debit + ", createDate=" + createDate + "]";
	}
	
}
