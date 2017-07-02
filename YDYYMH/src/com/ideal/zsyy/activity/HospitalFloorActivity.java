package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ideal.wdm.tools.DataCache;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.entity.FloorDeptInfo;
import com.ideal.zsyy.entity.PhFloorDeptInfo;
import com.ideal.zsyy.request.FloorDeptInfoReq;
import com.ideal.zsyy.request.PhFloorDeptInfoReq;
import com.ideal.zsyy.response.FloorDeptInfoRes;
import com.ideal.zsyy.response.PhFloorDeptInfoRes;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

public class HospitalFloorActivity extends Activity {

	private String build_no;
	private String build_name;
	private String build_id;
	private List<Map<String, Object>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_floor);

		build_id = getIntent().getStringExtra("build_id");

		build_no = getIntent().getStringExtra("build_no");

		build_name = getIntent().getStringExtra("build_name");
		if (build_name.trim().equals("门急诊大楼1")) {

			build_name = "门急诊大楼";
		} else if (build_name.trim().equals("门急诊大楼2")) {

			build_name = "门急诊大楼";
		}

		list = new ArrayList<Map<String, Object>>();

		initView();

		// queryData();

		queryData_new();

	}

	private void initView() {
		String titleStr = build_name;
		if (build_no != null && !"".equals(build_no)) {
			titleStr = build_no + "号楼 " + titleStr;
		}

		TextView top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(titleStr);

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	private void setListView() {
		ListView lv_hospital_floor = (ListView) findViewById(R.id.lv_hospital_floor);

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.hospital_floor_item, new String[] {
						"hospital_floor_item_no", "hospital_floor_item_name" },
				new int[] { R.id.hospital_floor_item_no,
						R.id.hospital_floor_item_name });

		lv_hospital_floor.setAdapter(adapter);
	}

	private void queryData() {

		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);

		FloorDeptInfoReq req = new FloorDeptInfoReq();
		req.setOperType("20");
		req.setHosId(Config.hosId);
		req.setBuildNo(build_no);

		GsonServlet<FloorDeptInfoReq, FloorDeptInfoRes> gServlet = new GsonServlet<FloorDeptInfoReq, FloorDeptInfoRes>(
				this);
		gServlet.request(req, FloorDeptInfoRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<FloorDeptInfoReq, FloorDeptInfoRes>() {

			@Override
			public void onResponseEnd(FloorDeptInfoReq commonReq,
					FloorDeptInfoRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(FloorDeptInfoReq commonReq,
					FloorDeptInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {

					for (int i = 0; i < commonRes.getFloorDeptInfos().size(); i++) {

						FloorDeptInfo floorDeptInfo = commonRes
								.getFloorDeptInfos().get(i);

						Map<String, Object> map = new HashMap<String, Object>();
						map.put("hospital_floor_item_no",
								floorDeptInfo.getDept_Floor() + "F");
						map.put("hospital_floor_item_name",
								floorDeptInfo.getDept_Names());
						list.add(map);
					}

					setListView();
				}

			}

			@Override
			public void onResponseEndErr(FloorDeptInfoReq commonReq,
					FloorDeptInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});
	}

	private boolean flag = false;
	private void queryData_new() {

		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);

		PhFloorDeptInfoReq req = new PhFloorDeptInfoReq();
		req.setOperType("21");
		req.setBuildId(build_id);

		GsonServlet<PhFloorDeptInfoReq, PhFloorDeptInfoRes> gServlet = new GsonServlet<PhFloorDeptInfoReq, PhFloorDeptInfoRes>(
				this);
		gServlet.request(req, PhFloorDeptInfoRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<PhFloorDeptInfoReq, PhFloorDeptInfoRes>() {

			@Override
			public void onResponseEnd(PhFloorDeptInfoReq commonReq,
					PhFloorDeptInfoRes commonRes, boolean result,
					String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(PhFloorDeptInfoReq commonReq,
					PhFloorDeptInfoRes commonRes, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
//					if (commonRes.getFloorDeptInfos().size() >= 10 ) {
					List<PhFloorDeptInfo> deptInfos = commonRes.getFloorDeptInfos();
					for (PhFloorDeptInfo deptInfo : deptInfos) {
						if (deptInfo.getFloor_no().contains("地下室") || deptInfo.getFloor_no().indexOf("-") >= 0) {
							flag = true; 
							break;
						}
					}
					Collections.sort(deptInfos,
							new Comparator<PhFloorDeptInfo>() {
								@Override
								public int compare(PhFloorDeptInfo lhs,
										PhFloorDeptInfo rhs) {
									int int1 = 0,int2 = 0;
									if (flag) {
										if (!lhs.getFloor_no().trim().equals("地下室") && lhs.getFloor_no().trim().lastIndexOf("-") <= 0) {
											int1 = Integer.parseInt(lhs.getFloor_no().replace("F", ""));
										} else if (!rhs.getFloor_no().trim().equals("地下室") && lhs.getFloor_no().trim().lastIndexOf("-") <= 0) {
											int2 = Integer.parseInt(rhs.getFloor_no().replace("F", ""));
										}
									} else {
										int1 = Integer.parseInt(lhs.getFloor_no().replace("F", ""));
										int2 = Integer.parseInt(rhs.getFloor_no().replace("F", ""));
									}
									return int1 - int2;
								}
							});
//					}

					for (int i = 0; i < commonRes.getFloorDeptInfos().size(); i++) {

						PhFloorDeptInfo phFloorDeptInfo = commonRes
								.getFloorDeptInfos().get(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("hospital_floor_item_no",
								phFloorDeptInfo.getFloor_no());
						map.put("hospital_floor_item_name",
								phFloorDeptInfo.getFloor_content());
						list.add(map);
					}

					setListView();
				}

			}

			@Override
			public void onResponseEndErr(PhFloorDeptInfoReq commonReq,
					PhFloorDeptInfoRes commonRes, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});
	}

	private List<Map<String, Object>> getData() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hospital_floor_item_no", "1F");
		map.put("hospital_floor_item_name", "服务台、检查、药房");
		list.add(map);

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("hospital_floor_item_no", "2F");
		map1.put("hospital_floor_item_name", "VIP门诊、VIP体验");
		list.add(map1);

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("hospital_floor_item_no", "3F");
		map2.put("hospital_floor_item_name", "特殊VIP病房");
		list.add(map2);

		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("hospital_floor_item_no", "4F");
		map3.put("hospital_floor_item_name", "特殊病房");
		list.add(map3);

		return list;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (list != null) {
			list.clear();
			list = null;
		}

		super.onDestroy();
	}

}
