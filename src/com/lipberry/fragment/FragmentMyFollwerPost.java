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
	int endindex=10;
	Activity activity;
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
		activity=getActivity();
		memberListobject=new MemberList();
		oncreatecallsate1=true;
	}
	public static  void setParent(HomeTabFragment parent2){
		parent=parent;
	}
	@Override
	public void onResume() {
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.GONE);
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
			 endindex=10;
			 if(Constants.isOnline(activity)){
					pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
							getActivity().getResources().getString(R.string.txt_please_wait), false);
					new AsyncTaskLoadPostFrommyFollowing().execute();
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}

		}
		oncreatecallsate1=false;
		appInstance = (LipberryApplication) getActivity().getApplication();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_myfollowerpost,
				container, false);
		list_view_latest_post2=(PullToRefreshListView) v.findViewById(R.id.list_view_latest_post);
		listviewforarticle=list_view_latest_post2.getRefreshableView();
		listviewforarticle.setDividerHeight(6);
		list_view_latest_post2.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(Constants.isOnline(getActivity())){
					//					pd=ProgressDialog.show(getActivity(), "Lipberry",
					//							"Retreving more Post", true);
					new AsyncTaskRefreashPostFrommyFollowing().execute();
				}
				else{
					getfromdb();
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		
		
		if(Constants.isOnline(activity)){
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
			Toast.makeText(activity,activity.getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		
//
//		if(Constants.isOnline(getActivity())){
////			pd=ProgressDialog.show(getActivity(), "Lipberry",
////					"Retreving more Post", true);
//			new AsyncTaskRefreashPostFrommyFollowing().execute();
//		}
//		else{
//			getfromdb();
//			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
//					Toast.LENGTH_SHORT).show();
//		}
//
//		
//		
		
//		
//		if(Constants.isOnline(activity)){
////
////			pd=ProgressDialog.show(activity, "Lipberry",
////					"Retreving Post", true);
//			new AsyncTaskLoadPostFrommyFollowing().execute();
//		}
//		else{
//			getfromdb();
//			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
//					Toast.LENGTH_SHORT).show();
//		}

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
				//getActivity().getl
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
			
			Log.i("res", result.getjObj().toString());
			
//			Toast.makeText(getActivity(), " "+startindex+"  "+endindex, 4000).show();
			refreasharticlelistfrommyfollowing(result.getjObj().toString()); 
		}
	}
	public void refreasharticlelistfrommyfollowing(String  a){
		try {
			
			JSONObject result=new JSONObject(a);
			String status=result.getString("status");
			if(status.equals("success")){
				startindex=endindex+1;
				endindex+=2;
				ArticleList articlelistinstance2=ArticleList.getArticlelist(result);
				articlaList.addAll(articlelistinstance2.getArticlelist());
				ladapter.notifyDataSetChanged();
				list_view_latest_post2.onRefreshComplete();
			}
			else{
				String message=result.getString("description");
				Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
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
				startindex=endindex+1;
				endindex+=2;

				loadlistview(true);
			}
			else{
				String message=result.getString("description");
				Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
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
		FragmentActivity  activity=getActivity();
		ListviewAdapterMember ladapter=new ListviewAdapterMember(activity,memberList,getActivity());
		listviewforarticle.setAdapter(ladapter);
	}
	public void  loadlistview(boolean from){
		ladapter=new ListviewAdapterimageloadingforArticle(activity, 
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
		LipberryDatabase dbInstance = new LipberryDatabase(activity);
		dbInstance.open();
		for(int i=0;i<artarticlelist.size();i++){
			dbInstance.insertOrUpdateArticlefollowing(artarticlelist.get(i));
		}
		List<Article>artlist= dbInstance.retrieveArticleListfollowing();
		dbInstance.close();
	}
	public void getfromdb(){
		LipberryDatabase dbInstance = new LipberryDatabase(activity);
		dbInstance.open();
		articlaList= (ArrayList<Article>) dbInstance.retrieveArticleListfollowing();
		dbInstance.close();
		if(articlaList.size()>0){
			loadlistview(false);
		}
		else{
			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_article_found),
					Toast.LENGTH_SHORT).show();
		}
	}
}
