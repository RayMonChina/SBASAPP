package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fang.sbas.R;
import com.ideal.zsyy.adapter.SkinChangeAdapter;
import com.ideal.zsyy.entity.SkinInfo;
import com.ideal.zsyy.service.PreferencesService;
import com.ideal.zsyy.utils.DataUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;

public class SkinChangerActivity extends Activity {

	private PreferencesService preferencesService;
	private String skin = "";
	private GridView gv_skin;
	private List<SkinInfo> skininfos;
	private SkinChangeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skin_change);
		preferencesService = new PreferencesService(getApplicationContext());
		skin = (String) preferencesService.getSkin().get("skinName");
		if (skin == null) {
			skin = "0";
		}
		initView();
	}

	private void initView() {
		// 皮肤列表
		gv_skin = (GridView) findViewById(R.id.gv_skin);
		gv_skin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SkinInfo skinInfo = skininfos.get(position);
				for (SkinInfo sk : skininfos) {
					if (sk.getSkinName().equals(skinInfo.getSkinName())) { 
						sk.setIsChoose("1");
					} else {
						sk.setIsChoose("0");
					}
				}
				preferencesService.saveSkin(skinInfo.getSkinName()); 
				adapter.notifyDataSetChanged();
			}
		});
		// 返回
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		queryDate();
	}

	//得到数据
	private void queryDate() {
		skininfos = DataUtils.getSkinInfos();
		for (SkinInfo sk : skininfos) {
			if (skin.equals(sk.getSkinName())) {
				sk.setIsChoose("1"); 
			} else {
				sk.setIsChoose("0"); 
			}
		}
		showdata();
	}

	private void showdata() {
		adapter = new SkinChangeAdapter(this, skininfos);
		gv_skin.setAdapter(adapter);
	}
}
