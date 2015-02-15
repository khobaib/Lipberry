
package com.lipberry.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.adapter.CustomAdapterFormemberPost;
import com.lipberry.model.ArticleDetails;
import com.lipberry.model.ArticleList;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.SingleMember;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;



@SuppressLint("NewApi")
public class FragmentMyProfile extends Fragment {
	ImageLoader imageLoader;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	WebView webview_member;
	GridView grd_memberpost;
	SingleMember singleMember;
	ArticleList  articlelistinstance;
	Activity activity;
	MenuTabFragment parent;
	TextView txt_num_seen,txt_num_following,txt_num_follower,txt_name,txt_nick_name,txt_bio,
	txt_seen_text,txt_following_text,txt_follower_text,txt_article;
	ImageView img_member_pic;
	Button btn_follow_her,btn_send,btn_share,btn_connect;
	ProgressDialog pd;
	boolean followstate=false;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_member,
				container, false);
		txt_seen_text=(TextView) v.findViewById(R.id.txt_seen_text);
		txt_following_text=(TextView) v.findViewById(R.id.txt_following_text);
		txt_follower_text=(TextView) v.findViewById(R.id.txt_follower_text);
		txt_article=(TextView) v.findViewById(R.id.txt_article);
		txt_num_seen=(TextView) v.findViewById(R.id.txt_num_seen);
		txt_num_following=(TextView) v.findViewById(R.id.txt_num_following);
		txt_num_follower=(TextView) v.findViewById(R.id.txt_num_follower);
		txt_name=(TextView) v.findViewById(R.id.txt_name);
		txt_nick_name=(TextView) v.findViewById(R.id.txt_nick_name);
		txt_bio=(TextView) v.findViewById(R.id.txt_bio);
		img_member_pic=(ImageView) v.findViewById(R.id.img_member_pic);
		btn_follow_her=(Button) v.findViewById(R.id.btn_follow_her);
		btn_send=(Button) v.findViewById(R.id.btn_send);
		grd_memberpost=(GridView) v.findViewById(R.id.grd_memberpost);
		btn_share=(Button) v.findViewById(R.id.btn_share);
		webview_member=(WebView) v.findViewById(R.id.webview_member);
		btn_connect=(Button) v.findViewById(R.id.btn_connect);
		activity=getActivity();
		txt_num_seen.setTypeface(Utility.getTypeface1(getActivity()));
		txt_num_follower.setTypeface(Utility.getTypeface1(getActivity()));
		txt_num_following.setTypeface(Utility.getTypeface1(getActivity()));
		txt_name.setTypeface(Utility.getTypeface1(getActivity()));
		txt_nick_name.setTypeface(Utility.getTypeface2(getActivity()));
		txt_bio.setTypeface(Utility.getTypeface2(getActivity()));
		btn_connect.setTypeface(Utility.getTypeface2(getActivity()));
		btn_follow_her.setTypeface(Utility.getTypeface2(getActivity()));
		btn_send.setTypeface(Utility.getTypeface2(getActivity()));
		btn_share.setTypeface(Utility.getTypeface2(getActivity()));
		txt_seen_text.setTypeface(Utility.getTypeface2(getActivity()));
		txt_following_text.setTypeface(Utility.getTypeface2(getActivity()));
		txt_follower_text.setTypeface(Utility.getTypeface2(getActivity()));
		txt_article.setTypeface(Utility.getTypeface2(getActivity()));
		btn_follow_her.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buttonfollowclicked();
			}
		});
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		appInstance = (LipberryApplication) getActivity().getApplication();
		jsonParser=new JsonParser();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(false).cacheOnDisc(false).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext()).defaultDisplayImageOptions(
						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), false);
			new AsyncTaskGetSinleMember().execute();
		}
		else{
			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}
	private class AsyncTaskGetSinleMember extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			String url =Constants.baseurl+"account/findmemberbyid/"+appInstance.getUserCred().getId()+"/";
			ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, url, null,
					null, null);
			return response;
		}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			setMemberObject(result.getjObj().toString());
		}
	}

	public void setMemberObject(String  respnse){
		try {
			JSONObject jobj=new JSONObject(respnse);
			String  status=jobj.getString("status");
			if(status.equals("success")){
				singleMember  =com.lipberry.model.SingleMember.parseSingleMember(jobj);
				setUserInterface();
				new AsyncTaskGetmemberPost().execute();
			}
			else{
				Toast.makeText(activity,activity.getResources().getString(R.string.Toast_member_found),
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_member_found),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	public void setUserInterface(){
		((HomeActivity)activity).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)activity).welcome_title.setText(singleMember.getName());
		if(singleMember.getId().equals(appInstance.getUserCred().getId())){
			btn_follow_her.setVisibility(View.GONE);
			btn_send.setVisibility(View.GONE);
		}
		else{
			btn_follow_her.setVisibility(View.VISIBLE);
			btn_send.setVisibility(View.VISIBLE);
		}
		
		txt_name.setText(singleMember.getNickname());
		txt_nick_name.setText(singleMember.getUsername());
		txt_bio.setText(singleMember.getBrief());
		txt_bio.setText(singleMember.getBrief());
		txt_bio.setMovementMethod(LinkMovementMethod.getInstance());
		txt_bio.setMovementMethod(LinkMovementMethod.getInstance());
		ShowHtmlText showtext=new ShowHtmlText(txt_bio, getActivity());
		showtext.updateImages(true,singleMember.getBrief());
		txt_num_follower.setText(singleMember.getNumber_of_followers());
		txt_num_following.setText(singleMember.getNumber_of_following());
		txt_num_seen.setText(singleMember.getPublicpage_visit());                                                                                                        
		imageLoader.displayImage(singleMember.getAvatar(), img_member_pic);
		btn_share.setVisibility(View.GONE);
		btn_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				webview_member.setVisibility(View.VISIBLE);
				WebSettings webSettings = webview_member.getSettings();
				webSettings.setJavaScriptEnabled(true);
				webview_member.setWebViewClient(new Callback());
				webview_member.loadUrl(singleMember.getSiteurl());
			}
		});
		
		if(!singleMember.getAlready_followin().equals("Yes")){
			followstate=false;
		}
		else{
			followstate=true;
		}
		
		if(followstate){
			btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_unfollow));
		}
		else{
			btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_follow_her));
		}
	}
	public void buttonfollowclicked(){
		if(!followstate){
			if(Constants.isOnline(activity)){
				pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
						getActivity().getResources().getString(R.string.txt_please_wait), false);
				new AsyncTaskSendFollowReq().execute();
			}
			else{
				Toast.makeText(activity,activity.getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		}
		else{
			if(Constants.isOnline(getActivity())){
				pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
						getActivity().getResources().getString(R.string.txt_please_wait), false);
				new AsyncTaskSendUnFollowReq().execute();
			}
			else{
				Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	private class AsyncTaskSendUnFollowReq extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/cancelFollowmember/"+Constants.userid+"/";
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
			if((pd!=null)&&(pd.isShowing())){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				if(status.equals("success")){
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_follow_her));
					followstate=false;
				}
				else{
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class AsyncTaskSendFollowReq extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/followmember/"+Constants.userid+"/";
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
			if((pd!=null)&&(pd.isShowing())){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				if(status.equals("success")){
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_unfollow));
					followstate=true;
				}
				else{
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					if(description.equals("Already followed")){
						btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_unfollow));
						followstate=true;
					}
				}
			} catch (JSONException e) {
			}
		}
	}
	private class Callback extends WebViewClient{  
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return (false);
		}
	}
	private class AsyncTaskGetmemberPost extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex", "0");
				loginObj.put("endIndex" , "10");
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/memberpostsbyid/"+appInstance.getUserCred().getId();
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
			if((pd!=null)&&(pd.isShowing())){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String status=jobj.getString("status");
				if(status.equals("success")){
					articlelistinstance=ArticleList.getArticlelist(jobj);
					if(articlelistinstance.getArticlelist().size()>0){
						loadgridview();
					}
				}
				else{
					String message=jobj.getString("description");
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void loadgridview(){
		CustomAdapterFormemberPost adapter=new CustomAdapterFormemberPost(getActivity(), articlelistinstance.getArticlelist());
		grd_memberpost.setAdapter(adapter);
		grd_memberpost.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		grd_memberpost.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Constants.GO_ARTCLE_PAGE_FROM_MEMBER=true; 
				Constants.ARTICLETOSEE=articlelistinstance.getArticlelist().get(position);
				imageviewarticlepicclicked(position);
				
				//parent.startFragmentArticleDetailsFromHome(articlelistinstance.getArticlelist().get(position));
			}
		});
	}
	public void imageviewarticlepicclicked(int position){
		if(Constants.isOnline(activity)){
			pd=ProgressDialog.show(activity, activity.getResources().getString(R.string.app_name_arabic),
					activity.getResources().getString(R.string.txt_please_wait), false);
			new AsyncTaskgetArticleDetails(position).execute();
		}
		else{
			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		
	}
	private class AsyncTaskgetArticleDetails extends AsyncTask<Void, Void, ServerResponse> {
		int position;
		public AsyncTaskgetArticleDetails(int position){
			this.position=position;
		}
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =articlelistinstance.getArticlelist().get(position).getArticle_url();
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
			if((pd!=null)&&(pd.isShowing())){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String status=jobj.getString("status");
				if(status.equals("success")){
					ArticleDetails articledetails=ArticleDetails.getArticleDetails(jobj);
					Constants.articledetails=articledetails;
					GoArticlePage(position,articledetails);
				}
				else{
					String message=jobj.getString("description");
					Toast.makeText(activity,message, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void GoArticlePage(int position,ArticleDetails articleDetails){
		((HomeActivity)getActivity()).mTabHost.setCurrentTab(4);
		//parent.startFragmentArticleDetailsFromHome(articlelistinstance.getArticlelist().get(position),articleDetails);
			
	}
}

