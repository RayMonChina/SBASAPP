package com.ideal.zsyy.adapter;

import java.util.List;

import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.base.HealthInformation;
import com.ideal.zsyy.entity.PhJkxjInfo;
import com.ideal.zsyy.entity.PhJkxjPicInfo;
import com.ideal.zsyy.imagecache.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HealthInformationAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	// private List<HealthInformation> list;
	private List<PhJkxjInfo> phJkxjInfos;
	// private List<PhJkxjPicInfo> jkxjpicinfo;

	private ImageLoader imageLoader;

	public HealthInformationAdapter(Context context,
			List<PhJkxjInfo> phJkxjInfos) {

		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.phJkxjInfos = phJkxjInfos;

		imageLoader = new ImageLoader(context);

	}

	public int getCount() {

		return phJkxjInfos.size();

	}

	public Object getItem(int position) {

		return phJkxjInfos.get(position);

	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.healthinformation_item,
					null);
			holder = new ViewHolder();

			holder.hi_title = (TextView) convertView
					.findViewById(R.id.hi_title);
			holder.hi_brief_introduction = (TextView) convertView
					.findViewById(R.id.hi_brief_introduction);
			holder.hi_img = (ImageView) convertView.findViewById(R.id.hi_img);
			holder.ll_video_play = (LinearLayout) convertView
					.findViewById(R.id.ll_health_video_play);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PhJkxjInfo phJkxjInfo = phJkxjInfos.get(position);

		holder.hi_title.setText(phJkxjInfo.getTitle());
		holder.hi_brief_introduction.setText(phJkxjInfo.getMemo());
		holder.hi_img.setImageResource(R.drawable.hi_list_pic_dauflt);

		holder.ll_video_play.setVisibility(View.INVISIBLE);

		if (!phJkxjInfo.isVedio()) {

			List<PhJkxjPicInfo> jkxjpicinfo = phJkxjInfo.getJkxjpicinfo();

			for (int i = 0; i < jkxjpicinfo.size(); i++) {

				PhJkxjPicInfo phJkxjPicInfo = jkxjpicinfo.get(i);

				if (phJkxjPicInfo.getType().equals("1")) {

					imageLoader.DisplayImage(
							Config.down_url + phJkxjPicInfo.getName(),
							holder.hi_img, this, false);
					break;
				}
			}

		} else {

			holder.ll_video_play.setVisibility(View.VISIBLE);
			holder.hi_img.setImageResource(R.drawable.vedio_list_bg);
		}

		return convertView;
	}

	public final class ViewHolder {

		public TextView hi_title, hi_brief_introduction;
		public ImageView hi_img;
		public LinearLayout ll_video_play;

	}

}
