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
public class FragmentMyCountriesPost extends Fragment {
	LipberryApplication appInstance;	
	 ProgressDialog pd;
	 TextView textView1;
	 ArticleList articlelistinstance;
	 JsonParser jsonParser;
	 ListView list_view_latest_post;
	 ArrayList<Article>articlaList;
		ArticleFromMyFollwing postofmycountries;
		ArrayList<LikeMember>limemberlist;
		 MemberList memberListobject;
		
		  public static FragmentMyCountriesPost newInstance() {
			  FragmentMyCountriesPost f = new FragmentMyCountriesPost();
	            return f;
	        }
		
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 jsonParser=new JsonParser();
		 articlaList=new ArrayList<Article>();
		limemberlist=new ArrayList<LikeMember>();
			
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		appInstance = (LipberryApplication) getActivity().getApplication();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_post_from_my_country,
				container, false);
		textView1=(TextView) v.findViewById(R.id.textView1);
		list_view_latest_post=(ListView) v.findViewById(R.id.list_view_latest_post);
		textView1.setText("My country");
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
				    "Retreving Post", true);
			new AsyncTaskLoadPostFrommyFollowing().execute();
			
		}
		else{
			getfromdb();
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet), 10000).show();
		}
		textView1.setVisibility(View.GONE);

		return v;
	}
	private class AsyncTaskLoadPostFrommyFollowing extends AsyncTask<Void, Void, ServerResponse> {
					@Override
						protected ServerResponse doInBackground(Void... params) {

							try {
									JSONObject loginObj = new JSONObject();
									loginObj.put("session_id", appInstance.getUserCred().getSession_id());
									loginObj.put("startIndex", "0");
									loginObj.put("endIndex", "10");
									String loginData = loginObj.toString();
									String url =Constants.baseurl+"home/latestposts/";
									ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
											loginData, null);

									Log.d("rtes", response.getjObj().toString());
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
			
			
				public void loadarticlelistfrommyfollowing(String  a){
	
						try {
							JSONObject result=new JSONObject(a);
							String status=result.getString("status");
							if(status.equals("success")){
								articlelistinstance=ArticleList.getArticlelist(result);
								loadlistview(articlelistinstance.getArticlelist(),true);
							}
							else{
								String message=result.getString("description");
								Toast.makeText(getActivity(), message, 10000).show();
								loadmemberlist( a);
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
				public void setlistformember(ArrayList<Member>memberList){
					FragmentActivity  activity=getActivity();
					ListviewAdapterMember ladapter=new ListviewAdapterMember(activity,memberList,getActivity());
					list_view_latest_post.setAdapter(ladapter);
				}

				public void  loadlistview(List<Article>articlelist,boolean from){
					FragmentActivity  activity=getActivity();
					ListviewAdapterimageloadingforArticle ladapter=new ListviewAdapterimageloadingforArticle(activity, (ArrayList<Article>)articlelist);
					list_view_latest_post.setAdapter(ladapter);
					
					if(from){
						saveindb((ArrayList<Article>)articlelist);
					}
					
					
			 }
 
				public void saveindb(ArrayList<Article>artarticlelist){
	 
					
					for(int i=0;i<artarticlelist.size();i++){
						for(int j=0;j<artarticlelist.get(i).getLikedmemberlist().size();j++){
			 
							String artid= artarticlelist.get(i).getArticle_id();
							artarticlelist.get(i).getLikedmemberlist().get(j).setForeign_key_article_id(artid);
						}
					}
	 
	 
					LipberryDatabase dbInstance = new LipberryDatabase(getActivity());
					dbInstance.open();
      
					for(int i=0;i<artarticlelist.size();i++){
						dbInstance.insertOrUpdateArticle(artarticlelist.get(i));
					}
      
						List<Article>artlist= dbInstance.retrieveArticleList();
						dbInstance.close();
				}
 
 
				public void getfromdb(){
						LipberryDatabase dbInstance = new LipberryDatabase(getActivity());
						dbInstance.open();
						List<Article>artlist= dbInstance.retrieveArticleList();
						dbInstance.close();
						if(artlist.size()>0){
							loadlistview(artlist,false);
						}
						else{
   	  
							Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_article_found), 10000).show();
		       
						}
     
    
				}
 


}
