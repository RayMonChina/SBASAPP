package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fang.sbas.R;
import com.ideal.zsyy.adapter.ExpandableAdapter;
import com.ideal.zsyy.adapter.PhDoctorAdapter;
import com.ideal.zsyy.entity.PhDoctorInfo;
import com.ideal.zsyy.request.DocDutyInfoReq;
import com.ideal.zsyy.response.DocDutyInfoRes;
import com.ideal2.base.gson.GsonServlet;

public class OrderDoctorInfoActivity extends Activity {

	private Intent intent;
	private LayoutInflater mInflater;
	private ListView lv_doctorInfo;
	private EditText queryDoctor;
	private TextView tvDoctortitle;
	private Button btnHospitalN;
	private Button btnHospitalS;
	private List<PhDoctorInfo> docdutyinfos_n = new ArrayList<PhDoctorInfo>();
	private List<PhDoctorInfo> docdutyinfos_s = new ArrayList<PhDoctorInfo>();
	private PhDoctorAdapter adapter_n;
	private PhDoctorAdapter adapter_s;
	private boolean isHospitalN = true;
	
	
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_doctorinfo);
		mInflater = LayoutInflater.from(this);
		initView();
		intent = getIntent(); 
		String dutytime = intent.getStringExtra("dutytime");
		String noontype = intent.getStringExtra("noontype");
		DocDutyInfoReq docdutyreq = new DocDutyInfoReq();
		docdutyreq.setDutytime(dutytime);
		docdutyreq.setNoontype(noontype);
		docdutyreq.setOperType("30");
		query(docdutyreq); 
	}

	private void initView() {
		// TODO Auto-generated method stub
//		doctortop = mInflater.inflate(R.layout.searcher_top, null);

		lv_doctorInfo = (ListView) findViewById(R.id.lv_doctorInfo);
//		lv_doctorInfo.addHeaderView(doctortop);

//		queryDoctor = (EditText) doctortop.findViewById(R.id.et_seacher);
//		queryDoctor.setHint(R.string.querydoctorhint);
		Button btnBack = (Button) findViewById(R.id.btn_back); 
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHospitalN = (Button) findViewById(R.id.btn_hospital_n); 
		btnHospitalS = (Button) findViewById(R.id.btn_hospital_s);
		
		btnHospitalN.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isHospitalN = true;
				btnHospitalN.setBackgroundResource(R.drawable.navigation_tab_left_down);
				btnHospitalN.setTextColor(getResources().getColor(android.R.color.white));
				btnHospitalS.setBackgroundResource(R.drawable.navigation_tab_right_up);
				btnHospitalS.setTextColor(getResources().getColor(R.color.textcolor1));
				if (adapter_n == null) {
					adapter_n = new PhDoctorAdapter(docdutyinfos_n, OrderDoctorInfoActivity.this, mHandler);
				} 
				lv_doctorInfo.setAdapter(adapter_n);
			}
		});
		btnHospitalS.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isHospitalN = false;
				btnHospitalN.setBackgroundResource(R.drawable.navigation_tab_left_up);
				btnHospitalN.setTextColor(getResources().getColor(R.color.textcolor1));
				btnHospitalS.setBackgroundResource(R.drawable.navigation_tab_right_down);
				btnHospitalS.setTextColor(getResources().getColor(android.R.color.white));
				if (adapter_s == null) {
					adapter_s = new PhDoctorAdapter(docdutyinfos_s, OrderDoctorInfoActivity.this, mHandler);
				}
				lv_doctorInfo.setAdapter(adapter_s);
			}
		});
		lv_doctorInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				PhDoctorInfo doctorInfo = null;
				if (isHospitalN) {
					doctorInfo = docdutyinfos_n.get(position);
				} else {
					doctorInfo = docdutyinfos_s.get(position);
				}
				Intent intent = new Intent(
						OrderDoctorInfoActivity.this,
						OrderRegister2Activity.class);
				String docname = doctorInfo.getDoctor_Name();
				docname = docname.replace(
						"<font color=#0080ff>", "").replace(
						"</font>", "");
				intent.putExtra("doc_id",
						doctorInfo.getDoctor_Id());
				intent.putExtra("doc_name", docname);
				if (doctorInfo.getDept_name_cn() != null
						&& !"".equals(doctorInfo
								.getDept_name_cn())) {
					intent.putExtra("deptname",
							doctorInfo.getDept_name_cn());
					intent.putExtra("dept_id",
							doctorInfo.getDept_id());
				}
				intent.putExtra("locate",
						doctorInfo.getLocate());
				setResult(1, intent);
				finish();
			}
		});
	} 
	public void query(final DocDutyInfoReq req) {
		GsonServlet<DocDutyInfoReq, DocDutyInfoRes> gServlet = new GsonServlet<DocDutyInfoReq, DocDutyInfoRes>(
				this);
		gServlet.request(req, DocDutyInfoRes.class);
		gServlet.setOnResponseEndListening(new GsonServlet.OnResponseEndListening<DocDutyInfoReq, DocDutyInfoRes>() {


			@Override
			public void onResponseEnd(DocDutyInfoReq commonReq,
					DocDutyInfoRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(DocDutyInfoReq commonReq,
					DocDutyInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					List<PhDoctorInfo> docdutyinfos = commonRes.getDocdutyinfos();
					for (PhDoctorInfo phDoctorInfo : docdutyinfos) {
						if ("N".equals(phDoctorInfo.getLocate())) {
							docdutyinfos_n.add(phDoctorInfo);
						} else {
							docdutyinfos_s.add(phDoctorInfo);
						}
					}
					adapter_n = new PhDoctorAdapter(docdutyinfos_n, OrderDoctorInfoActivity.this, mHandler);
					lv_doctorInfo.setAdapter(adapter_n); 
				}
			}

			@Override
			public void onResponseEndErr(DocDutyInfoReq commonReq,
					DocDutyInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}
		});
	}
}
