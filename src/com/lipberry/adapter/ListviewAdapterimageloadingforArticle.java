package com.lipberry.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.customalertdilog.LisAlertDialog;
import com.lipberry.fragment.HomeTabFragment;
import com.lipberry.fragment.CategoryTabFragment;
import com.lipberry.model.Article;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.lipberry.widzet.PanningEditText;
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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListviewAdapterimageloadingforArticle extends BaseAdapter {
	ArrayList<Article> list;

	FragmentManager fragmentManager = null;
	Activity activity;
	LipberryApplication appInstance;	
	ProgressDialog pd;
	HomeTabFragment parent;
	CategoryTabFragment parent3;
	String commentstext;
	int a;
	int positionforcomments;
	boolean stateoflike=false;
	JsonParser jsonParser;
	int index;
	ViewHolder holder2;
	ProgressDialog mProgress;
	EditText et_comment;
	ImageLoader imageLoader;

	public ListviewAdapterimageloadingforArticle(Activity activity,
			ArrayList<Article> list,CategoryTabFragment parent3) {
		super();
		jsonParser=new JsonParser();
		this.activity = activity;
		this.list = list;
		this.parent3=parent3;
		appInstance = (LipberryApplication) activity.getApplication();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				activity.getApplicationContext()).defaultDisplayImageOptions(
						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);
	}

	public ListviewAdapterimageloadingforArticle(Activity activity,
			ArrayList<Article> list,HomeTabFragment parent) {
		super();
		jsonParser=new JsonParser();
		this.activity = activity;
		this.list = list;
		this.parent=parent;
		appInstance = (LipberryApplication) activity.getApplication();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(false).cacheOnDisc(false).build();
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
		ImageView img_some_icon;
		ImageView img_like;
		ImageView image_comments;
		ImageView img_article_pro_pic;
		TextView text_user_name;
		TextView text_date_other;
		TextView txt_loading;
		ProgressBar progress_loading;
		TextView txt_articl_ename;
		TextView text_topic_text;
		TextView txt_like;
		TextView text_comment;
	
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup viewgroup) {
		LayoutInflater inflater = activity.getLayoutInflater();
		final ViewHolder holder ;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.post_inflate,
					null);
			holder = new ViewHolder();
			holder.progress_loading=(ProgressBar) convertView.findViewById(R.id.progress_loading);
			holder.txt_loading=(TextView) convertView.findViewById(R.id.txt_loading);
			holder.img_pro_pic=(ImageView) convertView.findViewById(R.id.img_pro_pic);
			holder.img_some_icon=(ImageView) convertView.findViewById(R.id.img_some_icon);
			holder.img_like=(ImageView) convertView.findViewById(R.id.img_like);
			holder.image_comments=(ImageView) convertView.findViewById(R.id.image_comments);
			holder.img_article_pro_pic=(ImageView) convertView.findViewById(R.id.img_article_pro_pic);
			
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
				positionforcomments=position;
				index=position;
				showCustomDialog();

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
		if(list.get(position).getcategory().equals("2")){
			if(list.get(position).getArticle_category_url().contains("shexp")){
				int id = activity.getResources().getIdentifier("l"+list.get(position).getcategory(), "drawable", activity.getPackageName());
				Log.e("category", "null"+list.get(position).getcategory());
				holder.img_some_icon.setImageResource(id);
			}
			else{
				int id = activity.getResources().getIdentifier("bl"+list.get(position).getcategory(), "drawable", activity.getPackageName());
				Log.e("category", "null"+list.get(position).getcategory());
				holder.img_some_icon.setImageResource(id);
			}
		}else{
			int id = activity.getResources().getIdentifier("l"+list.get(position).getcategory(), "drawable", activity.getPackageName());
			Log.e("category", "null"+list.get(position).getcategory());
			holder.img_some_icon.setImageResource(id);
		}
		if(list.get(position).getMember_nickname()!=null){
			holder.text_user_name.setText(list.get(position).getMember_nickname());
		}
		
		holder.text_date_other.setText(list.get(position).getCreated_at());
		holder.txt_articl_ename.setText(list.get(position).getArticle_title());
		holder.txt_articl_ename.setTypeface(Utility.getTypeface2(activity));
		holder.text_topic_text.setTypeface(Utility.getTypeface2(activity));
		holder.text_topic_text.setText(Html.fromHtml(list.get(position).getArticle_description()));
		holder.text_topic_text.setMovementMethod(LinkMovementMethod.getInstance());
		ShowHtmlText showtext=new ShowHtmlText(holder.text_topic_text, activity);
		showtext.updateImages(true,list.get(position).getArticle_description());
		holder.text_comment.setText(list.get(position).getComment_count()+ " "+activity.getResources().
				getString(R.string.txt_comments));
		String liketext="";
		holder.text_comment.setTypeface(Utility.getTypeface2(activity));
		holder.txt_like.setTypeface(Utility.getTypeface2(activity));
		holder.txt_like.setText(list.get(position).getLikemember_text());
		holder.txt_like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LisAlertDialog alert;
				if(list.get(position).getLikedmemberlist().size()>0){
					if(parent==null){
						alert=new LisAlertDialog(activity, list.get(position).getLikedmemberlist(),activity,null,parent3);

					}
					else{
						alert=new LisAlertDialog(activity, list.get(position).getLikedmemberlist(),activity,parent,null);
					}

					alert.show_alert();
				}
				else{

				}
			}
		});
		holder.img_article_pro_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imageviewarticlepicclicked(position);
			}
		});
		holder.txt_articl_ename.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(parent3==null){
					if(list.get(position)!=null){
						parent.startFragmentArticleDetailsFromHome(list.get(position));
					}
					
				}
				else{
					if(list.get(position)!=null){
						parent3.startFragmentArticleDetails(list.get(position));

					}
				}
			}
		});
		holder.text_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				imageviewcommentsclicked();
			}
		});

		if(list.get(position).getUserAlreadylikeThis().equals("No")){
			stateoflike=false;
		}
		else{
			stateoflike=true;
		}
		if(!list.get(position).getUserAlreadylikeThis().equals("No")){
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
			//	mProgress.show();
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {

				if((mProgress.isShowing())&&(mProgress!=null)){
					mProgress.dismiss();
				}
				holder.txt_loading.setVisibility(View.GONE);
				holder.progress_loading.setVisibility(View.GONE);
				holder.img_article_pro_pic.setImageResource(R.drawable.noimage);

			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub

				if((mProgress.isShowing())&&(mProgress!=null)){
					mProgress.dismiss();
				}
				Bitmap bitmap=loadedImage;
				
				if(bitmap!=null){
					int bitmapheight=bitmap.getHeight();
					int bitmapweight=bitmap.getWidth();
					int deviceheight=Utility.getDeviceHeight(activity);
					int devicewidth=Utility.getDeviceWidth(activity);
					float ratio=(float)devicewidth/(float)bitmapweight;
					int resizebitmapwidth=devicewidth;
					float a=(bitmapheight*ratio);
					int resizebitmaphight=(int)a ;
					Log.e("image dim", "fhcbvfh  "+bitmapheight+"  "+resizebitmaphight+"  "+bitmapweight+"  "+resizebitmapwidth);
					bitmap=Bitmap.createScaledBitmap(bitmap,resizebitmapwidth,resizebitmaphight, false);
					holder.img_article_pro_pic.setImageBitmap(bitmap);
					holder.txt_loading.setVisibility(View.GONE);
					holder.progress_loading.setVisibility(View.GONE);
				
				}
				
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				holder.txt_loading.setVisibility(View.GONE);
				holder.progress_loading.setVisibility(View.GONE);
				holder.img_article_pro_pic.setImageResource(R.drawable.noimage);
				if((mProgress.isShowing())&&(mProgress!=null)){
					mProgress.dismiss();
				}
			}
		};
		Log.i("x "+position, "null  "+list.get(position).getArticle_photo());
		if((list.get(position).getArticle_photo()==null)||(list.get(position).getArticle_photo().equals(""))){
			holder.txt_loading.setVisibility(View.GONE);
			holder.progress_loading.setVisibility(View.GONE);
			holder.img_article_pro_pic.setImageResource(R.drawable.noimage);
		}
		else{
			holder.txt_loading.setVisibility(View.VISIBLE);
			holder.progress_loading.setVisibility(View.VISIBLE);
			imageLoader.loadImage(list.get(position).getArticle_photo(),imll);


		}

		holder.text_user_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(parent!=null){
					Constants.userid=list.get(position).getMember_id();
					parent.startMemberFragment();
					
				}
				else{
					Constants.userid=list.get(position).getMember_id();
					parent3.startFragmentMemberFromCategories();
				}

			}
		});

		holder.img_pro_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(parent!=null){
					Constants.userid=list.get(position).getMember_id();
					parent.startMemberFragment();
				}
				else{
					Constants.userid=list.get(position).getMember_id();
					parent3.startFragmentMemberFromCategories();
				}

			}
		});
		holder.img_some_icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(parent!=null){
					
					Constants.catgeory=true;
					Constants.caturl=list.get(position).getArticle_category_url();
					Constants.caname=list.get(position).getCategory_name();
							
					((HomeActivity)activity).mTabHost.setCurrentTab(3);
					
				}
				
				
			}
		});

		Log.i(" image loading", list.get(position).getArticle_photo());
		return convertView;
	}

	public void imageviewarticlepicclicked(int position){
		if(parent3==null){
			if(list.get(position)!=null){
				parent.startFragmentArticleDetailsFromHome(list.get(position));
			}
			
		}
		else{
			if(list.get(position)!=null){
				parent3.startFragmentArticleDetails(list.get(position));

			}
		}
	}

	public void imageviewcommentsclicked(){

	}
	public void imgeviewlikeclicked(){
		Log.i("before", ""+stateoflike);
		if(!list.get(index).getUserAlreadylikeThis().equals("No")){
			if(Constants.isOnline(activity)){
				pd=ProgressDialog.show(activity, activity.getResources().getString(R.string.app_name_arabic),
						activity.getResources().getString(R.string.txt_please_wait), false);
				new AsyncTaskSetDislike().execute();

			}
			else{

				Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}

		}
		else{

			if(Constants.isOnline(activity)){
				pd=ProgressDialog.show(activity, activity.getResources().getString(R.string.app_name_arabic),
						activity.getResources().getString(R.string.txt_please_wait), false);
				new AsyncTaskSeLike().execute();

			}
			else{

				Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void setimageinimageview(int index){
	}

	public void sendposttoserver(){
		if(Constants.isOnline(activity)){
			pd=ProgressDialog.show(activity, activity.getResources().getString(R.string.app_name_arabic),
					activity.getResources().getString(R.string.txt_please_wait), false);
			new AsyncTaskPostComments().execute();
		}
		else{

			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
	}
	private class AsyncTaskPostComments extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				byte[] ba = commentstext.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("comment", base64Str);
				String loginData = loginObj.toString();
				String url =list.get(index).getComment_url();
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
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
				if(status.equals("success")){
					Toast.makeText(activity,activity.getResources().getString(R.string.txt_comment), Toast.LENGTH_SHORT).show();
					if(parent3==null){
						if(list.get(positionforcomments)!=null){
							parent.startFragmentArticleDetailsFromHome(list.get(positionforcomments));
						}
						
					}
					else{
						if(list.get(positionforcomments)!=null){
							parent3.startFragmentArticleDetails(list.get(positionforcomments));

						}
					}
				}
				else{
					String description=jobj.getString("description");
					Toast.makeText(activity,description, Toast.LENGTH_SHORT).show();
				}
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
				String url =list.get(index).getLike_url();
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
					Toast.makeText(activity,description, Toast.LENGTH_SHORT).show();
					holder2.img_like.setBackgroundResource(R.drawable.unlike);
					list.get(index).setUserAlreadylikeThis("Yes");
				}
				else{
					Toast.makeText(activity,description, Toast.LENGTH_SHORT).show();
					if(description.equals(activity.getResources().getString(R.string.txt_already_liked))){
						stateoflike=true;
						holder2.img_like.setBackgroundResource(R.drawable.unlike);
						list.get(index).setUserAlreadylikeThis("Yes");
					}


				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("after like", ""+stateoflike);	

		}


	}
	private class AsyncTaskSetDislike extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =list.get(index).getDislike_url();
				Log.i("url", url);
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
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
					Toast.makeText(activity,description, Toast.LENGTH_SHORT).show();
					holder2.img_like.setBackgroundResource(R.drawable.like);
					list.get(index).setUserAlreadylikeThis("No");
				}
				else{
					Toast.makeText(activity,description, 10000).show();
					if(description.equals("You pressed dislike before")){
						holder2.img_like.setBackgroundResource(R.drawable.like);
						stateoflike=false;
						list.get(index).setUserAlreadylikeThis("No");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void showCustomDialog(){
		final Dialog dialog = new Dialog(activity);
		dialog.setContentView(R.layout.custom_dilog);
		dialog.setTitle(activity.getResources().getString(R.string.app_name_arabic));
		et_comment =  (EditText) dialog.findViewById(R.id.et_comment);
		Button  btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		Button  bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String comments=et_comment.getText().toString();
				if(comments.equals("")){
					Toast.makeText(activity, activity.getResources().getString(R.string.Toast_enter_text),
							Toast.LENGTH_SHORT).show();
				}
				else{
					dialog.dismiss();
					commentstext=comments;
					sendposttoserver();
				}
			}
		});

		dialog.show();
	}
}
