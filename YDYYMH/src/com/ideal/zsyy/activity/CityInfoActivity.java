package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import com.ideal.wdm.tools.DataCache;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.CityInfosListAdapter;
import com.ideal.zsyy.entity.CityEntity;
import com.ideal.zsyy.entity.CityInfo;
import com.ideal.zsyy.request.CityReq;
import com.ideal.zsyy.response.CityRes;
import com.ideal.zsyy.utils.StringHelper;
import com.ideal2.base.gson.GsonServlet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import android.widget.ViewAnimator;

public class CityInfoActivity extends Activity {
	private List<CityInfo> cityInfos; 
	private String[] indexStr = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
	private LinearLayout ll_cityIndex;
	private ListView lv_citylist;
	private TextView tv_show;
	private List<CityEntity> cityEntity;
	private List<CityEntity> newcityEntity = new ArrayList<CityEntity>();
	private HashMap<String, Integer> selector = null;
	private CityInfosListAdapter cityinfoAdapter;
	private List<CityEntity> seacherCityEntity;
	
	private int height;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				showView();
				break;
			case 2:
				seacherCityEntity = (List<CityEntity>) msg.obj; 
				break;
			default:
				break;
			}
		};
	};
	private View citytopview;
	private EditText etSeacher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citylist);
		if (cityInfos == null) {
			queryData();
		}
		initView(); 
	}

	private void initView() {
		citytopview = LayoutInflater.from(this).inflate(R.layout.searcher_top, null);
		etSeacher = (EditText) citytopview.findViewById(R.id.et_seacher);  
		etSeacher.setHint("请输入城市名称..."); 
		etSeacher.addTextChangedListener(watcher); 
		
		ll_cityIndex = (LinearLayout) findViewById(R.id.ll_city_index); 
		ll_cityIndex.setBackgroundColor(Color.parseColor("#00ffffff"));
		lv_citylist = (ListView) findViewById(R.id.lv_citylist);  
		tv_show = (TextView) findViewById(R.id.tv_text);
		tv_show.setVisibility(View.GONE);
		
		Button btnBack = (Button) findViewById(R.id.btn_back); 
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		lv_citylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				CityEntity entity = null;
				final int index = position;
				if (seacherCityEntity != null) {
					entity = seacherCityEntity.get(position - 1); 
				} else {
					entity = newcityEntity.get(index - 1);
				}
				if (entity != null && !"".equals(entity.getCity_name())) {
					Intent intent = new Intent(CityInfoActivity.this,HospitalListActivity.class);
					intent.putExtra("city_name", entity.getCity_name()); 
					intent.putExtra("city_code", entity.getCity_code());
					setResult(1, intent);
					finish();
//					Toast.makeText(CityInfoActivity.this, "name:" + entity.getCity_name(), 1).show();
				}
			}
		});
	}

	private void showView() {
		setData();
		String[] allNames = sortIndex(cityEntity);
		sortList(allNames);
		selector = new HashMap<String, Integer>();
		for (int i = 0; i < indexStr.length; i++) {
			for (int j = 0; j < newcityEntity.size(); j++) {
				if (newcityEntity.get(j).getCity_name().equals(indexStr[i])) {
					selector.put(indexStr[i], j);
				}
			}
		}
		lv_citylist.addHeaderView(citytopview); 
		cityinfoAdapter = new CityInfosListAdapter(this, newcityEntity,mHandler); 
		cityinfoAdapter.setSeacher(false); 
		lv_citylist.setAdapter(cityinfoAdapter); 
		
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		int screen_height = wm.getDefaultDisplay().getHeight();// 屏幕高度
		height = (screen_height - 150) / indexStr.length;
		
		showIndexView();
	}

	//显示索引。
	private void showIndexView() {
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, height);
		for (int i = 0; i < indexStr.length; i++) {
			final TextView tv = new TextView(this);
			tv.setLayoutParams(params);
			tv.setTextSize(10);
			tv.setText(indexStr[i]);
			tv.setPadding(10, 0, 10, 0);
			ll_cityIndex.addView(tv);
			ll_cityIndex.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)

				{
					float y = event.getY();
					int index = (int) (y / height);
					if (index > -1 && index < indexStr.length) {// 防止越界
						String key = indexStr[index];
						if (selector.containsKey(key)) {
							int pos = selector.get(key);
							if (lv_citylist.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
								lv_citylist.setSelectionFromTop(
										pos
												+ lv_citylist
														.getHeaderViewsCount(),
										0);
							} else {
								lv_citylist.setSelectionFromTop(pos,
										0);// 滑动到第一项
							}
							tv_show.setVisibility(View.VISIBLE);
							tv_show.setText(indexStr[index]);
						}
					}
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						ll_cityIndex.setBackgroundColor(Color
								.parseColor("#606060"));
						break;

					case MotionEvent.ACTION_MOVE:

						break;
					case MotionEvent.ACTION_UP:
						ll_cityIndex.setBackgroundColor(Color
								.parseColor("#00ffffff"));
						tv_show.setVisibility(View.GONE);
						break;
					}
					return true;
				}
			});
		}
	}

	/**
	 * 获取排序后的新数据
	 * 
	 * @param persons
	 * @return
	 */
	public String[] sortIndex(List<CityEntity> cityinfos) {
		TreeSet<String> set = new TreeSet<String>();
		// 获取初始化数据源中的首字母，添加到set中
		for (CityEntity cityinfo : cityinfos) {

			set.add(StringHelper.getPinYinHeadChar(cityinfo.getCity_name()).substring(
					0, 1));
		}
		// 新数组的长度为原数据加上set的大小
		String[] names = new String[cityinfos.size() + set.size()];
		int i = 0;
		for (String string : set) {
			names[i] = string;
			i++;
		}
		String[] pinYinNames = new String[cityinfos.size()];
		for (int j = 0; j < cityinfos.size(); j++) {
			cityinfos.get(j).setPinYinName(
					StringHelper
							.getPingYin(cityinfos.get(j).getCity_name().toString()));
			pinYinNames[j] = StringHelper.getPingYin(cityinfos.get(j).getCity_name()
					.toString());
		}
		// 将原数据拷贝到新数据中
		System.arraycopy(pinYinNames, 0, names, set.size(), pinYinNames.length);
		// 自动按照首字母排序
		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
		return names;
	}
	
	/**
	 * 重新排序获得一个新的List集合
	 * 
	 * @param allNames
	 */
	private void sortList(String[] allNames) {

		for (int i = 0; i < allNames.length; i++) {
			if (allNames[i].length() != 1) {
				for (int j = 0; j < cityEntity.size(); j++) {
					if (allNames[i].equals(cityEntity.get(j).getPinYinName())) {
						CityEntity entity = new CityEntity(cityEntity.get(j).getCity_name(), cityEntity.get(j).getCity_code(), cityEntity.get(j).getPinYinName());
						newcityEntity.add(entity); 
					}
				}
			} else {
				newcityEntity.add(new CityEntity(allNames[i]));
			}
		}
	}
	
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if (s != null && s.length() > 0) {
				cityinfoAdapter.setSeacher(true); 
				cityinfoAdapter.getFilter().filter(s);
				ll_cityIndex.setVisibility(View.INVISIBLE); 
			} else {
				cityinfoAdapter.setSeacher(false); 
				cityinfoAdapter.getFilter().filter(s);
				ll_cityIndex.setVisibility(View.VISIBLE);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	private void setData() {
		// TODO Auto-generated method stub
		cityEntity = new ArrayList<CityEntity>();
		for (CityInfo info : cityInfos) {
			CityEntity entity = new CityEntity(info.getCity_name(), info.getCity_code());
			cityEntity.add(entity);
		}
	}
	
	private void queryData() {
		// TODO Auto-generated method stub
		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);
		
		CityReq cityreq = new CityReq();
		cityreq.setOperType("26");
		
		GsonServlet<CityReq, CityRes> gServlet = new GsonServlet<CityReq, CityRes>(this);
		gServlet.request(cityreq, CityRes.class);
		gServlet.setOnResponseEndListening(new GsonServlet.OnResponseEndListening<CityReq, CityRes>() {

			@Override
			public void onResponseEnd(CityReq commonReq, CityRes commonRes,
					boolean result, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
//				if (commonRes != null) {
//					cityInfos = commonRes.getCityinfos();
//				}
			}

			@Override
			public void onResponseEndSuccess(CityReq commonReq,
					CityRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					cityInfos = commonRes.getCityinfos();
					Message msg = mHandler.obtainMessage(1);
					mHandler.sendMessage(msg);
				}
			}

			@Override
			public void onResponseEndErr(CityReq commonReq, CityRes commonRes,
					String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
} 
