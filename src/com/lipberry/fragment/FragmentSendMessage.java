
package com.lipberry.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.CustomAdapterForIInboxMessage;
import com.lipberry.model.InboxMessage;
import com.lipberry.model.InboxMessgaeList;
import com.lipberry.model.MemberListForSendMessage;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.ThreadMessageList;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
@SuppressLint({ "NewApi", "ValidFragment" })
public class FragmentSendMessage extends Fragment{
	int threadposition;
	InboxTabFragment parent;
	ProgressDialog pd;
	int startindex=0;
	int selectedpos=-1;
	MemberListForSendMessage memberListobject;
	ThreadMessageList messagelist;
	int endex=10;
	private ArrayAdapter<String> mAdapter;
	RelativeLayout re_sent_msz,re_new_msz,re_setting;
	CustomAdapterForIInboxMessage adapter;
	ArrayList<String>membername;
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
	AutoCompleteTextView act_to;
	Button b_send;
	String subject;
	EditText et_su;
	@SuppressLint("NewApi")

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		memberListobject=new MemberListForSendMessage();
		oncreatecalledstate=true;
		inboxlist=new ArrayList<InboxMessage>();
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication)getActivity().getApplication();
		

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.send_message,
				container, false);
		act_to=(AutoCompleteTextView) v.findViewById(R.id.act_to);
		b_send=(Button) v.findViewById(R.id.b_send);
		et_msg_body=(EditText) v.findViewById(R.id.et_msg_body);
		et_su=(EditText) v.findViewById(R.id.et_su);
		act_to.setTypeface(Utility.getTypeface2(getActivity()));
		b_send.setTypeface(Utility.getTypeface2(getActivity()));
		et_msg_body.setTypeface(Utility.getTypeface2(getActivity()));
		et_su.setTypeface(Utility.getTypeface2(getActivity()));
		b_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				replymessage=et_msg_body.getText().toString();
				subject=et_su.getText().toString();
				if(Constants.isOnline(getActivity())){
					String id=act_to.getText().toString();
					  int pos = -1;

				        for (int i = 0; i < membername.size(); i++) {
				            if (membername.get(i).equals(id)) {
				                pos = i;
				                break;
				            }
				        }
				   
					selectedpos= pos;
					
					if (selectedpos==-1){
						Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_enter_member),
								Toast.LENGTH_SHORT).show();
					}
					else if(replymessage.equalsIgnoreCase("")){
						Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_enter_msz),
								Toast.LENGTH_SHORT).show();
						
					}
					else{
						if(memberListobject.getMemberlistForSendMessage().get(selectedpos).getStopPrivateMsz().equals("0")){
							pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
									getActivity().getResources().getString(R.string.txt_please_wait), false);
							new AsyncTaskSendMessage().execute();
						}
						else{
							//txt_not_allod_to_send_msz
							Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_not_allod_to_send_msz),
									Toast.LENGTH_SHORT).show();
						}
					
					}

				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), false);
			new AsyncTaskGetMemberList().execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		membername=new ArrayList<String>();

		return v;
	}
	private class AsyncTaskSendMessage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				byte[] ba =replymessage.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("message",base64Str);
				if(!subject.equals("")){
					ba =subject.getBytes();
					base64Str = Base64.encodeBytes(ba);
					loginObj.put("subject",base64Str);
				}
				 ba =memberListobject.getMemberlistForSendMessage().get(selectedpos).getUserName().getBytes();
				 base64Str = Base64.encodeBytes(ba);
				loginObj.put("tomember",memberListobject.getMemberlistForSendMessage().get(selectedpos).getUserName());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"inbox/sendmessage/";

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
			if(pd.isShowing()&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject job=result.getjObj();

			try {
				String status=job.getString("status");
				if(status.equals("success")){
					InboxMessgaeList messagelist=InboxMessgaeList.getMessageList(job);
					Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_msz_is_sent), Toast.LENGTH_SHORT).show();
					parent.onBackPressed();
				}
				else{
					Toast.makeText(getActivity(),job.getString("description"), Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	private class AsyncTaskGetMemberList extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex",""+0);
				loginObj.put("endIndex",""+50);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/myfollowinglist/";

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
			if(pd.isShowing()&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject job=result.getjObj();

			try {
				String status=job.getString("status");
				if(status.equals("success")){
					memberListobject=MemberListForSendMessage.getMemberlist(result.getjObj());
					membername.clear();
					for (int i=0;i<memberListobject.getMemberlistForSendMessage().size();i++){
						membername.add(memberListobject.getMemberlistForSendMessage().get(i).getNickName());
					}
					generateautocomplete(act_to, membername.toArray(new String[membername.size()]));
					act_to.setThreshold(1);
					act_to.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
							
							 String selection = (String) parent.getItemAtPosition(position);
						        int pos = -1;

						        for (int i = 0; i < membername.size(); i++) {
						            if (membername.get(i).equals(selection)) {
						                pos = i;
						                break;
						            }
						        }
						   
							selectedpos= pos;
							
							
						}
					});
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
	private void generateautocomplete(AutoCompleteTextView autextview,String[] arrayToSpinner){
		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
				
				getActivity(),  R.layout.my_autocomplete_text_style, arrayToSpinner);
		autextview.setAdapter(myAdapter);

	}
}


