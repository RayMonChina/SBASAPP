package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.PhDeptInfoAdapter;
import com.ideal.zsyy.entity.DeptInfo;
import com.ideal.zsyy.request.PhDeptInfoReq;
import com.ideal.zsyy.response.PhDeptRes;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

public class PhDeptInfoActivity extends Activity {

	private ListView lv_deptInfo; // 科室列表
	private PhDeptInfoAdapter adapter; // 科室适配器
	private EditText queryDept; // 检索文本框
	private List<DeptInfo> phDeptInfos; // 数据
	private Button btnBack; // 返回按钮
	private LayoutInflater mInflater; // 布局加载器
	private View detptop;
	private List<DeptInfo> checkphDeptInfo; // 数据
	
	private TextView title;
	private Intent intent;
	private Button btn_featuredept;
	private Button btn_commondept;
	private boolean flag = false;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			checkphDeptInfo = (List<DeptInfo>) msg.obj;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ph_dept_info);
		mInflater = LayoutInflater.from(this);
		intent = getIntent();
		if (intent != null) {
			String gh = intent.getStringExtra("xz");
			if (gh != null && !"".equals(gh)) {
				flag = true;
			}
			
		}
		initView();
		queryData("1");
	}

	// 初始化视图
	private void initView() {
		detptop = mInflater.inflate(R.layout.searcher_top, null);
		lv_deptInfo = (ListView) findViewById(R.id.lv_deptInfo);
		queryDept = (EditText) detptop.findViewById(R.id.et_seacher);
		queryDept.setHint(R.string.query);
		btnBack = (Button) findViewById(R.id.btn_back);
		btn_featuredept = (Button) findViewById(R.id.btn_featuredept);
		btn_commondept = (Button) findViewById(R.id.btn_commondept);
		setListener();
	}

	// 设置监听
	private void setListener() {
		// TODO Auto-generated method stub
		//设置文本框检索监听
		queryDept.addTextChangedListener(textWatcher);
		//设置listview条目点击事件un
		lv_deptInfo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position > 0) {
					DeptInfo deptInfo = null;
					if (checkphDeptInfo != null && checkphDeptInfo.size() > 0) {
						deptInfo = checkphDeptInfo.get(position - 1);
					} else {
						deptInfo = phDeptInfos.get(position - 1);
					}
					Intent intent = new Intent(PhDeptInfoActivity.this,
							DeptInfoActivity.class);
					intent.putExtra("deptid", deptInfo.getId());
					intent.putExtra("gh", flag);
					startActivity(intent);
				}
			}
		});
		//切换到临床科室列表
		btn_commondept.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_featuredept.setBackgroundResource(R.drawable.navigation_tab_left_up);
				btn_featuredept.setTextColor(getResources().getColor(R.color.textcolor1));
				btn_commondept.setBackgroundResource(R.drawable.navigation_tab_right_down);
				btn_commondept.setTextColor(getResources().getColor(android.R.color.white));
				if (phDeptInfos != null) {
					phDeptInfos.clear();
					if (checkphDeptInfo != null) {
						checkphDeptInfo.clear();
					}
					queryDept.setText("");
					adapter.notifyDataSetChanged();
					queryData("");
				}
			}
		});
		//切换到特色科室列表
		btn_featuredept.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_featuredept.setBackgroundResource(R.drawable.navigation_tab_left_down);
				btn_featuredept.setTextColor(getResources().getColor(android.R.color.white));
				btn_commondept.setBackgroundResource(R.drawable.navigation_tab_right_up);
				btn_commondept.setTextColor(getResources().getColor(R.color.textcolor1));
				if (phDeptInfos != null) {
					phDeptInfos.clear();
					if (checkphDeptInfo != null) {
						checkphDeptInfo.clear();
					}
					queryDept.setText("");
					adapter.notifyDataSetChanged();
					queryData("1");
				}
			}
		});
		//返回监听
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// 查询数据
	private void queryData(String isFeaturedept) {
		PhDeptInfoReq deptReq = new PhDeptInfoReq();
		deptReq.setHosID(Config.hosId);
		deptReq.setPageSize("");
		deptReq.setPageIndex("");
		deptReq.setOperType("4");
		deptReq.setIsFeatureDept(isFeaturedept);
		GsonServlet<PhDeptInfoReq, PhDeptRes> gServlet = new GsonServlet<PhDeptInfoReq, PhDeptRes>(
				this);
		gServlet.request(deptReq, PhDeptRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<PhDeptInfoReq, PhDeptRes>() {

			@Override
			public void onResponseEnd(PhDeptInfoReq commonReq,
					PhDeptRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(PhDeptInfoReq commonReq,
					PhDeptRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					if (lv_deptInfo.getHeaderViewsCount() <= 0) {
						lv_deptInfo.addHeaderView(detptop);
					}
					phDeptInfos = commonRes.getPhDeptInfos();
					adapter = new PhDeptInfoAdapter(phDeptInfos,
							PhDeptInfoActivity.this,mHandler);
					lv_deptInfo.setAdapter(adapter);
				}
			}

			@Override
			public void onResponseEndErr(PhDeptInfoReq commonReq,
					PhDeptRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});

	}

	// 编辑框监听器
	private TextWatcher textWatcher = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			adapter.getFilter().filter(s);
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		};
	};
	
}
