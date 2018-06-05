package com.ktn.craftsman.bean;

public class Review {

	private String memberUsername;
	private String memberName;
	private String memberAvatar;
	private int rateProfessional;
	private int rateCommunicate;
	private int rateGeneral;
	private String content;
	private int memberId;
	private long createDate;
	public String getMemberUsername() {
		return memberUsername;
	}
	public void setMemberUsername(String memberUsername) {
		this.memberUsername = memberUsername;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public int getRateProfessional() {
		return rateProfessional;
	}
	public void setRateProfessional(int rateProfessional) {
		this.rateProfessional = rateProfessional;
	}
	public String getMemberAvatar() {
		return memberAvatar;
	}
	public void setMemberAvatar(String memberAvatar) {
		this.memberAvatar = memberAvatar;
	}
	public int getRateCommunicate() {
		return rateCommunicate;
	}
	public void setRateCommunicate(int rateCommunicate) {
		this.rateCommunicate = rateCommunicate;
	}
	public int getRateGeneral() {
		return rateGeneral;
	}
	public void setRateGeneral(int rateGeneral) {
		this.rateGeneral = rateGeneral;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "Review [memberUsername=" + memberUsername + ", memberName=" + memberName + ", memberAvatar="
				+ memberAvatar + ", rateProfessional=" + rateProfessional + ", rateCommunicate=" + rateCommunicate
				+ ", rateGeneral=" + rateGeneral + ", content=" + content + ", memberId=" + memberId + ", createDate="
				+ createDate + "]";
	}
}
