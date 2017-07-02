package com.ideal.zsyy.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ideal.wdm.tools.DataCache;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.IndicationCentertList;
import com.ideal.zsyy.adapter.IndicationDotList;
import com.ideal.zsyy.adapter.MyViewPagerAdapter;
import com.ideal.zsyy.entity.BasiHosInfo;
import com.ideal.zsyy.entity.PhHotPic;
import com.ideal.zsyy.entity.SkinInfo;
import com.ideal.zsyy.request.AllHospitalInfoReq;
import com.ideal.zsyy.request.PhHotPicReq;
import com.ideal.zsyy.response.AllHospitalInfoRes;
import com.ideal.zsyy.response.PhHotPicRes;
import com.ideal.zsyy.service.PreferencesService;
import com.ideal.zsyy.utils.AutoUpdateUtil;
import com.ideal.zsyy.utils.DataUtils;
import com.ideal.zsyy.utils.HttpUtil;
import com.ideal.zsyy.utils.SdCardUtil;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

public class MainActivity extends Activity {

	// private List<BasiHosInfo> allInfos;
	private TextView main_tv_pic;
	private List<View> picViews;

	private boolean isHaveSdCard;
	private ViewPager main_viewpager_picture;
	private MyViewPagerAdapter myPicViewPagerAdapter;
	private Bitmap bitmap;
	private IndicationDotList mDotList;
	private IndicationCentertList buttonList;

	private List<String> picName;
	private List<String> pic_title;
	private List<String> pic_download_url;
	private List<Bitmap> bitmap_web_list;
	private TextView main_tv_personal;

	private LinearLayout.LayoutParams layoutParams;
	private PreferencesService preferencesService;

	private int index = 0;

	private final String phone_path = Environment.getExternalStorageDirectory()
			+ File.separator + Config.down_path + File.separator + "hot_image";

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				picViews.clear();

				for (String pic_name : picName) {

					bitmap = BitmapFactory.decodeFile(phone_path
							+ File.separator + pic_name);

					// Log.v("tags", phone_path
					// + File.separator + pic_name);

					// if(bitmap != null)
					{
						ImageView imageView = new ImageView(
								getApplicationContext());
						imageView.setLayoutParams(layoutParams);
						imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
						imageView.setImageBitmap(bitmap);
						picViews.add(imageView);
					}

				}

				if (pic_title.size() > 0) {
					main_tv_pic.setText(pic_title.get(0));
				}

				mDotList.setCount(picViews.size());
				mDotList.setIndex(0);

				myPicViewPagerAdapter = new MyViewPagerAdapter(picViews);
				main_viewpager_picture.setAdapter(myPicViewPagerAdapter);

				break;
			case 1:

				picViews.clear();

				int i = 0;
				for (Bitmap bitmap : bitmap_web_list) {

					if (bitmap != null) {

						ImageView imageView = new ImageView(
								getApplicationContext());
						imageView.setLayoutParams(layoutParams);
						imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
						imageView.setImageBitmap(bitmap);
						picViews.add(imageView);
					} else {
						if (pic_title != null && pic_title.size() >= i
								&& pic_title.size() > 0) {
							if (pic_title.size() == i)
								pic_title.remove(i - 1);
						}
					}
					i++;
				}

				if (pic_title.size() > 0) {
					main_tv_pic.setText(pic_title.get(0));
				}

				mDotList.setCount(picViews.size());
				mDotList.setIndex(0);

