package com.lipberry.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.fragment.CategoryTabFragment;
import com.lipberry.fragment.FragmentArticleDetailsFromCategory;
import com.lipberry.fragment.FragmentArticleDetailsFromHome;
import com.lipberry.fragment.FragmentArticleDetailsFromInteraction;
import com.lipberry.fragment.HomeTabFragment;
import com.lipberry.model.Comments;
import com.lipberry.model.Commentslist;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CustomAdapterForComment extends BaseAdapter {
	ArrayList<Comments> list;
	Activity activity;
	String url;
	LipberryApplication appInstance;	
	ImageLoader imageLoader;
	ProgressDialog pd;
	ViewHolder holder;
	JsonParser jsonParser;
	String comment;
	EditText et_comment;
	String description;
	boolean stateoflike=false;
	int index=0;
	HomeTabFragment parent;
	CategoryTabFragment parent3;
	FragmentArticleDetailsFromCategory lisenar2;
	FragmentArticleDetailsFromHome lisenar1;
	FragmentArticleDetailsFromInteraction lisenar3;
	int from;
	public CustomAdapterForComment(Activity activity,
			ArrayList<Comments> list,String url,int form,FragmentArticleDetailsFromHome lisenar1,
			FragmentArticleDetailsFromCategory lisenar2,FragmentArticleDetailsFromInteraction lisenar3) {

		super();
		this.lisenar1=lisenar1;
		this.lisenar2=lisenar2;
		this.lisenar3=lisenar3;

		this.from=from;
		this.url=url;
		appInstance = (LipberryApplication) activity.getApplication();
		this.list=list;
		this.activity=activity;
		jsonParser=new JsonParser();
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
		ImageView img_avatar;
		ImageView img_like;
		ImageView img_comment;
	//	ImageView img_report_abuse;
		TextView txt_name;
		TextView txt_title;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

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
			//holder.img_report_abuse=(ImageView) convertView.findViewById(R.id.img_report_abuse);
			holder.txt_title.setTypeface(Utility.getTypeface2(activity));
			holder.txt_name.setTypeface(Utility.getTypeface2(activity));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(list.get(position).getlikeommentFlag()){
			holder.img_like.setImageResource(R.drawable.unlike);
		}
		holder.img_avatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callmemberpage( position);

			}
		});
		holder.txt_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callmemberpage( position);
			}
		});
		
		holder.img_like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!list.get(position).getlikeommentFlag()){
					pd=ProgressDialog.show(activity,activity.getResources().getString(R.string.app_name_arabic),
							activity.getResources().getString(R.string.txt_please_wait), false);
					pd.show();
					index=position;
					AsyncTaskLike instance=new AsyncTaskLike(holder.img_like);
					instance.execute();
				}
				else{
					Toast.makeText(activity, activity.getResources().getString(R.string.txt_already_liked), Toast.LENGTH_SHORT).show();
				}

			}
		});

		holder.img_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCustomDialog();
			}
		});
		if(from==1){
			holder.txt_title.setTextColor(Color.parseColor(""));
		}
		else{
			
		}
		holder.txt_title.setText(Html.fromHtml(list.get(position).getComment()));
		ShowHtmlText showtext=new ShowHtmlText(holder.txt_title, activity);
		showtext.updateImages(true,list.get(position).getComment());
		holder.txt_name.setText(list.get(position).getMember_name());
		
			holder.txt_name.setTextColor(Color.parseColor("#777777"));
			holder.txt_title.setTextColor(Color.parseColor("#777777"));
		
		if(list.get(position).getMember_avatar()!=null){
			imageLoader.displayImage(list.get(position).getMember_avatar(), holder.img_avatar);
		}
		return convertView;
	}
	public void callmemberpage(int position){
		if(lisenar1!=null){
			Constants.userid=list.get(position).getMember_id();
			lisenar1.parent.startMemberFragment(0);
		}
		else if(lisenar2!=null){
			Constants.userid=list.get(position).getMember_id();
			lisenar2.parent.startFragmentMemberFromCategories();
		}
		else if(lisenar3!=null){
			Constants.userid=list.get(position).getMember_id();
			lisenar3.parent.startMemberFragment(0);
		}
	}

	private class AsyncTaskLike extends AsyncTask<Void, Void, ServerResponse> {
		ImageView imgview_like;
		public AsyncTaskLike(ImageView imgview_like){
			this.imgview_like=imgview_like;
		}
		@Override
		protected ServerResponse doInBackground(Void... params) {


			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =list.get(index).getLikecomment_url();
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST,
						url, null,loginData, null);
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
			Log.e("url", result.getjObj().toString());
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				if(status.equals("success")){
					stateoflike=true;
					Log.e("imageview", "1 "+this.imgview_like);
					list.get(index).setlikeommentFlag(true);
					notifyDataSetChanged();
					Toast.makeText(activity,activity.getResources().getString(R.string.txt_suc_like_comments), 10000).show();
				}
				else{
					Toast.makeText(activity,description, 10000).show();
					if(description.equals("You presed like before")){
						stateoflike=true;
						//	holder.img_like.setBackgroundResource(R.drawable.unlike);
						//list.get(index).setUserAlreadylikeThis("Yes");
					}


				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class AsyncTaskRepoertAbuse extends AsyncTask<Void, Void, ServerResponse> {
		ImageView imgview_abuse;
		public AsyncTaskRepoertAbuse(ImageView imgview_abuse){
			this.imgview_abuse=imgview_abuse;
		}
		@Override
		protected ServerResponse doInBackground(Void... params) {


			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =list.get(index).getAbusecomment_url();
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST,
						url, null,loginData, null);
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
				if(status.equals("success")){
					this.imgview_abuse.setVisibility(View.GONE);
					list.get(index).setabusecomment_flag(true);
					notifyDataSetChanged();
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
				String url ="";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST,
						url, null,loginData, null);
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
					//	holder2.img_like.setBackgroundResource(R.drawable.like);
					//list.get(index).setUserAlreadylikeThis("No");
				}
				else{
					Toast.makeText(activity,description, 10000).show();
					if(description.equals("You pressed dislike before")){
						//	holder2.img_like.setBackgroundResource(R.drawable.like);
						stateoflike=false;
						//	list.get(index).setUserAlreadylikeThis("No");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void showCustomDialog(){
		final Dialog dialog=new Dialog(activity,R.style.CustomDialog);
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
				dialog.dismiss();
				String comments=et_comment.getText().toString();

				if(comments.equals("")){
					Toast.makeText(activity,activity.getResources().getString(R.string.Toast_enter_text),
							Toast.LENGTH_SHORT).show();
				}
				else{
					comment=et_comment.getText().toString();
					pd=ProgressDialog.show(activity,activity.getResources().getString(R.string.app_name_arabic),
							activity.getResources().getString(R.string.txt_please_wait), false);
					pd.show();
					new AsyncTaskReplyOnComments().execute();
				}

			}
		});

		dialog.show();
	}


	private class AsyncTaskReplyOnComments extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				byte[] ba = comment.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("comment", base64Str);
				String loginData = loginObj.toString();
				String url =list.get(index).getReplyon_url();
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST,
						url, null,loginData, null);
				return response;
			} catch (JSONException e) {                
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);

			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");

				if(status.equals("success")){
					new AsyncTaskGetComments().execute();
				}
				else{
					if((pd.isShowing())&&(pd!=null)){
						pd.dismiss();
					} 
					description=jobj.getString("description");
					Toast.makeText(activity,description, 10000).show();

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class AsyncTaskGetComments extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {


				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex", "0");
				loginObj.put("endIndex", ""+list.size()+1);
				String loginData = loginObj.toString();
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
			JSONObject jobj=result.getjObj();
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			try {
				String status= jobj.getString("status");
				if(status.equals("success")){
					Commentslist commentslist=Commentslist.getCommentsListInstance(jobj);
					if(commentslist.getCommentslist().size()>0){
						list.clear();
						list=commentslist.getCommentslist();
						notifyDataSetChanged();
						if(lisenar1!=null){
							lisenar1.callback();
						}
						else if(lisenar2!=null){
							lisenar2.callback();

						}
						else if(lisenar3!=null){
							lisenar3.callback();

						}
					}
				}
				else{
					String description=jobj.getString("message");
					//	Toast.makeText(activity,description, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
			}
		}
	}

}
