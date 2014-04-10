

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

		
 // -- token android.os.BinderProxy@41a9c5c0 is not valid; is your activity running?

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_interaction,
				container, false);
		lst_interaction=(ListView) v.findViewById(R.id.lst_interaction);
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
					"Please wait", true);
			new AsyncTaskGetNotification().execute();
			if(Constants.notificationcount>0){
				new AsyncTasksetNotificationToggle().execute();
			}
			
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		
		
	/*	FragmentTabHost.TabSpec spec =((HomeActivity)getActivity()).mTabHost.newTabSpec("Interaction");
		View tabIndicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.tab_indicator, ((HomeActivity)getActivity()).mTabsPlaceHoler, false);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(R.drawable.ic_launcher);
		spec.setIndicator(tabIndicator);
		((HomeActivity)getActivity()).mTabHost.addTab(spec, IneractionTabFragment.class, null);*/
		return v;
	}
	private class AsyncTaskGetNotification extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex","0");
				loginObj.put("endIndex","53");
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
			Log.e("res", result.getjObj().toString());
			if(pd.isShowing()&&(pd!=null)){
				pd.dismiss();
			}
			//
			
			
			JSONObject job=result.getjObj();
			
			try {
				String status=job.getString("status");
				if(status.equals("success")){
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
		Log.e("size",""+notificationList.getnotificationslist().size());
		CustomAdapterForInteraction adapter=new CustomAdapterForInteraction(getActivity(), notificationList.getnotificationslist());
		lst_interaction.setAdapter(adapter);
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
			Log.e("res", result.getjObj().toString());
			if(pd.isShowing()&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject job=result.getjObj();
			
			try {
				String status=job.getString("status");
				if(status.equals("success")){
					((HomeActivity)getActivity()).text_notification_no_fromactivity.setVisibility(View.GONE);
				}
				else{
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}

