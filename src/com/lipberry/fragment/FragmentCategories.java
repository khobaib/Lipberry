
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
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.ListviewAdapterforCategory;
import com.lipberry.model.ArticleList;
import com.lipberry.model.Categories;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;



@SuppressLint("NewApi")
public class FragmentCategories extends Fragment {
	ProgressDialog pd;
	ListView list_categories ;
	ArrayList< Categories>categorylist;
	CategoryTabFragment parent;
	LipberryApplication appInstance;	
	JsonParser jsonParser;
	ArticleList article;
	int index;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.topbar_cat));
		categorylist=new ArrayList<Categories>();
		appInstance = (LipberryApplication) getActivity().getApplication();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_categories,
				container, false);
		list_categories=(ListView) v.findViewById(R.id.list_categories);
		jsonParser=new JsonParser();
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
					"Retreving categories", true);
			new AsyncTaskgetCategories().execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();
		( (HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.GONE);
		if(Constants.catgeory){
		
		//	parent.startFragmentSubCategoriesList(Constants.caturl,Constants.caname);
		
		}
	}
	private class AsyncTaskgetCategories extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"category/categorylist";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
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
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject res=result.getjObj();
			try {
				String status=res.getString("status");
				if(status.equals("success")){
					JSONArray jarraArray=res.getJSONArray("categorylist");
					categorylist.clear();
					for(int i=0;i<jarraArray.length();i++){
						JSONObject job=jarraArray.getJSONObject(i);
						Categories cat=new Categories();
						cat=cat.parsecaCategories(job);
						categorylist.add(cat);
					}

					if(categorylist.size()>0){
						loadlistview();
					}
					else{
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_nocat_found),
								Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_nocat_found),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_nocat_found),
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}
	public void loadlistview(){
		FragmentActivity facActivity=getActivity();	
		ListviewAdapterforCategory adapter=new ListviewAdapterforCategory(facActivity, categorylist);
		list_categories.setAdapter(adapter);
		list_categories.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				index=position;
				
				if(Constants.isOnline(getActivity())){
					pd=ProgressDialog.show(getActivity(), "Lipberry",
							"Retreving Post", true);
					new AsyncTaskgetSubCategories().execute();
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
						
			}
		});
	}
	
	
	private class AsyncTaskgetSubCategories extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put( "startIndex","0");
				loginObj.put( "endIndex","2");
				String loginData = loginObj.toString();
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, categorylist.get(index).getUrl(), null,
						loginData, null);
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
							parent.startFragmentSubCategoriesList(categorylist.get(index).getUrl(),categorylist.get(index).getName(),article);
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
	
	
}