				myPicViewPagerAdapter = new MyViewPagerAdapter(picViews);
				main_viewpager_picture.setAdapter(myPicViewPagerAdapter);
				thread.start();
				break;
			case 2:
				// 更新
				boolean update = preferencesService.getIsAutpUpdate();
				if (update) {
					AutoUpdateUtil auUpdateUtil = new AutoUpdateUtil(
							MainActivity.this, "2", preferencesService);
					auUpdateUtil.checkVersionSys();
				}
				break;
			case 3:
				int arg0 = msg.arg1;
				mDotList.setIndex(arg0);
				if (pic_title.size() > 0 && arg0 < pic_title.size()) {
					main_viewpager_picture.setCurrentItem(arg0);
					main_tv_pic.setText(pic_title.get(arg0));
				}
				break;
			}
		}
	};
	private int sy_bg;
	private LinearLayout ll_main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		picViews = new ArrayList<View>();

		picName = new ArrayList<String>();

		pic_title = new ArrayList<String>();

		pic_download_url = new ArrayList<String>();

		bitmap_web_list = new ArrayList<Bitmap>();

		isHaveSdCard = SdCardUtil.isHaveSdCard();

		mDotList = (IndicationDotList) findViewById(R.id.index_indication);

		buttonList = (IndicationCentertList) findViewById(R.id.index_button_indication);
		buttonList.setCount(2);
		buttonList.setIndex(0);

		preferencesService = new PreferencesService(getApplicationContext());
		preferencesService.saveIslauncher(true);

		getSkin();

		layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);

		intiView();

		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setLayoutParams(layoutParams);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageResource(R.drawable.sy_hot_1);
		picViews.add(imageView);

		ImageView imageView2 = new ImageView(getApplicationContext());
		imageView2.setLayoutParams(layoutParams);
		imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView2.setImageResource(R.drawable.sy_hot_2);
		picViews.add(imageView2);

		ImageView imageView3 = new ImageView(getApplicationContext());
		imageView3.setLayoutParams(layoutParams);
		imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView3.setImageResource(R.drawable.sy_hot_3);
		picViews.add(imageView3);

		ImageView imageView4 = new ImageView(getApplicationContext());
		imageView4.setLayoutParams(layoutParams);
		imageView4.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView4.setImageResource(R.drawable.sy_hot_4);
		picViews.add(imageView4);

		mDotList.setCount(4);
		mDotList.setIndex(0);

		myPicViewPagerAdapter = new MyViewPagerAdapter(picViews);
		main_viewpager_picture.setAdapter(myPicViewPagerAdapter);

		// queryData();
		//queryPicData();

	}

	private void getSkin() {
		String skin = (String) preferencesService.getSkin().get("skinName");
		if (skin == null) {
			skin = "0";
		}
		List<SkinInfo> skinInfos = DataUtils.getSkinInfos();
		sy_bg = R.drawable.sy_bj;
		for (SkinInfo skinInfo : skinInfos) {
			if (skin.equals(skinInfo.getSkinName())) {
				sy_bg = skinInfo.getMain_bj();
			}
		}
	}

	private void intiView() {
		// 设置皮肤
		ll_main = (LinearLayout) findViewById(R.id.ll_main);
		ll_main.setBackgroundResource(sy_bg);

		Button btnSelectCity = (Button) findViewById(R.id.btn_selectcity);
		btnSelectCity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(MainActivity.this,
				// HospitalListActivity.class);
				// startActivity(intent);
				// finish();
			}
		});

		LayoutInflater lf = LayoutInflater.from(this);

		// 上面图片滑动
		main_viewpager_picture = (ViewPager) findViewById(R.id.main_viewpager_picture);
		main_viewpager_picture
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub
						index = arg0;

						mDotList.setIndex(index);

						if (pic_title.size() > 0 && arg0 < pic_title.size()) {
							main_tv_pic.setText(pic_title.get(index));
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});
		main_viewpager_picture.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isTouch = true;
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					isTouch = false;
					break;
				default:
					break;
				}
				return false;
			}
		});

		// 下面列表滑动
		ViewPager main_viewpager_button = (ViewPager) findViewById(R.id.main_viewpager_button);
		main_viewpager_button
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub

						buttonList.setIndex(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});

		List<View> buttonViews = new ArrayList<View>();
		View main_viewpager_button_page1 = lf.inflate(
				R.layout.main_viewpager_button_page1, null);
		View main_viewpager_button_page2 = lf.inflate(
				R.layout.main_viewpager_button_page2, null);

		buttonViews.add(main_viewpager_button_page1);
		//buttonViews.add(main_viewpager_button_page2);

		MyViewPagerAdapter myBottonViewPagerAdapter = new MyViewPagerAdapter(
				buttonViews);
		main_viewpager_button.setAdapter(myBottonViewPagerAdapter);

		// 合同审批
		LinearLayout ll_contract_approve = (LinearLayout) main_viewpager_button_page1
				.findViewById(R.id.ll_contract_approve);
		ll_contract_approve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						HospitalDetailActivity.class);
				startActivity(intent);
			}
		});

		// 消息查看
		LinearLayout ll_message = (LinearLayout) main_viewpager_button_page1
				.findViewById(R.id.ll_message);
		ll_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						PhDeptInfoActivity.class);
				startActivity(intent);

			}
		});

		// 通讯录
		LinearLayout ll_address_list = (LinearLayout) main_viewpager_button_page1
				.findViewById(R.id.ll_address_list);
		ll_address_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						SbasContactActivity.class);
				startActivity(intent);

			}
		});

		// 系统设置
		LinearLayout ll_setting = (LinearLayout) main_viewpager_button_page1
				.findViewById(R.id.ll_setting);
		ll_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						StillMoreActivity.class);
				startActivity(intent);
			}
		});

		// 预约挂号
		LinearLayout ll_yygh = (LinearLayout) main_viewpager_button_page1
				.findViewById(R.id.ll_yygh);
		ll_yygh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		// 个人中心
		LinearLayout ll_grzx = (LinearLayout) main_viewpager_button_page2
				.findViewById(R.id.ll_grzx);
		ll_grzx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Config.phUsers == null) {

					Intent intent = new Intent(v.getContext(),
							LoginActivity.class);
					startActivity(intent);

				} else {

					if (preferencesService.getIsLogin() == false) {

						Intent intent = new Intent(v.getContext(),
								LoginActivity.class);
						startActivity(intent);
					} else {

						Intent intent = new Intent(v.getContext(),
								PersonalCenterActivity.class);
						startActivity(intent);
					}

				}

			}
		});

		// 健康咨询
		LinearLayout ll_jkzx = (LinearLayout) main_viewpager_button_page2
				.findViewById(R.id.ll_jkzx);
		ll_jkzx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						HealthInformationActivity.class);
				startActivity(intent);
			}
		});

		// 叫号查询
		LinearLayout ll_fsjz = (LinearLayout) main_viewpager_button_page1
				.findViewById(R.id.ll_fsjz);
		ll_fsjz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		// 回访信息
		LinearLayout ll_hf = (LinearLayout) main_viewpager_button_page2
				.findViewById(R.id.sy_hf);
		ll_hf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						BackVisitActivity.class);
				startActivity(intent);
			}
		});

		// 更多
		LinearLayout ll_gd = (LinearLayout) main_viewpager_button_page2
				.findViewById(R.id.ll_gd);
		ll_gd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						StillMoreActivity.class);
				startActivity(intent);
			}
		});

		// 诊疗信息
		LinearLayout ll_zl = (LinearLayout) main_viewpager_button_page2
				.findViewById(R.id.ll_zlxx);
		ll_zl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						ZlSearchActivity.class);
				startActivity(intent);
			}
		});

		// 设置
		Button btn_right = (Button) findViewById(R.id.btn_right);
		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(v.getContext(),
				// SettingActivity.class);
				// startActivity(intent);

			}
		});

		// 在线咨询
		LinearLayout online_chat = (LinearLayout) main_viewpager_button_page2
				.findViewById(R.id.online_zixun);
		online_chat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						DoctorChatActivity.class);
				startActivity(intent);
			}
		});

		LinearLayout main_ll_personal = (LinearLayout) findViewById(R.id.main_ll_personal);
		main_ll_personal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Config.phUsers == null) {

					Intent intent = new Intent(v.getContext(),
							LoginActivity.class);
					startActivity(intent);

				} else {

					if (preferencesService.getIsLogin() == false) {

						Intent intent = new Intent(v.getContext(),
								LoginActivity.class);
						startActivity(intent);
					} else {

						Intent intent = new Intent(v.getContext(),
								PersonalCenterActivity.class);
						startActivity(intent);
					}

				}

			}
		});

		main_tv_personal = (TextView) findViewById(R.id.main_tv_personal);
		// 检测更新
		handler.sendEmptyMessageDelayed(2, 1000);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// String skin = (String) preferencesService.getSkin().get("skinName");
		// Toast.makeText(getApplicationContext(), "skin=" + skin, 0).show();
		getSkin();
		ll_main.setBackgroundResource(sy_bg);
		if (Config.phUsers == null) {

			main_tv_personal.setText("请登录");

		} else {

			if (preferencesService.getIsLogin() == false) {

				main_tv_personal.setText("请登录");
			} else {

				main_tv_personal.setText(Config.phUsers.getName());
			}

		}

	}

	private void queryData() {

		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);

		AllHospitalInfoReq req = new AllHospitalInfoReq();
		req.setOperType("1");
		GsonServlet<AllHospitalInfoReq, AllHospitalInfoRes> gServlet = new GsonServlet<AllHospitalInfoReq, AllHospitalInfoRes>(
				this);
		gServlet.request(req, AllHospitalInfoRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<AllHospitalInfoReq, AllHospitalInfoRes>() {

			@Override
			public void onResponseEnd(AllHospitalInfoReq commonReq,
					AllHospitalInfoRes commonRes, boolean result,
					String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(AllHospitalInfoReq commonReq,
					AllHospitalInfoRes commonRes, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {

					if (commonRes.getAllInfos().size() >= 1) {

						BasiHosInfo basiHosInfo = commonRes.getAllInfos()
								.get(0);

						if (basiHosInfo != null) {

							Config.hosId = basiHosInfo.getId();
							Config.hospital_name = basiHosInfo.getHosName();
							main_tv_pic.setText(Config.hospital_name);
						}
					}
				}
			}

			@Override
			public void onResponseEndErr(AllHospitalInfoReq commonReq,
					AllHospitalInfoRes commonRes, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});

	}

	/**
	 * 热图
	 */
	private void queryPicData() {

		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);
		PhHotPicReq req = new PhHotPicReq();
		req.setOperType("3");
		req.setHosID(Config.hosId);

		GsonServlet<PhHotPicReq, PhHotPicRes> gServlet = new GsonServlet<PhHotPicReq, PhHotPicRes>(
				this);
		gServlet.request(req, PhHotPicRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<PhHotPicReq, PhHotPicRes>() {

			@Override
			public void onResponseEnd(PhHotPicReq commonReq,
					PhHotPicRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(PhHotPicReq commonReq,
					PhHotPicRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {

					// if (isHaveSdCard) {
					// picViews.clear();
					// }
					picName.clear();
					pic_title.clear();
					pic_download_url.clear();

					for (int i = 0; i < commonRes.getPhHotPics().size(); i++) {

						PhHotPic phHotPic = commonRes.getPhHotPics().get(i);
						final String pic_name = phHotPic.getPic_Path()
								.replaceAll("upload/hotpic/", "");
						// + FileUtil.choosePic(phHotPic.getPic_Path());

						final String pic_down_url = "http://" + Config.post
								+ File.separator + "PalmHospital"
								+ File.separator + phHotPic.getPic_Path();
						// final String pic_down_url = "http://" + Config.post
						// + File.separator + "PH" + File.separator
						// + phHotPic.getPic_Path();

						pic_download_url.add(pic_down_url);

						// if (isHaveSdCard) {
						//
						// new Thread(new Runnable() {
						// public void run() {
						//
						// if (HttpUtil
						// .checkNet(getApplicationContext())) {
						//
						// HttpUtil.createPic_new(phone_path,
						// pic_down_url, pic_name);
						//
						// }
						// }
						//
						// }).start();
						//
						// }

						pic_title.add(phHotPic.getPic_Name());
						picName.add(pic_name);

						// Log.v("tags", "pic_down_url=" + pic_down_url);
						// Log.v("tags", "pic_name=" + pic_name);
						// Log.v("tags", "phone_path=" + phone_path);

					}

					if (HttpUtil.checkNet(getApplicationContext())) {

						new Thread(new Runnable() {
							public void run() {

								for (String url : pic_download_url) {

									if (bitmap_web_list != null) {
										bitmap_web_list.add(HttpUtil
												.readPic(url));
									}
								}

								handler.sendEmptyMessage(1);

							}

						}).start();
					}

					// if (isHaveSdCard) {
					//
					// new Thread(new Runnable() {
					//
					// @Override
					// public void run() {
					// try {
					// Thread.sleep(30 * 1000);
					// } catch (InterruptedException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					//
					// handler.sendEmptyMessage(0);
					//
					// }
					// }).start();
					//
					// }

				} else {
					Toast.makeText(getApplicationContext(), "没有该医院的信息！",
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onResponseEndErr(PhHotPicReq commonReq,
					PhHotPicRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setTitle("是否要退出掌上医院？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();

						}
					});

			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}

					});
			builder.create().show();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean isEnd;
	private boolean isExit = false;
	private boolean isTouch;
	private Thread thread = new Thread() {

		public void run() {
			try {
				while (true) {
					if (isExit) {
						break;
					}
					if (!isTouch) {
						Thread.sleep(2000);
						Message msg = new Message();
						msg.arg1 = index;
						msg.what = 3;
						handler.sendMessage(msg);

						index++;
						if (index > picViews.size() - 1) {
							index = 0;
						}
						// if (isEnd) {
						// index--;
						// if (index < 0) {
						// index = 0;
						// isEnd = false;
						// }
						// } else {
						// index++;
						// if (index > picViews.size() - 1) {
						// index = 0;
						// isEnd = true;
						// }
						// }
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		};
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		// Config.phUsers = null;
		isExit = true;
		preferencesService.clearLogin();

		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}

		super.onDestroy();
		System.gc();
	}
}
