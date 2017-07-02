package com.ideal.zsyy.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fang.sbas.R;
import com.ideal.zsyy.activity.DeptInfoActivity;
import com.ideal.zsyy.entity.PhDutyInfo;
import com.ideal.zsyy.utils.DataUtils;

public class DocDutyInfoAdapter extends BaseAdapter {

	private List<PhDutyInfo> phdutyinfos;
	private LayoutInflater mInflater;
	private Context context;
	private List<PhDutyInfo> dutyinfos = new ArrayList<PhDutyInfo>();// 当天排班信息
	private String isSpecialist;
	
	private PhDutyInfo phDutyInfo; //
	private Calendar calendar;
	private boolean isShow = false;

	public DocDutyInfoAdapter(Context context, List<PhDutyInfo> phdutyinfos,String isSpecialist) {
		this.context = context;
		this.isSpecialist = isSpecialist;
		mInflater = LayoutInflater.from(context);
		this.phdutyinfos = phdutyinfos;
		this.calendar = Calendar.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8 * 3;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View.OnClickListener listener = new View.OnClickListener() {
			private Dialog dialog;

			@Override
			public void onClick(View v) {
//				View view = mInflater.inflate(R.layout.dialog, null);
//				view.getBackground().setAlpha(50);
//				dialog = new Dialog(context, R.style.dialog);
//				dialog.setContentView(view);
//				dialog.show();
				DataUtils.showToast(context); 
			}
		};
		ViewHoder holder = null;
		// if (convertView == null) {
		convertView = mInflater.inflate(R.layout.dutyinfo_item, null);
		holder = new ViewHoder();
		holder.btn_title = (TextView) convertView.findViewById(R.id.btn_title);
		convertView.setTag(holder);
		// } else {
		// holder = (ViewHoder) convertView.getTag();
		// }
		if (position % 3 == 0) { // 显示时间
			if (position / 3 > 0) {
				try {
					dutyinfos.clear();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String dateStr = getDateStr(
							sdf.parse(sdf.format(new Date())), position / 3 - 1);
					holder.btn_title.setText(dateStr);
					//0905
					String date = dateStr.substring(dateStr.lastIndexOf(" ") + 1);
					for (PhDutyInfo dutyinfo : phdutyinfos) {
						String vstdate = getDateStr(dutyinfo.getVst_date());
						vstdate = vstdate.substring(vstdate.lastIndexOf(" ") + 1);
						if (date.trim().equals(
								vstdate.trim())) {// 当天是否有排班信息
						// phDutyInfo = dutyinfo;
							dutyinfos.add(dutyinfo);
//							break;
						}
//						isShow = false;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				holder.btn_title.setText("   ");
			}
		}
		if (position % 3 == 1) { // 显示上午是否坐诊
			if (position / 3 > 0) {
				if (dutyinfos.size() > 0) {
					for (PhDutyInfo dutyinfo : dutyinfos) {
						if (dutyinfo.getNoon_type().endsWith("01")) {
							if ("1".equals(isSpecialist)) {
								holder.btn_title.setText("预约");
								holder.btn_title.setOnClickListener(listener);
							} else if ("0".equals(isSpecialist)) {
								holder.btn_title.setText("坐诊");
							}
							holder.btn_title.setTextColor(context
									.getResources().getColor(R.color.white));
							if (dutyinfo.getLocate().trim().equals("S")) {// 南院
								holder.btn_title.setBackgroundColor(context
										.getResources().getColor(
												R.color.dutyinfo_locate_s_bg));
							} else if (dutyinfo.getLocate().trim()
									.equals("N")) {
								holder.btn_title.setBackgroundColor(context
										.getResources().getColor(
												R.color.dutyinfo_locate_n_bg));
							}
						}
					}
				} else {
//					holder.btn_title.setText("   ");
				}
			} else {
				holder.btn_title.setText("上午");
			}
		}
		if (position % 3 == 2) { // 显示下午是否坐诊
			if (position / 3 > 0) {
				if (dutyinfos.size() > 0) {
					for (PhDutyInfo dutyinfo : dutyinfos) {
						if (dutyinfo.getNoon_type().endsWith("02")) {
							if ("1".equals(isSpecialist)) {
								holder.btn_title.setText("预约");
								holder.btn_title.setOnClickListener(listener);
							} else if ("0".equals(isSpecialist)) {
								holder.btn_title.setText("坐诊");
							}
							holder.btn_title.setTextColor(context
									.getResources().getColor(R.color.white));
							if (dutyinfo.getLocate().trim().equals("S")) {// 南院
								holder.btn_title.setBackgroundColor(context
										.getResources().getColor(
												R.color.dutyinfo_locate_s_bg));
							} else if (dutyinfo.getLocate().trim()
									.equals("N")) {
								holder.btn_title.setBackgroundColor(context
										.getResources().getColor(
												R.color.dutyinfo_locate_n_bg));
							}
						}
					}
				} else {
//					holder.btn_title.setText("   ");
				}
			} else {
				holder.btn_title.setText("下午");
			}
		}
		return convertView;
	}

	static class ViewHoder {
		TextView btn_title;
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
	private static String getDateStr(String dateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(dateStr);
			SimpleDateFormat sdfStr = new SimpleDateFormat("M.d E");
			return sdfStr.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
