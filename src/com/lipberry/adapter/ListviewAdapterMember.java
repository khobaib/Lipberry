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

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.WebViewActtivity;
import com.lipberry.fragment.HomeTabFragment;
import com.lipberry.model.Article;
import com.lipberry.model.Member;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListviewAdapterMember extends BaseAdapter {
	ArrayList<Member> list;
	FragmentManager fragmentManager = null;
	FragmentActivity activity;
	Context context;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	ProgressDialog pd;
	int a;
	ViewHolder holder;
	ProgressDialog mProgress;
	ImageLoader imageLoader;
	int index;
HomeTabFragment parent;
	public ListviewAdapterMember(FragmentActivity activity,
			ArrayList<Member> list,Context context,HomeTabFragment parent) {
		super();
		appInstance = (LipberryApplication) activity.getApplication();

		jsonParser=new JsonParser();
this.parent=parent;
		this.activity = activity;
		this.list = list;
		this.context=context;
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
		Button btn_follow;
		TextView text_member_name;
		TextView text_member_bio;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent2) {
		LayoutInflater inflater = activity.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.member_inflate,
					null);
			holder = new ViewHolder();
			holder.img_pro_pic=(ImageView) convertView.findViewById(R.id.img_pro_pic);
			holder.btn_follow= (Button) convertView.findViewById(R.id.btn_follow);
			holder.text_member_name=(TextView) convertView.findViewById(R.id.text_member_name);
			holder.text_member_bio=(TextView) convertView.findViewById(R.id.text_member_bio);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text_member_name.setText(list.get(position).getMember_nickname());
		holder.text_member_bio.setText(list.get(position).getMember_bio());
		holder.text_member_name.setTypeface(Utility.getTypeface2(activity));
		holder.text_member_bio.setTypeface(Utility.getTypeface2(activity));
		holder.btn_follow.setTypeface(Utility.getTypeface2(activity));
		holder.btn_follow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.isOnline(activity)){
					pd=ProgressDialog.show(activity,activity.getResources().getString(R.string.app_name_arabic),
							activity.getResources().getString(R.string.txt_please_wait), false);
					index=position;
					new AsyncTaskSendFollowReq(holder.btn_follow).execute();
				}

			}
		});

		setimageinimageview(position);

		holder.img_pro_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constants.userid=list.get(position).getMember_id();
				parent.startMemberFragment(0);
			}
		});
		holder.text_member_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constants.userid=list.get(position).getMember_id();
				parent.startMemberFragment(0);
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
				if((mProgress.isShowing())&&(mProgress!=null)){
					mProgress.dismiss();
				}
				Bitmap bitmap=loadedImage;
				holder.img_pro_pic.setImageBitmap(bitmap);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				if((mProgress.isShowing())&&(mProgress!=null)){
					mProgress.dismiss();
				}
			}
		};
		if(list.get(position).getMember_photo()==null){

		}
		else{
			imageLoader.loadImage(list.get(position).getMember_photo(),imll);
		}

		return convertView;
	}

	public void imageviewarticlepicclicked(){

	}

	public void imageviewcommentsclicked(){

	}
	public void imgeviewlikeclicked(){

	}

	public void setimageinimageview(int index){

	}

	private class AsyncTaskSendFollowReq extends AsyncTask<Void, Void, ServerResponse> {
		Button btn_follow;
		public AsyncTaskSendFollowReq(Button btn_follow ){
			this.btn_follow=btn_follow;
		}
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/followmember/"+list.get(index).getMember_id()+"/";
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
					Toast.makeText(activity,description, Toast.LENGTH_SHORT).show();
					btn_follow.setText(activity.getResources().getString(R.string.txt_following));

				}
				else{
					Toast.makeText(activity,description, Toast.LENGTH_SHORT).show();

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
