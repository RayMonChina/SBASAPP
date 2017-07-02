package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ideal.wdm.tools.DataCache;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.base.BuildingInfo;
import com.ideal.zsyy.entity.HosBuildInfo;
import com.ideal.zsyy.request.BuildReq;
import com.ideal.zsyy.response.BuildRes;
import com.ideal.zsyy.utils.DensityUtil;
import com.ideal.zsyy.utils.FileUtil;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HospitalNavigationActivity extends Activity implements
		OnTouchListener, OnGestureListener {

	private LinearLayout hn_building;
	private ArrayList<BuildingInfo> building_Infos_n = new ArrayList<BuildingInfo>();
	private ArrayList<BuildingInfo> building_Infos_s = new ArrayList<BuildingInfo>();
	private int isClickFlag = 0;
	private GestureDetector gestureDetector;
	private Matrix matrix;
	private static final float IMAGE_TRIGGER_SPACE = 30f;
	private String imagePoints_n[][];
	private String imagePoints_s[][];
	private LinearLayout hn_ll_all_content;
	private float currentScale = 1;
	private ImageView im_pic;

	private Bitmap bitmap;
	private boolean isLeft = false; // 是否是向左滑
	private boolean isSHosp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_navigation);

		gestureDetector = new GestureDetector(getApplicationContext(), this);
		matrix = new Matrix();
		// matrix.postScale(3, 3);

		imagePoints_n = new String[11][5];
		imagePoints_s = new String[15][5];

		initView();

		setImagePM();

		// addBuildingList();
		// init_Building(building_Infos);

		queryData();

	}

	private void setImagePM() {

		imagePoints_n[0][0] = "388";
		imagePoints_n[0][1] = "74";

		imagePoints_n[1][0] = "261";
		imagePoints_n[1][1] = "78";

		imagePoints_n[2][0] = "197";
		imagePoints_n[2][1] = "113";

		imagePoints_n[3][0] = "21";
		imagePoints_n[3][1] = "166";

		imagePoints_n[4][0] = "109";
		imagePoints_n[4][1] = "215";

		imagePoints_n[5][0] = "102";
		imagePoints_n[5][1] = "306";

		imagePoints_n[6][0] = "376";
		imagePoints_n[6][1] = "194";

		imagePoints_n[7][0] = "240";
		imagePoints_n[7][1] = "200";

		imagePoints_n[8][0] = "325";
		imagePoints_n[8][1] = "180";

		imagePoints_n[9][0] = "340";
		imagePoints_n[9][1] = "135";

		imagePoints_n[10][0] = "0";
		imagePoints_n[10][1] = "0";

	}

	private void setImageSJ() {

		imagePoints_n[0][0] = "436";
		imagePoints_n[0][1] = "106";

		imagePoints_n[1][0] = "333";
		imagePoints_n[1][1] = "33";

		imagePoints_n[2][0] = "209";
		imagePoints_n[2][1] = "94";

		imagePoints_n[3][0] = "36";
		imagePoints_n[3][1] = "155";

		imagePoints_n[4][0] = "110";
		imagePoints_n[4][1] = "160";

		imagePoints_n[5][0] = "55";
		imagePoints_n[5][1] = "235";

		imagePoints_n[6][0] = "411";
		imagePoints_n[6][1] = "160";

		imagePoints_n[7][0] = "285";
		imagePoints_n[7][1] = "198";

		imagePoints_n[8][0] = "367";
		imagePoints_n[8][1] = "180";

		imagePoints_n[9][0] = "332";
		imagePoints_n[9][1] = "170";

		imagePoints_n[10][0] = "0";
		imagePoints_n[10][1] = "0";

	}

	private void setImageSSJ() {

		imagePoints_s[0][0] = "121";
		imagePoints_s[0][1] = "36";

		imagePoints_s[1][0] = "60";
		imagePoints_s[1][1] = "95";

		imagePoints_s[2][0] = "175";
		imagePoints_s[2][1] = "95";

		imagePoints_s[3][0] = "35";
		imagePoints_s[3][1] = "160";

		imagePoints_s[4][0] = "90";
		imagePoints_s[4][1] = "160";

		imagePoints_s[5][0] = "150";
		imagePoints_s[5][1] = "160";

		imagePoints_s[6][0] = "200";
		imagePoints_s[6][1] = "160";

		imagePoints_s[7][0] = "125";
		imagePoints_s[7][1] = "220";

		imagePoints_s[8][0] = "35";
		imagePoints_s[8][1] = "260";

		imagePoints_s[9][0] = "90";
		imagePoints_s[9][1] = "260";

		imagePoints_s[10][0] = "150";
		imagePoints_s[10][1] = "260";

		imagePoints_s[11][0] = "200";
		imagePoints_s[11][1] = "260";

		imagePoints_s[12][0] = "385";
		imagePoints_s[12][1] = "230";

		imagePoints_s[13][0] = "385";
		imagePoints_s[13][1] = "130";

		imagePoints_s[14][0] = "385";
		imagePoints_s[14][1] = "60";

	}

	private void addBuildingList() {

		// BuildingInfo buildingInfo1 = new BuildingInfo("", "1", "医疗保健中心");
		// building_Infos.add(buildingInfo1);
		//
		// BuildingInfo buildingInfo2 = new BuildingInfo("", "2", "住院大楼");
		// building_Infos.add(buildingInfo2);
		//
		// BuildingInfo buildingInfo3 = new BuildingInfo("", "3", "医技大楼");
		// building_Infos.add(buildingInfo3);
		//
		// BuildingInfo buildingInfo4 = new BuildingInfo("", "4", "门诊副楼");
		// building_Infos.add(buildingInfo4);
		//
		// BuildingInfo buildingInfo5 = new BuildingInfo("", "5", "门急诊大楼");
		// building_Infos.add(buildingInfo5);
		//
		// BuildingInfo buildingInfo6 = new BuildingInfo("", "6", "行政楼");
		// building_Infos.add(buildingInfo6);
		//
		// BuildingInfo buildingInfo7 = new BuildingInfo("", "7", "硕士楼");
		// building_Infos.add(buildingInfo7);
		//
		// BuildingInfo buildingInfo8 = new BuildingInfo("", "8", "眼科中心");
		// building_Infos.add(buildingInfo8);
		//
		// BuildingInfo buildingInfo9 = new BuildingInfo("", "9", "博士楼");
		// building_Infos.add(buildingInfo9);
		//
		// BuildingInfo buildingInfo10 = new BuildingInfo("", "10", "体检部");
		// building_Infos.add(buildingInfo10);
		//
		// for (int i = 0; i < building_Infos.size(); i++) {
		//
		// imagePoints[i][2] = building_Infos.get(i).getBuild_no();
		// imagePoints[i][3] = building_Infos.get(i).getBuild_name();
		// imagePoints[i][4] = building_Infos.get(i).getId();
		//
		// }

	}

	private void initView() {

		hn_ll_all_content = (LinearLayout) findViewById(R.id.hn_ll_all_content);

		final TextView hn_title = (TextView) findViewById(R.id.hn_title);

		im_pic = (ImageView) this.getWindow().findViewById(R.id.im_pic);
		im_pic.setOnTouchListener(this);

		// audios/spot/197/audio.mp3
		bitmap = FileUtil.findBitmap(getAssets(), "img/hn_pmt.png");
		im_pic.setImageBitmap(bitmap);

		DisplayMetrics dm = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(dm);

		float bitmap_width = bitmap.getWidth();

		float screenWidth = dm.widthPixels;

		float scale = screenWidth / bitmap_width;

		currentScale = scale;

		// Toast.makeText(getApplicationContext(), "scale=" + scale, 0).show();

		matrix.postScale(scale, scale);
		im_pic.setImageMatrix(matrix);

		Button hn_right_button = (Button) findViewById(R.id.hn_right_button);
		hn_right_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isClickFlag == 0) {

					hn_title.setText("实景图");
					im_pic.startAnimation(AnimationUtils.loadAnimation(
							v.getContext(), R.anim.slide_left_in));
					bitmap = FileUtil.findBitmap(getAssets(), "img/hn_stt.png");
					im_pic.setImageBitmap(bitmap);
					setImageSJ();
					isClickFlag = 1;
				} else if (isClickFlag == 1) {

					hn_title.setText("平面图");
					im_pic.startAnimation(AnimationUtils.loadAnimation(
							v.getContext(), R.anim.slide_right_in));
					bitmap = FileUtil.findBitmap(getAssets(), "img/hn_pmt.png");
					im_pic.setImageBitmap(bitmap);
					setImagePM();
					isClickFlag = 0;
				}

			}
		});

		hn_building = (LinearLayout) findViewById(R.id.hn_building);

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		final Button rl_bt_sh = (Button) findViewById(R.id.rl_bt_sh);
		final Button rl_bt_yx = (Button) findViewById(R.id.rl_bt_yx);
		final Button rl_bt_ssh = (Button) findViewById(R.id.rl_bt_ssh);

		rl_bt_sh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				hn_title.setText("北院平面图");
				hn_ll_all_content.startAnimation(AnimationUtils.loadAnimation(
						v.getContext(), R.anim.slide_right_in));
				isLeft = false;
				isSHosp = false;
				bitmap = FileUtil.findBitmap(getAssets(), "img/hn_pmt.png");
				im_pic.setImageBitmap(bitmap);
				setImagePM();

				rl_bt_sh.setBackgroundResource(R.drawable.navigation_tab_left_down);
				rl_bt_sh.setTextColor(Color.parseColor("#FFFFFF"));

				rl_bt_yx.setBackgroundResource(R.drawable.middlenoselect);
				rl_bt_yx.setTextColor(Color.parseColor("#0079ff"));

				rl_bt_ssh
						.setBackgroundResource(R.drawable.navigation_tab_right_up);
				rl_bt_ssh.setTextColor(Color.parseColor("#0079ff"));
				init_Building(building_Infos_n);
			}
		});

		rl_bt_yx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				hn_title.setText("北院实体图");
				if (!isLeft) {
					hn_ll_all_content.startAnimation(AnimationUtils
							.loadAnimation(v.getContext(), R.anim.slide_left_in));
				} else {
					isLeft = false;
					isSHosp = false;
					hn_ll_all_content.startAnimation(AnimationUtils
							.loadAnimation(v.getContext(),
									R.anim.slide_right_in));
				}
				// setAnimation(true, 0, 1, 500)
				bitmap = FileUtil.findBitmap(getAssets(), "img/hn_stt.png");
				im_pic.setImageBitmap(bitmap);
				setImageSJ();

				rl_bt_sh.setBackgroundResource(R.drawable.navigation_tab_left_up);
				rl_bt_sh.setTextColor(Color.parseColor("#0079ff"));

				rl_bt_yx.setBackgroundResource(R.drawable.middleselect);
				rl_bt_yx.setTextColor(Color.parseColor("#FFFFFF"));

				rl_bt_ssh
						.setBackgroundResource(R.drawable.navigation_tab_right_up);
				rl_bt_ssh.setTextColor(Color.parseColor("#0079ff"));
				init_Building(building_Infos_n);
			}
		});
		rl_bt_ssh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				hn_title.setText("南院平面图");
				hn_ll_all_content.startAnimation(AnimationUtils.loadAnimation(
						v.getContext(), R.anim.slide_left_in));
				// setAnimation(true, 0, 1, 500)
				bitmap = FileUtil.findBitmap(getAssets(), "img/hn_pmt_s.png");
				im_pic.setImageBitmap(bitmap);
				 setImageSSJ();
				isLeft = true;
				isSHosp = true;
				rl_bt_sh.setBackgroundResource(R.drawable.navigation_tab_left_up);
				rl_bt_sh.setTextColor(Color.parseColor("#0079ff"));

				rl_bt_yx.setBackgroundResource(R.drawable.middlenoselect);
				rl_bt_yx.setTextColor(Color.parseColor("#0079ff"));

				rl_bt_ssh
						.setBackgroundResource(R.drawable.navigation_tab_right_down);
				rl_bt_ssh.setTextColor(Color.parseColor("#FFFFFF"));
				init_Building(building_Infos_s);
			}
		});

	}

	private AnimationSet setAnimation(boolean shareInterpolator,
			float fromAlpha, float toAlpha, long duration) {

		AnimationSet animationSet = new AnimationSet(shareInterpolator);
		AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
		alphaAnimation.setDuration(duration);
		animationSet.addAnimation(alphaAnimation);
		animationSet.setFillAfter(true);
		return animationSet;
	}

	private void queryData() {

		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);

		BuildReq req = new BuildReq();
		req.setOperType("8");
		req.setHosId(Config.hosId);

		GsonServlet<BuildReq, BuildRes> gServlet = new GsonServlet<BuildReq, BuildRes>(
				this);
		gServlet.request(req, BuildRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<BuildReq, BuildRes>() {

			@Override
			public void onResponseEnd(BuildReq commonReq, BuildRes commonRes,
					boolean result, String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}

			@SuppressWarnings("unchecked")
			@Override
			public void onResponseEndSuccess(BuildReq commonReq,
					BuildRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					for (HosBuildInfo hosBuildInfo : commonRes
							.getHosBuildInfos()) {
						String build_no = hosBuildInfo.getDeptBuilNo();
						int build_no_int = Integer.valueOf(build_no);
						if (hosBuildInfo.getDeptLocate().trim().equals("N")) {

							if (build_no_int <= imagePoints_n.length) {

								if (build_no_int > 0) {

									if (imagePoints_n[build_no_int - 1][2] == null) {
										String build_name = hosBuildInfo
												.getDeptBuilName();
										if (build_name.trim().equals("门急诊大楼1")) {

											build_name = "门急诊大楼";
										} else if (build_name.trim().equals(
												"门急诊大楼2")) {

											build_name = "门急诊大楼";
										}

										BuildingInfo buildingInfo = new BuildingInfo(
												hosBuildInfo.getId(), build_no,
												build_name);
										building_Infos_n.add(buildingInfo);

										imagePoints_n[build_no_int - 1][2] = hosBuildInfo
												.getDeptBuilNo();
										imagePoints_n[build_no_int - 1][3] = hosBuildInfo
												.getDeptBuilName();
										imagePoints_n[build_no_int - 1][4] = hosBuildInfo
												.getId();
									}
								}
							}
						} else if (hosBuildInfo.getDeptLocate().trim()
								.equals("S")) {
							if (build_no_int <= imagePoints_s.length) {

								if (build_no_int > 0) {

									if (imagePoints_s[build_no_int - 1][2] == null) {
										String build_name = hosBuildInfo
												.getDeptBuilName();
										BuildingInfo buildingInfo = new BuildingInfo(
												hosBuildInfo.getId(), build_no,
												build_name);
										building_Infos_s.add(buildingInfo);

										imagePoints_s[build_no_int - 1][2] = hosBuildInfo
												.getDeptBuilNo();
										imagePoints_s[build_no_int - 1][3] = hosBuildInfo
												.getDeptBuilName();
										imagePoints_s[build_no_int - 1][4] = hosBuildInfo
												.getId();
									}
								}
							}
						}
					}
//					building_Infos.clear();
//
//					for (int i = 0; i < commonRes.getHosBuildInfos().size(); i++) {
//
//						if (i < imagePoints.length) {
//
//							HosBuildInfo hosBuildInfo = commonRes
//									.getHosBuildInfos().get(i);
//
//							String build_no = hosBuildInfo.getDeptBuilNo();
//
//							int build_no_int = Integer.valueOf(build_no);
//
//							if (build_no_int <= imagePoints.length) {
//
//								if (build_no_int > 0) {
//
//									if (imagePoints[build_no_int - 1][2] == null) {
//
//										String build_name = hosBuildInfo
//												.getDeptBuilName();
//										if (build_name.trim().equals("门急诊大楼1")) {
//
//											build_name = "门急诊大楼";
//										} else if (build_name.trim().equals(
//												"门急诊大楼2")) {
//
//											build_name = "门急诊大楼";
//										}
//
//										BuildingInfo buildingInfo = new BuildingInfo(
//												hosBuildInfo.getId(), build_no,
//												build_name);
//										building_Infos.add(buildingInfo);
//
//										imagePoints[build_no_int - 1][2] = hosBuildInfo
//												.getDeptBuilNo();
//										imagePoints[build_no_int - 1][3] = hosBuildInfo
//												.getDeptBuilName();
//										imagePoints[build_no_int - 1][4] = hosBuildInfo
//												.getId();
//									}
//
//								}
//
//							}
//
//						}
//
//					}

					Collections.sort((List) building_Infos_n,
							new Comparator<BuildingInfo>() {
								@Override
								public int compare(BuildingInfo lhs,
										BuildingInfo rhs) {
									int count = Integer.parseInt(lhs
											.getBuild_no())
											- Integer.parseInt(rhs
													.getBuild_no());
									return count;
								}
							});
					Collections.sort((List) building_Infos_s,
							new Comparator<BuildingInfo>() {
								@Override
								public int compare(BuildingInfo lhs,
										BuildingInfo rhs) {
									int count = Integer.parseInt(lhs
											.getBuild_no())
											- Integer.parseInt(rhs
													.getBuild_no());
									return count;
								}
							});

					init_Building(building_Infos_n);
				}

			}

			@Override
			public void onResponseEndErr(BuildReq commonReq,
					BuildRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});
	}

	private String build_name = "";

	public void init_Building(ArrayList<BuildingInfo> building_Infos) {

		if (hn_building.getChildCount() > 0) {
			hn_building.removeAllViews();
		}
		// 以下代码自定义配置社区布局
		int shequLineCount = building_Infos.size() % 2 == 0 ? building_Infos
				.size() / 2 : building_Infos.size() / 2 + 1;
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < shequLineCount; i++) {

			LinearLayout shequ_layout_child = (LinearLayout) inflater.inflate(
					R.layout.hn_budilng_list, null);

			LinearLayout hn_ll_budilng_item = (LinearLayout) shequ_layout_child
					.findViewById(R.id.hn_ll_budilng_item);

			for (int j = 0; j < 2; j++) {

				if (i * 2 + j >= building_Infos.size()) {
					break;
				}
				final Button shequ_btnButton = (Button) hn_ll_budilng_item
						.getChildAt(j);
				final String build_no = building_Infos.get(i * 2 + j)
						.getBuild_no();
				final String build_name = building_Infos.get(i * 2 + j)
						.getBuild_name();
				final String build_id = building_Infos.get(i * 2 + j).getId();

				shequ_btnButton.setText(Html.fromHtml(build_no + "、"
						+ build_name));
				shequ_btnButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						if (build_no != null) {

							Intent intent = new Intent(v.getContext(),
									HospitalFloorActivity.class);
							if (!isSHosp) {
								intent.putExtra("build_no", build_no);
							}
							intent.putExtra("build_name", build_name);
							intent.putExtra("build_id", build_id);
							startActivity(intent);

						}

					}
				});
			}
			hn_building.addView(shequ_layout_child);
		}

	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub

		im_pic.getImageMatrix();

		float values[] = new float[9];
		matrix.getValues(values);
		float currentTransX = values[Matrix.MTRANS_X];
		float currentTransY = values[Matrix.MTRANS_Y];
		float x = (e.getX() - currentTransX) / currentScale;
		float y = (e.getY() - currentTransY) / currentScale;

		String points[][] = null;
		if (isSHosp) { 
			points = imagePoints_s;
		} else {
			points = imagePoints_n;
		}
		for (int i = 0; i < points.length; i++) {
//
//			if (i == 10) {
//
//				continue;
//			}

			float spaceX = Math.abs(x
					- Integer.valueOf(points[i][0]).intValue());
			float spaceY = Math.abs(y
					- Integer.valueOf(points[i][1]).intValue());

			// Log.v("tags", "spaceX=" + spaceX);
			// Log.v("tags", "spaceY=" + spaceY);

			double spaceAverage = (spaceX + spaceY) / 2;

			// Log.v("tags", "spaceAverage=" + (i + 1) + "楼：" + spaceAverage);

			if (spaceX <= IMAGE_TRIGGER_SPACE && spaceY <= IMAGE_TRIGGER_SPACE) {

				// float spaceAverage = (spaceX + spaceY) / 2;

				if (points[i][2] != null) {

					Intent intent = new Intent(getApplicationContext(),
							HospitalFloorActivity.class);
					if (!isSHosp) {
						intent.putExtra("build_no", points[i][2]);
					}
//					intent.putExtra("build_no", points[i][2]);
					intent.putExtra("build_name", points[i][3]);
					intent.putExtra("build_id", points[i][4]);
					startActivity(intent);

				}
				break;
			}
		}

		return true;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub

		onSingleTapUp(event);
		return gestureDetector.onTouchEvent(event);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (bitmap != null && !bitmap.isRecycled()) {

			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}

}
