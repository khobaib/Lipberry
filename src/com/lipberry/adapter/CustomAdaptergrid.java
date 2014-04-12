package com.lipberry.adapter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lipberry.R;
import com.lipberry.model.ImageScale;

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

public class CustomAdaptergrid extends BaseAdapter {
	ArrayList<String> list;
	Activity activity;
	
	//File file = new File(selectedFilePath);
	//boolean deleted = file.delete();
	
	public CustomAdaptergrid(Activity activity,
			ArrayList<String> list) {
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
		ImageView imag_inflate;
		ImageView image_cut;
		
	
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grid_inflate,
					null);
			holder = new ViewHolder();
			holder.imag_inflate=(ImageView) convertView.findViewById(R.id.imag_inflate);
			holder.image_cut=(ImageView) convertView.findViewById(R.id.image_cut);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
	//	Bitmap.Cr
		ImageScale bitmapimage =new ImageScale();
		Bitmap bitmap=bitmapimage.decodeImage(list.get(position));
		//Bitmap bitmap = decodeFile(new File(list.get(position)), 100);
		holder.image_cut.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				File file = new File(list.get(position));
				boolean deleted = file.delete();
				Log.e("isdeleted", ""+deleted);
			//	String deletedfile=list.get(position);
				list.remove(position);
				notifyDataSetChanged();
				
			}
		});
		holder.imag_inflate.setImageBitmap(bitmap);
		return convertView;
	}
	
	
	private Bitmap decodeFile(File f, int imageQuality) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = imageQuality;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;

			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			Log.i("SCALE", "scale = " + scale);

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	

}
