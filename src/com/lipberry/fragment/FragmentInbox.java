
package com.lipberry.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bugsense.trace.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.CustomAdapterForIInboxMessage;
import com.lipberry.model.Article;
import com.lipberry.model.InboxMessage;
import com.lipberry.model.InboxMessgaeList;
import com.lipberry.model.NotificationList;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.ThreadMessageList;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
@SuppressLint("NewApi")
public class FragmentInbox extends Fragment{
	int threadposition;
	InboxTabFragment parent;
	ProgressDialog pd;
	int startindex=0;
	ThreadMessageList messagelist;
	int endex=10;
	private ArrayAdapter<String> mAdapter;
	RelativeLayout re_sent_msz,re_new_msz,re_setting;
	CustomAdapterForIInboxMessage adapter;
	public static boolean oncreatecalledstate=false;
	JsonParser jsonParser;
	ListView actualListView;
	int screenmessagestate=0;
	LipberryApplication appInstance;
	ArrayList<InboxMessage>inboxlist;
	PullToRefreshListView list_view_inbox;
	ListView listviewforinbbox;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		oncreatecalledstate=true;
		inboxlist=new ArrayList<InboxMessage>();
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication)getActivity().getApplication();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_inbox,
				container, false);
		re_new_msz=(RelativeLayout) v.findViewById(R.id.re_new_msz);
		re_setting=(RelativeLayout) v.findViewById(R.id.re_setting);
		list_view_inbox=(PullToRefreshListView) v.findViewById(R.id.list_view_inbox);
		listviewforinbbox=list_view_inbox.getRefreshableView();
		actualListView = list_view_inbox.getRefreshableView();
		re_sent_msz=(RelativeLayout) v.findViewById(R.id.re_sent_msz);
		registerForContextMenu(actualListView);
		registerForContextMenu(listviewforinbbox);
		re_new_msz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.startfragmentSendMessage();
			}
		});
		re_sent_msz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.startFragmentNewMessage();
			}
		});
		re_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constants.MESSAGESETTINGSTATE=true;
				((HomeActivity)getActivity()).mTabHost.setCurrentTab(5);
			}
		});
		list_view_inbox.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(Constants.isOnline(getActivity())){
						new AsyncTaskRefreshMessage().execute();
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		if(oncreatecalledstate){
			if(Constants.isOnline(getActivity())){
				startindex=0;
				endex=10;
				pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
						getActivity().getResources().getString(R.string.txt_please_wait), false);
				new AsyncTaskGetMessage().execute();
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		}
		else{
			if(inboxlist.size()>0){
				LoadListView();
			}
		}
		oncreatecalledstate=false;
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.txt_inbox));
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.GONE);
	}
	private class AsyncTaskGetMessage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex",""+startindex);
				loginObj.put("endIndex",""+endex);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"inbox/inbox/";
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
					InboxMessgaeList messagelist=InboxMessgaeList.getMessageList(job);
					inboxlist=messagelist.getinboxlist();
					if(inboxlist.size()>0){
						startindex=endex+1;
						endex=endex+6;
						LoadListView();
					}
					else{
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_you_dont_have_msz), Toast.LENGTH_SHORT).show();
					}

				}
				else{
					Toast.makeText(getActivity(),job.getString("message"), Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class AsyncTaskRefreshMessage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex",""+startindex);
				loginObj.put("endIndex",""+endex);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"inbox/inbox/";

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
					InboxMessgaeList messagelist=InboxMessgaeList.getMessageList(job);
					if(messagelist.getinboxlist().size()>0){
						inboxlist.addAll(messagelist.getinboxlist());
						startindex=endex+1;
						endex=endex+6;
						adapter.notifyDataSetChanged();

					}
					else{
						Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_you_dont_have_msz), Toast.LENGTH_SHORT).show();
					}
					list_view_inbox.onRefreshComplete();
				}
				else{
					Toast.makeText(getActivity(),job.getString("message"), Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void LoadListView(){
		LinkedList<String> mListItems = new LinkedList<String>();
		adapter=new CustomAdapterForIInboxMessage(getActivity(), inboxlist,FragmentInbox.this);
		actualListView.setAdapter(adapter);
	}


	public void loadthreadmessage(int position){
		if(Constants.isOnline(getActivity())){

			pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), false);
			LoadThreadMessage individualmessage=new LoadThreadMessage(position); 
			individualmessage.execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
	}

	private class LoadThreadMessage extends AsyncTask<Void, Void, ServerResponse> {
		int position;
		public  LoadThreadMessage(int postion){
			this.position=postion;
		}
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"inbox/inbox/";

				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, inboxlist.get(position).getMessage_url(), null,
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
					messagelist=ThreadMessageList.getList(job);

					if(messagelist.getIndividualThreadlist().size()>0){
						parent.startMessagefragment(messagelist,""+inboxlist.get(position).getMessage_id()); 
					}
					else{
						Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_you_dont_have_msz), Toast.LENGTH_SHORT).show();
					}

				}   
				else{
					Toast.makeText(getActivity(),job.getString("message"), Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}

