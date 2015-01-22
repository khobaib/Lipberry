package com.lipberry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;

import com.lipberry.utility.Constants;
import com.splunk.mint.Mint;

public class WebViewActtivity extends FragmentActivity   {
	WebView web_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(this, "761a56f9");
		
		String type="normal";
		boolean foregroud=false;
		int callno=2;
		try {
			
			type=getIntent().getExtras().getString("type");
			foregroud=getIntent().getExtras().getBoolean("foregroud");
			if(type.equals("inbox message")){
				callno=1;
			}
			else{
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
	}

}
