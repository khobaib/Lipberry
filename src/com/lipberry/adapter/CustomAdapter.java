package com.lipberry.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lipberry.R;
import com.lipberry.model.ArticleGallery;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CustomAdapter extends BaseAdapter {
	ArrayList<ArticleGallery> list;
	Activity activity;
	ImageLoader imageLoader;
	public CustomAdapter(Activity activity,
			ArrayList<ArticleGallery>  list) {
		super();
		this.list=list;
		this.activity=activity;
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
		ImageView img_thumb;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.image_inflate,
					null);
			holder = new ViewHolder();
			holder.img_thumb=(ImageView) convertView.findViewById(R.id.img_thumb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(list.get(position).getImage_thumb_src()==null){
		}
		else{
			imageLoader.displayImage(list.get(position).getImage_thumb_src(), holder.img_thumb);
		}
		return convertView;
	}
}