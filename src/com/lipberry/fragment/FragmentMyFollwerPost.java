package com.lipberry.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.SignupActivity;
import com.lipberry.adapter.ListviewAdapterMember;
import com.lipberry.adapter.ListviewAdapterimageloadingforArticle;
import com.lipberry.db.LipberryDatabase;
import com.lipberry.model.Article;
import com.lipberry.model.ArticleFromMyFollwing;
import com.lipberry.model.ArticleList;
import com.lipberry.model.LikeMember;
import com.lipberry.model.Member;
import com.lipberry.model.MemberList;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.viewpagerindicator.TabPageIndicator;
@SuppressLint("NewApi")
public class FragmentMyFollwerPost extends Fragment {
	LipberryApplication appInstance;	
	ProgressDialog pd;
	static HomeTabFragment parent;
	ListviewAdapterimageloadingforArticle ladapter;
	ArticleList articlelistinstance;
	JsonParser jsonParser;
	int startindex=0;
	int endindex=9;
	HomeActivity homeactivity;
	PullToRefreshListView list_view_latest_post2;
	ListView listviewforarticle;
	ArrayList<Article>articlaList;
	ArticleFromMyFollwing postofmycountries;
	ArrayList<LikeMember>limemberlist;
	MemberList memberListobject;
	public static boolean oncreatecallsate1=false;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jsonParser=new JsonParser();
		articlaList=new ArrayList<Article>();
		limemberlist=new ArrayList<LikeMember>();
		memberListobject=new MemberList();
		oncreatecallsate1=true;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		homeactivity=(HomeActivity) activity;
	}
	public static  void setParent(HomeTabFragment parent2){
		parent=parent;
	}
	@Override
	public void onResume() {
		homeactivity.backbuttonoftab.setVisibility(View.GONE);
		super.onResume();
	}

	@Override
	public void onPause() {
//		startindex=0;
//		endindex=2;
		super.onPause();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(oncreatecallsate1){
			startindex=0;
			 endindex=9;
			 if(Constants.isOnline(homeactivity)){
					pd=ProgressDialog.show(homeactivity,homeactivity.getResources().getString(R.string.app_name_arabic),
							homeactivity.getResources().getString(R.string.txt_please_wait), false);
					new AsyncTaskLoadPostFrommyFollowing().execute();
				}
				else{
					Toast.makeText(homeactivity, homeactivity.getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}

		}
		oncreatecallsate1=false;
		appInstance = (LipberryApplication)homeactivity.getApplication();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_myfollowerpost,
				container, false);
		list_view_latest_post2=(PullToRefreshListView) v.findViewById(R.id.list_view_latest_post);
		listviewforarticle=list_view_latest_post2.getRefreshableView();
		listviewforarticle.setDividerHeight(6);
		list_view_latest_post2.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(Constants.isOnline(homeactivity)){
					new AsyncTaskRefreashPostFrommyFollowing().execute();
				}
				else{
					getfromdb();
					Toast.makeText(homeactivity, homeactivity.getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		if(Constants.isOnline(homeactivity)){
			if(articlaList.size()>0){
				loadlistview(true);
			}
			else{
					if(memberListobject.getMemberlist().size()>0){
						setlistformember(memberListobject.getMemberlist());
					}
			}

		}
		
		else{
			getfromdb();
			Toast.makeText(homeactivity,homeactivity.getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}



		return v;
	}
	private class AsyncTaskLoadPostFrommyFollowing extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex", ""+startindex);
				loginObj.put("endIndex", ""+endindex);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"home/myfollowerposts/";
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
			loadarticlelistfrommyfollowing(result.getjObj().toString());
		}
	}
	private class AsyncTaskRefreashPostFrommyFollowing extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex", ""+startindex);
				loginObj.put("endIndex", ""+endindex);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"home/myfollowerposts/";
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
			
		refreasharticlelistfrommyfollowing(result.getjObj().toString()); 
		}
	}
	public void refreasharticlelistfrommyfollowing(String  a){
		try {
			
			JSONObject result=new JSONObject(a);
			String status=result.getString("status");
			if(status.equals("success")){
				endindex+=10;
				ArticleList articlelistinstance2=ArticleList.getArticlelist(result);
				articlaList.clear();
				articlaList.addAll(articlelistinstance2.getArticlelist());
				ladapter.notifyDataSetChanged();
				list_view_latest_post2.onRefreshComplete();
			}
			else{
//				String message=result.getString("description");
//				if(message.equals("There is no followers")){
					Toast.makeText(homeactivity, homeactivity.getResources().getString(R.string.txt_there_is_no_follower),
							Toast.LENGTH_SHORT).show();
//				}
//				else{
//					Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//	
//				}
//				String message=result.getString("description");
//				Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void loadarticlelistfrommyfollowing(String  a){
		try {
			JSONObject result=new JSONObject(a);
			String status=result.getString("status");
			if(status.equals("success")){

				articlelistinstance=ArticleList.getArticlelist(result);
				articlaList=articlelistinstance.getArticlelist();
				endindex+=10;

				loadlistview(true);
			}
			else{
				//06-08 21:44:44.739: D/JsonParser(21070): sb = {"status":"failure","description":"There is no followers","memberstofollow":[{"member_id":"9028","member_nickname":"\u0645\u0631\u064a\u0645 \u0627\u0644\u0639\u064a\u062f","member_username":"mariam","member_bio":"\u0627\u062e\u0635\u0627\u0626\u064a\u0629 \u062a\u063a\u0630\u064a\u0629  \u062d\u0627\u0635\u0644\u0629 \u0639\u0644\u0649 \u0634\u0647\u0627\u062f\u0629 \u0627\u0644\u0645\u0627\u062c\u0633\u062a\u064a\u0631 .. \u062a\u0633\u0639\u062f\u0646\u064a \u0642\u0631\u0627\u0626\u062a\u0643\u0645 \u0644\u0643\u062a\u0627\u0628\u0627\u062a\u064a \u0627\u0644\u062a\u064a \u0627\u0633\u0639\u0649 \u0641\u064a\u0647\u0627 \u0644\u0627\u0641\u0627\u062f\u062a\u0643\u0645 \r\n\u0627\u0644\u062a\u063a\u0630\u064a\u0629 \u0637\u0631\u064a\u0642 \u0627\u0644\u062c\u0645\u0627\u0644 .. \u062a\u0627\u0628\u0639\u0648\u0646\u064a \u0628\u0627\u0644\u0636\u063a\u0637 \u0639\u0644\u0649 \u0632\u0631 \u0627\u0644\u0645\u062a\u0627\u0628\u0639\u0647 :) \u062d\u064a\u0627\u0643\u0645","member_photo":"http:\/\/www.lipberry.com\/uploaded_content\/member\/member_9028.jpg","member_url":"http:\/\/www.lipberry.com\/user\/mariam\/"},{"member_id":"9575","member_nickname":"manalll14","member_username":"manalll14","member_bio":"\u0645\u0646\u0627\u0644....\u0627\u062d\u0628 \u0643\u0644 \u0634\u064a \u064a\u062e\u0635 \u0627\u0644\u062c\u0645\u0627\u0644 \u0648\u0627\u0644\u0645\u0643\u064a\u0627\u062c \u0648\u0627\u062d\u0628 \u0627\u0641\u064a\u062f \u063a\u064a\u0631\u064a \u0628\u062a\u062c\u0627\u0631\u0628\u064a","member_photo":"http:\/\/www.lipberry.com\/uploaded_content\/member\/member_9575.jpg","member_url":"http:\/\/www.lipberry.com\/user\/manalll14\/"},{"member_id":"9625","member_nickname":"\u0645\u0631\u0648\u0647","member_username":"marwa","member_bio":"\u0645\u062f\u0648\u0646\u0629  \u0645\u0647\u062a\u0645\u0629 \u0628\u0627\u0644\u0645\u0643\u064a\u0627\u062c \u0648 \u0627\u0644\u0627\u0632\u064a\u0627\u0621 \u0648 \u0627\u0646\u0634\u0631 \u0627\u0644\u0644\u0627\u0634\u064a\u0627\u0621 \u0627\u0644\u0644\u064a \u062a\u0639\u062c\u0628\u0646\u064a :) .. \u062a\u0627\u0628\u0639\u064a \u0635\u0641\u062d\u062a\u064a \u0639\u0634\u0627\u0646 \u064a\u0648\u0635\u0644\u0643 \u062c\u062f\u064a\u062f\u064a \r\n\u062d\u0627\u0644\u064a\u0627 \u0627\u0646\u0634\u0631 \u0627\u0632\u064a\u0627\u0621 2014 \u0644\u0644\u0645\u0635\u0645\u0645\u064a\u0646","member_photo":"http:\/\/www.lipberry.com\/uploaded_content\/member\/member_9625.jpg","member_url":"http:\/\/www.lipberry.com\/user\/marwa\/"}]}
				if(result.has("description")){
					Toast.makeText(homeactivity,result.getString("description"),
							Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(homeactivity,homeactivity.getResources().getString(R.string.txt_there_is_no_follower),
							Toast.LENGTH_SHORT).show();
				}
//				String message=result.getString("description");
//				if(message.equals("There is no followers")){
				
//				}
//				else{
//					Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//	
//				}
				loadmemberlist( a);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void loadmemberlist(String result){
		try {
			JSONObject jobj=new JSONObject(result);
			memberListobject=MemberList.getMemberlist(jobj);
			if(memberListobject.getMemberlist().size()>0){
				setlistformember(memberListobject.getMemberlist());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setlistformember(ArrayList<Member>memberList){
		FragmentActivity  activity=homeactivity;
		ListviewAdapterMember ladapter=new ListviewAdapterMember(activity,memberList,homeactivity,parent);
		listviewforarticle.setAdapter(ladapter);
	}
	public void  loadlistview(boolean from){
		ladapter=new ListviewAdapterimageloadingforArticle(homeactivity, 
				articlaList,parent);
		listviewforarticle.setAdapter(ladapter);
		if(from){
			saveindb(articlaList);
		}
	}
	public void saveindb(ArrayList<Article>artarticlelist){
		for(int i=0;i<artarticlelist.size();i++){
			for(int j=0;j<artarticlelist.get(i).getLikedmemberlist().size();j++){
				String artid= artarticlelist.get(i).getArticle_id();
				artarticlelist.get(i).getLikedmemberlist().get(j).setForeign_key_article_id(artid);
			}
		}
		LipberryDatabase dbInstance = new LipberryDatabase(homeactivity);
		dbInstance.open();
		for(int i=0;i<artarticlelist.size();i++){
			dbInstance.insertOrUpdateArticlefollowing(artarticlelist.get(i));
		}
		List<Article>artlist= dbInstance.retrieveArticleListfollowing();
		dbInstance.close();
	}
	public void getfromdb(){
		LipberryDatabase dbInstance = new LipberryDatabase(homeactivity);
		dbInstance.open();
		articlaList= (ArrayList<Article>) dbInstance.retrieveArticleListfollowing();
		dbInstance.close();
		if(articlaList.size()>0){
			loadlistview(false);
		}
		else{
			Toast.makeText(homeactivity, homeactivity.getResources().getString(R.string.Toast_article_found),
					Toast.LENGTH_SHORT).show();
		}
	}
}
