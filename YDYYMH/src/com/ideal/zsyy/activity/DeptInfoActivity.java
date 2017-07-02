package com.ideal.zsyy.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.PhDoctorAdapter;
import com.ideal.zsyy.entity.DeptInfoDetail;
import com.ideal.zsyy.entity.PhDoctorInfo;
import com.ideal.zsyy.request.PhDeptDetailReq;
import com.ideal.zsyy.request.PhDoctorReq;
import com.ideal.zsyy.response.DeptInfoDetailRes;
import com.ideal.zsyy.response.PhDoctorRes;
import com.ideal.zsyy.utils.DataCache;
import com.ideal.zsyy.utils.DiyDatePickerDialog;
import com.ideal.zsyy.view.PullDownListView;
import com.ideal2.base.gson.GsonServlet;

public class DeptInfoActivity extends Activity  {

	private String deptid;
	private TextView tvDeptId;
	private TextView tvDeptName;
	private TextView tvDeptIntroduce;
	private DeptInfoDetail deptInfodetail;
	private TextView tvDept;
	private TextView tvDoctor;
	private Button btnBack, btnBack1;
	private LinearLayout llDept;
	private LinearLayout llDoctor;
	private ListView lvDoctor;
	private Button btnDeptTime;
	private View doctortop;
	private LayoutInflater mInflater;
	private final static int DOCTOR_QUERY = 0;
	private PhDoctorAdapter adapter;

