package com.ideal.zsyy.adapter;

import java.util.List;

import com.fang.sbas.R;
import com.ideal.zsyy.entity.OnlineDoctor;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OnlineDoctorAdapter  extends BaseAdapter{

	private Context mContext;
	private List<OnlineDoctor> doctors;
	private LayoutInflater inflater;
	public OnlineDoctorAdapter(Context mContext,List<OnlineDoctor> doctors) {
		this.mContext=mContext;
		this.doctors=doctors;
		inflater=LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return doctors.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.doctor_chat_list_item, null);
			holder=new ViewHolder();
			holder.img=(ImageView) convertView.findViewById(R.id.doctor_img);
			holder.name=(TextView) convertView.findViewById(R.id.doctor_name);
			holder.status=(TextView) convertView.findViewById(R.id.doctor_status);
			holder.job=(TextView) convertView.findViewById(R.id.doctor_job);
			holder.dept=(TextView) convertView.findViewById(R.id.doctor_dept);
			convertView.setTag(holder);
		}else
		{
			holder=(ViewHolder) convertView.getTag();
		}
		OnlineDoctor doctor=doctors.get(position);
		holder.img.setBackgroundResource(R.drawable.man);
		holder.name.setText(doctor.getDoctorName());
		holder.dept.setText(doctor.getDept());
		holder.job.setText(doctor.getJob());
		if(doctor.getStatus()==0)
		{
			holder.status.setText("[在线]");
		}else
		{
			holder.status.setText("[离线]");
		}
		return convertView;
	}

	private static class ViewHolder
	{
		public   ImageView img;
		public   TextView name;
		public   TextView status;
		public   TextView dept;
		public   TextView job;
	}
}
