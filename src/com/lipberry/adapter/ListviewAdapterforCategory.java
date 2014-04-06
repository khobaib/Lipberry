package com.lipberry.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.lipberry.R;
import com.lipberry.WebViewActtivity;
import com.lipberry.model.Article;
import com.lipberry.model.Categories;
import com.lipberry.model.Member;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
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

public class ListviewAdapterforCategory extends BaseAdapter {
	ArrayList<Categories> list;
	FragmentManager fragmentManager = null;
	FragmentActivity activity;
	int a;
	ViewHolder holder;
	ProgressDialog mProgress;
	ImageLoader imageLoader;
	public ListviewAdapterforCategory(FragmentActivity activity,
			ArrayList<Categories> list) {
		super();
		this.activity = activity;
		this.list = list;
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
		ImageView img_app_icon;
		ImageView img_category_pro_pic;
		TextView txt_cat_name;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.categories_inflate,
					null);
			holder = new ViewHolder();
			holder.img_app_icon=(ImageView) convertView.findViewById(R.id.img_app_icon);
			holder.img_category_pro_pic=(ImageView) convertView.findViewById(R.id.img_category_pro_pic);
			holder.txt_cat_name=(TextView) convertView.findViewById(R.id.txt_cat_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_cat_name.setText(list.get(position).getName());


		ImageLoadingListener imll=new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				mProgress=new ProgressDialog(activity);
				mProgress.setTitle("Image is  Loading");
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {

				if((mProgress.isShowing())&&(mProgress!=null)){
					mProgress.dismiss();
				}
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub

				if((mProgress.isShowing())&&(mProgress!=null)){
					mProgress.dismiss();
				}
				Bitmap bitmap=loadedImage;
				holder.img_category_pro_pic.setImageBitmap(bitmap);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub

				if((mProgress.isShowing())&&(mProgress!=null)){
					mProgress.dismiss();
				}
			}
		};

		return convertView;
	}
}