	private PhDoctorAdapter adapter_search;
	private List<PhDoctorInfo> phDoctorInfos;
	private List<PhDoctorInfo> checkphDoctorInfos;
	private int pageSize = 20;// 如果为空则不分页
	private int pageIndex = 0;// 从0开始
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				phDoctorInfos = (List<PhDoctorInfo>) msg.obj;
				if (phDoctorInfos != null && phDoctorInfos.size() > 0) {
					adapter = new PhDoctorAdapter(phDoctorInfos,
							DeptInfoActivity.this, mHandler);
					lvDoctor.setAdapter(adapter);
				} else {
					Toast.makeText(DeptInfoActivity.this, "该科室没有医生", 1).show();
				}
				break;
			case 1:
				checkphDoctorInfos = (List<PhDoctorInfo>) msg.obj;
				break;
			case 2:
				checkphDoctorInfos = (List<PhDoctorInfo>) msg.obj;
				adapter_search = new PhDoctorAdapter(checkphDoctorInfos,
						DeptInfoActivity.this, mHandler);
//				lv_doctorInfo.addHeaderView(doctortop);
				lvDoctor.setAdapter(adapter_search);
				break;
			default:
				break;
			}
		};
	};
	private ImageView iv_deptimg;
	private EditText etDoctor;
	private PullDownListView mPullDownView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dept_info);
		mInflater = LayoutInflater.from(this);
		initView();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		deptid = intent.getStringExtra("deptid");
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd E");
		btnDeptTime.setText(getResources().getString(R.string.depttime) + " "
				+ sdf.format(new Date()));
		queryData();
	}

	// 初始化视图
	private void initView() {
		// doctor_top = mInflater.inflate(R.layout.doctor_top, null);
		doctortop = mInflater.inflate(R.layout.searcher_top, null);

		etDoctor = (EditText) doctortop.findViewById(R.id.et_seacher);
		etDoctor.setHint(R.string.querydoctorhint);  
		tvDeptId = (TextView) findViewById(R.id.tv_deptid);
		tvDeptName = (TextView) findViewById(R.id.tv_deptname);
		tvDeptIntroduce = (TextView) findViewById(R.id.tv_deptintroduce);
		tvDept = (TextView) findViewById(R.id.tv_dept);
		tvDoctor = (TextView) findViewById(R.id.tv_doctor);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack1 = (Button) findViewById(R.id.btn_back1);
		llDept = (LinearLayout) findViewById(R.id.ll_dept);
		llDoctor = (LinearLayout) findViewById(R.id.ll_doctor);

		 lvDoctor = (ListView) findViewById(R.id.lv_doctor);
//		mPullDownView = (PullDownListView) findViewById(R.id.sreach_list);
//		mPullDownView.setRefreshListioner(this);
//		lvDoctor = mPullDownView.mListView;
//		lvDoctor.addHeaderView(doctortop);
//		mPullDownView.setMore(true);// 这里设置true表示还有更多加载，设置为false底部将不显示更多

		btnDeptTime = (Button) findViewById(R.id.btn_deptTime);
		iv_deptimg = (ImageView) findViewById(R.id.iv_deptimg);
		btn_deptyy = (Button) findViewById(R.id.btn_deptyy);
		rl_order = (RelativeLayout) findViewById(R.id.rl_order);
		rl_deptlist = (RelativeLayout) findViewById(R.id.rl_deptlist);
		Intent intent = getIntent();
		boolean booleanExtra = intent.getBooleanExtra("gh", false);
		if (booleanExtra) {// 是预约挂号
			rl_deptlist.setVisibility(View.GONE);
			rl_order.setVisibility(View.VISIBLE);
		} else {
			rl_deptlist.setVisibility(View.VISIBLE);
			rl_order.setVisibility(View.GONE);
		}
		btnQuery = (Button) findViewById(R.id.btn_query);
		setListener();
	}

	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			// adapter.getFilter().filter(s);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (s.toString().trim().equals("")) {

				if (adapter != null) {

					lvDoctor.setAdapter(adapter);

				}
				mPullDownView.setMore(true);

			} else {

				mPullDownView.setMore(false);
				querySearchData(s.toString().trim());
			}
		}
	};

	// 查询数据
	private void queryData1() {
		if (deptInfodetail != null) {
			PhDoctorReq req = new PhDoctorReq();
			req.setHosID(Config.hosId);
			req.setPageSize("");
			req.setDept_Id(deptInfodetail.getId());
			req.setPageIndex("");
			req.setIsPro("");
			req.setOperType("5");
			// DataUtils.getDoctorInfos(this, req, mHandler);
			
			getDoctorInfos_db(this, req, mHandler, Config.hosId, "");
		}
	}

	private void querySearchData(String docName) {

		PhDoctorReq req = new PhDoctorReq();
		req.setHosID(Config.hosId);
		req.setDept_Id(deptInfodetail.getId());
		req.setPageSize("");
		req.setPageIndex("0");
		req.setIsPro("");
		req.setDocName(docName);
		req.setOperType("5");

		getDoctorInfos_search(this, req, mHandler, Config.hosId, "");
	}

	private Button btn_deptyy;
	private RelativeLayout rl_order;
	private RelativeLayout rl_deptlist;

	// 设置监听
	private void setListener() {
		// TODO Auto-generated method stub
		etDoctor.addTextChangedListener(watcher);
		// 科室信息
		tvDept.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				tvDept.setBackgroundResource(R.drawable.navigation_tab_left_down);
				tvDept.setTextColor(getResources().getColor(
						android.R.color.white));
				tvDoctor.setBackgroundResource(R.drawable.navigation_tab_right_up);
				tvDoctor.setTextColor(Color.parseColor("#0079ff"));
				llDept.setVisibility(View.VISIBLE);
				llDoctor.setVisibility(View.GONE);
				iv_deptimg.setVisibility(View.VISIBLE);
			}
		});
		// 科室对应的医生
		tvDoctor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				tvDept.setBackgroundResource(R.drawable.navigation_tab_left_up);
				tvDept.setTextColor(Color.parseColor("#0079ff"));
				tvDoctor.setBackgroundResource(R.drawable.navigation_tab_right_down);
				tvDoctor.setTextColor(getResources().getColor(
						android.R.color.white));
				llDept.setVisibility(View.GONE);
				llDoctor.setVisibility(View.VISIBLE);
				iv_deptimg.setVisibility(View.GONE);
				if (phDoctorInfos == null) {
//					PhDoctorReq req = new PhDoctorReq();
//					req.setHosID(Config.hosId);
//					req.setPageSize("");
//					req.setPageIndex("0");
//					req.setIsPro("");
//					req.setDept_Id(deptInfo.getId());
//					req.setOperType("5");
//					DataUtils.getDoctorInfos(DeptInfoActivity.this, req,
//							mHandler);
					queryData1();
				}
			}
		});
		// 返回
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 返回
		btnBack1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 预约时间
		btnDeptTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DiyDatePickerDialog dialog = new DiyDatePickerDialog(
						btnDeptTime, DeptInfoActivity.this, null);
				dialog.show();
			}
		});
		// 预约
		btn_deptyy.setOnClickListener(new View.OnClickListener() {
			private Dialog dialog;

			@Override
			public void onClick(View v) {
				// DataUtils.showToast(DeptInfoActivity.this);
				
				if (Config.phUsers != null) {
//					Intent intent = new Intent(DeptInfoActivity.this,
//							OrderDeptInfoDutyActivity.class);
					Intent intent = new Intent(DeptInfoActivity.this,
							OrderDeptInfoDuty1Activity.class);
					intent.putExtra("dept_id", deptInfodetail.getId()); 
					startActivity(intent);
					finish();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							DeptInfoActivity.this);
					builder.setTitle("请登录");
					builder.setNeutralButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(
											DeptInfoActivity.this,
											LoginActivity.class);
									intent.putExtra("logintype", "deptinfo");
									intent.putExtra("dept_id", deptInfodetail.getDept_Id());
									startActivity(intent);
								}
							});

					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					builder.create().show();
				}
			}
		});

		// 点击医生查看医生详情
		lvDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0) {
					PhDoctorInfo doctorInfo = null;
					if (checkphDoctorInfos != null
							&& checkphDoctorInfos.size() > 0) {
						doctorInfo = checkphDoctorInfos.get(position-1);
					} else {
						doctorInfo = phDoctorInfos.get(position-1);
					}
					Intent intent = new Intent(DeptInfoActivity.this,
							DoctorInfoActivity.class);
					intent.putExtra("doctorId", doctorInfo.getId());
					startActivity(intent);
				}
			}
		});
		
		btnQuery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataCache.deptInfodetail = deptInfodetail;
				Intent intent = new Intent(DeptInfoActivity.this,DeptInfoFloorPicActivity.class);
				startActivity(intent); 
			}
		});
	}

	// 显示数据
	private void showData() {
		if (deptInfodetail != null) {
			tvDeptId.setText(deptInfodetail.getId());
			tvDeptName.setText(deptInfodetail.getDept_Name());
			tvDeptIntroduce.setText(deptInfodetail.getDept_Introduce());
		} else {
			Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
		}
	}

	public void queryData(){
		PhDeptDetailReq req = new PhDeptDetailReq();
		req.setId(deptid);
		req.setOperType("7");
		GsonServlet<PhDeptDetailReq, DeptInfoDetailRes> gsonServlet = new GsonServlet<PhDeptDetailReq, DeptInfoDetailRes>(this);
		gsonServlet.request(req, DeptInfoDetailRes.class);
		gsonServlet.setOnResponseEndListening(new GsonServlet.OnResponseEndListening<PhDeptDetailReq, DeptInfoDetailRes>() {

			@Override
			public void onResponseEnd(PhDeptDetailReq commonReq,
					DeptInfoDetailRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onResponseEndSuccess(PhDeptDetailReq commonReq,
					DeptInfoDetailRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					deptInfodetail = commonRes.getDeptInfos();
					showData();
				}
			}

			@Override
			public void onResponseEndErr(PhDeptDetailReq commonReq,
					DeptInfoDetailRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void getDoctorInfos_db(final Context context, final PhDoctorReq req,
			final Handler mHandler, final String hospital_id, final String isPro) {
		GsonServlet<PhDoctorReq, PhDoctorRes> gServlet = new GsonServlet<PhDoctorReq, PhDoctorRes>(
				context);
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

					Message msg = mHandler.obtainMessage(0,
							commonRes.getPhDoctorInfos());
					mHandler.sendMessage(msg);

				} else {

					mPullDownView.setMore(false);
					pageIndex--;
				}
			}

			@Override
			public void onResponseEndErr(PhDoctorReq commonReq,
					PhDoctorRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void getDoctorInfos_search(final Context context,
			final PhDoctorReq req, final Handler mHandler,
			final String hospital_id, final String isPro) {
		GsonServlet<PhDoctorReq, PhDoctorRes> gServlet = new GsonServlet<PhDoctorReq, PhDoctorRes>(
				context);
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

					Message msg = mHandler.obtainMessage(2,
							commonRes.getPhDoctorInfos());
					mHandler.sendMessage(msg);

				}
			}

			@Override
			public void onResponseEndErr(PhDoctorReq commonReq,
					PhDoctorRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}
		});
	}

	private Handler myHandler = new Handler();
	private Button btnQuery;
