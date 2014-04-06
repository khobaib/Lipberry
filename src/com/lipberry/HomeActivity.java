package com.lipberry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.lipberry.fragment.HomeTabFragment;
import com.lipberry.fragment.WriteTopicTabFragment;
import com.lipberry.fragment.CategoryTabFragment;
import com.lipberry.fragment.IneractionTabFragment;
import com.lipberry.fragment.InboxTabFragment;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.fragment.TabFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends FragmentActivity {
	public static Typeface tp;
	private FragmentTabHost mTabHost;
	public TabFragment activeFragment;
	private ViewGroup mTabsPlaceHoler;
	public Button backbuttonoftab;
	public TextView welcome_title;
	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		welcome_title=(TextView) findViewById(R.id.welcome_title);
		backbuttonoftab=(Button) findViewById(R.id.backbuttonoftab);
		backbuttonoftab.setVisibility(View.GONE);
		setTabs();
		mTabHost.setCurrentTab(4);

	}
	private void setTabs() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(),
				android.R.id.tabcontent);
		mTabsPlaceHoler = (TabWidget) findViewById(android.R.id.tabs);
		addTab("Unkknown", R.drawable.lunknown, WriteTopicTabFragment.class);
		addTab("Interaction", R.drawable.linteraction, IneractionTabFragment.class);
		addTab("Inbox", R.drawable.linbox, InboxTabFragment.class);
		addTab("Categories", R.drawable.lcategory, CategoryTabFragment.class);
		addTab("Home", R.drawable.lhome, HomeTabFragment.class);
		addTab("Menu", R.drawable.lmenu, MenuTabFragment.class);

	}

	private void addTab(String labelId, int drawableId, Class<?> c) {

		FragmentTabHost.TabSpec spec = mTabHost.newTabSpec(labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, mTabsPlaceHoler, false);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		mTabHost.addTab(spec, c, null);

	}

	public void setTabSelection() {
		// TODO Auto-generated method stub
		mTabHost.setCurrentTab(1);
	}

	public void setTabSelection0() {
		// TODO Auto-generated method stub
		mTabHost.setCurrentTab(4);
	}
	@Override
	public void onBackPressed() {
		activeFragment.onBackPressed();
	}
	public void close() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				HomeActivity.this);
		alertDialogBuilder.setTitle("Lipberry");
		alertDialogBuilder
		.setMessage("Would you like to exit?")
		.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				finisssh();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void finisssh(){
		super.onBackPressed();
	}


}

