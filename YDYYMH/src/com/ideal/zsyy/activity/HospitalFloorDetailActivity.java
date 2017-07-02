package com.ideal.zsyy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.utils.BitmapUtil;

public class HospitalFloorDetailActivity extends Activity {

	private TextView top_title;
	private String build_no;
	private String hospital_floor_item_no;
	private String build_name;
	private String hospital_floor_item_name;
	private String floor_pic;
	public static Bitmap bitmap;
	private ImageView hfd_pic;
	private TextView hfd_title_text;
	private TextView hfd_title_ins;
	private LinearLayout hfd_ll_text;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Object[] object;
			ImageView iv_icon;

			switch (msg.what) {

			case 0:
				object = (Object[]) msg.obj;
				bitmap = (Bitmap) object[0];

				if (bitmap != null) {

					hfd_ll_text.setVisibility(View.GONE);

					iv_icon = (ImageView) object[1];
					iv_icon.setImageBitmap(bitmap);
				}

				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_floor_detail);

		Bundle bundle = getIntent().getExtras();
		build_no = bundle.getString("build_no");
		hospital_floor_item_no = bundle.getString("hospital_floor_item_no");
		build_name = bundle.getString("build_name");
		hospital_floor_item_name = bundle.getString("hospital_floor_item_name");
		floor_pic = bundle.getString("floor_pic");

		hfd_ll_text = (LinearLayout) findViewById(R.id.hfd_ll_text);

		intiView();
	}

	private void intiView() {

		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(build_no + "号楼" + hospital_floor_item_no + "平面图");

		hfd_pic = (ImageView) findViewById(R.id.hfd_pic);
		hfd_pic.setOnClickListener(new OnClickListener() { // 点击放大
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						FullScreenPicActivity.class);
				startActivity(intent);

			}
		});

		Log.v("tags", "floor_pic=" + floor_pic);

		if (floor_pic != null && !floor_pic.trim().equals("")) {

			BitmapUtil.downloadBitmap(hfd_pic, Config.down_url + floor_pic,
					handler, 0);
		} else {

			TextView hfd_img_text = (TextView) findViewById(R.id.hfd_img_text);
			hfd_img_text.setText("图片暂无");
		}

		hfd_title_text = (TextView) findViewById(R.id.hfd_title_text);

		hfd_title_text.setText(build_no + "号楼" + build_name + "——"
				+ hospital_floor_item_no + "层所有科室");

		hfd_title_ins = (TextView) findViewById(R.id.hfd_title_ins);
		hfd_title_ins.setText(hospital_floor_item_name);

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	@Override
	protected void onDestroy() {

		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}

		super.onDestroy();
	}

}
