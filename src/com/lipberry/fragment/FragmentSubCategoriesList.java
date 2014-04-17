package com.lipberry.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.ListviewAdapterforCategory;
import com.lipberry.adapter.ListviewAdapterimageloadingforArticle;
import com.lipberry.model.Article;
import com.lipberry.model.ArticleList;
import com.lipberry.model.Categories;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
@SuppressLint("NewApi")
public class FragmentSubCategoriesList extends Fragment {
	String url;
	int startindex=0;
	int endindex=2;
	ProgressDialog pd;
	PullToRefreshListView list_categories ;
	ListView listviewforarticle;
	ArticleList article;
	CategoryTabFragment parent;
	LipberryApplication appInstance;	
	JsonParser jsonParser;
	String catname;
	Button btn_go_another_category;
	TextView txt_make_up;
	ListviewAdapterimageloadingforArticle adapter;
	public void setUrl(String url,String catname){
		this.url=Constants.caturl;
		
		this.catname=Constants.caname;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		((HomeActivity)getActivity()).welcome_title.setText(catname);
		appInstance = (LipberryApplication) getActivity().getApplication();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_sub_categories,
				container, false);
		list_categories=(PullToRefreshListView) v.findViewById(R.id.list_categories);
		listviewforarticle=list_categories.getRefreshableView();
		list_categories.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(Constants.isOnline(getActivity())){
//					pd=ProgressDialog.show(getActivity(), "Lipberry",
//							"Retreving more Post", true);
					new AsyncTaskgetmoreSubCategoriesPost().execute();
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		jsonParser=new JsonParser();
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
					"Retreving Post", true);
			new AsyncTaskgetSubCategories().execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		btn_go_another_category=(Button) v.findViewById(R.id.btn_go_another_category);
		btn_go_another_category.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});

		txt_make_up=(TextView) v.findViewById(R.id.txt_make_up);
		txt_make_up.setText(catname);
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
//		if(Constants.catgeory){
//			
//			((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.GONE);
//		}
//		else{
//			((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
//		}
//		
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
		Constants.catgeory=false;
	}
	@Override
	public void onPause() {
		super.onPause();
		startindex=0;
		endindex=2;
	}
	private class AsyncTaskgetSubCategories extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put( "startIndex",""+startindex);
				loginObj.put( "endIndex",""+endindex);
				String loginData = loginObj.toString();
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
				startindex=endindex+1;
				endindex+=2;
				return response;
			} catch (JSONException e) { 
				if((pd.isShowing())&&(pd!=null)){
					pd.dismiss();
				}
				e.printStackTrace();
				return null;
			}
		}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			Log.e("response", result.getjObj().toString());
			
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject res=result.getjObj();
			if(result.getjObj().toString().equals("[]")){
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_article_found),
						Toast.LENGTH_SHORT).show();
			}
			else{
				try {
					String status=res.getString("status");
					if(status.equals("success")){
						article=new ArticleList();
						article=article.getArticlelist(res);
						if(article.getArticlelist().size()>0){
							loadlistview(); 
						}
						else{
							Toast.makeText(getActivity(), getActivity().getResources().
									getString(R.string.Toast_article_found)
									, Toast.LENGTH_SHORT).show(); 
						}
					}
					else{
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_article_found), 
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_article_found),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
	
	
	
	private class AsyncTaskgetmoreSubCategoriesPost extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put( "startIndex",""+startindex);
				loginObj.put( "endIndex",""+endindex);
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
			
			JSONObject res=result.getjObj();
			if(result.getjObj().toString().equals("[]")){
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_article_found),
						Toast.LENGTH_SHORT).show();
			}
			else{
				try {
					String status=res.getString("status");
					if(status.equals("success")){
						startindex=endindex+1;
						endindex+=2;
						
						
//						article=new ArticleList();
//						article=article.getArticlelist(res);
//						
						ArticleList article2=new ArticleList();
						article2=article2.getArticlelist(res);
						article.getArticlelist().addAll(article2.getArticlelist());
						
						
						
						if(article.getArticlelist().size()>0){
							adapter.notifyDataSetChanged();
							list_categories.onRefreshComplete(); 
						}
						else{
							Toast.makeText(getActivity(), getActivity().getResources().
									getString(R.string.Toast_article_found)
									, Toast.LENGTH_SHORT).show(); 
						}
					}
					else{
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_article_found), 
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_article_found),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}
	}

	public void loadlistview(){
		Log.e("erroe", "1");
		FragmentActivity fragActivity=getActivity();	
		Log.e("erroe", ""+article.getArticlelist().size()+"  "+listviewforarticle);

		 adapter=new ListviewAdapterimageloadingforArticle(fragActivity,
				article.getArticlelist(),parent);
			Log.e("erroe", "3");

		 listviewforarticle.setAdapter(adapter);
			Log.e("erroe", "24");

	}
}
