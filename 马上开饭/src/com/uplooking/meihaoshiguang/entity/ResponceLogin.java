package com.uplooking.meihaoshiguang.entity;

import com.google.gson.annotations.SerializedName;

public class ResponceLogin {
	private int loginState;//����״̬
	private ResponceUserInfo userInfo;
	public int getLoginState() {
		return loginState;
	}
	public void setLoginState(int loginState) {
		this.loginState = loginState;
	}
	public ResponceUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(ResponceUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	@SerializedName("HeWeather data service 3.0")
	public String HeWeather;
}
