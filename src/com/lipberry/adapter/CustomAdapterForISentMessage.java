package com.lipberry.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.fragment.FragmentInbox;
import com.lipberry.fragment.FragmentSentMessage;
import com.lipberry.model.ArticleGallery;
import com.lipberry.model.InboxMessage;
import com.lipberry.model.Notifications;
import com.lipberry.utility.Constants;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapterForISentMessage extends BaseAdapter {
	ArrayList<InboxMessage> list;
	Activity activity;
	FragmentSentMessage inbox;
	ImageLoader imageLoader;
	DisplayImageOptions defaultOptions;
	public CustomAdapterForISentMessage(Activity activity,
			ArrayList<InboxMessage> list,FragmentSentMessage inbox) {
		super();
		this.list=list;
		this.activity=activity;
		this.inbox=inbox;
		defaultOptions = new DisplayImageOptions.Builder()
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
		RelativeLayout re_top;
		TextView text_name,text_msz,text_time;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.inbox_inflate,
					null);
			holder = new ViewHolder();
			holder.re_top=(RelativeLayout) convertView.findViewById(R.id.re_top);
			holder.img_pro_pic=(ImageView) convertView.findViewById(R.id.img_pro_pic);
			holder.text_msz=(TextView) convertView.findViewById(R.id.text_msz);
			holder.text_name=(TextView) convertView.findViewById(R.id.text_name);
			holder.text_time=(TextView) convertView.findViewById(R.id.text_time);
			holder.text_msz.setTypeface(Utility.getTypeface2(activity));
			holder.text_name.setTypeface(Utility.getTypeface2(activity));
			holder.text_time.setTypeface(Utility.getTypeface2(activity));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.re_top.setOnClickListener(new OnClickListener() {

             @Override
             public void onClick(View arg0) {
            	 inbox.loadthreadmessage(position) ;   
               }
         });
		holder.text_msz.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
               inbox.loadthreadmessage(position) ;   
              
              }
        });
		imageLoader.displayImage(list.get(position).getTo_avatar(),  holder.img_pro_pic, defaultOptions, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				Bitmap bitmap=loadedImage;
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		});
		holder.img_pro_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.userid=list.get(position).getTo_id();
				Constants.GO_MEMBER_STATE_FROM_IMESSAGE=true;
				((HomeActivity)activity).mTabHost.setCurrentTab(4);

			}
		});
		if(list.get(position).getRead_flag().equals("0")){
			//holder.text_name.setTextColor(Color.parseColor("#5D933D"));
			holder.text_msz.setTextColor(Color.parseColor("#000000"));
			holder.text_time.setTextColor(Color.parseColor("#000000"));
			holder.re_top.setBackgroundColor(Color.parseColor("#ffffff"));
		}else{
			holder.text_msz.setTextColor(Color.parseColor("#000000"));
			holder.text_time.setTextColor(Color.parseColor("#000000"));
			holder.re_top.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		holder.text_time.setText(list.get(position).getCreated_at());
		holder.text_msz.setText(list.get(position).getMessage());
		holder.text_msz.setTypeface(Utility.getTypeface1(activity));
		holder.text_msz.setMovementMethod(LinkMovementMethod.getInstance());
		ShowHtmlText showtext=new ShowHtmlText(holder.text_msz,activity);
		showtext.updateImages(true,list.get(position).getMessage());
		holder.text_name.setText(list.get(position).getTo_nickname());
		holder.text_name.setTypeface(Utility.getTypeface1(activity));
		return convertView;
	}
}
