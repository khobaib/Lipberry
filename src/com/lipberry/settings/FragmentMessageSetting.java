
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.model.UserCred;
import com.lipberry.utility.LipberryApplication;
@SuppressLint("NewApi")
public class FragmentMessageSetting extends Fragment {
	public MenuTabFragment parent;
	CheckBox system_notification,check_weekly_news_letter,check_direct_msz_to_mail,check_allow_member_directmsz,
	check_push_new_msz;
	ListView list_menu_item;
	LipberryApplication appInstance;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_message_setting,
				container, false);
		appInstance = (LipberryApplication) getActivity().getApplication();
		system_notification=(CheckBox) v.findViewById(R.id.system_notification);
		check_weekly_news_letter=(CheckBox) v.findViewById(R.id.check_weekly_news_letter);
		check_direct_msz_to_mail=(CheckBox) v.findViewById(R.id.check_direct_msz_to_mail);
		check_allow_member_directmsz=(CheckBox) v.findViewById(R.id.check_allow_member_directmsz);
		check_push_new_msz=(CheckBox) v.findViewById(R.id.check_push_new_msz);
		check_direct_msz_to_mail.setChecked(appInstance.getUserCred().getDirect_msz_mail());
		check_allow_member_directmsz.setChecked(appInstance.getUserCred().getAllow_direct_msz());
		check_push_new_msz.setChecked(appInstance.getUserCred().getStop_push_new_message());
		system_notification.setChecked(appInstance.getUserCred().getWeekly_newsletter());
		check_weekly_news_letter.setChecked(appInstance.getUserCred().getWeekly_newsletter());
		Log.e("outside", "  "+appInstance.getUserCred().getDirect_msz_mail());
		check_direct_msz_to_mail.setOnClickListener(new OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean a=!appInstance.getUserCred().getDirect_msz_mail();
				//appInstance.getUserCred().setDirect_msz_mail(a);
				UserCred ucred=appInstance.getUserCred();
				ucred.setDirect_msz_mail(a);
				appInstance.setUserCred(ucred);
				
				Log.e("result", a+"  "+appInstance.getUserCred().getDirect_msz_mail());
			}
		});
		check_direct_msz_to_mail.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				appInstance.getUserCred().setSystem_notification(check_direct_msz_to_mail.isChecked());
				appInstance.setUserCred(appInstance.getUserCred());
			}
		});
		check_direct_msz_to_mail.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				appInstance.getUserCred().setSystem_notification(check_direct_msz_to_mail.isChecked());
				appInstance.setUserCred(appInstance.getUserCred());
			}
		});
		check_direct_msz_to_mail.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				appInstance.getUserCred().setSystem_notification(check_direct_msz_to_mail.isChecked());
				appInstance.setUserCred(appInstance.getUserCred());
			}
		});check_direct_msz_to_mail.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				appInstance.getUserCred().setSystem_notification(check_direct_msz_to_mail.isChecked());
				appInstance.setUserCred(appInstance.getUserCred());
			}
		});

		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
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

