package com.ideal.zsyy.response;

import java.util.List;

import com.ideal.zsyy.entity.SBASContactInfo;
import com.ideal2.base.gson.CommonRes;

public class SbasContactRes extends CommonRes {

	private List<SBASContactInfo>ContactList;

	public List<SBASContactInfo> getContactList() {
		return ContactList;
	}

	public void setContactList(List<SBASContactInfo> contactList) {
		this.ContactList = contactList;
	}
	
}
