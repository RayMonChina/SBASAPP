package com.ideal.zsyy.request;

import com.ideal2.base.gson.CommonReq;


public class UserReq extends CommonReq {

	private String UserAccount;
	private String Password;
	public String getUserAccount() {
		return UserAccount;
	}
	public void setUserAccount(String userAccount) {
		UserAccount = userAccount;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	

}
