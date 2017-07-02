package com.ideal.zsyy.response;

import com.ideal2.base.gson.CommonRes;

public class UserRes extends CommonRes {
	private String UserId;
	private String User_Code;
	private String User_Name;
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getUser_Code() {
		return User_Code;
	}
	public void setUser_Code(String user_Code) {
		User_Code = user_Code;
	}
	public String getUser_Name() {
		return User_Name;
	}
	public void setUser_Name(String user_Name) {
		User_Name = user_Name;
	}
	

}
