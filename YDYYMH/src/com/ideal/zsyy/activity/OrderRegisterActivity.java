package com.ideal.zsyy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fang.sbas.R;

public class OrderRegisterActivity extends Activity {

	private Button btnSpe;
	private Button btnReg;
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_register);
		initView();
	}

	private void initView() {
		btnSpe = (Button) findViewById(R.id.btn_order_specialist);
		btnReg = (Button) findViewById(R.id.btn_order_register);
		btnBack = (Button) findViewById(R.id.btn_back);
		setListener();
	}

	private void setListener() {
		//挂专家号
		btnSpe.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(OrderRegisterActivity.this,PhDoctorInfoActivity.class);
				intent.putExtra("zj", "zj");
				startActivity(intent);
			}
		});
		//普通挂号
		btnReg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(OrderRegisterActivity.this,PhDeptInfoActivity.class);
				intent.putExtra("xz", "xz");
				startActivity(intent);
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
