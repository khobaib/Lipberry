
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
	CheckBox system_notification,check_weekly_news_letter,check_direct_msz_to_mail,check_allow_member_directmsz,
	check_push_new_msz;
	ListView list_menu_item;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	Button btn_save;
	ProgressDialog pd;
	int weekly_news,stop_emailmessage,stop_privatemessage,stop_commentMails;
	TextView txt_sys_noti,txt_weekly_news_letter,txt_direct_msz_to_mail,txt_allow_member_directmsz,txt_check_push_new_msz;
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
		txt_sys_noti.setTypeface(Utility.getTypeface1(getActivity()));
		txt_weekly_news_letter.setTypeface(Utility.getTypeface1(getActivity()));
		txt_direct_msz_to_mail.setTypeface(Utility.getTypeface1(getActivity()));
		txt_allow_member_directmsz.setTypeface(Utility.getTypeface1(getActivity()));
		txt_check_push_new_msz.setTypeface(Utility.getTypeface1(getActivity()));
		appInstance = (LipberryApplication) getActivity().getApplication();
		system_notification=(CheckBox) v.findViewById(R.id.system_notification);
		check_weekly_news_letter=(CheckBox) v.findViewById(R.id.check_weekly_news_letter);
		check_direct_msz_to_mail=(CheckBox) v.findViewById(R.id.check_direct_msz_to_mail);
		check_allow_member_directmsz=(CheckBox) v.findViewById(R.id.check_allow_member_directmsz);
		btn_save=(Button) v.findViewById(R.id.btn_save);
		check_push_new_msz=(CheckBox) v.findViewById(R.id.check_push_new_msz);
		system_notification.setTypeface(Utility.getTypeface1(getActivity()));
		check_weekly_news_letter.setTypeface(Utility.getTypeface1(getActivity()));
		check_direct_msz_to_mail.setTypeface(Utility.getTypeface1(getActivity()));
		check_allow_member_directmsz.setTypeface(Utility.getTypeface1(getActivity()));
		btn_save.setTypeface(Utility.getTypeface1(getActivity()));
		check_push_new_msz.setTypeface(Utility.getTypeface1(getActivity()));
		check_direct_msz_to_mail.setChecked(appInstance.getUserCred().getDirect_msz_mail());
		check_allow_member_directmsz.setChecked(appInstance.getUserCred().getAllow_direct_msz());
		check_push_new_msz.setChecked(appInstance.getUserCred().getStop_push_new_message());
		system_notification.setChecked(appInstance.getUserCred().getSystem_notification());
		check_weekly_news_letter.setChecked(appInstance.getUserCred().getWeekly_newsletter());
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(check_weekly_news_letter.isChecked()){
					weekly_news=1;
				}
				else{
					weekly_news=0;
				}
				// TODO Auto-generated method stub
				if(check_direct_msz_to_mail.isChecked()){
					stop_emailmessage=1;
				}
				else{
					stop_emailmessage=0;
				}
				if(check_allow_member_directmsz.isChecked()){
					stop_privatemessage=1;
				}
				else{
					stop_privatemessage=0;
				}
				if(system_notification.isChecked()){
					stop_commentMails=1;
				}
				else{
					stop_commentMails=0;
				}
				boolean a=check_push_new_msz.isChecked();
				UserCred ucred=appInstance.getUserCred();
				ucred.setStop_push_new_message(a);
				appInstance.setUserCred(ucred);

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
		boolean a;
		
		UserCred ucred=appInstance.getUserCred();
		if(weekly_news==0){
			a=false;
		}
		else{
			a=true;
		}
		ucred.setWeekly_newsletter(a);
		if(stop_emailmessage==0){
			a=false;
		}
		else{
			a=true;
		}
		ucred.setDirect_msz_mail(a);
		if(stop_commentMails==0){
			a=false;
		}
		else{
			a=true;
		}
		Log.e("sys", stop_commentMails+" "+a);
		ucred.setSystem_notification(a);
		if(stop_privatemessage==0){
			a=false;
		}
		else{
			a=true;
		}
		
		ucred.setAllow_direct_msz(a);
		appInstance.setUserCred(ucred);
		Log.e("syst", appInstance.getUserCred().getSystem_notification()+"");
		
		
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

