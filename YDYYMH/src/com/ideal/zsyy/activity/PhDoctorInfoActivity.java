package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.PhDoctorAdapter;
import com.ideal.zsyy.db.ZsyyOpenHelper;
import com.ideal.zsyy.entity.PhDoctorInfo;
import com.ideal.zsyy.imagecache.CommonUtil;
import com.ideal.zsyy.request.DocDutyInfoReq;
import com.ideal.zsyy.request.PhDoctorReq;
import com.ideal.zsyy.response.DocDutyInfoRes;
import com.ideal.zsyy.response.PhDoctorRes;
import com.ideal.zsyy.utils.AsynImageLoader;
import com.ideal.zsyy.utils.DataUtils;
import com.ideal.zsyy.utils.FileUtil;
import com.ideal.zsyy.view.PullDownListView;
import com.ideal2.base.gson.GsonServlet;

public class PhDoctorInfoActivity extends Activity{

	private LayoutInflater mInflater;
	private ListView lv_doctorInfo;
	private View doctortop;
	private EditText queryDoctor;
	private List<PhDoctorInfo> phDoctorInfos  = new ArrayList<PhDoctorInfo>();;
	private PhDoctorAdapter adapter;

	private PhDoctorAdapter adapter_search;
	private Button btnBack;
	private TextView tvDoctortitle;
	private Intent intent;
	private List<PhDoctorInfo> checkphDoctorInfos;

	private Handler myHandler = new Handler();
	private int pageSize = 20;// 如果为空则不分页
	private int pageIndex = 0;// 从0开始

	// private int number = 20; // 每次加载的数据

	private SQLiteDatabase db;
	private PullDownListView mPullDownView;
	

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// int n = phDoctorInfos.size() + pageSize;

				List<PhDoctorInfo> new_phDoctorInfos = (List<PhDoctorInfo>) msg.obj;

				for (PhDoctorInfo phDoctorInfo : new_phDoctorInfos) {

					phDoctorInfos.add(phDoctorInfo);
				}

				// phDoctorInfos = (List<PhDoctorInfo>) msg.obj;

				if (adapter == null) {

					adapter = new PhDoctorAdapter(phDoctorInfos,
							PhDoctorInfoActivity.this, mHandler);
					 lv_doctorInfo.addHeaderView(doctortop);
					lv_doctorInfo.setAdapter(adapter);

				} else {

					adapter.notifyDataSetChanged();
				}

