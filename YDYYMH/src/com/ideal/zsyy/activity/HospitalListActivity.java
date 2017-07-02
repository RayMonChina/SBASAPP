package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.List;

import com.ideal.wdm.tools.DataCache;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.HospitalListAdapter;
import com.ideal.zsyy.entity.BasiHosInfo;
import com.ideal.zsyy.entity.PhHospitalInfo;
import com.ideal.zsyy.request.AllHospitalInfoReq;
import com.ideal.zsyy.response.AllHospitalInfoRes;
import com.ideal.zsyy.service.PreferencesService;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HospitalListActivity extends Activity {

	private GridView gv_hospitallist;
	private List<BasiHosInfo> hospitallist;
	private ListView lv_hospitallist;
	private TextView tv_city;
	private PreferencesService preferencesService;
	private String cityName = "上海市";
	private String cityCode = "310000";

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				initView();
				break;

			default:
				break;
			}
		};
	};
	private HospitalListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospitallist);
		preferencesService = new PreferencesService(getApplicationContext());
		// getData();
		queryData();
		// initView();
	}

	private void initView() {
		lv_hospitallist = (ListView) findViewById(R.id.lv_hospitallist);
		adapter = new HospitalListAdapter(this, hospitallist);
		lv_hospitallist.setAdapter(adapter);

		// 城市列表
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_city.setText(cityName);
		tv_city.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HospitalListActivity.this,
						CityInfoActivity.class);
				startActivityForResult(intent, 1);
			}
		});

		// 选择医院
		lv_hospitallist
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						BasiHosInfo hosInfo = hospitallist.get(position);
						if (hosInfo.getId() != null
								&& !"".equals(hosInfo.getId())) {
							Intent intent = new Intent(
									HospitalListActivity.this,
									MainActivity.class);
							intent.putExtra("hospId", "CD3FDDCD-97CA-4810-85AC-BFBDFD60F2BC");
							startActivity(intent);
							finish();
						}
					}
				});

		// gv_hospitallist = (GridView) findViewById(R.id.gv_hospitallist);
		// HospitalListAdapter adapter = new HospitalListAdapter(this,
		// hospitallist);
		// gv_hospitallist.setAdapter(adapter);
		// gv_hospitallist.setOnItemClickListener(new
		// AdapterView.OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// // ImageView iv_hosp_bg = (ImageView)
		// view.findViewById(R.id.iv_hosp_bg);
		// // iv_hosp_bg.setVisibility(View.VISIBLE);
		// }
		// });
	}

	private void getData() {
		hospitallist = new ArrayList<BasiHosInfo>();
		BasiHosInfo hospital1 = new BasiHosInfo();
		hospital1.setHosName("第一人民医院");
		hospital1.setId("CD3FDDCD-97CA-4810-85AC-BFBDFD60F2BC");
		hospitallist.add(hospital1);

		BasiHosInfo hospital2 = new BasiHosInfo();
		hospital2.setHosName("同济医院");
		hospitallist.add(hospital2);

		BasiHosInfo hospital3 = new BasiHosInfo();
		hospital3.setHosName("华山医院");
		hospitallist.add(hospital3);

		BasiHosInfo hospital4 = new BasiHosInfo();
		hospital4.setHosName("肿瘤医院");
		hospitallist.add(hospital4);

		BasiHosInfo hospital5 = new BasiHosInfo();
		hospital5.setHosName("瑞金医院");
		hospitallist.add(hospital5);

		BasiHosInfo hospital6 = new BasiHosInfo();
		hospital6.setHosName("东方医院");
		hospitallist.add(hospital6);
	}

	private void queryData() {

		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);

		AllHospitalInfoReq req = new AllHospitalInfoReq();
		req.setOperType("1");
		req.setHosp_code(cityCode);

		GsonServlet<AllHospitalInfoReq, AllHospitalInfoRes> gServlet = new GsonServlet<AllHospitalInfoReq, AllHospitalInfoRes>(
				this);
		gServlet.request(req, AllHospitalInfoRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<AllHospitalInfoReq, AllHospitalInfoRes>() {

			@Override
			public void onResponseEnd(AllHospitalInfoReq commonReq,
					AllHospitalInfoRes commonRes, boolean result,
					String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(AllHospitalInfoReq commonReq,
					AllHospitalInfoRes commonRes, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					hospitallist = commonRes.getAllInfos();
					Message msg = mHandler.obtainMessage(1);
					mHandler.sendMessage(msg);
				} else {
					if (hospitallist != null && hospitallist.size() > 0) {
						hospitallist.clear();
					}
					adapter.notifyDataSetChanged();
					Toast.makeText(HospitalListActivity.this, "没有该城市医院信息", 1)
							.show();
				}
			}

			@Override
			public void onResponseEndErr(AllHospitalInfoReq commonReq,
					AllHospitalInfoRes commonRes, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			if (data != null) {
				cityName = data.getStringExtra("city_name");
				cityCode = data.getStringExtra("city_code");
				tv_city.setText(cityName);
				queryData();
			} else {
				cityCode = "310000";
				cityName = "上海市";
			}
		}
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//			AlertDialog.Builder builder = new AlertDialog.Builder(
//					HospitalListActivity.this);
//			builder.setTitle("是否要退出掌上医院？");
//			builder.setPositiveButton("确定",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							finish();
//
//						}
//					});
//
//			builder.setNegativeButton("取消",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//						}
//
//					});
//			builder.create().show();
//
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//
//		// Config.phUsers = null;
//
//		preferencesService.clearLogin();
//
//		// if (bitmap != null && !bitmap.isRecycled()) {
//		// bitmap.recycle();
//		// bitmap = null;
//		// }
//
//		super.onDestroy();
//		System.gc();
//	}
}
