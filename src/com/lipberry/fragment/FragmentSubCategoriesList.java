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
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import com.lipberry.utility.Utility;
@SuppressLint("NewApi")
public class FragmentSubCategoriesList extends ListFragment {
	String url;
	int startindex=0;
	int endindex=9;
	ProgressDialog pd;
	private int index = -1;
    private int top = 0;
    ImageView img_cat;
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
	public void setUrl(String url,String catname,ArticleList article){
		this.url=Constants.caturl;
		this.article=article;
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
		Log.d("name", "a  "+catname);
		((HomeActivity)getActivity()).welcome_title.setText(catname);
		appInstance = (LipberryApplication) getActivity().getApplication();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_sub_categories,
				container, false);
		img_cat=(ImageView) v.findViewById(R.id.img_cat);
		list_categories=(PullToRefreshListView) v.findViewById(R.id.list_categories);
		listviewforarticle=list_categories.getRefreshableView();
		listviewforarticle.setDividerHeight(6);
		((HomeActivity)getActivity()).ProductList=listviewforarticle;
		
		list_categories.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				if(Constants.isOnline(getActivity())){
						new AsyncTaskgetmoreSubCategoriesPost().execute();
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		jsonParser=new JsonParser();
		btn_go_another_category=(Button) v.findViewById(R.id.btn_go_another_category);
		btn_go_another_category.setTypeface(Utility.getTypeface2(getActivity()));
		btn_go_another_category.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});

		txt_make_up=(TextView) v.findViewById(R.id.txt_make_up);
		txt_make_up.setText(catname);
		txt_make_up.setTypeface(Utility.getTypeface1(getActivity()));
		loadlistview();

	      if(index!=-1){
	    	  listviewforarticle.setSelection(index);
	    	
	      }

			if(article.getArticlelist().get(0).getcategory().equals("2")){
				if(article.getArticlelist().get(0).getArticle_category_url().contains("shexp")){
					int id = getActivity().getResources().getIdentifier("l"+article.getArticlelist().get(0).getcategory(), "drawable", getActivity().getPackageName());
					img_cat.setImageResource(id);
				}
				else{
					int id = getActivity().getResources().getIdentifier("bl"+article.getArticlelist().get(0).getcategory(), "drawable", getActivity().getPackageName());
					img_cat.setImageResource(id);
				}
			}else{
				int id = getActivity().getResources().getIdentifier("l"+article.getArticlelist().get(0).getcategory(), "drawable", getActivity().getPackageName());
				img_cat.setImageResource(id);
			}
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
		index=listviewforarticle.getSelectedItemPosition();
//		startindex=0;
//		endindex=2;
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
					
						endindex+=10;
						
						
//						article=new ArticleList();
//						article=article.getArticlelist(res);
//						
						ArticleList article2=new ArticleList();
						article2=article2.getArticlelist(res);
						article.getArticlelist().clear();
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
