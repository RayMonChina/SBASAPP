package com.ideal.zsyy.request;

import com.ideal2.base.gson.CommonReq;

public class SbasContactReq extends CommonReq {

	private String user_ID;
	private String search_Content;
	public String getUser_ID() {
		return user_ID;
	}
	public void setUser_ID(String user_ID) {
		this.user_ID = user_ID;
	}
	public String getSearch_Content() {
		return search_Content;
	}
	public void setSearch_Content(String search_Content) {
		this.search_Content = search_Content;
	}
	
}
