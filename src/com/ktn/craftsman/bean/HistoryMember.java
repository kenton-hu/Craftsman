package com.ktn.craftsman.bean;

import android.content.Loader.ForceLoadContentObserver;

public class HistoryMember {

	private int memberConsulationCount;
	private long memberCreateDate;
	private int memberId;
	private String memberLat;
	private String memberLng;
	private String memberName;
	private String memberPhone;
	private String memberUsername;
	private int memberViews;
	private String memberAvatar;
	private String memberJobName;
	private int memberJobId;
	private float memberRateGeneral;
	public int getMemberConsulationCount() {
		return memberConsulationCount;
	}
	public void setMemberConsulationCount(int memberConsulationCount) {
		this.memberConsulationCount = memberConsulationCount;
	}
	
	public float getMemberRateGeneral() {
		return memberRateGeneral;
	}
	public void setMemberRateGeneral(float memberRateGeneral) {
		this.memberRateGeneral = memberRateGeneral;
	}
	public long getMemberCreateDate() {
		return memberCreateDate;
	}
	public void setMemberCreateDate(long memberCreateDate) {
		this.memberCreateDate = memberCreateDate;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getMemberLat() {
		return memberLat;
	}
	public void setMemberLat(String memberLat) {
		this.memberLat = memberLat;
	}
	public String getMemberLng() {
		return memberLng;
	}
	public void setMemberLng(String memberLng) {
		this.memberLng = memberLng;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberPhone() {
		return memberPhone;
	}
	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}
	public String getMemberUsername() {
		return memberUsername;
	}
	public void setMemberUsername(String memberUsername) {
		this.memberUsername = memberUsername;
	}
	public int getMemberViews() {
		return memberViews;
	}
	public void setMemberViews(int memberViews) {
		this.memberViews = memberViews;
	}
	public String getMemberAvatar() {
		return memberAvatar;
	}
	public void setMemberAvatar(String memberAvatar) {
		this.memberAvatar = memberAvatar;
	}
	public String getMemberJobName() {
		return memberJobName;
	}
	public void setMemberJobName(String memberJobName) {
		this.memberJobName = memberJobName;
	}
	public int getMemberJobId() {
		return memberJobId;
	}
	public void setMemberJobId(int memberJobId) {
		this.memberJobId = memberJobId;
	}
	@Override
	public String toString() {
		return "HistoryMember [memberConsulationCount=" + memberConsulationCount + ", memberCreateDate="
				+ memberCreateDate + ", memberId=" + memberId + ", memberLat=" + memberLat + ", memberLng=" + memberLng
				+ ", memberName=" + memberName + ", memberPhone=" + memberPhone + ", memberUsername=" + memberUsername
				+ ", memberViews=" + memberViews + ", memberAvatar=" + memberAvatar + ", memberJobName=" + memberJobName
				+ ", memberJobId=" + memberJobId + ", memberRateGeneral=" + memberRateGeneral + "]";
	}
	
}
