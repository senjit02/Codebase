package com.ishare.split.vo;

public class UserSessionVO extends UserVO{
	
	int sessionUserId;
	String sessionCreateDate;
	String sessionUserName;
	public int getSessionUserId() {
		return sessionUserId;
	}
	public void setSessionUserId(int sessionUserId) {
		this.sessionUserId = sessionUserId;
	}
	public String getSessionCreateDate() {
		return sessionCreateDate;
	}
	public void setSessionCreateDate(String sessionCreateDate) {
		this.sessionCreateDate = sessionCreateDate;
	}
	public String getSessionUserName() {
		return sessionUserName;
	}
	public void setSessionUserName(String sessionUserName) {
		this.sessionUserName = sessionUserName;
	}
	
	

}
