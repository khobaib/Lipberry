

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
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.lipberry.HomeActivity;
import com.lipberry.LoginActivity;
import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.adapter.CustomAdapterForInteraction;
import com.lipberry.model.ArticleDetails;
import com.lipberry.model.NotificationList;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
@SuppressLint("NewApi")
public class FragmentInteraction extends Fragment {
	IneractionTabFragment parent;
	ListView lst_interaction;
	JsonParser jsonParser;
	NotificationList notificationList;
	ProgressDialog pd;
	LipberryApplication appInstance;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication)getActivity().getApplication();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_interaction,
				container, false);
		lst_interaction=(ListView) v.findViewById(R.id.lst_interaction);
		if(Constants.isOnline(getActivity())){
			
			new AsyncTaskGetNotification().execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		
	
		return v;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).welcome_title.setText(R.string.txt_interaction);
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}
	private class AsyncTaskGetNotification extends AsyncTask<Void, Void, ServerResponse> {
		ProgressDialog pd2=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
				getActivity().getResources().getString(R.string.txt_please_wait), true);
		@Override
		protected void onPreExecute() {
			
		};
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				int i=Constants.notificationcount+20;
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex","0");
				loginObj.put("endIndex",""+i);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/notifications/";
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
			if(pd2!=null){
				if(pd2.isShowing()){
					pd2.cancel();
				}
			}
			
			
			
			JSONObject job=result.getjObj();
			
			try {
				String status=job.getString("status");
				if(status.equals("success")){
					new AsyncTasksetNotificationToggle().execute();
					notificationList=NotificationList.getNotificationList(result.getjObj());
					if(notificationList.getnotificationslist().size()>0){
						setlistinteraction();
					}
					
				}
				else{
					Toast.makeText(getActivity(),job.getString("message"), Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//txt_test.setText(result.getjObj().toString());
		}
	}
	
	public void setlistinteraction(){
		CustomAdapterForInteraction adapter=new CustomAdapterForInteraction(getActivity(), notificationList.getnotificationslist());
		lst_interaction.setAdapter(adapter);
		lst_interaction.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(notificationList.getnotificationslist().get(position).getInteraction_types()!=0){
					Constants.userid=notificationList.getnotificationslist().get(position).getFrom_id();
					Constants.GO_MEMBER_STATE_FROM_INTERACTION=true;
					((HomeActivity)getActivity()).mTabHost.setCurrentTab(4);
				}
				else{
					
					if(Constants.isOnline(getActivity())){
						
						
						
						if((notificationList.getnotificationslist().get(position).getArticle_id()!=null)
								){
							pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
									getActivity().getResources().getString(R.string.txt_please_wait), false);
							new AsyncTaskgetArticleDetails(position).execute();
						}
						else{
							Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_articleid_not_found),
									Toast.LENGTH_SHORT).show();
						}
					}
					else{
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
								Toast.LENGTH_SHORT).show();
					}
//					Constants.userid=notificationList.getnotificationslist().get(position).getFrom_id();
//					Constants.GOARTCLEPAGE=true;
//					Constants.INTER_ARTICLE_ID=notificationList.getnotificationslist().get(position).getArticle_id();
//				//	Constants.INTER_MEMBER_ID=notificationList.getnotificationslist().get(position).getArticle_id();
//					((HomeActivity)getActivity()).mTabHost.setCurrentTab(4);
				}
				
			}
		});
		
		
		
	}
	
	
	private class AsyncTasksetNotificationToggle extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/togglenotifications/";
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
			JSONObject job=result.getjObj();
			
			try {
				String status=job.getString("status");
				if(status.equals("success")){
					if(((HomeActivity)getActivity()).text_notification_no_fromactivity!=null){
						((HomeActivity)getActivity()).text_notification_no_fromactivity.setVisibility(View.GONE);

					}
				}
				else{
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
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
				String url=Constants.baseurl+"article/findarticlebyid/"+notificationList.getnotificationslist().get(position).getArticle_id();
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
					Constants.GO_ARTCLE_PAGE=true;
					Constants.articledetails=articledetails;
					Constants.from=2;
					((HomeActivity)getActivity()).mTabHost.setCurrentTab(4);
				}
				else{
					String message=jobj.getString("description");
					Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}

