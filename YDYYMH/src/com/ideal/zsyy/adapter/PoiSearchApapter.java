package com.ideal.zsyy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.search.poisearch.PoiItem;
import com.fang.sbas.R;

public class PoiSearchApapter extends BaseAdapter {

	private List<PoiItem> poiItems;
	private Context context;
	private LayoutInflater inflater;

	public PoiSearchApapter(Context context, List<PoiItem> poiItems) {

		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.poiItems = poiItems;

	}

	public int getCount() {

		return poiItems.size();

	}

	public Object getItem(int position) {

		return poiItems.get(position);

	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.poisearch_item, null);
			holder = new ViewHolder();

			holder.poisearch_item_one = (TextView) convertView
					.findViewById(R.id.poisearch_item_one);
			holder.poisearch_item_second = (TextView) convertView
					.findViewById(R.id.poisearch_item_second);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.poisearch_item_one.setText(poiItems.get(position).getTitle());

		holder.poisearch_item_second.setText("地址："+poiItems.get(position)
				.getSnippet() + "  距离：" + poiItems.get(position).getDistance()+"米");

		return convertView;
	}

	public final class ViewHolder {

		public TextView poisearch_item_one, poisearch_item_second;

	}

}
