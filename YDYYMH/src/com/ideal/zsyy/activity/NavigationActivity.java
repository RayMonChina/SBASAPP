package com.ideal.zsyy.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.search.core.AMapException;
import com.amap.api.search.core.LatLonPoint;
import com.amap.api.search.route.Route;
import com.amapv2.cn.apis.route.RouteOverlay;
import com.amapv2.cn.apis.util.AMapUtil;
import com.amapv2.cn.apis.util.Constants;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.NavigationAdater;

public class NavigationActivity extends FragmentActivity implements
		LocationSource, AMapLocationListener {

	private AMap aMap;

	private LocationManagerProxy mAMapLocManager = null;
	private OnLocationChangedListener mListener;
	private TextView myLocation;
	// private MyLocationStyle myLocationStyle;

	private Double geoLat = null;
	private Double geoLng = null;

	private ProgressDialog progDialog;
	private List<Route> routeResult;
	private Route route;
	private int mode = Route.DrivingDefault;
	private RouteOverlay routeOverlay;

	private LatLonPoint startPoint = null;
	private LatLonPoint endPoint = null;
	boolean isSuccess = false;

	private LinearLayout routeNav;
	private ImageButton routePre, routeNext;
	private Button drivingButton;
	private Button transitButton;
	private Button walkButton;

	private LinearLayout navigation_tab;
	private Button navigation_ditu;
	private Button navigation_liebiao;

	private LinearLayout ll_navigation_bus_list;
	private ListView lv_navigation_bus;

	private NavigationAdater navigationAdater;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			myLocation.setText((String) msg.obj);

			startSearchResult();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation);

		Toast.makeText(getApplicationContext(), "定位中，请稍后……", Toast.LENGTH_LONG)
				.show();

		myLocation = (TextView) findViewById(R.id.myLocation);
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}

		if (AMapUtil.checkReady(this, aMap)) {

			// MyLocationStyle myLocationStyle = new MyLocationStyle();
			// myLocationStyle.myLocationIcon(BitmapDescriptorFactory
			// .fromResource(R.drawable.location_marker));
			// myLocationStyle.strokeColor(Color.BLACK);
			// myLocationStyle.strokeWidth(5);
			// aMap.setMyLocationStyle(myLocationStyle);
			// aMap.setLocationSource(NavigationActivity.this);
			// aMap.setMyLocationEnabled(false);//
			// 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false
			// aMap.setOnMarkerClickListener(this);
			// aMap.setInfoWindowAdapter(this);

			addHospitalLocation();
		}

		mAMapLocManager = LocationManagerProxy.getInstance(this);

		initView();

	}

	private void initView() {

		ll_navigation_bus_list = (LinearLayout) findViewById(R.id.ll_navigation_bus_list);
		lv_navigation_bus = (ListView) findViewById(R.id.lv_navigation_bus);
		lv_navigation_bus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int item,
					long arg3) {

				findViewById(R.id.map).setVisibility(View.VISIBLE);
				ll_navigation_bus_list.setVisibility(View.GONE);

				routeNav.setVisibility(View.VISIBLE);

				navigation_ditu
						.setBackgroundResource(R.drawable.navigation_tab_left_down);
				navigation_ditu.setTextColor(Color.parseColor("#FFFFFF"));

				navigation_liebiao
						.setBackgroundResource(R.drawable.navigation_tab_right_up);
				navigation_liebiao.setTextColor(Color.parseColor("#0079ff"));

				if (mode == Route.BusDefault) {

					aMap.clear();

					if (routeResult != null) {
						route = routeResult.get(item);

						// Log.v("tags", "item="+item);

						if (route != null) {
							routeOverlay = new RouteOverlay(
									NavigationActivity.this, aMap, route);
							routeOverlay.removeFormMap();
							routeOverlay.addMarkerLine();
							routeNav.setVisibility(View.VISIBLE);
							routePre.setBackgroundResource(R.drawable.prev_disable);
							routeNext
									.setBackgroundResource(R.drawable.btn_route_next);
						}
					}
				}
			}

		});

		navigation_tab = (LinearLayout) findViewById(R.id.navigation_tab);
		routeNav = (LinearLayout) findViewById(R.id.LinearLayoutLayout_index_bottom);
		drivingButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_driving);
		transitButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_transit);
		walkButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_walk);
		drivingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				addHospitalLocation();
				mode = Route.DrivingDefault;
				drivingButton.setBackgroundResource(R.drawable.bt_driving_down);
				transitButton.setBackgroundResource(R.drawable.bt_transit_up);
				walkButton.setBackgroundResource(R.drawable.bt_walk_up);

				navigation_tab.setVisibility(View.GONE);
				findViewById(R.id.map).setVisibility(View.VISIBLE);
				ll_navigation_bus_list.setVisibility(View.GONE);
				startSearchResult();
			}
		});
		transitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				addHospitalLocation();
				mode = Route.BusDefault;
				drivingButton.setBackgroundResource(R.drawable.bt_driving_up);
				transitButton.setBackgroundResource(R.drawable.bt_transit_down);
				walkButton.setBackgroundResource(R.drawable.bt_walk_up);

				navigation_tab.setVisibility(View.VISIBLE);
				findViewById(R.id.map).setVisibility(View.VISIBLE);
				ll_navigation_bus_list.setVisibility(View.GONE);

				navigation_ditu
						.setBackgroundResource(R.drawable.navigation_tab_left_down);
				navigation_ditu.setTextColor(Color.parseColor("#FFFFFF"));

				navigation_liebiao
						.setBackgroundResource(R.drawable.navigation_tab_right_up);
				navigation_liebiao.setTextColor(Color.parseColor("#0079ff"));

				startSearchResult();

			}
		});

		walkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				addHospitalLocation();
				mode = Route.WalkDefault;
				drivingButton.setBackgroundResource(R.drawable.bt_driving_up);
				transitButton.setBackgroundResource(R.drawable.bt_transit_up);
				walkButton.setBackgroundResource(R.drawable.bt_walk_down);

				navigation_tab.setVisibility(View.GONE);
				findViewById(R.id.map).setVisibility(View.VISIBLE);
				ll_navigation_bus_list.setVisibility(View.GONE);
				startSearchResult();

			}
		});

		routePre = (ImageButton) findViewById(R.id.pre_index);
		routePre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (routeOverlay != null) {
					boolean enablePre = routeOverlay.showPrePopInfo();
					if (!enablePre) {
						routePre.setBackgroundResource(R.drawable.prev_disable);
						routeNext
								.setBackgroundResource(R.drawable.btn_route_next);
					} else {
						routePre.setBackgroundResource(R.drawable.btn_route_pre);
						routeNext
								.setBackgroundResource(R.drawable.btn_route_next);
					}
				}
			}
		});
		routeNext = (ImageButton) findViewById(R.id.next_index);
		routeNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (routeOverlay != null) {
					boolean enableNext = routeOverlay.showNextPopInfo();
					if (!enableNext) {
						routePre.setBackgroundResource(R.drawable.btn_route_pre);
						routeNext
								.setBackgroundResource(R.drawable.next_disable);
					} else {
						routePre.setBackgroundResource(R.drawable.btn_route_pre);
						routeNext
								.setBackgroundResource(R.drawable.btn_route_next);
					}
				}
			}
		});

		navigation_ditu = (Button) findViewById(R.id.navigation_ditu);
		navigation_ditu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				findViewById(R.id.map).setVisibility(View.VISIBLE);
				ll_navigation_bus_list.setVisibility(View.GONE);

				if (routeResult != null) {
					routeNav.setVisibility(View.VISIBLE);
				}

				navigation_ditu
						.setBackgroundResource(R.drawable.navigation_tab_left_down);
				navigation_ditu.setTextColor(Color.parseColor("#FFFFFF"));

				navigation_liebiao
						.setBackgroundResource(R.drawable.navigation_tab_right_up);
				navigation_liebiao.setTextColor(Color.parseColor("#0079ff"));

			}
		});

		navigation_liebiao = (Button) findViewById(R.id.navigation_liebiao);
		navigation_liebiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				findViewById(R.id.map).setVisibility(View.GONE);
				ll_navigation_bus_list.setVisibility(View.VISIBLE);
				routeNav.setVisibility(View.GONE);

				navigation_ditu
						.setBackgroundResource(R.drawable.navigation_tab_left_up);
				navigation_ditu.setTextColor(Color.parseColor("#0079ff"));

				navigation_liebiao
						.setBackgroundResource(R.drawable.navigation_tab_right_down);
				navigation_liebiao.setTextColor(Color.parseColor("#FFFFFF"));

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

	public void enableMyLocation() {

		if (mAMapLocManager == null) {
			mAMapLocManager = LocationManagerProxy.getInstance(this);
		}

		// Location API定位采用GPS和网络混合定位方式，时间最短是5000毫秒
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
		 */
		mAMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);
	}

	public void disableMyLocation() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		enableMyLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();

		disableMyLocation();
	}

	// 查询路径规划起点
	public void startSearchResult() {

		if (geoLat == null || geoLat == null) {

			return;
		}

		LatLng latLng = new LatLng(geoLat, geoLng);
		startPoint = AMapUtil.convertToLatLonPoint(latLng);

		LatLng latLng2 = new LatLng(Config.hospital_lat, Config.hospital_lng);
		endPoint = AMapUtil.convertToLatLonPoint(latLng2);

		progDialog = ProgressDialog.show(NavigationActivity.this, null,
				"正在获取线路", true, true);
		final Route.FromAndTo fromAndTo = new Route.FromAndTo(startPoint,
				endPoint);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					routeResult = Route.calculateRoute(NavigationActivity.this,
							fromAndTo, mode);
					if (progDialog.isShowing()) {
						if (routeResult != null || routeResult.size() > 0)
							routeHandler.sendMessage(Message
									.obtain(routeHandler,
											Constants.ROUTE_SEARCH_RESULT));
					}
				} catch (AMapException e) {
					Message msg = new Message();
					msg.what = Constants.ROUTE_SEARCH_ERROR;
					msg.obj = e.getErrorMessage();
					routeHandler.sendMessage(msg);
				}
			}
		});
		t.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mListener = null;
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
			mAMapLocManager = null;
		}

		if (aMap != null) {
			aMap.clear();
			aMap = null;
		}

	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onLocationChanged(AMapLocation location) {

		if (location != null) {

			geoLat = location.getLatitude();
			geoLng = location.getLongitude();

			if (isSuccess == false) {

				String str = ("定位成功:(" + geoLng + "," + geoLat + ")");
				Toast.makeText(getApplicationContext(), "定位成功",
						Toast.LENGTH_SHORT).show();
				Message msg = new Message();
				msg.obj = str;
				if (handler != null) {
					handler.sendMessage(msg);
				}
				isSuccess = true;
			}

		}

		if (mListener != null) {
			mListener.onLocationChanged(location);

		}

	}

	private Handler routeHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == Constants.ROUTE_SEARCH_RESULT) {
				progDialog.dismiss();

				if (routeResult != null && routeResult.size() > 0) {
					route = routeResult.get(0);

					if (route != null) {
						routeOverlay = new RouteOverlay(
								NavigationActivity.this, aMap, route);
						routeOverlay.removeFormMap();
						routeOverlay.addMarkerLine();
						routeNav.setVisibility(View.VISIBLE);
						routePre.setBackgroundResource(R.drawable.prev_disable);
						routeNext
								.setBackgroundResource(R.drawable.btn_route_next);
					}

					if (mode == Route.BusDefault) {

						navigation_tab.setVisibility(View.VISIBLE);
						navigationAdater = new NavigationAdater(
								getApplicationContext(), routeResult);
						lv_navigation_bus.setAdapter(navigationAdater);
					} else {
						navigation_tab.setVisibility(View.GONE);
					}

				}

			} else if (msg.what == Constants.ROUTE_SEARCH_ERROR) {
				progDialog.dismiss();
				showToast((String) msg.obj);
			}

		}
	};

	public void showToast(String showString) {
		Toast.makeText(getApplicationContext(), showString, Toast.LENGTH_SHORT)
				.show();
	}

	private void addHospitalLocation() {

		if (aMap == null) {
			return;
		}

		aMap.clear();
//		aMap.addMarker(new MarkerOptions()
//				.position(new LatLng(Config.hospital_lat, Config.hospital_lng))
//				.title(Config.hospital_name)
//				.snippet(Config.hospital_address)
//				.icon((BitmapDescriptorFactory
//						.fromResource(R.drawable.hospital_marker))));
//
//		CameraPosition cameraPosition = new CameraPosition.Builder()
//				.target(new LatLng(Config.hospital_lat, Config.hospital_lng))
//				.zoom(15).build();
//
//		aMap.animateCamera(
//				CameraUpdateFactory.newCameraPosition(cameraPosition), 1000,
//				null);
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		enableMyLocation();
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

}
