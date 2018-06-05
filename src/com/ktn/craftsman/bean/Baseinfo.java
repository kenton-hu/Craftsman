package com.ktn.craftsman.bean;

public class Baseinfo {
	private String avatar;
	private String gender;
	private long balance;
	private String identity;
	private boolean authId;
	private boolean authWork;
	private Job job;
	private long award;
	private long expireDate;
	private String phone;
	private String name;
	private String introduction;
	public long getBalance() {
		return balance;
	}
	public String getAvatar() {
		return avatar;
	}
	public long getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(long expireDate) {
		this.expireDate = expireDate;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public long getAward() {
		return award;
	}
	public void setAward(long award) {
		this.award = award;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public boolean isAuthId() {
		return authId;
	}
	public void setAuthId(boolean authId) {
		this.authId = authId;
	}
	public boolean isAuthWork() {
		return authWork;
	}
	public void setAuthWork(boolean authWork) {
		this.authWork = authWork;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
