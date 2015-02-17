package com.lipberry.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lipberry.R;
import com.lipberry.utility.Utility;

public class CustomAdapterForMenu extends BaseAdapter {
	ArrayList<String> list;
	Activity activity;
	public CustomAdapterForMenu(Activity activity,
			ArrayList<String>  list) {
		super();
		this.list=list;
		this.activity=activity;
		

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	private class ViewHolder {
		TextView tv_menu;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_textview,
					null);
			holder = new ViewHolder();
			holder.tv_menu=(TextView) convertView.findViewById(R.id.tv_menu);
			holder.tv_menu.setTypeface(Utility.getTypeface2(activity));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_menu.setText(list.get(position));
		
		return convertView;
	}
}