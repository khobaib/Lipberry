
package com.lipberry.settings;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.model.InboxMessgaeList;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.UserCred;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
@SuppressLint("NewApi")
public class FragmentMessageSetting extends Fragment {
	public MenuTabFragment parent;
	CheckBox check_weekly_news,check_stop_emailmessage,check_stop_privatemessage,check_stop_commentMails,
	check_stop_followerMails,check_stop_likeArtMails,check_push_new_msz;
	ListView list_menu_item;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	Button btn_save;
	ProgressDialog pd;
	int weekly_news,stop_emailmessage,stop_privatemessage,stop_commentMails,stop_followerMails,stop_likeArtMails;
	TextView txt_sys_noti,txt_weekly_news_letter,txt_direct_msz_to_mail,txt_allow_member_directmsz,txt_check_push_new_msz,
	txt_stop_followerMails,txt_stop_likeArtMails;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appInstance = (LipberryApplication)getActivity().getApplication();
		jsonParser=new JsonParser();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Constants.MESSAGESETTINGSTATE=false;
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_message_setting,
				container, false);
		txt_sys_noti=(TextView) v.findViewById(R.id.txt_sys_noti);
		txt_weekly_news_letter=(TextView) v.findViewById(R.id.txt_weekly_news_letter);
		txt_direct_msz_to_mail=(TextView) v.findViewById(R.id.txt_direct_msz_to_mail);
		txt_allow_member_directmsz=(TextView) v.findViewById(R.id.txt_allow_member_directmsz);
		txt_check_push_new_msz=(TextView) v.findViewById(R.id.txt_check_push_new_msz);
		txt_stop_followerMails=(TextView) v.findViewById(R.id.txt_stop_followerMails);
		txt_stop_likeArtMails=(TextView) v.findViewById(R.id.txt_stop_likeArtMails);

		txt_stop_followerMails.setTypeface(Utility.getTypeface1(getActivity()));
		txt_stop_likeArtMails.setTypeface(Utility.getTypeface1(getActivity()));
		txt_sys_noti.setTypeface(Utility.getTypeface1(getActivity()));
		txt_weekly_news_letter.setTypeface(Utility.getTypeface1(getActivity()));
		txt_direct_msz_to_mail.setTypeface(Utility.getTypeface1(getActivity()));
		txt_allow_member_directmsz.setTypeface(Utility.getTypeface1(getActivity()));
		txt_check_push_new_msz.setTypeface(Utility.getTypeface1(getActivity()));
		
		
		appInstance = (LipberryApplication) getActivity().getApplication();
		check_weekly_news=(CheckBox) v.findViewById(R.id.check_weekly_news);
		check_stop_emailmessage=(CheckBox) v.findViewById(R.id.check_stop_emailmessage);
		check_stop_privatemessage=(CheckBox) v.findViewById(R.id.check_stop_privatemessage);
		check_stop_commentMails=(CheckBox) v.findViewById(R.id.check_stop_commentMails);
		check_stop_followerMails=(CheckBox) v.findViewById(R.id.check_stop_followerMails);
		check_stop_likeArtMails=(CheckBox) v.findViewById(R.id.check_stop_likeArtMails);
		check_push_new_msz=(CheckBox) v.findViewById(R.id.check_push_new_msz);
		btn_save=(Button) v.findViewById(R.id.btn_save);
		btn_save.setTypeface(Utility.getTypeface1(getActivity()));
		//
		//		check_push_new_msz=(CheckBox) v.findViewById(R.id.check_push_new_msz);
		//		system_notification.setTypeface(Utility.getTypeface1(getActivity()));
		//		check_weekly_news_letter.setTypeface(Utility.getTypeface1(getActivity()));
		//		btn_save.setTypeface(Utility.getTypeface1(getActivity()));
		//		btn_save.setTypeface(Utility.getTypeface1(getActivity()));
		//		btn_save.setTypeface(Utility.getTypeface1(getActivity()));
		//		check_push_new_msz.setTypeface(Utility.getTypeface1(getActivity()));
		//		
		//		
		//		check_direct_msz_to_mail.setChecked(appInstance.getUserCred().getDirect_msz_mail());
		//		check_allow_member_directmsz.setChecked(appInstance.getUserCred().getAllow_direct_msz());
		//		check_push_new_msz.setChecked(appInstance.getUserCred().getStop_push_new_message());
		//		system_notification.setChecked(appInstance.getUserCred().getSystem_notification());
		//		check_weekly_news_letter.setChecked(appInstance.getUserCred().getWeekly_newsletter());
		
