package com.lipberry.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.model.ArticleGallery;
import com.lipberry.model.Notifications;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapterForInteraction extends BaseAdapter {
	ArrayList<Notifications> list;
	Activity activity;
	ImageLoader imageLoader;
	public CustomAdapterForInteraction(Activity activity,
			ArrayList<Notifications> list) {
		super();
		this.list=list;
		this.activity=activity;
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				activity.getApplicationContext()).defaultDisplayImageOptions(
						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);

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
		ImageView img_pro_pic;
		TextView text_msz,text_date_other;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.interaction_inflate,
					null);
			holder = new ViewHolder();
			holder.img_pro_pic=(ImageView) convertView.findViewById(R.id.img_pro_pic);
			holder.text_msz=(TextView) convertView.findViewById(R.id.text_msz);
			holder.text_date_other=(TextView) convertView.findViewById(R.id.text_date_other);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(list.get(position).getFrom_avatar()==null){
		}
		else{
			imageLoader.displayImage(list.get(position).getFrom_avatar(),holder.img_pro_pic);
		}
		holder.text_msz.setText(list.get(position).getMessage());
		ShowHtmlText showtext=new ShowHtmlText(holder.text_msz,activity);
		showtext.updateImages(true,list.get(position).getMessage());
		
		holder.text_date_other.setText(list.get(position).getCreated_at());
		ShowHtmlText showtext1=new ShowHtmlText(holder.text_date_other,activity);
		showtext1.updateImages(true,list.get(position).getCreated_at());
		return convertView;
	}
}
