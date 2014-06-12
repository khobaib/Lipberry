package com.lipberry;

import com.lipberry.utility.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.WebView;

public class WebViewActtivity extends FragmentActivity   {
	WebView web_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String type="normal";
		boolean foregroud=false;
		int callno=2;
		try {
			
			type=getIntent().getExtras().getString("type");
			foregroud=getIntent().getExtras().getBoolean("foregroud");
			
			if(type.equals("inbox message")){
				Log.e("tag", "6");

				callno=1;
				//mTabHost.setCurrentTab(1);

			}
			else{
				Log.e("tag", "7");

				callno=2;
			}
	    	
			if(foregroud){
				Constants.homeActivity.mTabHost.setCurrentTab(callno);
				finish();
			}
			else{
				Intent intent=new Intent(WebViewActtivity.this, HomeActivity.class);
				intent.putExtra("type", type);
				startActivity(intent);
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			
		} catch (Exception e) {
			
		}
		
		//.mTabHost.setCurrentTab(5);
	
		
		
	}

}
