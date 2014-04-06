
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.SingleMember;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;



@SuppressLint("NewApi")
public class FragmentMember extends Fragment {
	ImageLoader imageLoader;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	WriteTopicTabFragment parent;
	TextView txt_num_seen,txt_num_following,txt_num_follower,txt_name,txt_nick_name,txt_bio;
	ImageView img_member_pic;
	Button btn_follow_her,btn_send,btn_share;
	ProgressDialog pd;
	boolean followstate=false;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appInstance = (LipberryApplication) getActivity().getApplication();
		jsonParser=new JsonParser();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext()).defaultDisplayImageOptions(
						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_member,
				container, false);
		txt_num_seen=(TextView) v.findViewById(R.id.txt_num_seen);
		txt_num_following=(TextView) v.findViewById(R.id.txt_num_following);
		txt_num_follower=(TextView) v.findViewById(R.id.txt_num_follower);
		txt_name=(TextView) v.findViewById(R.id.txt_name);
		txt_nick_name=(TextView) v.findViewById(R.id.txt_nick_name);
		txt_bio=(TextView) v.findViewById(R.id.txt_bio);
		img_member_pic=(ImageView) v.findViewById(R.id.img_member_pic);
		btn_follow_her=(Button) v.findViewById(R.id.btn_follow_her);
		btn_send=(Button) v.findViewById(R.id.btn_send);
		btn_share=(Button) v.findViewById(R.id.btn_share);
		if(followstate){
			btn_follow_her.setText("unfollow");
		}
		else{
			btn_follow_her.setText("follow");
		}
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
					"Retreving member", true);
			new AsyncTaskGetSinleMember().execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		btn_follow_her.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonfollowclicked();
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
	private class AsyncTaskGetSinleMember extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			String url =Constants.baseurl+"account/findmemberbyid/"+Constants.userid+"/";
			ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, url, null,
					null, null);
			return response;
		}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if((pd!=null)&&(pd.isShowing())){
				pd.dismiss();
			}
			setMemberObject(result.getjObj().toString());
		}
	}
	public void setMemberObject(String  respnse){
		try {
			JSONObject jobj=new JSONObject(respnse);
			String  status=jobj.getString("status");
			if(status.equals("success")){
				SingleMember SingleMember  =com.lipberry.model.SingleMember.parseSingleMember(jobj);
				setUserInterface(SingleMember);
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_member_found),
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_member_found),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	public void setUserInterface(SingleMember singleMember){
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setText(getActivity().getResources().getString(R.string.back_string));
		((HomeActivity)getActivity()).welcome_title.setText(singleMember.getName());
		txt_name.setText(singleMember.getName());
		txt_nick_name.setText(singleMember.getNickname());
		txt_bio.setText(singleMember.getBrief());
		txt_num_seen.setText(singleMember.getPublicpage_visit());
		txt_num_follower.setText(singleMember.getNumber_of_followers());
		txt_num_following.setText(singleMember.getNumber_of_following());
		imageLoader.displayImage(singleMember.getAvatar(), img_member_pic);
	}
	public void buttonfollowclicked(){
		if(!followstate){
			if(Constants.isOnline(getActivity())){
				pd=ProgressDialog.show(getActivity(), "Lipberry",
						"Please wait", true);
				new AsyncTaskSendFollowReq().execute();
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		}
		else{
			if(Constants.isOnline(getActivity())){
				pd=ProgressDialog.show(getActivity(), "Lipberry",
						"Please wait", true);
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	private class AsyncTaskSendFollowReq extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/followmember/"+Constants.userid+"/";
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
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
		}
	}
}

