package com.ideal.zsyy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fang.sbas.R;

/**
 * 设置
 * 
 * @author KOBE
 * 
 */
public class SettingActivity extends Activity {

	
	private TextView tv_skin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		initView();

	}

	private void initView() {
		tv_skin = (TextView) findViewById(R.id.tv_skin);
		//进入换肤界面
		tv_skin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,SkinChangerActivity.class);
				startActivity(intent);
			}
		});
		
		
		//返回
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
