package com.ideal.zsyy.request;

import com.ideal2.base.gson.CommonReq;

public class VersionValidateReq extends CommonReq {

	private String clientType;//客户端类型
	private String authenticationCode;//更新认证码
	private String imei;//设备号
	private String versionCode;//版本号
//	private String clientKind; // 1- idal   2-人民医院
	
	public VersionValidateReq() {
		setOperType("0");
	}

	public String getAuthenticationCode() {
		return authenticationCode;
	}

	public void setAuthenticationCode(String authenticationCode) {
		this.authenticationCode = authenticationCode;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

//	public String getClientKind() {
//		return clientKind;
//	}
//
//	public void setClientKind(String clientKind) {
//		this.clientKind = clientKind;
//	}
	
}
