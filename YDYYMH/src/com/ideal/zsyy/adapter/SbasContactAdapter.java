package com.ideal.zsyy.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fang.sbas.R;
import com.ideal.zsyy.entity.SBASContactInfo;

public class SbasContactAdapter extends BaseAdapter implements Filterable {
	private List<SBASContactInfo> sbasContactInfos; // 当前收索到的信息列表
	private List<SBASContactInfo> oldSbasContactInfos;// 所有的信息
	private LayoutInflater inflater;
	private Handler mHandler;
	private Context _context;
	//private List<String> nameList;
	//private List<String> floorList;

	public SbasContactAdapter(List<SBASContactInfo> list, Context context,
			Handler mHandler) {
		this.inflater = LayoutInflater.from(context);
		this.mHandler = mHandler;
		if (sbasContactInfos != null) {
			sbasContactInfos.clear();
		}
		this.sbasContactInfos = list;
		this.oldSbasContactInfos = this.sbasContactInfos;
		_context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sbasContactInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return sbasContactInfos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
			convertView = inflater.inflate(R.layout.sbas_contact_item, null);
			ViewHolder holder = new ViewHolder();
			holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
			holder.tv_dept = (TextView) convertView.findViewById(R.id.tv_dept);
			holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			holder.img_call=(ImageView)convertView.findViewById(R.id.img_call);
			convertView.setTag(holder);
			SBASContactInfo sbasContract=sbasContactInfos.get(position);
			holder.img_call.setTag(sbasContract.getMobliePhoneNo());
			if(sbasContract!=null){
				holder.tv_user_name.setText(sbasContract.getUser_Name());
				holder.tv_phone.setText(sbasContract.getMobliePhoneNo());
				holder.tv_dept.setText(sbasContract.getOrganization_Name());
				holder.img_call.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(v.getTag()!=null){
							Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+v.getTag().toString()));
							_context.startActivity(intent);
						}
						else {
							Toast.makeText(_context, "",Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			}
		return convertView;
	}
	

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		Filter filter = new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence con) {
				// TODO Auto-generated method stub
				FilterResults results = new FilterResults();
				if(con==null||con.length()==0){
					results.values = oldSbasContactInfos;
					results.count = oldSbasContactInfos.size();
				}
				List<SBASContactInfo> sbasInfos = new ArrayList<SBASContactInfo>();
				for(SBASContactInfo info:oldSbasContactInfos){
					if(info.getUser_Name().contains(con)){
						sbasInfos.add(info);
						continue;
					}
					if(info.getMobliePhoneNo()!=null&&info.getMobliePhoneNo().contains(con)){
						sbasInfos.add(info);
						continue;
					}
					if(info.getOrganization_Name()!=null&&info.getOrganization_Name().contains(con)){
						sbasInfos.add(info);
						continue;
					}
				}
				
				results.values = sbasInfos;
				results.count = sbasInfos.size();
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				sbasContactInfos = (ArrayList<SBASContactInfo>) results.values;
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

		};
		return filter;
	}

	static class ViewHolder {
		TextView tv_user_name;
		TextView tv_dept;
		TextView tv_phone;
		ImageView img_call;
	}
}
