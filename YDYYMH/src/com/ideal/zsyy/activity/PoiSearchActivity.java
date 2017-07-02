package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.search.core.AMapException;
import com.amap.api.search.core.LatLonPoint;
import com.amap.api.search.poisearch.PoiItem;
import com.amap.api.search.poisearch.PoiPagedResult;
import com.amap.api.search.poisearch.PoiSearch;
import com.amap.api.search.poisearch.PoiSearch.SearchBound;
import com.amap.api.search.poisearch.PoiTypeDef;
import com.amapv2.cn.apis.poisearch.MySuggestionProvider;
import com.amapv2.cn.apis.util.AMapUtil;
import com.amapv2.cn.apis.util.Constants;
import com.amapv2.cn.apis.util.ToastUtil;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.PoiSearchApapter;

public class PoiSearchActivity extends FragmentActivity implements
		OnMarkerClickListener, InfoWindowAdapter {

	private AMap aMap;
	private String query = null;
	private PoiPagedResult result;
	private ProgressDialog progDialog = null;
	private Button btn;

	private int search_range = 3000;

	private String search_text = "药店";
	private String search_type = PoiTypeDef.MedicalService;

	private final int POISEARCH_LIST = 1;

	private List<Marker> marker_list;

	// private int curpage = 1;
	// private int cnt = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_search);

		init();
		initView();

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {

				// onSearchRequested();
				aMap.setOnMarkerClickListener(this);
				aMap.setInfoWindowAdapter(this);
				// doSearchQuery(search_text);

				aMap.addMarker(new MarkerOptions()
						.position(
								new LatLng(Config.hospital_lat,
										Config.hospital_lng))
						.title(Config.hospital_name)
						.snippet(Config.hospital_address)
						.icon((BitmapDescriptorFactory
								.fromResource(R.drawable.hospital_marker))));

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(Config.hospital_lat,
								Config.hospital_lng)).zoom(15).build();

				aMap.animateCamera(
						CameraUpdateFactory.newCameraPosition(cameraPosition),
						1000, null);

			}
		}
	}

	private LinearLayout search_yaodian;
	private LinearLayout search_canyin;
	private LinearLayout search_jiayouzhan;
	private LinearLayout search_tingchechang;
	private LinearLayout search_atm;
	private TextView top_title;
	private Button search_ditu;
	private Button search_liebiao;

	private LinearLayout search_title;
	private LinearLayout search_poi_list;
	private LinearLayout search_tab;

	private ListView lv_search_poi;

	private void initView() {

		marker_list = new ArrayList<Marker>();

		search_title = (LinearLayout) findViewById(R.id.search_title);

		search_tab = (LinearLayout) findViewById(R.id.search_tab);

		// search_tab.setFocusable(false);

		top_title = (TextView) findViewById(R.id.top_title);

		lv_search_poi = (ListView) findViewById(R.id.lv_search_poi);
		lv_search_poi.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int item,
					long arg3) {

				findViewById(R.id.map).setVisibility(View.VISIBLE);
				search_poi_list.setVisibility(View.GONE);

				// for (Marker marker : marker_list) {
				//
				// if (marker != null) {
				// marker.setIcon(BitmapDescriptorFactory.defaultMarker());
				// }
				//
				// }

				Marker marker = marker_list.get(item);

				if (marker != null) {
					// marker.setIcon(BitmapDescriptorFactory
					// .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

					marker.showInfoWindow();

					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(marker.getPosition()).zoom(18).build();
					aMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition), 1000, null);
				}

				search_ditu
						.setBackgroundResource(R.drawable.navigation_tab_left_down);
				search_ditu.setTextColor(Color.parseColor("#FFFFFF"));

				search_liebiao
						.setBackgroundResource(R.drawable.navigation_tab_right_up);
				search_liebiao.setTextColor(Color.parseColor("#0079ff"));

			}
		});

		search_poi_list = (LinearLayout) findViewById(R.id.search_poi_list);

		search_ditu = (Button) findViewById(R.id.search_ditu);
		search_ditu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				findViewById(R.id.map).setVisibility(View.VISIBLE);
				search_poi_list.setVisibility(View.GONE);

				search_ditu
						.setBackgroundResource(R.drawable.navigation_tab_left_down);
				search_ditu.setTextColor(Color.parseColor("#FFFFFF"));

				search_liebiao
						.setBackgroundResource(R.drawable.navigation_tab_right_up);
				search_liebiao.setTextColor(Color.parseColor("#0079ff"));

				aMap.clear();
				handler.sendMessage(Message
						.obtain(handler, Constants.POISEARCH));

			}
		});

		search_liebiao = (Button) findViewById(R.id.search_liebiao);
		search_liebiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				findViewById(R.id.map).setVisibility(View.GONE);
				search_poi_list.setVisibility(View.VISIBLE);

				search_ditu
						.setBackgroundResource(R.drawable.navigation_tab_left_up);
				search_ditu.setTextColor(Color.parseColor("#0079ff"));
				search_liebiao
						.setBackgroundResource(R.drawable.navigation_tab_right_down);
				search_liebiao.setTextColor(Color.parseColor("#FFFFFF"));

				lv_search_poi.setAdapter(null);
				if (result != null) {

					try {
						PoiSearchApapter poiSearchApapter = new PoiSearchApapter(
								v.getContext(), result.getPage(1));
						lv_search_poi.setAdapter(poiSearchApapter);
					} catch (AMapException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		Button btn_right = (Button) findViewById(R.id.btn_right);
		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (search_title.getVisibility() == View.GONE) {

					search_title.setVisibility(View.VISIBLE);

				} else if (search_title.getVisibility() == View.VISIBLE) {

					search_title.setVisibility(View.GONE);
				}
			}
		});

		search_yaodian = (LinearLayout) findViewById(R.id.search_yaodian);
		search_yaodian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				search_text = "药店";
				search_type = PoiTypeDef.MedicalService;
				setTitle(search_text);
				doSearchQuery(search_text);

			}
		});

		search_canyin = (LinearLayout) findViewById(R.id.search_canyin);
		search_canyin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				search_text = "餐饮";
				search_type = PoiTypeDef.FoodBeverages;
				setTitle(search_text);
				doSearchQuery(search_text);

			}
		});

		search_jiayouzhan = (LinearLayout) findViewById(R.id.search_jiayouzhan);
		search_jiayouzhan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				search_text = "加油站";
				search_type = PoiTypeDef.AutoCarService;
				setTitle(search_text);
				doSearchQuery(search_text);

			}
		});

		search_tingchechang = (LinearLayout) findViewById(R.id.search_tingchechang);
		search_tingchechang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				search_text = "停车场";
				search_type = PoiTypeDef.LifeService;
				setTitle(search_text);
				doSearchQuery(search_text);

			}
		});

		search_atm = (LinearLayout) findViewById(R.id.search_atm);
		search_atm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				search_text = "ATM";
				search_type = PoiTypeDef.Financial;
				setTitle(search_text);
				doSearchQuery(search_text);

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

	private void setTitle(String title) {

		top_title.setText(search_text);
		search_title.setVisibility(View.GONE);
		search_tab.setVisibility(View.VISIBLE);

	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在搜索:\n" + query);
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	protected void doSearchQuery(String search) {
		// query = intent.getStringExtra(SearchManager.QUERY);
		query = search;
		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
				PoiSearchActivity.this, MySuggestionProvider.AUTHORITY,
				MySuggestionProvider.MODE);
		suggestions.saveRecentQuery(query, null);
		// curpage = 1;
		// cnt = 0;

		showProgressDialog();// 显示进度框

		result = null;
		new Thread(new Runnable() {
			public void run() {
				try {
					PoiSearch poiSearch = new PoiSearch(PoiSearchActivity.this,
							new PoiSearch.Query(query, search_type, "021")); // 设置搜索字符串，poi搜索类型，poi搜索区域（空字符串代表全国）
					// poiSearch.setPageSize(10);// 设置搜索每次最多返回结果数

					poiSearch.setBound(new SearchBound(new LatLonPoint(
							Config.hospital_lat, Config.hospital_lng),
							search_range));

					result = poiSearch.searchPOI();
					// if (result != null) {
					// cnt = result.getPageCount();
					// }

					if (findViewById(R.id.map).getVisibility() == View.VISIBLE) {

						handler.sendMessage(Message.obtain(handler,
								Constants.POISEARCH));

					} else if (search_poi_list.getVisibility() == View.VISIBLE) {

						handler.sendMessage(Message.obtain(handler,
								POISEARCH_LIST));

					} else {

						dissmissProgressDialog();
					}

				} catch (AMapException e) {
					handler.sendMessage(Message
							.obtain(handler, Constants.ERROR));
					result = null;
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		return false;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	/**
	 * 一次性打印多个Marker出来
	 */
	private void addMarkers(List<PoiItem> poiItems) {

		aMap.addMarker(new MarkerOptions()
				.position(new LatLng(Config.hospital_lat, Config.hospital_lng))
				.title(Config.hospital_name)
				.snippet(Config.hospital_address)
				.icon((BitmapDescriptorFactory
						.fromResource(R.drawable.hospital_marker))));

		marker_list.clear();
		for (int i = 0; i < poiItems.size(); i++) {

			Marker marker = aMap.addMarker(new MarkerOptions()
					.position(
							new LatLng(
									poiItems.get(i).getPoint().getLatitude(),
									poiItems.get(i).getPoint().getLongitude()))
					.title(poiItems.get(i).getTitle())
					.snippet(poiItems.get(i).getSnippet())
					.icon(BitmapDescriptorFactory.defaultMarker()));

			marker_list.add(marker);
		}

	}

	private LatLngBounds getLatLngBounds(List<PoiItem> poiItems) {

		// (lv_search_poi.getAdapter()).

		LatLngBounds.Builder b = LatLngBounds.builder();
		for (int i = 0; i < poiItems.size(); i++) {
			b.include(new LatLng(poiItems.get(i).getPoint().getLatitude(),
					poiItems.get(i).getPoint().getLongitude()));
		}
		return b.build();
	}

	private void showPoiItem(List<PoiItem> poiItems) {
		if (poiItems != null && poiItems.size() > 0) {
			if (aMap == null)
				return;
			aMap.clear();
			LatLngBounds bounds = getLatLngBounds(poiItems);
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
			addMarkers(poiItems);
		} else {
			ToastUtil.show(getApplicationContext(), "没有搜索到数据！");
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == Constants.POISEARCH) {
				dissmissProgressDialog();// 隐藏对话框

				if (result != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								final List<PoiItem> poiItems = result
										.getPage(1);
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										showPoiItem(poiItems);// 每页显示10个poiitem
									}
								});

							} catch (AMapException e) {
								e.printStackTrace();

							}
						}
					}).start();
				}

			} else if (msg.what == POISEARCH_LIST) {

				dissmissProgressDialog();// 隐藏对话框

				if (result != null) {

					try {

						PoiSearchApapter poiSearchApapter = new PoiSearchApapter(
								getApplicationContext(), result.getPage(1));
						lv_search_poi.setAdapter(poiSearchApapter);

					} catch (AMapException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else if (msg.what == Constants.ERROR) {
				dissmissProgressDialog();// 隐藏对话框
				ToastUtil.show(getApplicationContext(), "搜索失败,请检查网络连接！");
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		aMap = null;
	}

}
