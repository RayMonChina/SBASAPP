package com.ideal.zsyy.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.activity.ConfimAppointmentActivity;
import com.ideal.zsyy.activity.DeptInfoActivity;
import com.ideal.zsyy.activity.LoginActivity;
import com.ideal.zsyy.activity.OrderRegister1Activity;
import com.ideal.zsyy.entity.PhDoctorInfo;
import com.ideal.zsyy.entity.PhDutyInfo;
import com.ideal.zsyy.utils.DataUtils;

public class DocDutyInfo1Adapter extends BaseAdapter {

	private List<PhDutyInfo> phdutyinfos;
	private LayoutInflater mInflater;
	private Context context;
	private List<PhDutyInfo> dutyinfos = new ArrayList<PhDutyInfo>();// 当天排班信息
	private String isSpecialist;
	private List<String> datetimeList;
	private String[] amtimelist;
	private String[] pmtimelist;
	private PhDoctorInfo doctorInfo;

	private PhDutyInfo phDutyInfo; //
	private Calendar calendar;
	private boolean isShow = false;

	public DocDutyInfo1Adapter(Context context, List<PhDutyInfo> phdutyinfos,
			PhDoctorInfo doctorInfo) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.phdutyinfos = phdutyinfos;
		this.calendar = Calendar.getInstance();
		this.datetimeList = DataUtils.getDateTimeStr(14, false);
		amtimelist = DataUtils.amtimelist();
		this.doctorInfo = doctorInfo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datetimeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return phdutyinfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHoder holder = null;
		convertView = mInflater.inflate(R.layout.doctor_dutyinfo, null);
		holder = new ViewHoder();
		holder.tv_text1 = (TextView) convertView.findViewById(R.id.tv_text1);
		holder.tv_text2 = (TextView) convertView.findViewById(R.id.tv_text2);
		holder.tv_text3 = (TextView) convertView.findViewById(R.id.tv_text3);
		convertView.setTag(holder);
		String currentTime1 = datetimeList.get(position);
		String currentTime = getDateStr(currentTime1, "yyyy-MM-dd");
		holder.tv_text1.setText(currentTime);
		// holder.tv_text1.setVisibility(View.VISIBLE);
		for (PhDutyInfo dutyinfo : phdutyinfos) {
			String vst_date = dutyinfo.getVst_date();
			vst_date = getDateStr(vst_date, "yyyy-MM-dd HH:mm:ss");
			if (currentTime.equals(vst_date)) {
				if (dutyinfo.getNoon_type().endsWith("01")) {
					holder.tv_text2.setText("预约");
					MyOnClickListener listener = new MyOnClickListener(
							amtimelist, dutyinfo, currentTime1, "01",dutyinfo.getLocate());
					holder.tv_text2.setOnClickListener(listener);
					holder.tv_text2.setTextColor(context.getResources()
							.getColor(R.color.white));
					if (dutyinfo.getLocate().trim().equals("S")) {// 南院
						holder.tv_text2.setBackgroundColor(context
								.getResources().getColor(
										R.color.dutyinfo_locate_s_bg));
					} else if (dutyinfo.getLocate().trim().equals("N")) {
						holder.tv_text2.setBackgroundColor(context
								.getResources().getColor(
										R.color.dutyinfo_locate_n_bg));
					}
				} else if (dutyinfo.getNoon_type().endsWith("02")) {
					holder.tv_text3.setText("预约");
					
					if ("N".equals(dutyinfo.getLocate())) {
						pmtimelist = DataUtils.pmtimelist_n();
					} else if ("S".equals(dutyinfo.getLocate())) {
						pmtimelist = DataUtils.pmtimelist_s(); 
					}
					MyOnClickListener listener = new MyOnClickListener(
							pmtimelist, dutyinfo, currentTime1, "02",dutyinfo.getLocate());
					holder.tv_text3.setOnClickListener(listener);
					holder.tv_text3.setTextColor(context.getResources()
							.getColor(R.color.white));
					if (dutyinfo.getLocate().trim().equals("S")) {// 南院
						holder.tv_text3.setBackgroundColor(context
								.getResources().getColor(
										R.color.dutyinfo_locate_s_bg));
					} else if (dutyinfo.getLocate().trim().equals("N")) {
						holder.tv_text3.setBackgroundColor(context
								.getResources().getColor(
										R.color.dutyinfo_locate_n_bg));
					}
				}
			}
		}
		if (holder.tv_text2.getText().toString().equals("")) {
			holder.tv_text2.setText("上午");
			holder.tv_text2.setVisibility(View.INVISIBLE);
		}
		if (holder.tv_text3.getText().toString().equals("")) {
			holder.tv_text3.setText("上午");
			holder.tv_text3.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	private class MyOnClickListener implements OnClickListener {
		private String[] list;
		private PhDutyInfo dutyinfo;
		private String currentTime;
		private String noontype;
		private String locate;

		public MyOnClickListener(String[] list, PhDutyInfo dutyinfo,
				String currentTime, String noontype,String locate) {
			this.list = list;
			this.dutyinfo = dutyinfo;
			this.currentTime = currentTime;
			this.noontype = noontype;
			this.locate = locate;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (Config.phUsers != null) {
				AlertDialog.Builder buildertime = new AlertDialog.Builder(
						context);
				buildertime.setTitle("请选择时间...");
				buildertime.setItems(list,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								String time = list[which];
								
								Intent intent = new Intent(context,
										ConfimAppointmentActivity.class);
								intent.putExtra("locate", dutyinfo.getLocate());
								intent.putExtra("dutydate", currentTime);
								intent.putExtra("noontype", dutyinfo.getNoon_type());
								intent.putExtra("dutytime", time);
								intent.putExtra("doc_id",
										doctorInfo.getDoctor_Id());
								intent.putExtra("doc_name",doctorInfo.getDoctor_Name());
								String dept_id = "",dept_name = "";
								if (dutyinfo.getDept_name_cn() != null && !"".equals(dutyinfo.getDept_name_cn())) {
									dept_id = dutyinfo.getDept_id();
									dept_name = dutyinfo.getDept_name_cn();
								}
								intent.putExtra("dept_id", dept_id);
								intent.putExtra("dept_name", dept_name);
								context.startActivity(intent);
								((Activity) context).finish();
							}
						});
				buildertime.create().show();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("请登录");
				builder.setNeutralButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context,
										LoginActivity.class);
								intent.putExtra("logintype", "docinfo");
								context.startActivity(intent);
							}
						});

				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				builder.create().show();
			}
		}
	}

	static class ViewHoder {
		TextView tv_text1;
		TextView tv_text2;
		TextView tv_text3;
		// Button btn_title;
	}

	/**
	 * 计算出当前日期后的一周时间
	 * 
	 * @param dateStr
	 * @param position
	 * @return
	 * @throws Exception
	 */
	private static String getDateStr(Date date, int position) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int day = cal.get(Calendar.DATE);
			cal.set(Calendar.DATE, position + day);
			SimpleDateFormat sdfStr = new SimpleDateFormat("M.d E");
			return sdfStr.format(cal.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将Stringyyyy-MM-dd HH:mm:ss类型的时间转换成M.d E
	 * 
	 * @param dateStr
	 * @return
	 */
	private static String getDateStr(String dateStr, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Date date = sdf.parse(dateStr);
			SimpleDateFormat sdfStr = new SimpleDateFormat("MM.dd E");
			return sdfStr.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