//		CheckBox check_weekly_news,check_stop_emailmessage,check_stop_privatemessage,check_stop_commentMails,
//		check_stop_followerMails,check_stop_likeArtMails,check_push_new_msz;
		Log.e("result", appInstance.getUserCred().getPush_new_msz());

		if(appInstance.getUserCred().getPush_new_msz().equals("0")){
			check_push_new_msz.setChecked(false);
		}
		else{
			check_push_new_msz.setChecked(true);
		}
		
		if(appInstance.getUserCred().getStop_commentMails().equals("0")){
			check_stop_commentMails.setChecked(false);
		}
		else{
			check_stop_commentMails.setChecked(true);
		}
		if(appInstance.getUserCred().getStop_followerMails().equals("0")){
			check_stop_followerMails.setChecked(false);
		}
		else{
			check_stop_followerMails.setChecked(true);
		}
		if(appInstance.getUserCred().getStop_likeArtMails().equals("0")){
			check_stop_likeArtMails.setChecked(false);
		}
		else{
			check_stop_likeArtMails.setChecked(true);
		}
		if(appInstance.getUserCred().getStop_privateMails().equals("0")){
			check_stop_emailmessage.setChecked(false);
		}
		else{
			check_stop_emailmessage.setChecked(true);
		}
		if(appInstance.getUserCred().getStop_privateMessages().equals("0")){
			check_stop_privatemessage.setChecked(false);
		}
		else{
			check_stop_privatemessage.setChecked(true);
		}
		if(appInstance.getUserCred().getWeekly_news().equals("0")){
			check_weekly_news.setChecked(false);
		}
		else{
			check_weekly_news.setChecked(true);
		}



		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
//				CheckBox check_weekly_news,check_stop_emailmessage,check_stop_privatemessage,check_stop_commentMails,
//				check_stop_followerMails,check_stop_likeArtMails,check_push_new_msz;
				
				
				if(check_weekly_news.isChecked()){
					weekly_news=1;
				}
				else{
					weekly_news=0;
				}
				// TODO Auto-generated method stub
				if(check_stop_emailmessage.isChecked()){
					stop_emailmessage=1;
				}
				else{
					stop_emailmessage=0;
				}
				if(check_stop_privatemessage.isChecked()){
					stop_privatemessage=1;
				}
				else{
					stop_privatemessage=0;
				}
				if(check_stop_commentMails.isChecked()){
					stop_commentMails=1;
				}
				else{
					stop_commentMails=0;
				}
				if(check_stop_followerMails.isChecked()){
					stop_followerMails=1;
				}
				else{
					stop_followerMails=0;
				}
				if(check_stop_likeArtMails.isChecked()){
					stop_likeArtMails=1;
				}
				else{
					stop_likeArtMails=0;
				}
			

				if(Constants.isOnline(getActivity())){

					pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
							getActivity().getResources().getString(R.string.txt_please_wait), false);
					new AsyncTaskSaveMessageSetting().execute();


				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		return v;
	}
	private class AsyncTaskSaveMessageSetting extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("weekly_news", weekly_news);
				loginObj.put("stop_emailmessage", stop_emailmessage);
				loginObj.put("stop_privatemessage", stop_privatemessage);
				loginObj.put("stop_commentMails", stop_commentMails);
				loginObj.put("stop_followerMails", stop_followerMails);
				loginObj.put("stop_likeArtMails", stop_likeArtMails);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"settings/messagesettings/";
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
					savemessagesettingslocally();
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

	public void savemessagesettingslocally(){
		String a;
		
//		loginObj.put("weekly_news", weekly_news);
//		loginObj.put("stop_emailmessage", stop_emailmessage);
//		loginObj.put("stop_privatemessage", stop_privatemessage);
//		loginObj.put("stop_commentMails", stop_commentMails);
//		loginObj.put("stop_followerMails", stop_followerMails);
//		loginObj.put("stop_likeArtMails", stop_likeArtMails);
		
		UserCred ucred=appInstance.getUserCred();
		
		ucred.setWeekly_news(""+weekly_news);
		ucred.setStop_commentMails(""+stop_commentMails);
		ucred.setStop_privateMails(""+stop_emailmessage);
		ucred.setStop_privateMessages(""+stop_privatemessage);
		ucred.setStop_followerMails(""+stop_followerMails);
		ucred.setStop_likeArtMails(""+stop_likeArtMails);
		
		if(check_push_new_msz.isChecked()){
			ucred.setPush_new_msz("1");
		}
		else{
			ucred.setPush_new_msz("0");

		}
	
		appInstance.setUserCred(ucred);
		Log.e("result", appInstance.getUserCred().getPush_new_msz());


	}

	@Override
	public void onResume() {
		super.onResume();
		((HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.txt_body_details));
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}


}

