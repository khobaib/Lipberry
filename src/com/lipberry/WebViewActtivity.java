package com.lipberry;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActtivity extends Activity {
	WebView web_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		String url=getIntent().getExtras().getString("url");
		web_view=(WebView) findViewById(R.id.web_view);
	web_view.loadUrl(url);
		
	}

}
