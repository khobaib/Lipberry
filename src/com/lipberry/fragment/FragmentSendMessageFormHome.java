
package com.lipberry.fragment;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.CustomAdapterForIInboxMessage;
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
public class FragmentSendMessageFormHome extends Fragment{
	int threadposition;
	HomeTabFragment parent;
	ProgressDialog pd;
	MemberListForSendMessage memberListobject;
	ThreadMessageList messagelist;;
	private ArrayAdapter<String> mAdapter;
	RelativeLayout re_sent_msz,re_new_msz,re_setting;
	CustomAdapterForIInboxMessage adapter;
	String messageid;
	boolean oncreatecalledstate=false;
	JsonParser jsonParser;
	LipberryApplication appInstance;
	Button b_reply,b_delete;
	String replymessage;
	EditText et_msg_body;
	TextView act_to;
	Button b_send;
	String subject;
	EditText et_su;
	String nickname,userid;
	public FragmentSendMessageFormHome(String nickname,String userid){
		this.nickname=nickname;
		this.userid=userid;
	}
	@SuppressLint("NewApi")

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		memberListobject=new MemberListForSendMessage();
		oncreatecalledstate=true;
		
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication)getActivity().getApplication();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.send_messagefrommemberpage,
				container, false);
		act_to=(TextView) v.findViewById(R.id.act_to);
		b_send=(Button) v.findViewById(R.id.b_send);
		et_msg_body=(EditText) v.findViewById(R.id.et_msg_body);
		et_su=(EditText) v.findViewById(R.id.et_su);
		act_to.setText(nickname);
		
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
					if(replymessage.equalsIgnoreCase("")){
						Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_enter_msz),
								Toast.LENGTH_SHORT).show();
						
					}
					
					else{
						pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
								getActivity().getResources().getString(R.string.txt_please_wait), false);
						new AsyncTaskSendMessage().execute();
					}

				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
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
				//loginObj.put("tomember",base64Str);
				if(!subject.equals("")){
					ba =subject.getBytes();
					base64Str = Base64.encodeBytes(ba);
					loginObj.put("subject",base64Str);
				}
				ba =userid.getBytes();
				base64Str = Base64.encodeBytes(ba);
				loginObj.put("tomember",userid);
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
			Log.e("Sending", result.getjObj().toString());
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
	
}


