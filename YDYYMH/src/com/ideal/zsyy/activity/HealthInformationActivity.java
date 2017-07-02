package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ideal.wdm.tools.DataCache;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.HealthInformationAdapter;
import com.ideal.zsyy.entity.JKXJLBInfo;
import com.ideal.zsyy.entity.PhJkxjInfo;
import com.ideal.zsyy.entity.PhJkxjPicInfo;
import com.ideal.zsyy.request.JKXJLBReq;
import com.ideal.zsyy.request.PhJkxjReq;
import com.ideal.zsyy.response.JKXJLBRes;
import com.ideal.zsyy.response.PhJkxjRes;
import com.ideal.zsyy.view.PullDownListView;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

/**
 * 健康资讯
 * 
 * @author  PYM
 * 
 */
public class HealthInformationActivity extends Activity implements
		PullDownListView.OnRefreshListioner {

	private Button btn_back;
	private AlertDialog alert;
	private TextView tv_btn_type;

	private LinearLayout login_dialog_list;
	private ListView login_dialog_listview;
	private List<Map<String, String>> product_type_list;

	private HealthInformationAdapter healthInformationAdapter;

	private PullDownListView mPullDownView;
	private ListView mListView;
	// private List<String> list1 = new ArrayList<String>();
	// private MyAdapter adapter;
	private Handler mHandler = new Handler();
	// private int maxAount = 10 * 1000;// 设置了最大数据值

	private String product_type_id = "";
	private String product_type_name = "最新";

	private String pageSize = "20";
	private int pageIndex = 0;

	private List<PhJkxjInfo> phJkxjInfos = new ArrayList<PhJkxjInfo>();

	public static PhJkxjInfo phJkxjInfo;

	private String yinpinORshipin = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_information);

		// list = new ArrayList<HealthInformation>();
		product_type_list = new ArrayList<Map<String, String>>();

		initView();

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("product_type_id", "");
		// map.put("product_type_name", "全部");
		// product_type_list.add(map);
		//
		// Map<String, String> map1 = new HashMap<String, String>();
		// map1.put("product_type_id", "1");
		// map1.put("product_type_name", "图文");
		// product_type_list.add(map1);
		//
		// Map<String, String> map2 = new HashMap<String, String>();
		// map2.put("product_type_id", "2");
		// map2.put("product_type_name", "视频");
		// product_type_list.add(map2);
		//
		// SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
		// product_type_list, R.layout.login_dialog_window,
		// new String[] { "product_type_name" },
		// new int[] { R.id.login_dialog_item, });
		// login_dialog_listview.setAdapter(adapter);
		// AlertDialog.Builder radio = new AlertDialog.Builder(this);
		// radio.setTitle("请选择类型");
		// radio.setView(login_dialog_list);
		// alert = radio.create();

		queryProductTypeData();
		queryInformationData(product_type_id, product_type_name, true,
				yinpinORshipin);

	}

	/**
	 * 刷新，先清空list中数据然后重新加载更新内容
	 */
	public void onRefresh() {

		mHandler.postDelayed(new Runnable() {

			public void run() {

				pageIndex = 0;
				phJkxjInfos.clear();
				queryInformationData(product_type_id, product_type_name, true,
						yinpinORshipin);
				// list1.clear();
				// addLists(10);
				mPullDownView.onRefreshComplete();// 这里表示刷新处理完成后把上面的加载刷新界面隐藏
				mPullDownView.setMore(true);// 这里设置true表示还有更多加载，设置为false底部将不显示更多
				if (healthInformationAdapter != null) {
					healthInformationAdapter.notifyDataSetChanged();
				}

			}
		}, 1500);

	}

	/**
	 * 加载更多，在原来基础上在添加新内容
	 */
	public void onLoadMore() {

		mHandler.postDelayed(new Runnable() {
			public void run() {

				pageIndex++;
				queryInformationData(product_type_id, product_type_name, false,
						yinpinORshipin);
				mPullDownView.onLoadMoreComplete();// 这里表示加载更多处理完成后把下面的加载更多界面（隐藏或者设置字样更多）
				// if(list1.size()<maxAount)//判断当前list中已添加的数据是否小于最大值maxAount，是那么久显示更多否则不显示
				// mPullDownView.setMore(true);//这里设置true表示还有更多加载，设置为false底部将不显示更多
				// else
				// mPullDownView.setMore(false);
				if (healthInformationAdapter != null) {
					healthInformationAdapter.notifyDataSetChanged();
				}

			}
		}, 1500);
	}

	private LinearLayout health_ll_title_img;
	private LinearLayout health_ll_title_vedio;

	// 初始化视图
	private void initView() {

		btn_back = (Button) findViewById(R.id.btn_back);
		// lv_health = (ListView) findViewById(R.id.lv_health);
		tv_btn_type = (TextView) findViewById(R.id.tv_btn_type);

		mPullDownView = (PullDownListView) findViewById(R.id.sreach_list);
		mPullDownView.setRefreshListioner(this);
		mListView = mPullDownView.mListView;
		mPullDownView.setMore(true);// 这里设置true表示还有更多加载，设置为false底部将不显示更多

		final ImageView health_im_title_img = (ImageView) findViewById(R.id.health_im_title_img);

		final ImageView health_im_title_vedio = (ImageView) findViewById(R.id.health_im_title_vedio);

		health_ll_title_img = (LinearLayout) findViewById(R.id.health_ll_title_img);
		health_ll_title_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				pageIndex = 0;
				mPullDownView.setMore(true);

				health_im_title_img.setVisibility(View.VISIBLE);
				health_im_title_vedio.setVisibility(View.INVISIBLE);

				yinpinORshipin = "1";
				queryInformationData(product_type_id, product_type_name, true,
						yinpinORshipin);

			}
		});

		health_ll_title_vedio = (LinearLayout) findViewById(R.id.health_ll_title_vedio);
		health_ll_title_vedio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				pageIndex = 0;
				mPullDownView.setMore(true);

				health_im_title_img.setVisibility(View.INVISIBLE);
				health_im_title_vedio.setVisibility(View.VISIBLE);

				yinpinORshipin = "2";
				queryInformationData(product_type_id, product_type_name, true,
						yinpinORshipin);

			}
		});

		// adapter = new MyAdapter(this,list1);
		// mListView.setAdapter(adapter);

		setListener();
	}

	// 设置监听
	private void setListener() {
		// TODO Auto-generated method stub
		// 返回监听
		btn_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		login_dialog_list = (LinearLayout) inflater.inflate(
				R.layout.login_dialog_list, null);

		login_dialog_listview = (ListView) login_dialog_list
				.findViewById(R.id.login_dialog_listview);
		login_dialog_listview.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.FILL_PARENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
		login_dialog_listview.setOnItemClickListener(new OnItemClickListener() {

			private Map<String, String> map;

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				if (alert != null) {

					alert.dismiss();
				}

				pageIndex = 0;
				mPullDownView.setMore(true);

				map = product_type_list.get(position);
				String type = map.get("product_type_id");
				String type_name = map.get("product_type_name");

				// yinpinORshipin = type;

				queryInformationData(type, type_name, true, yinpinORshipin);

			}
		});

		LinearLayout ll_btn_type = (LinearLayout) findViewById(R.id.ll_btn_type);
		ll_btn_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (alert != null) {

					alert.show();
				}

			}
		});

		// listview条目监听
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				// HealthInformation healthInformation = list.get(position);

				if (position >= 1) {

					phJkxjInfo = phJkxjInfos.get(position - 1);

					if (phJkxjInfo != null) {

						Intent intent = new Intent(view.getContext(),
								HealthInfoDetailActivity.class);

						// intent.putExtra("type", phJkxjInfo.getType());
						// intent.putExtra("title", phJkxjInfo.getTitle());
						// intent.putExtra("time", "");
						// intent.putExtra("image_id", "");
						// intent.putExtra("information_detail_introduction",
						// phJkxjInfo.getMemo());
						startActivity(intent);

					}
				}

			}
		});
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			String[] a = (String[]) v.getTag();

			pageIndex = 0;
			mPullDownView.setMore(true);
			queryInformationData(a[0], a[1], true, yinpinORshipin);

			for (ImageView imageView : imageViews) {

				imageView.setVisibility(View.GONE);
			}

			LinearLayout layout = (LinearLayout) v.findViewById(v.getId());
			ImageView imageView = (ImageView) layout.getChildAt(1);
			if (imageView != null) {
				imageView.setVisibility(View.VISIBLE);
			}

		}
	};

	private List<ImageView> imageViews = new ArrayList<ImageView>();

	// 分类
	private void queryProductTypeData() {

		final LinearLayout health_ll_image = (LinearLayout) findViewById(R.id.health_ll_image);
		health_ll_image.setOrientation(LinearLayout.HORIZONTAL);

		// final LinearLayout.LayoutParams layoutParams = new
		// LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);

		DataCache datacache = DataCache.getCache(getApplicationContext());
		datacache.setUrl(Config.url);

		JKXJLBReq req = new JKXJLBReq();
//		req.setOperType("38");
		req.setOperType("37");

		GsonServlet<JKXJLBReq, JKXJLBRes> gServlet = new GsonServlet<JKXJLBReq, JKXJLBRes>(
				HealthInformationActivity.this);
		gServlet.request(req, JKXJLBRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<JKXJLBReq, JKXJLBRes>() {

			@Override
			public void onResponseEnd(JKXJLBReq commonReq, JKXJLBRes commonRes,
					boolean result, String errmsg, int responseCode) {

				if (commonRes != null) {

					List<JKXJLBInfo> jkxjlbinfos = commonRes.getJkxjlbinfos();

					Map<String, String> map = new HashMap<String, String>();
					map.put("product_type_id", "");
					map.put("product_type_name", "最新");
					product_type_list.add(map);

					for (JKXJLBInfo jkxjlbInfo : jkxjlbinfos) {

						Map<String, String> map2 = new HashMap<String, String>();
						map2.put("product_type_id", jkxjlbInfo.getItem_key());
						map2.put("product_type_name",
								jkxjlbInfo.getItem_value());
						product_type_list.add(map2);

					}

					SimpleAdapter adapter = new SimpleAdapter(
							getApplicationContext(), product_type_list,
							R.layout.login_dialog_window,
							new String[] { "product_type_name" },
							new int[] { R.id.login_dialog_item, });
					login_dialog_listview.setAdapter(adapter);
					AlertDialog.Builder radio = new AlertDialog.Builder(
							HealthInformationActivity.this);
					radio.setTitle("请选择类型");
					radio.setView(login_dialog_list);
					alert = radio.create();

					// LinearLayout linearLayout = new LinearLayout(
					// getApplicationContext());
					// linearLayout.setLayoutParams(layoutParams);
					// linearLayout.setOrientation(LinearLayout.VERTICAL);
					// String[] type = new String[2];
					// type[0] = "";
					// type[1] = "最新";
					// linearLayout.setTag(type);
					// linearLayout.setId(0);
					// linearLayout.setOnClickListener(clickListener);
					//
					// TextView textView = new
					// TextView(getApplicationContext());
					// textView.setLayoutParams(layoutParams);
					// textView.setText("最新");
					// textView.setPadding(20, 0, 20, 0);
					// textView.setTextColor(Color.BLACK);
					// textView.setTextSize(16);
					// linearLayout.addView(textView);
					//
					// ImageView imageView = new
					// ImageView(getApplicationContext());
					// LinearLayout.LayoutParams layoutParams2 = new
					// LinearLayout.LayoutParams(
					// LinearLayout.LayoutParams.FILL_PARENT,
					// LinearLayout.LayoutParams.WRAP_CONTENT);
					// layoutParams2.gravity = Gravity.CENTER_HORIZONTAL;
					// imageView.setLayoutParams(layoutParams2);
					// imageView
					// .setImageResource(R.drawable.health_title_xiahuaxian);
					// linearLayout.addView(imageView);
					// health_ll_image.addView(linearLayout);
					// imageViews.add(imageView);
					//
					// List<JKXJLBInfo> jkxjlbinfos =
					// commonRes.getJkxjlbinfos();
					//
					// int i = 1;
					// for (JKXJLBInfo jkxjlbInfo : jkxjlbinfos) {
					//
					// LinearLayout linearLayout1 = new LinearLayout(
					// getApplicationContext());
					// linearLayout1.setLayoutParams(layoutParams);
					// linearLayout1.setOrientation(LinearLayout.VERTICAL);
					// String[] type2 = new String[2];
					// type2[0] = jkxjlbInfo.getItem_key();
					// type2[1] = jkxjlbInfo.getItem_value();
					// linearLayout1.setTag(type2);
					// linearLayout1.setId(i);
					// linearLayout1.setOnClickListener(clickListener);
					//
					// TextView textView2 = new TextView(
					// getApplicationContext());
					// textView2.setText(jkxjlbInfo.getItem_value());
					// textView2.setPadding(10, 0, 10, 0);
					// textView2.setTextColor(Color.BLACK);
					// textView2.setTextSize(16);
					// linearLayout1.addView(textView2);
					//
					// ImageView imageView1 = new ImageView(
					// getApplicationContext());
					// imageView1.setVisibility(View.GONE);
					// LinearLayout.LayoutParams layoutParams21 = new
					// LinearLayout.LayoutParams(
					// LinearLayout.LayoutParams.FILL_PARENT,
					// LinearLayout.LayoutParams.WRAP_CONTENT);
					// layoutParams21.gravity = Gravity.CENTER_HORIZONTAL;
					// imageView1.setLayoutParams(layoutParams21);
					// imageView1
					// .setImageResource(R.drawable.health_title_xiahuaxian);
					// linearLayout1.addView(imageView1);
					// imageViews.add(imageView1);
					//
					// health_ll_image.addView(linearLayout1);
					//
					// i++;
					// }

				}

			}

			@Override
			public void onResponseEndSuccess(JKXJLBReq commonReq,
					JKXJLBRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndErr(JKXJLBReq commonReq,
					JKXJLBRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});
	}

	// 得到数据
	private void queryInformationData(final String type,
			final String the_product_type_name, final boolean isClean,
			final String yinpinORshipin) {

		DataCache datacache = DataCache.getCache(getApplicationContext());
		datacache.setUrl(Config.url);

		PhJkxjReq req = new PhJkxjReq();
//		req.setOperType("37");
		req.setOperType("36");
		// req.setHosp_id(Config.hosId);
		req.setType(type);
		req.setPageSize(pageSize);
		req.setPageIndex("" + pageIndex);

		GsonServlet<PhJkxjReq, PhJkxjRes> gServlet = new GsonServlet<PhJkxjReq, PhJkxjRes>(
				HealthInformationActivity.this);
		gServlet.request(req, PhJkxjRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<PhJkxjReq, PhJkxjRes>() {

			@Override
			public void onResponseEnd(PhJkxjReq commonReq, PhJkxjRes commonRes,
					boolean result, String errmsg, int responseCode) {

				if (isClean) {

					phJkxjInfos.clear();
				}

				if (commonRes != null) {

					product_type_id = type;
					product_type_name = the_product_type_name;
					tv_btn_type.setText(product_type_name);

					if (commonRes.getPhJkxjInfos().size() > 0) {

						List<PhJkxjInfo> web_phJkxjInfos = commonRes
								.getPhJkxjInfos();

						for (int i = 0; i < web_phJkxjInfos.size(); i++) {

							PhJkxjInfo my_phJkxjInfo = web_phJkxjInfos.get(i);
							my_phJkxjInfo.setVedio(false);

							List<PhJkxjPicInfo> my_jkxjpicinfo = my_phJkxjInfo
									.getJkxjpicinfo();

							for (int j = 0; j < my_jkxjpicinfo.size(); j++) {

								String type = my_jkxjpicinfo.get(j).getType();

								if (type.equals("2")) {

									my_phJkxjInfo.setVedio(true);
									break;
								}

							}

							phJkxjInfos.add(my_phJkxjInfo);
						}

						if (yinpinORshipin.equals("1")) {

							List<PhJkxjInfo> a_list = new ArrayList<PhJkxjInfo>();

							for (int i = 0; i < phJkxjInfos.size(); i++) {

								if (!phJkxjInfos.get(i).isVedio()) {

									a_list.add(phJkxjInfos.get(i));

								}

							}

							phJkxjInfos = a_list;

						} else if (yinpinORshipin.equals("2")) {

							List<PhJkxjInfo> a_list = new ArrayList<PhJkxjInfo>();

							for (int i = 0; i < phJkxjInfos.size(); i++) {

								if (phJkxjInfos.get(i).isVedio()) {

									a_list.add(phJkxjInfos.get(i));

								}

							}

							phJkxjInfos = a_list;

						}

					} else {

						mPullDownView.setMore(false);
						pageIndex--;
					}
				} else {

					mPullDownView.setMore(false);
					pageIndex--;
				}

				healthInformationAdapter = new HealthInformationAdapter(
						getApplicationContext(), phJkxjInfos);
				mListView.setAdapter(healthInformationAdapter);

			}

			@Override
			public void onResponseEndSuccess(PhJkxjReq commonReq,
					PhJkxjRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndErr(PhJkxjReq commonReq,
					PhJkxjRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});

	}
}
