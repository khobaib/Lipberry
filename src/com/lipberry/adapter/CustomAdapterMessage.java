package com.lipberry.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.model.ArticleGallery;
import com.lipberry.model.InboxMessage;
import com.lipberry.model.Notifications;
import com.lipberry.model.TndividualThreadMessage;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
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

public class CustomAdapterMessage extends BaseAdapter {
	ArrayList<TndividualThreadMessage>list;
	Activity activity;
	ImageLoader imageLoader;
	LipberryApplication appInstance;
	public CustomAdapterMessage(Activity activity,
		ArrayList<TndividualThreadMessage>list) {
		super();
		this.list=list;
		this.activity=activity;
		appInstance = (LipberryApplication) activity.getApplication();
//		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//		.cacheInMemory(false).cacheOnDisc(false).build();
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				activity.getApplicationContext()).defaultDisplayImageOptions(
//						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
//		ImageLoader.getInstance().init(config);

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
		ImageView iv_pic,iv_pic_own;
		RelativeLayout re_top;
		TextView tv_name,tv_timestamp,tv_conv_desc;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_conversation,
					null);
			holder = new ViewHolder();
			holder.re_top=(RelativeLayout) convertView.findViewById(R.id.re_top);
			holder.iv_pic=(ImageView) convertView.findViewById(R.id.iv_pic);
			holder.iv_pic_own=(ImageView) convertView.findViewById(R.id.iv_pic_own);
			holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_timestamp=(TextView) convertView.findViewById(R.id.tv_timestamp);
			holder.tv_conv_desc=(TextView) convertView.findViewById(R.id.tv_conv_desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(list.get(position).getFrom_id().equals(appInstance.getUserCred().getId())){
			holder.iv_pic.setVisibility(View.GONE);
			holder.iv_pic_own.setVisibility(View.VISIBLE);
			holder.tv_conv_desc.setBackgroundResource(R.drawable.rounded_text_gren);
			imageLoader.displayImage(list.get(0).getFrom_avatar(),holder.iv_pic_own);
		}
		else{
			holder.iv_pic_own.setVisibility(View.GONE);
			holder.iv_pic.setVisibility(View.VISIBLE);
			holder.tv_conv_desc.setBackgroundResource(R.drawable.rounded_text_pink);
			imageLoader.displayImage(list.get(0).getTo_avatar(), holder.iv_pic);
		}
		
		holder.tv_name.setText(list.get(position).getFrom_nickname());
		holder.tv_timestamp.setText(list.get(position).getCreated_at());
		holder.tv_conv_desc.setText(list.get(position).getMessage());
		holder.tv_name.setTypeface(Utility.getTypeface1(activity));
		holder.tv_timestamp.setTypeface(Utility.getTypeface1(activity));
		holder.tv_conv_desc.setTypeface(Utility.getTypeface1(activity));
		holder.tv_name.setTypeface(Utility.getTypeface1(activity));
		holder.tv_timestamp.setTypeface(Utility.getTypeface1(activity));
		holder.tv_conv_desc.setMovementMethod(LinkMovementMethod.getInstance());
		ShowHtmlText showtext=new ShowHtmlText(holder.tv_conv_desc,activity);
		showtext.updateImages(true,list.get(position).getMessage());
		return convertView;
	}
}
