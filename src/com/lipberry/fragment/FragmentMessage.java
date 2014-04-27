
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
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.lipberry.adapter.CustomAdapterMessage;
import com.lipberry.model.Article;
import com.lipberry.model.InboxMessage;
import com.lipberry.model.InboxMessgaeList;
import com.lipberry.model.NotificationList;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.ThreadMessageList;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
@SuppressLint({ "NewApi", "ValidFragment" })
public class FragmentMessage extends Fragment{
	int threadposition;
	InboxTabFragment parent;
	ProgressDialog pd;
	int startindex=0;
	ThreadMessageList messagelist;
	int endex=10;
	private ArrayAdapter<String> mAdapter;
	RelativeLayout re_sent_msz,re_new_msz,re_setting;
	CustomAdapterForIInboxMessage adapter;
	String messageid;
	boolean oncreatecalledstate=false;
	JsonParser jsonParser;
	ListView actualListView;
	int screenmessagestate=0;
	LipberryApplication appInstance;
	ArrayList<InboxMessage>inboxlist;
	PullToRefreshListView list_view_inbox;
	ListView lv_thread_messages;
	Button b_reply,b_delete;
	String replymessage;
	EditText et_msg_body;
	@SuppressLint("NewApi")

	public FragmentMessage(ThreadMessageList messagelist,String messageid){
		this.messagelist=messagelist;
		this.messageid=messageid;
	}
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
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.thread_message,
				container, false);
		lv_thread_messages=(ListView) v.findViewById(R.id.lv_thread_messages);
		CustomAdapterMessage adapter=new CustomAdapterMessage(getActivity(), messagelist.getIndividualThreadlist());
		lv_thread_messages.setAdapter(adapter);
		b_reply=(Button) v.findViewById(R.id.b_reply);
		b_delete=(Button) v.findViewById(R.id.b_delete);
		et_msg_body=(EditText) v.findViewById(R.id.et_msg_body);
		
		if(Constants.isOnline(getActivity())){
			if(messagelist.getIndividualThreadlist().get(0).equals("0")){
				pd=ProgressDialog.show(getActivity(), "Lipberry",
						"Please Wait", true);
				new AsyncTaskSetasReadMessage().execute();
			}
				
		}
		b_reply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				replymessage=et_msg_body.getText().toString();
				if(Constants.isOnline(getActivity())){
					if(!replymessage.equalsIgnoreCase("")){
						pd=ProgressDialog.show(getActivity(), "Lipberry",
								"Please Wait", true);
						new AsyncTaskReplyMessage().execute();
					}

				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		b_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(Constants.isOnline(getActivity())){
					
						pd=ProgressDialog.show(getActivity(), "Lipberry",
								"Please Wait", true);
						new AsyncTaskDeleteMessage().execute();
				

				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}

	private class AsyncTaskReplyMessage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("message",replymessage);
				String toid;
				if(messagelist.getIndividualThreadlist().get(0).getFrom_id().equalsIgnoreCase(appInstance.getUserCred().getId())){
					toid=messagelist.getIndividualThreadlist().get(0).getTo_id();
				}
				else{
					toid=messagelist.getIndividualThreadlist().get(0).getFrom_id();
				}
//				byte[] ba =toid.getBytes();
//				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("tomember",toid);
				String loginData = loginObj.toString();
				
				String url =Constants.baseurl+"inbox/replyTo/"+messagelist.getIndividualThreadlist().get(0).getId();

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
					Toast.makeText(getActivity(),"Message is sent", Toast.LENGTH_SHORT).show();
					parent.onBackPressed();
//					if(messagelist.getinboxlist().size()>0){
//						
//					}
//					else{
//						Toast.makeText(getActivity(),"you dont have any message", Toast.LENGTH_SHORT).show();
//					}
//					list_view_inbox.onRefreshComplete();
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
	
	
	
	private class AsyncTaskDeleteMessage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
//				loginObj.put("message",replymessage);
//				String toid;
//				if(messagelist.getIndividualThreadlist().get(0).getFrom_id().equalsIgnoreCase(appInstance.getUserCred().getId())){
//					toid=messagelist.getIndividualThreadlist().get(0).getTo_id();
//				}
//				else{
//					toid=messagelist.getIndividualThreadlist().get(0).getFrom_id();
//				}
////				byte[] ba =toid.getBytes();
////				String base64Str = Base64.encodeBytes(ba);
//				loginObj.put("tomember",toid);
				String loginData = loginObj.toString();
//				
				String url =Constants.baseurl+"inbox/deletemessage/"+messageid;

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
					Toast.makeText(getActivity(),"Message is deleted successfully", Toast.LENGTH_SHORT).show();
					FragmentInbox.oncreatecalledstate=true;
					parent.onBackPressed();
//					if(messagelist.getinboxlist().size()>0){
//						
//					}
//					else{
//						Toast.makeText(getActivity(),"you dont have any message", Toast.LENGTH_SHORT).show();
//					}
//					list_view_inbox.onRefreshComplete();
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

	private class AsyncTaskSetasReadMessage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
//				loginObj.put("message",replymessage);
//				String toid;
//				if(messagelist.getIndividualThreadlist().get(0).getFrom_id().equalsIgnoreCase(appInstance.getUserCred().getId())){
//					toid=messagelist.getIndividualThreadlist().get(0).getTo_id();
//				}
//				else{
//					toid=messagelist.getIndividualThreadlist().get(0).getFrom_id();
//				}
////				byte[] ba =toid.getBytes();
////				String base64Str = Base64.encodeBytes(ba);
//				loginObj.put("tomember",toid);
				String loginData = loginObj.toString();
//				
				String url =Constants.baseurl+"inbox/markmessageAsread/"+messageid;

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
					FragmentInbox.oncreatecalledstate=true;
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
}

