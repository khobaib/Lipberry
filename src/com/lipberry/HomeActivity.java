package com.lipberry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.lipberry.fragment.FragmentTab1;
import com.lipberry.fragment.FragmentTab2;
import com.lipberry.fragment.FragmentTab3;
import com.lipberry.fragment.FragmentTab4;
import com.lipberry.fragment.FragmentTab5;
import com.lipberry.fragment.FragmentTab6;
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

			
		}
	
	
		private void setTabs() {
			mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup(this, getSupportFragmentManager(),
				android.R.id.tabcontent);
			mTabsPlaceHoler = (TabWidget) findViewById(android.R.id.tabs);
			addTab("Menu", R.drawable.menu, FragmentTab1.class);
			addTab("Home", R.drawable.home, FragmentTab2.class);
			addTab("Interaction", R.drawable.interaction, FragmentTab4.class);
			addTab("Categories", R.drawable.categories, FragmentTab3.class);
			
			addTab("Inbox", R.drawable.inbox, FragmentTab5.class);
			addTab("Topic", R.drawable.topic, FragmentTab6.class);
		
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
		mTabHost.setCurrentTab(0);
	}

	
	
	
	
	
	
	
	@Override
	public void onBackPressed() {
		activeFragment.onBackPressed();
	}

	// method for TabFragment to call when the user navigates out of the app
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

