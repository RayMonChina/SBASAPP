package com.ideal.zsyy.activity;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.DocDutyInfo1Adapter;
import com.ideal.zsyy.adapter.DocDutyInfoAdapter;
import com.ideal.zsyy.entity.PhDoctorInfo;
import com.ideal.zsyy.entity.PhDutyInfo;
import com.ideal.zsyy.request.PhDoctorReq;
import com.ideal.zsyy.request.PhDutyInfoReq;
import com.ideal.zsyy.response.PhDoctorRes;
import com.ideal.zsyy.response.PhDutyInfoRes;
import com.ideal.zsyy.utils.AsynImageLoader;
import com.ideal.zsyy.utils.BitmapUtil;
import com.ideal.zsyy.utils.DataUtils;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

public class DoctorInfoActivity extends Activity {

	private String doctorId;
	private PhDoctorInfo doctorInfo;
	private TextView tvDocName;
	private TextView tvDocJob;
	private ImageView ivDocIcon;
	private TextView tvDocExptertin;
	private TextView tvDocSynopsis;
	private Button btnBack;
	private Button tvDosynopsis;
	private Button tvDopaiban;
	private LinearLayout llDocsynopsis;
	private LinearLayout llPaiban;
	private ImageView ivIspro;
	private GridView gvDutyInfo;
	private List<PhDutyInfo> phdutyInfos;
	private ListView lvDutyinfo;
	private TextView iv_cut_zz;
	private Bitmap bitmap;
	private Handler mHandler  = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Object[] object;
			ImageView iv_icon;

