package com.ideal.zsyy.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.MyViewPagerAdapter;
import com.ideal.zsyy.utils.DataUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderRegister2Activity extends Activity implements OnPageChangeListener,OnClickListener{
	private ViewPager order_viewpager;
	private ImageView iv_subscribe_dept;
	private ImageView iv_subscribe_doc;
	private TextView tv_subscribe_dept;
	private TextView tv_subscribe_doc;
	private LinearLayout order_ll_dept;
	private LinearLayout order_ll_doctor;
	private TextView et_dutydate;
	private TextView tvNoontype;
	private TextView tvDept;
	private TextView tvDutytime;
	private TextView et_dutydate1;
	private TextView tvNoontype1;
	private TextView tvDoctor;
	private TextView tvDutytime1;
	private String doc_id = "";
	private String dept_id = "";
	private String locate = "";
	private String dept_name_cn = "";
	private String dept_id_cn = "";
	private String doc_name = "";
	private String[] datelist = null;
	private String[] amtimelist = null;
	private String[] pmtimelist_n = null;
	private String[] pmtimelist_s = null;
	private String[] noontype = new String[]{"上午","下午"};
	private String deptName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_register2);
		datelist = DataUtils.getDateTimeStr1(14);
		amtimelist = DataUtils.amtimelist();
		pmtimelist_n = DataUtils.pmtimelist_n();
		pmtimelist_s = DataUtils.pmtimelist_s();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
		//预约须知
		Button btn_notesforregistration = (Button) findViewById(R.id.btn_notesforregistration);
		btn_notesforregistration.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OrderRegister2Activity.this,NotesForRegistrationActivity.class);
				startActivity(intent);
			}
		});
		order_viewpager = (ViewPager) findViewById(R.id.order_viewpager);
		order_viewpager.setOnPageChangeListener(this);
		
		ArrayList<View> aViews = new ArrayList<View>();
		LayoutInflater lf = LayoutInflater.from(this);

		View order_viewpager_button_dept = lf.inflate(
				R.layout.order_viewpager_page1, null);
		View order_viewpager_button_doctor = lf.inflate(
				R.layout.order_viewpager_page2, null);

		aViews.add(order_viewpager_button_dept);
		aViews.add(order_viewpager_button_doctor);
		MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(aViews);
		order_viewpager.setAdapter(myViewPagerAdapter);
		
		iv_subscribe_dept = (ImageView) findViewById(R.id.iv_subscribe_dept);
		tv_subscribe_dept = (TextView) findViewById(R.id.tv_subscribe_dept);
		order_ll_dept = (LinearLayout) findViewById(R.id.order_ll_dept);
		order_ll_dept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDept();
				locate = "";
				order_viewpager.setCurrentItem(0);
			}
		});
		
		iv_subscribe_doc = (ImageView) findViewById(R.id.iv_subscribe_doc);
		tv_subscribe_doc = (TextView) findViewById(R.id.tv_subscribe_doc);
		order_ll_doctor = (LinearLayout) findViewById(R.id.order_ll_doctor);
		order_ll_doctor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDoctor();
				locate = "";
				order_viewpager.setCurrentItem(1);
			}
		});
		
		et_dutydate = (TextView) order_viewpager_button_dept.findViewById(R.id.et_dutydate);
		initTextView(et_dutydate);
		et_dutydate.setOnClickListener(this);

		tvNoontype = (TextView) order_viewpager_button_dept.findViewById(R.id.et_noontype);
		initNoontype(tvNoontype);
		tvNoontype.setOnClickListener(this);

		tvDept = (TextView) order_viewpager_button_dept.findViewById(R.id.tv_deptname);
		tvDept.setOnClickListener(this);
		
		tvDutytime = (TextView) order_viewpager_button_dept.findViewById(R.id.tv_dutytime);
		tvDutytime.setOnClickListener(this);

		et_dutydate1 = (TextView) order_viewpager_button_doctor.findViewById(R.id.et_dutydate1);
		initTextView(et_dutydate1);
		et_dutydate1.setOnClickListener(this);
		
		tvNoontype1 = (TextView) order_viewpager_button_doctor.findViewById(R.id.et_noontype1);
		initNoontype(tvNoontype1);
		tvNoontype1.setOnClickListener(this);
		
		tvDoctor = (TextView) order_viewpager_button_doctor.findViewById(R.id.tv_docname);
		tvDoctor.setOnClickListener(this);
		
		tvDutytime1 = (TextView) order_viewpager_button_doctor.findViewById(R.id.tv_dutytime1);
		tvDutytime1.setOnClickListener(this);
		
		Button btn_submit_dept = (Button) order_viewpager_button_dept.findViewById(R.id.btn_submit);
		btn_submit_dept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submitOrderRegisterInfo(false);
			}
		});
		
		Button btn_submit_doctor = (Button) order_viewpager_button_doctor.findViewById(R.id.btn_submit1);
		btn_submit_doctor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submitOrderRegisterInfo(true);
			}
		});
	}
	@SuppressLint("NewApi")
	protected void submitOrderRegisterInfo(boolean flag) {
		if (Config.phUsers == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("请登录....");
			builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(OrderRegister2Activity.this, LoginActivity.class);
					intent.putExtra("logintype", "orderregister");
					startActivity(intent); 
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			builder.create().show();
		} else {
			String dutydate = "";
			String noontype = "";
			String dutytime = "";
			String dept_name = "";
			String deptid = "";
			if (flag) { // 医生
				dutydate = et_dutydate1.getText().toString();
				noontype = tvNoontype1.getText().toString().equals("上午")?"01":"02";
				dutytime = tvDutytime1.getText().toString();
				deptid = dept_id_cn;
				dept_name = dept_name_cn;
			} else {
				dutydate = et_dutydate.getText().toString();
				noontype = tvNoontype.getText().toString().equals("上午")?"01":"02";
				dutytime = tvDutytime.getText().toString();
				dept_name = deptName; 
				deptid = dept_id;
			}
			if (dutydate.equals("")) {
				Toast.makeText(this, "就诊日期不能为空", 1).show();
				return;
			}
			if (noontype.equals("")) {
				Toast.makeText(this, "上下午不能为空", 1).show();
				return;
			}
			if (flag) {
				if (doc_name.equals("")) {
					Toast.makeText(this, "预约医生不能为空", 1).show();
					return;
				}
			} else {
				if (dept_name.equals("")) {
					Toast.makeText(this, "预约科室不能为空", 1).show();
					return;
				}
			}
			if (dutytime.equals("")) {
				Toast.makeText(this, "就诊时间不能为空", 1).show();
				return;
			}
//			if (dutydate.equals("") || noontype.equals("") || dutytime.equals("") || deptid.equals("")) {
//				Toast.makeText(this, "信息不能为空", 1).show();
//				return;
//			}
			Intent intent = new Intent(OrderRegister2Activity.this,ConfimAppointmentActivity.class);
			intent.putExtra("locate", locate);
			intent.putExtra("dutydate", dutydate);
			intent.putExtra("noontype", noontype);
			intent.putExtra("dutytime", dutytime);
			intent.putExtra("dept_id", deptid);
			intent.putExtra("doc_id", doc_id);
			intent.putExtra("dept_name", dept_name);
			intent.putExtra("doc_name", doc_name);
			startActivity(intent);   
			finish();  
		}
	}
	private void setDept() {
		initTextView(et_dutydate);
		initNoontype(tvNoontype);
		tvDept.setText(""); 
		tvDutytime.setText(""); 
		
		tv_subscribe_dept.setTextColor(Color.parseColor("#FFFFFF"));
		iv_subscribe_dept.setVisibility(View.VISIBLE);

		tv_subscribe_doc.setTextColor(Color.parseColor("#ededed"));
		iv_subscribe_doc.setVisibility(View.INVISIBLE);

	}

	private void setDoctor() {
		initTextView(et_dutydate1);
		initNoontype(tvNoontype1);
		tvDoctor.setText(""); 
		tvDutytime1.setText(""); 
		
		tv_subscribe_dept.setTextColor(Color.parseColor("#ededed"));
		iv_subscribe_dept.setVisibility(View.INVISIBLE);

		tv_subscribe_doc.setTextColor(Color.parseColor("#FFFFFF"));
		iv_subscribe_doc.setVisibility(View.VISIBLE);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {

		case 0:
			setDept();
			// setYuyueList();
			break;
		case 1:
			setDoctor(); 
			// setCancelList();
			break;

		}
	}
	
	private void initNoontype(TextView tvNoontype) {
		String now_time = et_dutydate.getText().toString();
		if(now_time.equals(datelist[0])){
			tvNoontype.setText("下午");
		}else{
			tvNoontype.setText("上午");
		}
	}

	private void initTextView(TextView tv) {
		Calendar cal = Calendar.getInstance();
		int _year = cal.get(Calendar.YEAR);
		int _month = cal.get(Calendar.MONTH) + 1;
		int _day = cal.get(Calendar.DAY_OF_MONTH);
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		if(hour>=12){
			_day=_day+1;
		}
		int lastday = DataUtils.getLastDay();
		if (_day > lastday) {
			_month = _month + 1;
			_day = _day - lastday;
		}
		String month = "";
		String day = "";
		if (_month < 10) {
			month = "0" + _month;
		} else {
			month = _month + "";
		}
		if (_day < 10) {
			day = "0" + _day;
		} else {
			day = _day + "";
		}
		String time = _year + "-" + month + "-" + day;
		tv.setText(time);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			doc_name = data.getStringExtra("doc_name"); 
			locate = data.getStringExtra("locate");
			doc_id = data.getStringExtra("doc_id");
			dept_id_cn = data.getStringExtra("dept_id");
			dept_name_cn = data.getStringExtra("deptname");
			String str = doc_name;
			if (dept_name_cn != null && !"".equals(dept_name_cn)) {
				str = str  + "(" + dept_name_cn + ")";
			}
			tvDoctor.setText(str); 
		} else if (resultCode == 2) {
			deptName = data.getStringExtra("dept_name");
			locate = data.getStringExtra("locate");
			dept_id = data.getStringExtra("dept_id");
			if ("N".equals(locate)) { 
				tvDept.setText(deptName + "(北院)");   
			} else if ("S".equals(locate)) { 
				tvDept.setText(deptName + "(南院)");    
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) v;
		switch (v.getId()) {
		case R.id.et_dutydate:
		case R.id.et_dutydate1:
			if(et_dutydate.getText().toString().equals(datelist[0])){
				tvNoontype.setText("下午");
			}else{
				tvNoontype.setText("上午");
			}
			if(et_dutydate1.getText().toString().equals(datelist[0])){
				tvNoontype1.setText("下午");
			}else{
				tvNoontype1.setText("上午");
			}
			showTimeDialog(datelist, "请选择日期...", tv);
			break;
		case R.id.et_noontype:
			String now_time = et_dutydate.getText().toString();
			if(now_time.equals(datelist[0])){
				String now_day = now_time.substring(now_time.lastIndexOf("-")+1);
				Calendar cal = Calendar.getInstance();
				int _day = cal.get(Calendar.DAY_OF_MONTH);
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if(Integer.parseInt(now_day) ==_day){
					noontype=new String[]{"下午"};
				}
			}else{
				noontype=new String[]{"上午","下午"};
			}
			showTimeDialog(noontype, "请选择上下午...", tv);
			break;
		case R.id.et_noontype1:
			String now_time1 = et_dutydate1.getText().toString();
			if(now_time1.equals(datelist[0])){
				String now_day = now_time1.substring(now_time1.lastIndexOf("-")+1);
				Calendar cal = Calendar.getInstance();
				int _day = cal.get(Calendar.DAY_OF_MONTH);
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if(Integer.parseInt(now_day) ==_day){
					noontype=new String[]{"下午"};
				}
			}else{
				noontype=new String[]{"上午","下午"};
			}
			showTimeDialog(noontype, "请选择上下午...", tv);
			break;
		case R.id.tv_dutytime:
			if (!"".equals(locate)) {
				if (tvNoontype.getText().equals("上午")) {
					showTimeDialog(amtimelist, "请选择时间...", tv);
				} else if (tvNoontype.getText().equals("下午")) {
					if ("N".equals(locate)) {
						showTimeDialog(pmtimelist_n, "请选择时间...", tv);
					} else if ("S".equals(locate)) {
						showTimeDialog(pmtimelist_s, "请选择时间...", tv);
					}
				}
			} else {
				Toast.makeText(OrderRegister2Activity.this, "请选择科室...", 1).show();
			}
			break;
		case R.id.tv_dutytime1:
			if (!"".equals(locate)) {
				if (tvNoontype1.getText().equals("上午")) {
					showTimeDialog(amtimelist, "请选择时间...", tv);
				} else if (tvNoontype1.getText().equals("下午")) {
					if ("N".equals(locate)) {
						showTimeDialog(pmtimelist_n, "请选择时间...", tv);
					} else if ("S".equals(locate)) {
						showTimeDialog(pmtimelist_s, "请选择时间...", tv);
					}
				}
			} else {
				Toast.makeText(OrderRegister2Activity.this, "请选择医生...", 1).show();
			}
			break;
		case R.id.tv_deptname:
			String data = et_dutydate.getText().toString();
			String day = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date parse = sdf.parse(data);
				day = parse.getDay() + "";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String noontype = "";
			if (tvNoontype1.getText().toString().equals("上午")) {
				noontype = "01";
			} else if (tvNoontype1.getText().toString().equals("下午")) {
				noontype = "02";
			}
			
			Intent toOrderDeptinfo = new Intent(this, OrderDeptInfoActivity.class);
			toOrderDeptinfo.putExtra("day", day);
			toOrderDeptinfo.putExtra("noontype", noontype);
			startActivityForResult(toOrderDeptinfo, 1); 
			break;
		case R.id.tv_docname:
			String dutytime = et_dutydate1.getText().toString();
			String noontype1 = "";
			if (tvNoontype1.getText().toString().equals("上午")) {
				noontype1 = "01";
			} else if (tvNoontype1.getText().toString().equals("下午")) {
				noontype1 = "02";
			}
			Intent intent = new Intent(this, OrderDoctorInfoActivity.class);
//			Intent intent = new Intent(this, PhDoctorInfoActivity.class);
			intent.putExtra("isOrder", true);
			intent.putExtra("dutytime", dutytime);
			intent.putExtra("noontype", noontype1);
			startActivityForResult(intent, 1); 
			break;
		default:
			break;
		}
	}
	private void showTimeDialog(final String[] list, String titel,
			final TextView tv) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titel);
		builder.setItems(list, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String data = list[which];
				switch (tv.getId()) {
				case R.id.et_dutydate:
					
					tv.setText(data);
					break;
				case R.id.et_noontype:
				case R.id.et_noontype1:
					tv.setText(data);
					tvDutytime.setText("");
					tvDutytime1.setText("");
					tvDept.setText("");
					tvDoctor.setText("");
					break;
				case R.id.et_dutydate1:
					tv.setText(data);
					break;
				case R.id.tv_dutytime:
				case R.id.tv_dutytime1:
					tv.setText(data);
					break;
				default:
					break;
				}
			}
		});
		builder.create().show();
	}
}
