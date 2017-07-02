package com.ideal.zsyy.service;

import java.util.HashMap;
import java.util.Map;

import com.ideal.zsyy.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {

	private Context context;

	public final static String SPF = "DATA_CACHE_ZSYY";

	public PreferencesService(Context context) {
		this.context = context;
	}

	public void clearLogin() {
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putBoolean("isLogin", false);
		editor.commit();
	}

	public boolean getIsLogin() {
		
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		return preferences.getBoolean("isLogin", false);
	}

	public void saveLoginInfo(String loginName, String pwd, boolean isLogin, String use_id,String user_code) {
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putString("loginName", loginName);
		editor.putString("pwd", pwd);
		editor.putBoolean("isLogin", isLogin);
		editor.putString("use_id", use_id);
		editor.putString("user_code",user_code);
		editor.commit();
	}

	public Map<String, Object> getLoginInfo() {
		Map<String, Object> params = new HashMap<String, Object>();
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		params.put("loginName", preferences.getString("loginName", ""));
		params.put("pwd", preferences.getString("pwd", ""));
		params.put("isLogin", preferences.getBoolean("isLogin", true));
		params.put("use_id", preferences.getString("use_id", ""));
		params.put("user_code", preferences.getString("user_code", ""));
		return params;
	}
	
	public void saveAutoUpdate(boolean isFlag){
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putBoolean("isAuto", isFlag);
		editor.commit();
	}
	
	public boolean getIsAutpUpdate(){
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		boolean isAuto = preferences.getBoolean("isAutp", true);
		return isAuto;
	}
	
	public void saveSkin(String skinName) {
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putString("skinName", skinName);
		editor.commit();
	}
	
	public Map<String, Object> getSkin() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		
		params.put("skinName", preferences.getString("skinName", Config.SKIN_DEFAULT));
		return params;
	}
	
	public boolean getIslauncher(){
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		boolean isAuto = preferences.getBoolean("Islauncher", false);
		return isAuto;
	}

	public void saveIslauncher(boolean Islauncher){
		SharedPreferences preferences = context.getSharedPreferences(SPF,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putBoolean("Islauncher", Islauncher);
		editor.commit();
	}
}
