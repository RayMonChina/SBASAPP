package com.ideal.zsyy.adapter;

import java.util.List;

import com.amap.api.search.poisearch.PoiItem;
import com.amap.api.search.route.Route;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.PoiSearchApapter.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NavigationAdater extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Route> routeResult;

	public NavigationAdater(Context context, List<Route> routeResult) {

		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.routeResult = routeResult;

	}

	public int getCount() {

		return routeResult.size();

	}

	public Object getItem(int position) {

		return routeResult.get(position);

	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.navigation_item, null);
			holder = new ViewHolder();

			holder.navigation_line_name = (TextView) convertView
					.findViewById(R.id.navigation_line_name);
			holder.navigation_line_content = (TextView) convertView
					.findViewById(R.id.navigation_line_content);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.navigation_line_name.setText("线路：" + (position + 1));

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < routeResult.get(position).getStepCount(); i++) {

			if (i == 0) {
				continue;
			}

			String description = i
					+ "："
					+ routeResult.get(position).getStepedDescription(i)
							.replaceAll("\n", ",");

//			Log.v("tags", "description=" + description);

			buffer.append(description);

			if (i < routeResult.get(position).getStepCount() - 1) {
				buffer.append("\n");
			}

		}

		holder.navigation_line_content.setText(buffer.toString());

		return convertView;
	}

	public final class ViewHolder {

		public TextView navigation_line_name, navigation_line_content;

	}

}
