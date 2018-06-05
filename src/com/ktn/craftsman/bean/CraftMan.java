package com.ktn.craftsman.bean;

public class CraftMan {

	private int consulationCount;
	private long createDate;
	private int id;
	private String avatar;
	private String lat;
	private String lng;
	private String name;
	private String phone;
	private float rateGeneral;
	private int views;
	
	private double distance;

	
	public CraftMan(){
		
	}
	
	public CraftMan(HistoryMember member){
		this.consulationCount = member.getMemberConsulationCount();
		this.createDate = member.getMemberCreateDate();
		this.id = member.getMemberId();
		this.avatar = member.getMemberAvatar();
		this.lat = member.getMemberLat();
		this.lng = member.getMemberLng();
		this.name = member.getMemberName();
		this.phone = member.getMemberPhone();
		this.rateGeneral = member.getMemberRateGeneral();
		this.views = member.getMemberViews();
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getConsulationCount() {
		return consulationCount;
	}

	public void setConsulationCount(int consulationCount) {
		this.consulationCount = consulationCount;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public float getRateGeneral() {
		return rateGeneral;
	}

	public void setRateGeneral(float rateGeneral) {
		this.rateGeneral = rateGeneral;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "CraftMan [consulationCount=" + consulationCount + ", createDate=" + createDate + ", id=" + id + ", lat="
				+ lat + ", lng=" + lng + ", name=" + name + ", phone=" + phone + ", rateGeneral=" + rateGeneral
				+ ", views=" + views + ", distance=" + distance + "]";
	}
	
}