			switch (msg.what) {

			case 0:
				object = (Object[]) msg.obj;
				bitmap = (Bitmap) object[0];
				bitmap = BitmapUtil.getRoundedCornerBitmap(bitmap);
				if (bitmap != null) {
					iv_icon = (ImageView) object[1];
					iv_icon.setImageBitmap(bitmap);
				}

				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctorinfo);
		initData();
		initView();
//		if (doctorInfo != null) {
//			showData();
//		}
	}

	//初始化视图
	private void initView() {
		btnBack = (Button) findViewById(R.id.btn_back);
		tvDocName = (TextView) findViewById(R.id.tv_docName);
		tvDocJob = (TextView) findViewById(R.id.tv_docJob);
		ivDocIcon = (ImageView) findViewById(R.id.iv_docImg);
		tvDocExptertin = (TextView) findViewById(R.id.tv_docexpertin);
		tvDocSynopsis = (TextView) findViewById(R.id.tv_docsynopsis);
		ivIspro = (ImageView) findViewById(R.id.iv_ispro);
		tvDosynopsis = (Button) findViewById(R.id.btn_dosynopsis);
		tvDopaiban = (Button) findViewById(R.id.btn_paiban);
		
		llDocsynopsis = (LinearLayout) findViewById(R.id.ll_docsynopsis);
		llPaiban = (LinearLayout) findViewById(R.id.ll_paiban);
		gvDutyInfo = (GridView) findViewById(R.id.gv_dutyinfo);
		lvDutyinfo = (ListView) findViewById(R.id.lv_dutyinfo);
		iv_cut_zz = (TextView) findViewById(R.id.iv_cut_zz);
		setListener();  
	}

	//设置监听
	private void setListener() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//医生简介
		tvDosynopsis.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				tvDosynopsis.setBackgroundResource(R.drawable.navigation_tab_left_down);
				tvDosynopsis.setTextColor(getResources().getColor(android.R.color.white));
				tvDopaiban.setBackgroundResource(R.drawable.navigation_tab_right_up);
				tvDopaiban.setTextColor(Color.parseColor("#0079ff"));
				llDocsynopsis.setVisibility(View.VISIBLE);
				llPaiban.setVisibility(View.GONE);
			}
		});
		//医生排班
		tvDopaiban.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvDopaiban.setBackgroundResource(R.drawable.navigation_tab_right_down);
				tvDopaiban.setTextColor(getResources().getColor(android.R.color.white));
				tvDosynopsis.setBackgroundResource(R.drawable.navigation_tab_left_up);
				tvDosynopsis.setTextColor(Color.parseColor("#0079ff"));
				llDocsynopsis.setVisibility(View.GONE);
				llPaiban.setVisibility(View.VISIBLE);
				queryDutyInfoData();
			}
		});
		//专家预约
		gvDutyInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if (view != null) {
//					Toast.makeText(DoctorInfoActivity.this, "posistion:" + position + "  line:" + position / 3 + "  id:" + id, 1).show();
//				}
			}
		});
	}
	//查询医生本周的排班
	private void queryDutyInfoData() {
		PhDutyInfoReq dutyinforeq = new PhDutyInfoReq();
//		dutyinforeq.setHospId(Config.hosId);
		dutyinforeq.setDocId(doctorInfo.getDoctor_Id());
		dutyinforeq.setOperType("29");
		GsonServlet<PhDutyInfoReq, PhDutyInfoRes> gServlet = new GsonServlet<PhDutyInfoReq, PhDutyInfoRes>(this);
		gServlet.request(dutyinforeq, PhDutyInfoRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<PhDutyInfoReq, PhDutyInfoRes>() {
			@Override
			public void onResponseEnd(PhDutyInfoReq commonReq,
					PhDutyInfoRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onResponseEndSuccess(PhDutyInfoReq commonReq,
					PhDutyInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					phdutyInfos = commonRes.getPhdutyInfos();
//					DocDutyInfoAdapter dutinfoadapter = new DocDutyInfoAdapter(DoctorInfoActivity.this,phdutyInfos,doctorInfo.getIs_Specialist());
//					gvDutyInfo.setAdapter(dutinfoadapter);
					DocDutyInfo1Adapter dutinfoadapter = new DocDutyInfo1Adapter(DoctorInfoActivity.this,phdutyInfos,doctorInfo);
					lvDutyinfo.setAdapter(dutinfoadapter);
				}
			}
			@Override
			public void onResponseEndErr(PhDutyInfoReq commonReq,
					PhDutyInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	//得到传过来的数据
	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		doctorId = intent.getStringExtra("doctorId"); 
		queryData();
//		Bundle extras = intent.getExtras();
//		doctorInfo = (PhDoctorInfo) extras.getSerializable("doctorinfo");
		
	}
	
	//显示数据
	private void showData(){
		tvDocName.setText(doctorInfo.getDoctor_Name());
		tvDocJob.setText(doctorInfo.getJob_title());
		tvDocExptertin.setText(doctorInfo.getExpertise());
		tvDocSynopsis.setText(doctorInfo.getIntroduce());
		iv_cut_zz.setText("[点击预约专家:"+doctorInfo.getDoctor_Name()+"]"); 
		if (doctorInfo.getPhoto() != null && !"".equals(doctorInfo.getPhoto())) {
			BitmapUtil.downloadBitmap(ivDocIcon, Config.down_url + doctorInfo.getPhoto(), mHandler, 0); 
//			String filename = doctorInfo.getPhoto().substring(
//					doctorInfo.getPhoto().lastIndexOf("/") + 1);
//			Bitmap bitmap = BitmapFactory.decodeFile(DataUtils.doctor_path
//					+ File.separator + filename);
//			if (bitmap != null) {
//				ivDocIcon.setImageBitmap(AsynImageLoader.getRoundedCornerBitmap(bitmap));
//			}
		}else {
			ivDocIcon.setImageResource(R.drawable.default_doctor);
		}
		if ("1".equals(doctorInfo.getIs_Specialist())) { 
			ivIspro.setVisibility(View.VISIBLE);
		}
	}

	//查询数据
	private void queryData() {
		// TODO Auto-generated method stub
		PhDoctorReq req = new PhDoctorReq();
		req.setHosID(Config.hosId); 
		req.setDocId(doctorId);
		req.setPageSize("");
		req.setPageIndex("");
		req.setIsPro("");
		req.setOperType("5");
		GsonServlet<PhDoctorReq, PhDoctorRes> gServlet = new GsonServlet<PhDoctorReq, PhDoctorRes>(this);
		gServlet.request(req, PhDoctorRes.class);
		gServlet.setOnResponseEndListening(new GsonServlet.OnResponseEndListening<PhDoctorReq, PhDoctorRes>() {

			@Override
			public void onResponseEnd(PhDoctorReq commonReq,
					PhDoctorRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onResponseEndSuccess(PhDoctorReq commonReq,
					PhDoctorRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					if (commonRes.getPhDoctorInfos().size() > 0) {
						doctorInfo = commonRes.getPhDoctorInfos().get(0); 
						showData();
					}
				}
			}

			@Override
			public void onResponseEndErr(PhDoctorReq commonReq,
					PhDoctorRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				
			} 
		});
	}
}
