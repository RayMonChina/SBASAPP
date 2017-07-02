package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.fang.sbas.R;
import com.ideal.zsyy.adapter.ExpandableAdapter;
import com.ideal.zsyy.entity.DeptDutyInfo;
import com.ideal.zsyy.request.DeptDutyInfoReq;
import com.ideal.zsyy.response.DeptDutyInfoRes;
import com.ideal2.base.gson.GsonServlet;

public class OrderDeptInfoActivity extends Activity {

	private ExpandableListView elvDeptinfo;
	private List<String> deptnamegroup;
	private List<String> deptnamegroup_n;
	private List<String> deptnamegroup_s;
	private List<List<DeptDutyInfo>> deptdutychild_n;
	private List<List<DeptDutyInfo>> deptdutychild_s;
	private Button btn_hospital_n;
	private Button btn_hospital_s;
	private boolean isHospitalN = true;
	private ExpandableAdapter adapter_hospital_s;
	private ExpandableAdapter adapter_hospital_n;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_deptinfo);
		Intent intent = getIntent();
		String day = intent.getStringExtra("day");
		String noontype = intent.getStringExtra("noontype");
		if (day != null && !"".equals(day) && noontype != null
				&& !"".equals(noontype)) {
			queryData(day, noontype);
		}
		initView();
	}

	private void initView() {
		Button btnBack = (Button) findViewById(R.id.btn_back);
		btn_hospital_n = (Button) findViewById(R.id.btn_hospital_n);
		btn_hospital_s = (Button) findViewById(R.id.btn_hospital_s);
		btn_hospital_n.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isHospitalN = true;
				btn_hospital_n.setBackgroundResource(R.drawable.navigation_tab_left_down);
				btn_hospital_n.setTextColor(getResources().getColor(android.R.color.white));
				btn_hospital_s.setBackgroundResource(R.drawable.navigation_tab_right_up);
				btn_hospital_s.setTextColor(getResources().getColor(R.color.textcolor1));
				if (deptnamegroup_n != null && deptdutychild_n != null) {
					if (adapter_hospital_n == null) {
						adapter_hospital_n = new ExpandableAdapter(OrderDeptInfoActivity.this, deptnamegroup_n, deptdutychild_n);
					}
					elvDeptinfo.setAdapter(adapter_hospital_n);
					for (int i = 0; i < deptnamegroup_n.size(); i++) {
						elvDeptinfo.expandGroup(i);
					}
				}
			}
		});
		btn_hospital_s.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isHospitalN = false;
				btn_hospital_n.setBackgroundResource(R.drawable.navigation_tab_left_up);
				btn_hospital_n.setTextColor(getResources().getColor(R.color.textcolor1));
				btn_hospital_s.setBackgroundResource(R.drawable.navigation_tab_right_down);
				btn_hospital_s.setTextColor(getResources().getColor(android.R.color.white));
				if (deptnamegroup_s != null && deptdutychild_s != null) {
					if (adapter_hospital_s == null) {
						adapter_hospital_s = new ExpandableAdapter(OrderDeptInfoActivity.this, deptnamegroup_s, deptdutychild_s);
					}
					elvDeptinfo.setAdapter(adapter_hospital_s);
					for (int i = 0; i < deptnamegroup_s.size(); i++) {
						elvDeptinfo.expandGroup(i);
					}
				}
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		elvDeptinfo = (ExpandableListView) findViewById(R.id.elv_order_deptinfo);
		elvDeptinfo.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				DeptDutyInfo dutyInfo = null;
				if (isHospitalN) {
					dutyInfo = deptdutychild_n.get(groupPosition).get(childPosition);
				} else {
					dutyInfo = deptdutychild_s.get(groupPosition).get(childPosition);
				}
				String dept_id = dutyInfo.getDept_id();
				String dept_name = dutyInfo.getDept_infosName();
				Intent intent = new Intent(
						OrderDeptInfoActivity.this,
						OrderRegister1Activity.class);
				intent.putExtra("dept_id", dept_id);
				intent.putExtra("dept_name", dept_name); 
				intent.putExtra("locate", dutyInfo.getLocate());
				setResult(2, intent);
				finish(); 
				return false;
			}
		});
	}

	private void queryData(String day, String noontype) {
		DeptDutyInfoReq deptdutyReq = new DeptDutyInfoReq();
		deptdutyReq.setDutytime(day);
		deptdutyReq.setNoontype(noontype);
		deptdutyReq.setOperType("31");
		GsonServlet<DeptDutyInfoReq, DeptDutyInfoRes> gsonServlet = new GsonServlet<DeptDutyInfoReq, DeptDutyInfoRes>(this);
		gsonServlet.request(deptdutyReq, DeptDutyInfoRes.class);
		gsonServlet.setOnResponseEndListening(new GsonServlet.OnResponseEndListening<DeptDutyInfoReq, DeptDutyInfoRes>() {

			@Override
			public void onResponseEnd(DeptDutyInfoReq commonReq,
					DeptDutyInfoRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onResponseEndSuccess(DeptDutyInfoReq commonReq,
					DeptDutyInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					List<DeptDutyInfo> deptdutyinfos = commonRes.getDeptdutyinfos();
					deptnamegroup = removeDuplicateWithOrder(deptdutyinfos);
					deptdutychild_n = new ArrayList<List<DeptDutyInfo>>(); 
					deptdutychild_s = new ArrayList<List<DeptDutyInfo>>(); 
					deptnamegroup_n = new ArrayList<String>();
					deptnamegroup_s = new ArrayList<String>();
					for (String deptname : deptnamegroup) {
						List<DeptDutyInfo> childers_n = new ArrayList<DeptDutyInfo>();
						List<DeptDutyInfo> childers_s = new ArrayList<DeptDutyInfo>();
						for (DeptDutyInfo deptduty : deptdutyinfos) {
							if (deptname.equals(deptduty.getDept_name())) {
								if ("N".equals(deptduty.getLocate())) {
									childers_n.add(deptduty);
								} else if ("S".equals(deptduty.getLocate())){
									childers_s.add(deptduty);
								}
							}
						}
						if (childers_n.size() > 0) {
							deptnamegroup_n.add(deptname);
							deptdutychild_n.add(childers_n);
						}
						if (childers_s.size() > 0) {
							deptnamegroup_s.add(deptname);
							deptdutychild_s.add(childers_s);
						}
					}
					if (adapter_hospital_n == null) {
						adapter_hospital_n = new ExpandableAdapter(OrderDeptInfoActivity.this, deptnamegroup_n, deptdutychild_n);
					}
					elvDeptinfo.setAdapter(adapter_hospital_n);
					for (int i = 0; i < deptnamegroup_n.size(); i++) {
						elvDeptinfo.expandGroup(i);
					}
				}
			}

			@Override
			public void onResponseEndErr(DeptDutyInfoReq commonReq,
					DeptDutyInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public static List<String> removeDuplicateWithOrder(
			List<DeptDutyInfo> deptdutyinfos) {
		Set set = new HashSet();
		List<String> grouplist = new ArrayList<String>();
		for (Iterator iter = deptdutyinfos.iterator(); iter.hasNext();) {
			DeptDutyInfo element = (DeptDutyInfo) iter.next();
			set.add(element.getDept_name());
		}
		grouplist.clear();
		grouplist.addAll(set);
		return grouplist;
	}
}
