package com.lipberry.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.model.ArticleGallery;
import com.lipberry.model.Notifications;
import com.lipberry.utility.Constants;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.method.LinkMovementMethod;


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
import android.widget.RelativeLayout;
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
		.cacheInMemory(false).cacheOnDisc(false).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				activity.getApplicationContext()).defaultDisplayImageOptions(
						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);
		for(int i=0;i<list.size();i++){
			String msz=list.get(i).getMessage().substring(list.get(i).getMessage().indexOf("</a>"));
			list.get(i).setMessage(msz);
		}
		

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
		RelativeLayout re_top;
		TextView text_msz,text_date_other,text_name;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.interaction_inflate,
					null);
			holder = new ViewHolder();
			holder.re_top=(RelativeLayout) convertView.findViewById(R.id.re_top);
			holder.img_pro_pic=(ImageView) convertView.findViewById(R.id.img_pro_pic);
			holder.text_msz=(TextView) convertView.findViewById(R.id.text_msz);
			holder.text_name=(TextView) convertView.findViewById(R.id.text_name);
			holder.text_date_other=(TextView) convertView.findViewById(R.id.text_date_other);
			holder.text_msz.setTypeface(Utility.getTypeface2(activity));
			holder.text_date_other.setTypeface(Utility.getTypeface2(activity));
			holder.text_name.setTypeface(Utility.getTypeface2(activity));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(list.get(position).getFrom_avatar()==null){
		}
		else{
			imageLoader.displayImage(list.get(position).getFrom_avatar(),holder.img_pro_pic);
		}
		if(list.get(position).geTread_flag().equals("0")){
			holder.text_msz.setTextColor(Color.parseColor("#ffffff"));
			holder.re_top.setBackgroundColor(Color.parseColor("#F0E68C"));
		}else{
			holder.text_msz.setTextColor(Color.parseColor("#000000"));
			holder.re_top.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		holder.text_name.setText(list.get(position).getFrom_username());
		holder.text_msz.setText(list.get(position).getMessage());
		ShowHtmlText showtext=new ShowHtmlText(holder.text_msz,activity);
		showtext.updateImages(true,list.get(position).getMessage());
	Log.e(""+position, list.get(position).getMessage());
		holder.text_date_other.setText(list.get(position).getCreated_at());
		ShowHtmlText showtext1=new ShowHtmlText(holder.text_date_other,activity);
		showtext1.updateImages(true,list.get(position).getCreated_at());
		holder.img_pro_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.userid=list.get(position).getFrom_id();
				Constants.GOMEMBERSTATEFROMINTERACTION=true;
				((HomeActivity)activity).mTabHost.setCurrentTab(4);

			}
		});
		return convertView;

	}
}
