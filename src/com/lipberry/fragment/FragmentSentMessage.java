
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
import android.widget.LinearLayout;
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
import com.lipberry.adapter.CustomAdapterForISentMessage;
import com.lipberry.db.LipberryDatabase;
import com.lipberry.model.Article;
import com.lipberry.model.InboxMessage;
import com.lipberry.model.InboxMessgaeList;
import com.lipberry.model.NotificationList;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.ThreadMessageList;
import com.lipberry.model.TndividualThreadMessage;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
@SuppressLint("NewApi")
public class FragmentSentMessage extends Fragment{
	int threadposition;
	InboxTabFragment parent;
	ProgressDialog pd;
	int startindex=0;
	ThreadMessageList messagelist;
	int endex=10;
	private ArrayAdapter<String> mAdapter;
	CustomAdapterForISentMessage adapter;
	public static boolean oncreatecalledstate=false;
	JsonParser jsonParser;
	ListView actualListView;
	int screenmessagestate=0;
	LipberryApplication appInstance;
	ArrayList<InboxMessage>inboxlist;
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
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_sent_msz,
				container, false);
		listviewforinbbox =(ListView) v.findViewById(R.id.listviewforinbbox);

	
//		if(oncreatecalledstate){
			if(Constants.isOnline(getActivity())){
				startindex=0;
				endex=10;
				pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
						getActivity().getResources().getString(R.string.txt_please_wait), false);
				new AsyncTaskGetMessage().execute();
			}
			else{
				getinbox_list();
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
//		}
//		else{
//			if(inboxlist.size()>0){
//				LoadListView();
//			}
//		}
		oncreatecalledstate=false;
		return v;
	}


	private class AsyncTaskGetMessage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex",""+startindex);
				loginObj.put("endIndex",""+10);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"inbox/sentmessages/";
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
						saveindb(inboxlist);
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

//	private class AsyncTaskRefreshMessage extends AsyncTask<Void, Void, ServerResponse> {
//		@Override
//		protected ServerResponse doInBackground(Void... params) {
//
//			try {
//				JSONObject loginObj = new JSONObject();
//				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
//				loginObj.put("startIndex",""+startindex);
//				loginObj.put("endIndex",""+endex);
//				String loginData = loginObj.toString();
//				String url =Constants.baseurl+"inbox/sentmessages/";
//
//				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
//						loginData, null);
//				return response;
//			} catch (JSONException e) {                
//				e.printStackTrace();
//				return null;
//			}
//		}
//
//		@Override
//		protected void onPostExecute(ServerResponse result) {
//			super.onPostExecute(result);
//			Log.e("res", result.getjObj().toString());
//			if(pd.isShowing()&&(pd!=null)){
//				pd.dismiss();
//			}
//			JSONObject job=result.getjObj();
//
//			try {
//				String status=job.getString("status");
//				if(status.equals("success")){
//					InboxMessgaeList messagelist=InboxMessgaeList.getMessageList(job);
//					if(messagelist.getinboxlist().size()>0){
//						inboxlist.addAll(messagelist.getinboxlist());
//						startindex=endex+1;
//						endex=endex+6;
//						adapter.notifyDataSetChanged();
//
//					}
//					else{
//						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_you_dont_have_msz), Toast.LENGTH_SHORT).show();
//					}
//					list_view_inbox.onRefreshComplete();
//				}
//				else{
//					Toast.makeText(getActivity(),job.getString("message"), Toast.LENGTH_SHORT).show();
//				}
//
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.txt_sent_msz));
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}
	public void saveindb(ArrayList<InboxMessage>inboxlist){
		deletetable();
		LipberryDatabase dbInstance = new LipberryDatabase(getActivity());
		dbInstance.open();
		dbInstance.insertOrUpdateSentMessageList(inboxlist);
		dbInstance.close();
	}
	public void deletetable(){
		LipberryDatabase dbInstance = new LipberryDatabase(getActivity());
		dbInstance.open();
		dbInstance.droptableSentMessageDbManager();
		dbInstance.createtableSentMessageDbManager();
		dbInstance.close();
	}
	public void getinbox_list(){
		LipberryDatabase dbInstance = new LipberryDatabase(getActivity());
		dbInstance.open();
		try {
			dbInstance.createtableSentMessageDbManager();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inboxlist= (ArrayList<InboxMessage>) dbInstance.retrieveSentMessage();
		LoadListView();
		dbInstance.close();
	}
	public void LoadListView(){
		adapter=new CustomAdapterForISentMessage(getActivity(), inboxlist,FragmentSentMessage.this);
		listviewforinbbox.setAdapter(adapter);
	}

	public void loadthreadmessage(int position){
		if(Constants.isOnline(getActivity())){

			pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), false);
			LoadThreadMessage individualmessage=new LoadThreadMessage(position); 
			individualmessage.execute();
		}
		else{
			tryfromdb(inboxlist.get(position).getMessage_id());
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
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, inboxlist.get(position).getMessage_url()+
						inboxlist.get(position).getMessage_id(), null,
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
						
						boolean read_flag;
						if(inboxlist.get(position).getRead_flag().equals("0")){
							read_flag=false;
						}
						else{
							read_flag=true;

						}
						saveindb(inboxlist.get(position).getMessage_id());
						parent.startMessagefragment(messagelist,""+inboxlist.get(position).getMessage_id(),read_flag); 
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
	
	public void tryfromdb(String parent_id){
		//messagelist.getIndividualThreadlist()
		ArrayList<TndividualThreadMessage>inbox_message=new ArrayList<TndividualThreadMessage>();
		LipberryDatabase dbInstance = new LipberryDatabase(getActivity());
		dbInstance.open();
		try {
			dbInstance.createtableThreadMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<TndividualThreadMessage>inbox_list= (ArrayList<TndividualThreadMessage>) dbInstance.retrieveThreadInboxtMessage();
		
		for(int i=0;i<inbox_list.size();i++){
			Log.e("parent id", "1 "+inbox_list.get(i).getParent_id());
			if(inbox_list.get(i).getParent_id().equals(parent_id)){
				inbox_message.add(inbox_list.get(i));
				
			}
		}
		if(inbox_message.size()>0){
			messagelist=new ThreadMessageList();
			messagelist.setIndividualThreadlist(inbox_message);
			parent.startMessagefragment(messagelist,parent_id,true); 
			
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		
		
		dbInstance.close();
	}
	public void saveindb(String parent_id){
		LipberryDatabase dbInstance = new LipberryDatabase(getActivity());
		dbInstance.open();
		try {
			dbInstance.createtableThreadMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<messagelist.getIndividualThreadlist().size();i++){
			messagelist.getIndividualThreadlist().get(i).setParent_flag(parent_id);
		}
		dbInstance.insertOrUpdateThreadMessageInboxList(messagelist.getIndividualThreadlist());
		ArrayList<TndividualThreadMessage>inbox_list= (ArrayList<TndividualThreadMessage>) dbInstance.retrieveThreadInboxtMessage();
		dbInstance.close();
	}

}

