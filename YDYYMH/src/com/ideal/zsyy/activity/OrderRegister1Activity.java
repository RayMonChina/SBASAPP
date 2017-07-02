package com.ideal.zsyy.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.utils.DataUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderRegister1Activity extends Activity implements OnClickListener {

	private TextView et_dutydate,tvNoontype,tvDept,et_dutydate1,tvDoctor,tvNoontype1,tvDutytime1,tvDutytime;
	private String[] datelist = null;
	private String[] amtimelist = null;
	private String[] pmtimelist = null;
	private String[] noontype = new String[]{"上午","下午"};
	private boolean flag = false;
	private String doc_id = "";
	private String dept_id = "";
	private String locate = "";
	private String dept_name_cn = "";
	private String dept_id_cn = "";
	private String doc_name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_register1);
		datelist = DataUtils.getDateTimeStr1(14);
		amtimelist = DataUtils.amtimelist();
		pmtimelist = DataUtils.pmtimelist_n();
		initView();
	}

	private void initView() {
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
				Intent intent = new Intent(OrderRegister1Activity.this,NotesForRegistrationActivity.class);
				startActivity(intent);
			}
		});
		
		final LinearLayout ll_dept_subscribe = (LinearLayout) findViewById(R.id.ll_dept_subscribe);
		final LinearLayout ll_doctor_subscribe = (LinearLayout) findViewById(R.id.ll_doctor_subscribe);
		final ImageView iv_subscribe_dept = (ImageView) findViewById(R.id.iv_subscribe_dept);
		final ImageView iv_subscribe_doc = (ImageView) findViewById(R.id.iv_subscribe_doc);
		TextView tv_subscribe_dept = (TextView) findViewById(R.id.tv_subscribe_dept);
		tv_subscribe_dept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = false;
				doc_id = "";
				dept_id = "";
				initTextView(et_dutydate);
				initNoontype(tvNoontype);
				tvDept.setText("");
				tvDutytime.setText("");
				ll_dept_subscribe.setVisibility(View.VISIBLE);
				iv_subscribe_dept.setVisibility(View.VISIBLE);
				ll_doctor_subscribe.setVisibility(View.GONE);
				iv_subscribe_doc.setVisibility(View.INVISIBLE);

			}
		});
		TextView tv_subscribe_doc = (TextView) findViewById(R.id.tv_subscribe_doc);
		tv_subscribe_doc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = true;
				doc_id = "";
				dept_id = "";
				initTextView(et_dutydate1);
				initNoontype(tvNoontype1);
				tvDoctor.setText("");
				tvDutytime1.setText(""); 
				ll_dept_subscribe.setVisibility(View.GONE);
				iv_subscribe_dept.setVisibility(View.INVISIBLE);
				ll_doctor_subscribe.setVisibility(View.VISIBLE);
				iv_subscribe_doc.setVisibility(View.VISIBLE);
			}
		});

		et_dutydate = (TextView) findViewById(R.id.et_dutydate);
		initTextView(et_dutydate);
		et_dutydate.setOnClickListener(this);

		tvNoontype = (TextView) findViewById(R.id.et_noontype);
		initNoontype(tvNoontype);
		tvNoontype.setOnClickListener(this);

		tvDept = (TextView) findViewById(R.id.tv_deptname);
		tvDept.setOnClickListener(this);
		
		tvDutytime = (TextView) findViewById(R.id.tv_dutytime);
		tvDutytime.setOnClickListener(this);

		et_dutydate1 = (TextView) findViewById(R.id.et_dutydate1);
		initTextView(et_dutydate1);
		et_dutydate1.setOnClickListener(this);
		
		tvNoontype1 = (TextView) findViewById(R.id.et_noontype1);
		initNoontype(tvNoontype1);
		tvNoontype1.setOnClickListener(this);
		
		tvDoctor = (TextView) findViewById(R.id.tv_docname);
		tvDoctor.setOnClickListener(this);
		
		tvDutytime1 = (TextView) findViewById(R.id.tv_dutytime1);
		tvDutytime1.setOnClickListener(this);
		
		Button btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submitOrderRegisterInfo();
			}
		});
	}

	protected void submitOrderRegisterInfo() {
		if (Config.phUsers == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("请登录....");
			builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(OrderRegister1Activity.this, LoginActivity.class);
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
				dept_name = tvDept.getText().toString();
				deptid = dept_id;
			}
			if (dutydate.equals("") || noontype.equals("") || dutytime.equals("") || deptid.equals("")) {
				Toast.makeText(this, "信息不能为空", 1).show();
				return;
			}
			Intent intent = new Intent(OrderRegister1Activity.this,ConfimAppointmentActivity.class);
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

	private void initNoontype(TextView tvNoontype) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(new Date());
//		int noon = cal.get(Calendar.AM_PM);
//		if (noon == 0) {
			tvNoontype.setText("上午");
//		} else if (noon == 1) {
//			tvNoontype.setText("下午");
//		}
	}

	private void initTextView(TextView tv) {
		Calendar cal = Calendar.getInstance();
		int _year = cal.get(Calendar.YEAR);
		int _month = cal.get(Calendar.MONTH) + 1;
		int _day = cal.get(Calendar.DAY_OF_MONTH) + 1;
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) v;
		switch (v.getId()) {
		case R.id.et_dutydate:
		case R.id.et_dutydate1:
			showTimeDialog(datelist, "请选择日期...", tv);
			break;
		case R.id.et_noontype:
		case R.id.et_noontype1:
			showTimeDialog(noontype, "请选择上下午...", tv);
			break;
		case R.id.tv_dutytime:
			if (tvNoontype.getText().equals("上午")) {
				showTimeDialog(amtimelist, "请选择时间...", tv);
			} else if (tvNoontype.getText().equals("下午")) {
				showTimeDialog(pmtimelist, "请选择时间...", tv);
			}
			break;
		case R.id.tv_dutytime1:
			if (tvNoontype1.getText().equals("上午")) {
				showTimeDialog(amtimelist, "请选择时间...", tv);
			} else if (tvNoontype1.getText().equals("下午")) {
				showTimeDialog(pmtimelist, "请选择时间...", tv);
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
			String dept_name = data.getStringExtra("dept_name");
			locate = data.getStringExtra("locate");
			dept_id = data.getStringExtra("dept_id");
			tvDept.setText(dept_name);  
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
