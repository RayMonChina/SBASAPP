package com.ideal.zsyy.activity;

import java.io.File;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.service.PreferencesService;
import com.ideal.zsyy.utils.AutoUpdateUtil;
import com.ideal.zsyy.utils.DeviceHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StillMoreActivity extends Activity {

	private PreferencesService preferencesService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stillmore);
		preferencesService = new PreferencesService(getApplicationContext());
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		// 返回
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
		// 安全退出
		Button btn_quit = (Button) findViewById(R.id.btn_quit);
		btn_quit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						StillMoreActivity.this);
				builder.setTitle("确定退出?");
				builder.setNeutralButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								preferencesService.clearLogin();
								Config.phUsers = null;
								finish();
							}
						});

				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				builder.create().show();

			}
		});
		
		//清除缓存
		LinearLayout ll_clear=(LinearLayout) findViewById(R.id.ll_clear);
		ll_clear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//清除当前应用缓存
				File cacheDir = getApplicationContext().getCacheDir();
				clearCacheDir(cacheDir);
				//清除外部应用缓存
				/*if(Environment.getExternalStorageState().equals(  
	                    Environment.MEDIA_MOUNTED)){
					clearCacheDir(getApplicationContext().getExternalCacheDir());
				}*/
				Toast.makeText(getApplicationContext(), "清除成功", 1).show();
			}
		});
		
		// 更换皮肤
		LinearLayout ll_skin = (LinearLayout) findViewById(R.id.ll_skin);
		ll_skin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StillMoreActivity.this,
						SkinChangerActivity.class);
				startActivity(intent);
			}
		});

		// 意见反馈
		LinearLayout ll_feedback = (LinearLayout) findViewById(R.id.ll_feedback);
		ll_feedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(StillMoreActivity.this,
//						PhFeedBackActivity.class);
//				startActivity(intent);
			}
		});

		// 版本更新
		LinearLayout ll_version = (LinearLayout) findViewById(R.id.ll_version);
		ll_version.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isNewApk = AutoUpdateUtil.isNewApk;
				if (isNewApk) {
					AutoUpdateUtil auUpdateUtil = new AutoUpdateUtil(StillMoreActivity.this, "2",preferencesService);
					auUpdateUtil.checkVersionSys();
				}
			}
		});
		
		TextView tvVersion = (TextView) findViewById(R.id.tv_version);
		boolean isNewApk = AutoUpdateUtil.isNewApk;
		if (isNewApk) {
			tvVersion.setText("有最新版本"); 
		} else {
			tvVersion.setText("当前版本 "  + DeviceHelper.getVersionName(this));  
		}
		
		//免责申明
		LinearLayout llRelift = (LinearLayout) findViewById(R.id.ll_relief);
		llRelift.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(StillMoreActivity.this,MZSMActivity.class);
//				startActivity(intent); 
			}
		});
	}

	private void clearCacheDir(File cacheDir) {
		if(cacheDir!=null && cacheDir.exists() && cacheDir.isDirectory()){
			for(File item:cacheDir.listFiles()){
				item.delete();
			}
		}
	}
}
