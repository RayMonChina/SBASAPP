package com.ideal.zsyy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.fang.sbas.R;
import com.ideal.zsyy.service.PreferencesService;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 3000; //延迟三秒
	private PreferencesService preferencesService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_PROGRESS); //去标题栏 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		preferencesService = new PreferencesService(getApplicationContext());
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				//boolean islauncher = preferencesService.getIslauncher();
				Intent intent=null;
				if(preferencesService.getIsLogin()){
					intent = new Intent(SplashActivity.this,MainActivity.class);
				}
				else {
					intent = new Intent(SplashActivity.this,LoginActivity.class);
				}
				
//				if (islauncher) {
//					intent = new Intent(SplashActivity.this,MainActivity.class);
////					intent = new Intent(SplashActivity.this,HospitalListActivity.class);
//				} else {
//					intent = new Intent(SplashActivity.this,WizardInterfaceActivity.class);
//				}
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();
			}
		}, SPLASH_DISPLAY_LENGHT);
	}
}
