package com.lipberry.adapter;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lipberry.R;
import com.lipberry.model.Categories;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

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
//		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//		.cacheInMemory(true).cacheOnDisc(true).build();
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
		ImageView img_big_img;
	 
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
			holder.img_big_img=(ImageView) convertView.findViewById(R.id.img_big_img);
			holder.img_category_pro_pic=(ImageView) convertView.findViewById(R.id.img_category_pro_pic);
			holder.txt_cat_name=(TextView) convertView.findViewById(R.id.txt_cat_name);
			holder.txt_cat_name.setTypeface(Utility.getTypeface1(activity));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_cat_name.setText(list.get(position).getName());
		if(list.get(position).getId().equals("2")){
			if(list.get(position).getPrefix().equalsIgnoreCase("beauty")){
				int id = activity.getResources().getIdentifier("bl"+list.get(position).getId(), "drawable", activity.getPackageName());
				holder.img_category_pro_pic.setImageResource(id);
				
				id = activity.getResources().getIdentifier("bll"+list.get(position).getId(), "drawable", activity.getPackageName());
				Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),id);
				if(bitmap!=null){
					int bitmapheight=bitmap.getHeight();
					int bitmapweight=bitmap.getWidth();
					int deviceheight=Utility.getDeviceHeight(activity);
					int devicewidth=Utility.getDeviceWidth(activity);
					float ratio=(float)devicewidth/(float)bitmapweight;
					int resizebitmapwidth=devicewidth;
					float a=(bitmapheight*ratio);
					int resizebitmaphight=(int)a ;
					bitmap=Bitmap.createScaledBitmap(bitmap,resizebitmapwidth,resizebitmaphight, false);
					holder.img_big_img.setImageBitmap(bitmap);
				}
			}
			else{
				int id = activity.getResources().getIdentifier("l"+list.get(position).getId(), "drawable", activity.getPackageName());
				holder.img_category_pro_pic.setImageResource(id);
				id = activity.getResources().getIdentifier("ll"+list.get(position).getId(), "drawable", activity.getPackageName());
				Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),id);
				if(bitmap!=null){
					int bitmapheight=bitmap.getHeight();
					int bitmapweight=bitmap.getWidth();
					int deviceheight=Utility.getDeviceHeight(activity);
					int devicewidth=Utility.getDeviceWidth(activity);
					float ratio=(float)devicewidth/(float)bitmapweight;
					int resizebitmapwidth=devicewidth;
					float a=(bitmapheight*ratio);
					int resizebitmaphight=(int)a ;
					bitmap=Bitmap.createScaledBitmap(bitmap,resizebitmapwidth,resizebitmaphight, false);
					holder.img_big_img.setImageBitmap(bitmap);
				}
			}
		}
		else{
			int id = activity.getResources().getIdentifier("l"+list.get(position).getId(), "drawable", activity.getPackageName());
			holder.img_category_pro_pic.setImageResource(id);
			id = activity.getResources().getIdentifier("ll"+list.get(position).getId(), "drawable", activity.getPackageName());
			
			Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),id);
			if(bitmap!=null){
				int bitmapheight=bitmap.getHeight();
				int bitmapweight=bitmap.getWidth();
				int deviceheight=Utility.getDeviceHeight(activity);
				int devicewidth=Utility.getDeviceWidth(activity);
				float ratio=(float)devicewidth/(float)bitmapweight;
				int resizebitmapwidth=devicewidth;
				float a=(bitmapheight*ratio);
				int resizebitmaphight=(int)a ;
				bitmap=Bitmap.createScaledBitmap(bitmap,resizebitmapwidth,resizebitmaphight, false);
				holder.img_big_img.setImageBitmap(bitmap);
			}
			//holder.img_big_img.setImageResource(id);
		}
		
		

		return convertView;
	}
}
