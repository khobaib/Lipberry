package com.lipberry.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lipberry.R;
import com.lipberry.model.ArticleGallery;
import com.lipberry.model.Comments;
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

public class CustomAdapterForComment extends BaseAdapter {
	ArrayList<Comments> list;
	Activity activity;
	ImageLoader imageLoader;
	public CustomAdapterForComment(Activity activity,
			ArrayList<Comments>  list) {
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
		ImageView img_avatar;
		ImageView img_like;
		ImageView img_comment;
		TextView txt_name;
		TextView txt_title;

	
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.comments_inflate,
					null);
			holder = new ViewHolder();
			holder.img_avatar=(ImageView) convertView.findViewById(R.id.img_avatar);
			holder.img_like=(ImageView) convertView.findViewById(R.id.img_like);
			holder.img_comment=(ImageView) convertView.findViewById(R.id.img_comment);
			holder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);
			holder.txt_title=(TextView) convertView.findViewById(R.id.txt_title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_title.setText(list.get(position).getComment());
		holder.txt_name.setText(list.get(position).getMember_name());
		
		if(list.get(position).getMember_avatar()!=null){
			imageLoader.displayImage(list.get(position).getMember_avatar(), holder.img_avatar);
			
			
		}
		
	return convertView;
	}
	
	

}