				break;
			case 1:
				checkphDoctorInfos = (List<PhDoctorInfo>) msg.obj;
				break;
			case 2:
				checkphDoctorInfos = (List<PhDoctorInfo>) msg.obj;
				adapter_search = new PhDoctorAdapter(checkphDoctorInfos,
						PhDoctorInfoActivity.this, mHandler);
				lv_doctorInfo.addHeaderView(doctortop);
				lv_doctorInfo.setAdapter(adapter_search);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// File file = new File(DataUtils.doctor_path);
		// if (Environment.getExternalStorageState().equals(
		// Environment.MEDIA_MOUNTED) && file != null && file.exists()) {
		// AsynImageLoader.deleteImage();
		// }
		setContentView(R.layout.ph_doctorinfo);
		ZsyyOpenHelper dbHelper = new ZsyyOpenHelper(getApplicationContext());
		db = dbHelper.getWritableDatabase();
		mInflater = LayoutInflater.from(this);
		intent = getIntent();
		isOrder = intent.getBooleanExtra("isOrder", false);
		String dutytime = intent.getStringExtra("dutytime");
		String noontype = intent.getStringExtra("noontype");
		initView();
		String dataFileCache = FileUtil.getDataFileCache(DataUtils.dataurl + "doctor.txt");  
		if (dataFileCache != null && !"".equals(dataFileCache)) { 
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			PhDoctorRes docinfo = gson.fromJson(dataFileCache, PhDoctorRes.class);
			Message msg = mHandler.obtainMessage(0,
					docinfo.getPhDoctorInfos()); 
			mHandler.sendMessage(msg);
		} else {
			if (isOrder) {
				DocDutyInfoReq docdutyreq = new DocDutyInfoReq();
				docdutyreq.setDutytime(dutytime);
				docdutyreq.setNoontype(noontype);
				docdutyreq.setOperType("30");
				query(docdutyreq);
			} else {
				queryData();
			}
		}
		// 开启自动更新
	}

	/*
	 * 初始化视图
	 */
	private void initView() {
		doctortop = mInflater.inflate(R.layout.searcher_top, null);

		 lv_doctorInfo = (ListView) findViewById(R.id.lv_doctorInfo);

//		mPullDownView = (PullDownListView) findViewById(R.id.sreach_list);
//		mPullDownView.setRefreshListioner(this);
//		lv_doctorInfo = mPullDownView.mListView;
//		lv_doctorInfo.addHeaderView(doctortop);
//		mPullDownView.setMore(true);// 这里设置true表示还有更多加载，设置为false底部将不显示更多

		queryDoctor = (EditText) doctortop.findViewById(R.id.et_seacher);
		queryDoctor.setHint(R.string.querydoctorhint);
		btnBack = (Button) findViewById(R.id.btn_back);
		tvDoctortitle = (TextView) findViewById(R.id.tv_doctor_title);
		
		setListener();
	}

	// 设置监听
	private void setListener() {
		// lv_doctorInfo.setOnScrollListener(listener);
		queryDoctor.addTextChangedListener(watcher);
		lv_doctorInfo
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position > 0) {
							PhDoctorInfo doctorInfo = null;
							if (checkphDoctorInfos != null
									&& checkphDoctorInfos.size() > 0) {
								doctorInfo = checkphDoctorInfos
										.get(position - 1);
							} else {
								doctorInfo = phDoctorInfos.get(position - 1);
							}
							if (isOrder) {
								Intent intent = new Intent(
										PhDoctorInfoActivity.this,
										OrderRegister1Activity.class);
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
							} else {
								Intent intent = new Intent(
										PhDoctorInfoActivity.this,
										DoctorInfoActivity.class);
								intent.putExtra("doctorId", doctorInfo.getId());
								startActivity(intent);
							}
						}
					}
				});
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// 文本框检索
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			 adapter.getFilter().filter(s);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
//			if (s.toString().trim().equals("")) {
//
//				if (adapter != null) {
//
//					lv_doctorInfo.setAdapter(adapter);
//
//				}
//				mPullDownView.setMore(true);
//
//			} else {
//
//				mPullDownView.setMore(false);
//				querySearchData(s.toString().trim());
//			}
		}
	};
	private String isPro;
	private boolean isOrder;

	// 查询数据
	private void queryData() {
		isPro = "";
		if (intent != null && intent.getStringExtra("zj") != null) {
			tvDoctortitle.setText(R.string.specialist);
			isPro = "1";
		}
		PhDoctorReq req = new PhDoctorReq();
		req.setHosID(Config.hosId);
		req.setPageSize("");
		req.setPageIndex("");
//		req.setPageSize("" + pageSize);
//		req.setPageIndex("" + pageIndex);
		req.setIsPro(isPro);
		req.setOperType("5");
		 DataUtils.getDoctorInfos(this, req, mHandler);

//		getDoctorInfos_db(this, req, mHandler, Config.hosId, isPro);
	}

	// 查询数据
	private void querySearchData(String docName) {

		PhDoctorReq req = new PhDoctorReq();
		req.setHosID(Config.hosId);
		req.setPageSize("");
		req.setPageIndex("0");
		req.setIsPro(isPro);
		req.setDocName(docName);
		req.setOperType("5");

		getDoctorInfos_search(this, req, mHandler, Config.hosId, isPro);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (db != null) {

			db.close();
		}
//		AsynImageLoader.destory();
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
					Message msg = mHandler.obtainMessage(2,
							commonRes.getDocdutyinfos());
					mHandler.sendMessage(msg);
				}
			}

			@Override
			public void onResponseEndErr(DocDutyInfoReq commonReq,
					DocDutyInfoRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}
		});
	}

//	@Override
//	public void onRefresh() {
//		// TODO Auto-generated method stub
//		myHandler.postDelayed(new Runnable() {
//
//			public void run() {
//
//				pageIndex = 0;
//
//				queryDoctor.setText("");
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
	
}