//	@Override
//	public void onRefresh() {
//		// TODO Auto-generated method stub
//		myHandler.postDelayed(new Runnable() {
//
//			public void run() {
//
//				pageIndex = 0;
//
//				etDoctor.setText("");
//
//				if (phDoctorInfos != null) {
//
//					phDoctorInfos.clear();
//				}
//
//				// list1.clear();
//				// addLists(10);
//				mPullDownView.onRefreshComplete();// 这里表示刷新处理完成后把上面的加载刷新界面隐藏
//				mPullDownView.setMore(true);// 这里设置true表示还有更多加载，设置为false底部将不显示更多
//
//				queryData();
//
//				if (adapter != null) {
//					adapter.notifyDataSetChanged();
//				}
//
//			}
//		}, 1500);
//	}
//
//	@Override
//	public void onLoadMore() {
//		// TODO Auto-generated method stub
//		mHandler.postDelayed(new Runnable() {
//			public void run() {
//
//				pageIndex++;
//
//				mPullDownView.onLoadMoreComplete();// 这里表示加载更多处理完成后把下面的加载更多界面（隐藏或者设置字样更多）
//				// if(list1.size()<maxAount)//判断当前list中已添加的数据是否小于最大值maxAount，是那么久显示更多否则不显示
//				mPullDownView.setMore(true);// 这里设置true表示还有更多加载，设置为false底部将不显示更多
//				// else
//				// mPullDownView.setMore(false);
//
//				queryData();
//
//				if (adapter != null) {
//					adapter.notifyDataSetChanged();
//				}
//
//			}
//		}, 1500);
//	}
}
