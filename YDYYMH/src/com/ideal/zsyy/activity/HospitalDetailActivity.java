package com.ideal.zsyy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ideal.wdm.tools.DataCache;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.request.HosInfoReq;
import com.ideal.zsyy.response.HosInfoRes;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;
import com.ideal.zsyy.entity.PhHospitalInfo;

public class HospitalDetailActivity extends Activity {

	private PhHospitalInfo phHospitalInfo;
	private TextView hosp_Introduce;
	private TextView hosp_Introduce_eng;
	private ImageView iv_hide;
	private Button btn_introduce;
	private Button btn_introduce_hide;
	private Button btn_introduce_eng;
	private Button btn_introduce_eng_hide;
	private FrameLayout fl_present;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_detail);
		// FontManager.changeFonts((LinearLayout) findViewById(R.id.hd_all),
		// HospitalDetailActivity.this);
		initView();
		queryData();

	}

	private void setData(PhHospitalInfo phHospitalInfo) {

		if (phHospitalInfo == null) {

			return;
		}
		
		
		Config.hospital_lat = Double.valueOf(phHospitalInfo.getLatitude());
		Config.hospital_lng = Double.valueOf(phHospitalInfo.getLongitude());
		
		// 医院名称
		TextView hospital_name = (TextView) findViewById(R.id.hospital_name);
		hospital_name.setText(phHospitalInfo.getHosp_Name());

		// 医院级别
		TextView hosp_level = (TextView) findViewById(R.id.hosp_level);
		hosp_level.setText(phHospitalInfo.getHosp_Level());

		// 医院网址
		TextView hosp_url = (TextView) findViewById(R.id.hosp_url);
		hosp_url.setText(phHospitalInfo.getHosp_Url());

		// 总机
		TextView hosp_tel1 = (TextView) findViewById(R.id.hosp_tel1);
		hosp_tel1.setText(phHospitalInfo.getTel1());

		// 急诊
		TextView hosp_tel2 = (TextView) findViewById(R.id.hosp_tel2);
		hosp_tel2.setText(phHospitalInfo.getTel2());

		// 地址
		TextView hosp_Add = (TextView) findViewById(R.id.hosp_Add);
		hosp_Add.setText(phHospitalInfo.getHosp_Add().replace("        ", "\n"));
		Config.hospital_address = phHospitalInfo.getHosp_Add().replace(
				"        ", "\n");

		// 交通
		TextView bus_Rd = (TextView) findViewById(R.id.bus_Rd);
		bus_Rd.setText(phHospitalInfo.getBus_Rd().replace("                  ",
				"\n"));

		// 特色科室
		TextView dept_Feature = (TextView) findViewById(R.id.dept_Feature);
		dept_Feature.setText(phHospitalInfo.getDept_Feature());

		View.OnClickListener listener = new View.OnClickListener() {
			boolean flag = false;
			boolean flageng = false;

			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_more_introduce:
					flag = true;
					btn_introduce.setVisibility(View.GONE);
					btn_introduce_hide.setVisibility(View.VISIBLE);
					hosp_Introduce.setSingleLine(false);
					hosp_Introduce.setEllipsize(null);
					iv_hide.setVisibility(View.VISIBLE);
					break;
				case R.id.btn_more_introduce_eng:
					flageng = true;
					btn_introduce_eng.setVisibility(View.GONE);
					btn_introduce_eng_hide.setVisibility(View.VISIBLE);
					hosp_Introduce_eng.setSingleLine(false);
					hosp_Introduce_eng.setEllipsize(null);
					iv_hide.setVisibility(View.VISIBLE);
					break;
				case R.id.iv_hide:
					if (flag) {
						flag = true;
						hosp_Introduce.setLines(6);
						hosp_Introduce.setEllipsize(TruncateAt.END);
						btn_introduce.setVisibility(View.VISIBLE);
						btn_introduce_hide.setVisibility(View.GONE);
					}
					if (flageng) {
						flag = true;
						hosp_Introduce_eng.setLines(6);
						hosp_Introduce_eng.setEllipsize(TruncateAt.END);
						hosp_Introduce.setEllipsize(TruncateAt.END);
						iv_hide.setVisibility(View.GONE);
						
					}
					iv_hide.setVisibility(View.GONE);
					break;
				case R.id.btn_more_introduce_hide:
					btn_introduce.setVisibility(View.VISIBLE);
					btn_introduce_hide.setVisibility(View.GONE);
					flag = true;
					hosp_Introduce.setLines(6);
					hosp_Introduce.setEllipsize(TruncateAt.END);
					iv_hide.setVisibility(View.GONE);
					break;
				case R.id.btn_more_introduce_eng_hide:
					flag = true;
					btn_introduce_eng.setVisibility(View.VISIBLE);
					btn_introduce_eng_hide.setVisibility(View.GONE);
					hosp_Introduce_eng.setLines(6);
					hosp_Introduce_eng.setEllipsize(TruncateAt.END);
					iv_hide.setVisibility(View.GONE);
					break;
				default:
					break;
				}
			}
		};
		// 隐藏
		iv_hide = (ImageView) findViewById(R.id.iv_hide);
		iv_hide.setOnClickListener(listener);

		// 医院简介
		hosp_Introduce = (TextView) findViewById(R.id.hosp_Introduce);
		hosp_Introduce.setText(phHospitalInfo.getHosp_Introduce().replace(
				"[$%]", "\n"));

		// 查看全文
		btn_introduce = (Button) findViewById(R.id.btn_more_introduce);
		btn_introduce.setOnClickListener(listener);

		// 收起
		btn_introduce_hide = (Button) findViewById(R.id.btn_more_introduce_hide);
		btn_introduce_hide.setOnClickListener(listener);

		// 英文简介
		hosp_Introduce_eng = (TextView) findViewById(R.id.hosp_Introduce_eng);
		hosp_Introduce_eng.setText(phHospitalInfo.getHosp_introduce_eng()
				.replace("[$%]", "\n"));

		// 查看全文
		btn_introduce_eng = (Button) findViewById(R.id.btn_more_introduce_eng);
		btn_introduce_eng.setOnClickListener(listener);

		// 收起
		btn_introduce_eng_hide = (Button) findViewById(R.id.btn_more_introduce_eng_hide);
		btn_introduce_eng_hide.setOnClickListener(listener);
		
		fl_present.setVisibility(View.VISIBLE);
	}

	private void initView() {
		
		fl_present = (FrameLayout) findViewById(R.id.fl_present);
		fl_present.setVisibility(View.INVISIBLE);
		
		LinearLayout hd_ll_navigation = (LinearLayout) findViewById(R.id.hd_ll_navigation);
		hd_ll_navigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						NavigationActivity.class);
				startActivity(intent);

			}
		});

		LinearLayout hd_ll_yuannei = (LinearLayout) findViewById(R.id.hd_ll_yuannei);
		hd_ll_yuannei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						HospitalNavigationActivity.class);
				startActivity(intent);

			}
		});

		LinearLayout hd_ll_poisearch = (LinearLayout) findViewById(R.id.hd_ll_poisearch);
		hd_ll_poisearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						PoiSearchActivity.class);
				startActivity(intent);

			}
		});

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	private void queryData() {

		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);

		HosInfoReq req = new HosInfoReq();
		req.setOperType("2");
		req.setHospId(Config.hosId);

		GsonServlet<HosInfoReq, HosInfoRes> gServlet = new GsonServlet<HosInfoReq, HosInfoRes>(
				this);
		gServlet.request(req, HosInfoRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<HosInfoReq, HosInfoRes>() {

			@Override
			public void onResponseEnd(HosInfoReq commonReq,
					HosInfoRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(HosInfoReq commonReq,
					HosInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {

					if (commonRes.getPhHospitalInfo().size() >= 1) {

						phHospitalInfo = commonRes.getPhHospitalInfo().get(0);
						setData(phHospitalInfo);

					}

				}
			}

			@Override
			public void onResponseEndErr(HosInfoReq commonReq,
					HosInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});

	}

}
