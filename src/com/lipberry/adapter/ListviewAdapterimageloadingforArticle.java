package com.lipberry.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.lipberry.R;
import com.lipberry.model.Article;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.Activity;
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
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListviewAdapterimageloadingforArticle extends BaseAdapter {
	ArrayList<Article> list;
	FragmentManager fragmentManager = null;
	FragmentActivity activity;
	LipberryApplication appInstance;	
	ProgressDialog pd;
	String commentstext;
	int a;
	boolean stateoflike=false;
	JsonParser jsonParser;
	int index;
	ViewHolder holder2;
	ProgressDialog mProgress;
	ImageLoader imageLoader;
	public ListviewAdapterimageloadingforArticle(FragmentActivity activity,
			ArrayList<Article> list) {
		super();
		jsonParser=new JsonParser();
		this.activity = activity;
		this.list = list;
		appInstance = (LipberryApplication) activity.getApplication();
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
	}/*
	03-19 23:43:36.661: D/rtes(6281): {"status":"success","article_list":[[{"article_id":"87"},{"article_title":"اريد حل سريع لانقاص الوزن وتبييض المناطق الحساسة"},{"article_url":"http:\/\/www.lipberry.com\/SharingExperience\/experiencePage\/87\/اريد-حل-سريع-لانقاص-الوزن-وتبييض-المناطق-الحساسة\/"},{"article_photo":"http:\/\/www.lipberry.com\/themes\/default\/images\/article_default_thumb.jpg"},{"article_description":"\r\n\tاريد حل سريع لانقاص الوزن ووصفات سريعة لتبييض المناطق"},{"like_count":"10"},{"likemember_array":[[{"likemember_nickname":"Princess Zozo"},{"likemember_url":"http:\/\/www.lipberry.com\/user\/u1039\/"}],[{"likemember_nickname":"لبني"},{"likemember_url":"http:\/\/www.lipberry.com\/user\/u2778\/"}],[{"likemember_nickname":"سيده بلون الذهب"},{"likemember_url":"http:\/\/www.lipberry.com\/user\/u2606\/"}]]},{"comment_count":"13"},{"category_name":"هل لديك سؤال للمجربات؟ اسئلي المشتركات"}]]}

	*/

	private class ViewHolder {
		ImageView img_pro_pic;
		ImageView img_some_icon;
		ImageView img_like;
		ImageView image_comments;
		ImageView img_article_pro_pic;
		TextView text_user_name;
		TextView text_date_other;
		TextView txt_articl_ename;
		TextView text_topic_text;
		TextView txt_like;
		TextView text_comment;
		EditText et_comment;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
	
		LayoutInflater inflater = activity.getLayoutInflater();
		final ViewHolder holder ;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.post_inflate,
					null);
		 holder = new ViewHolder();
			
			holder.img_pro_pic=(ImageView) convertView.findViewById(R.id.img_pro_pic);
			holder.img_some_icon=(ImageView) convertView.findViewById(R.id.img_some_icon);
			holder.img_like=(ImageView) convertView.findViewById(R.id.img_like);
			holder.image_comments=(ImageView) convertView.findViewById(R.id.image_comments);
			holder.img_article_pro_pic=(ImageView) convertView.findViewById(R.id.img_article_pro_pic);
			holder.et_comment=(EditText) convertView.findViewById(R.id.et_comment);
			holder.text_user_name=(TextView) convertView.findViewById(R.id.text_user_name);
			holder.text_date_other=(TextView) convertView.findViewById(R.id.text_date_other);
			holder.txt_articl_ename=(TextView) convertView.findViewById(R.id.txt_articl_ename);
			holder.text_topic_text=(TextView) convertView.findViewById(R.id.text_topic_text);
			holder.txt_like=(TextView) convertView.findViewById(R.id.txt_like);
			holder.text_comment=(TextView) convertView.findViewById(R.id.text_comment);
			convertView.setTag(holder);
		} else {
			 holder = (ViewHolder) convertView.getTag();
		}
		
		holder.image_comments.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String comments=holder.et_comment.getText().toString();
				if(comments.equals("")){
					Toast.makeText(activity, activity.getResources().getString(R.string.Toast_enter_text), 10000).show();
					
				}
				
				else{
					commentstext=comments;
					sendposttoserver();
				}
				
			}
		});
		String url=list.get(position).getMember_photo();
		if(url==null){
			
		}
		else{
			url=url.replace("..", "http://www.lipberry.com");
		    Log.i("propicurl", url);
			imageLoader.displayImage(url, holder.img_pro_pic);
			
		}
		
		holder.text_user_name.setText("some cat");
		holder.txt_articl_ename.setText(list.get(position).getArticle_title());
		holder.text_topic_text.setText(list.get(position).getArticle_description());
		//holder.text_topic_text.setText(list.get(position).getArticle_title());

		
		holder.text_comment.setText(list.get(position).getComment_count()+ " "+activity.getResources().
				getString(R.string.txt_comments));
		
		String liketext="";
		
		if(list.get(position).getLike_count()==null){
			holder.txt_like.setText("");
			if(list.get(position).getLikedmemberlist().size()>0){
				for (int k=0;k<list.get(position).getLikedmemberlist().size();k++){
					if(k<(list.get(position).getLikedmemberlist().size()-2)){
			
							liketext=liketext.concat(list.get(position).getLikedmemberlist().get(k).getNickname());
							liketext=liketext.concat(" ,");
					}
					else if((k<(list.get(position).getLikedmemberlist().size()-1))){
						liketext=liketext.concat("&");
						liketext=liketext.concat(list.get(position).getLikedmemberlist().get(k).getNickname());
			
					}
					else{
						liketext=liketext.concat(list.get(position).getLikedmemberlist().get(k).getNickname());
					}
	
			}
			
				liketext=liketext.concat(" like this");
			
		
			holder.txt_like.setText(liketext);
			}
		}
		else if(Integer.parseInt(list.get(position).getLike_count())==0){
			holder.txt_like.setText("");
			if(list.get(position).getLikedmemberlist().size()>0){
				for (int k=0;k<list.get(position).getLikedmemberlist().size();k++){
					if(k<(list.get(position).getLikedmemberlist().size()-2)){
			
							liketext=liketext.concat(list.get(position).getLikedmemberlist().get(k).getNickname());
							liketext=liketext.concat(" ,");
					}
					else if((k<(list.get(position).getLikedmemberlist().size()-1))){
						liketext=liketext.concat("&");
						liketext=liketext.concat(list.get(position).getLikedmemberlist().get(k).getNickname());
			
					}
					else{
						liketext=liketext.concat(list.get(position).getLikedmemberlist().get(k).getNickname());
					}
	
			}
			
				liketext=liketext.concat(" like this");
			
		
			holder.txt_like.setText(liketext);
			}
		}else{
			if(list.get(position).getLikedmemberlist().size()>0){
				
					for (int k=0;k<list.get(position).getLikedmemberlist().size();k++){
								if(k<(list.get(position).getLikedmemberlist().size()-2)){
						
										liketext=liketext.concat(list.get(position).getLikedmemberlist().get(k).getNickname());
										liketext=liketext.concat(" ,");
								}
								else if((k<(list.get(position).getLikedmemberlist().size()-1))){
									liketext=liketext.concat("&");
									liketext=liketext.concat(list.get(position).getLikedmemberlist().get(k).getNickname());
						
								}
								else{
									liketext=liketext.concat(list.get(position).getLikedmemberlist().get(k).getNickname());
								}
				
						}
						int likeremainingcount=Integer.parseInt(list.get(position).getLike_count())-list.get(position).getLikedmemberlist().size();
						if(likeremainingcount>0){
							liketext=liketext.concat("  " +likeremainingcount+" other like this");
						}
						else{
							liketext=liketext.concat(" like this");
						}
					
						holder.txt_like.setText(liketext);
			
			}
		}
		
		holder.img_article_pro_pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imageviewarticlepicclicked();
			}
		});
		holder.text_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imageviewcommentsclicked();
			}
		});
		
		if(stateoflike){
			holder.img_like.setBackgroundResource(R.drawable.unlike);
		}
		else{
			holder.img_like.setBackgroundResource(R.drawable.like);
		}
		
		holder.img_like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				holder2=holder;
				index=position;
				imgeviewlikeclicked();
			}
		});
		
		ImageLoadingListener imll=new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				mProgress=new ProgressDialog(activity);
				mProgress.setTitle("Image is  Loading");
				//mProgress.show();
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
				
				int bitmapheight=bitmap.getHeight();
				int bitmapweight=bitmap.getWidth();
				int deviceheight=Utility.getDeviceHeight(activity);
				int devicewidth=Utility.getDeviceWidth(activity);
				float ratio=devicewidth/bitmapweight;
				int resizebitmapwidth=bitmapweight;
				int resizebitmaphight=(int) (bitmapheight*ratio);
			
				bitmap=Bitmap.createScaledBitmap(bitmap,resizebitmapwidth,resizebitmaphight, false);
				holder.img_article_pro_pic.setImageBitmap(bitmap);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
				if((mProgress.isShowing())&&(mProgress!=null)){
					mProgress.dismiss();
				}
			}
		};
		if(list.get(position).getArticle_photo()==null){
			
		}
		else{
			imageLoader.loadImage(list.get(position).getArticle_photo(),imll);
			
			
		}
		
		Log.i(" image loading", list.get(position).getArticle_photo());
		return convertView;
	}
	
	public void imageviewarticlepicclicked(){
		
	}
	
	public void imageviewcommentsclicked(){
		
	}
	public void imgeviewlikeclicked(){
		
		if(stateoflike){
			if(Constants.isOnline(activity)){
				pd=ProgressDialog.show(activity, "Lipberry",
				    "Start dislike", true);
				new AsyncTaskSetDislike().execute();
			
			}
			else{
			
				Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet), 10000).show();
			}
			
		}
			else{
				
					if(Constants.isOnline(activity)){
						pd=ProgressDialog.show(activity, "Lipberry",
						    "Sending like", true);
						new AsyncTaskSeLike().execute();
					
					}
					else{
					
						Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet), 10000).show();
					}
			
			
		}
	}
	
	public void setimageinimageview(int index){
		
		
		
	}
	
	public void sendposttoserver(){
		if(Constants.isOnline(activity)){
			pd=ProgressDialog.show(activity, "Lipberry",
			    "Posting comments", true);
			new AsyncTaskPostComments().execute();
		
		}
		else{
		
			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet), 10000).show();
		}
	}
	
	
	
	private class AsyncTaskPostComments extends AsyncTask<Void, Void, ServerResponse> {
		@Override
					protected ServerResponse doInBackground(Void... params) {

					try {
							JSONObject loginObj = new JSONObject();
							loginObj.put("session_id", appInstance.getUserCred().getSession_id());
							loginObj.put("comment", commentstext);
							String loginData = loginObj.toString();
							String url =Constants.baseurl+list.get(index).getComment_url();
							ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
									loginData, null);

							Log.i("rtes", response.getjObj().toString());
					 return response;
					} catch (JSONException e) {                
						e.printStackTrace();
						return null;
					}
			}

			@Override
			protected void onPostExecute(ServerResponse result) {
				super.onPostExecute(result);
				if((pd.isShowing())&&(pd!=null)){
					pd.dismiss();
				}
				JSONObject jobj=result.getjObj();
				try {
					String status= jobj.getString("status");
					String description=jobj.getString("description");
					Toast.makeText(activity,description, 10000).show();
						
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
	}
	
	
	
	
	
	
	
	private class AsyncTaskSeLike extends AsyncTask<Void, Void, ServerResponse> {
		@Override
					protected ServerResponse doInBackground(Void... params) {

					try {
							JSONObject loginObj = new JSONObject();
							loginObj.put("session_id", appInstance.getUserCred().getSession_id());
							String loginData = loginObj.toString();
							String url =Constants.baseurl+list.get(index).getLike_url();
							ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
									loginData, null);

							Log.i("rtes", response.getjObj().toString());
					 return response;
					} catch (JSONException e) {                
						e.printStackTrace();
						return null;
					}
			}

			@Override
			protected void onPostExecute(ServerResponse result) {
				super.onPostExecute(result);
				if((pd.isShowing())&&(pd!=null)){
					pd.dismiss();
				}
				JSONObject jobj=result.getjObj();
				try {
					String status= jobj.getString("status");
					String description=jobj.getString("description");
					if(status.equals("success")){
						stateoflike=true;
						Toast.makeText(activity,description, 10000).show();
						holder2.img_like.setBackgroundResource(R.drawable.unlike);
						holder2.text_comment.setText("hgfcrjhflrhgf");
					}
					else{
						
						Toast.makeText(activity,description, 10000).show();
						
							if(description.equals("You presed like before")){
								holder2.img_like.setBackgroundResource(R.drawable.unlike);
								stateoflike=true;
								
						}
						
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
	}
	
	

	
	private class AsyncTaskSetDislike extends AsyncTask<Void, Void, ServerResponse> {
		@Override
					protected ServerResponse doInBackground(Void... params) {

					try {
							JSONObject loginObj = new JSONObject();
							loginObj.put("session_id", appInstance.getUserCred().getSession_id());
							String loginData = loginObj.toString();
							String url =Constants.baseurl+list.get(index).getDislike_url();
							ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
									loginData, null);

							Log.i("rtes", response.getjObj().toString());
					 return response;
					} catch (JSONException e) {                
						e.printStackTrace();
						return null;
					}
			}

			@Override
			protected void onPostExecute(ServerResponse result) {
				super.onPostExecute(result);
				if((pd.isShowing())&&(pd!=null)){
					pd.dismiss();
				}
				JSONObject jobj=result.getjObj();
				try {
					String status= jobj.getString("status");
					String description=jobj.getString("description");
					if(status.equals("success")){
						stateoflike=false;
						Toast.makeText(activity,description, 10000).show();
						holder2.img_like.setBackgroundResource(R.drawable.like);
						
					}
					else{
						
						Toast.makeText(activity,description, 10000).show();
						
							if(description.equals("You presed unlike before")){
								holder2.img_like.setBackgroundResource(R.drawable.like);
								stateoflike=false;
						}
						
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
	}
	


}
