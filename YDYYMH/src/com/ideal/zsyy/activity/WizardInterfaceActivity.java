package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fang.sbas.R;
import com.ideal.zsyy.adapter.IndicationCentertList;
import com.ideal.zsyy.adapter.IndicationDotList;
import com.ideal.zsyy.adapter.IndicationDotList1;
import com.ideal.zsyy.adapter.MyViewPagerAdapter;
import com.ideal.zsyy.entity.SkinInfo;
import com.ideal.zsyy.service.PreferencesService;
import com.ideal.zsyy.utils.DataUtils;

public class WizardInterfaceActivity extends Activity {

	private List<View> picViews;
	private ViewPager viewpager;
	private IndicationDotList1 mDotList;
	private MyViewPagerAdapter myPicViewPagerAdapter;
	private LinearLayout.LayoutParams layoutParams;
	private int currentIndex;
	private Button btn_begin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_PROGRESS); // 去标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wizard_interface);
		picViews = new ArrayList<View>();
		initView();
	}

	private void initView() {

		viewpager = (ViewPager) findViewById(R.id.splash_viewpager_picture);
		mDotList = (IndicationDotList1) findViewById(R.id.index_indication);
		btn_begin = (Button) findViewById(R.id.btn_begin);
		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentIndex = arg0;
				mDotList.setIndex(arg0);
				if (arg0 == picViews.size() - 1) {
					btn_begin.setVisibility(View.VISIBLE);
				} else {
					btn_begin.setVisibility(View.INVISIBLE);
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
		viewpager.setOnTouchListener(new View.OnTouchListener() {
			float startindex = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startindex = event.getX();
					break;
				case MotionEvent.ACTION_UP:
					float endindex = event.getX();
					if (currentIndex == 3) {
						if (endindex < startindex) {
							 Intent intent = new Intent(
							 WizardInterfaceActivity.this,
							 MainActivity.class);
//							Intent intent = new Intent(
//									WizardInterfaceActivity.this,
//									HospitalListActivity.class);
							startActivity(intent);
							finish();
						}
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
		btn_begin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent(
				 WizardInterfaceActivity.this,
				 MainActivity.class);
//				Intent intent = new Intent(WizardInterfaceActivity.this,
//						HospitalListActivity.class);
				startActivity(intent);
				finish();
			}
		});
		layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);

		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setLayoutParams(layoutParams);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setPadding(0, 30, 0, 0);
		imageView.setImageResource(R.drawable.h1);
		picViews.add(imageView);

		ImageView imageView2 = new ImageView(getApplicationContext());
		imageView2.setLayoutParams(layoutParams);
		imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView2.setPadding(0, 30, 0, 0);
		imageView2.setImageResource(R.drawable.h2);
		picViews.add(imageView2);

		ImageView imageView3 = new ImageView(getApplicationContext());
		imageView3.setLayoutParams(layoutParams);
		imageView3.setPadding(0, 30, 0, 0);
		imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView3.setImageResource(R.drawable.h3);
		picViews.add(imageView3);

		ImageView imageView4 = new ImageView(getApplicationContext());
		imageView4.setLayoutParams(layoutParams);
		imageView4.setPadding(0, 30, 0, 0);
		imageView4.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView4.setImageResource(R.drawable.h4);
		picViews.add(imageView4);

		mDotList.setCount(4);
		mDotList.setIndex(0);

		myPicViewPagerAdapter = new MyViewPagerAdapter(picViews);
		viewpager.setAdapter(myPicViewPagerAdapter);
	}
}
