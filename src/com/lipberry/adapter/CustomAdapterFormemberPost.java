package com.lipberry.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lipberry.R;
import com.lipberry.model.Article;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CustomAdapterFormemberPost extends BaseAdapter {
	ArrayList<Article> list;
	Activity activity;
	ImageLoader imageLoader;
	public CustomAdapterFormemberPost(Activity activity,
			ArrayList<Article> list) {
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
		TextView txt_article_title;
		TextView txt_article_created;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.image_inflate_member_post,
					null);
			holder = new ViewHolder();
			holder.img_thumb=(ImageView) convertView.findViewById(R.id.img_thumb);
			holder.txt_article_created=(TextView) convertView.findViewById(R.id.txt_article_created);
			holder.txt_article_title=(TextView) convertView.findViewById(R.id.txt_article_title);
			holder.txt_article_created.setTypeface(Utility.getTypeface2(activity));
			holder.txt_article_created.setTypeface(Utility.getTypeface2(activity));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_article_created.setText(list.get(position).getCreated_at());
		holder.txt_article_title.setText(list.get(position).getArticle_title());
		if(list.get(position).getArticle_photo()==null){

		}
		else{
			imageLoader.displayImage(list.get(position).getArticle_photo(), holder.img_thumb);
		}
		return convertView;
	}
}
