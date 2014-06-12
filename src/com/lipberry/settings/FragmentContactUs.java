
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
import android.content.Loader.OnLoadCanceledListener;
import android.content.Loader.OnLoadCompleteListener;
import android.graphics.Bitmap;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.lipberry.HomeActivity;
import com.lipberry.LoginActivity;
import com.lipberry.R;
import com.lipberry.Splash2Activity;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
@SuppressLint("NewApi")
public class FragmentContactUs extends Fragment {
	public MenuTabFragment parent;
	Button btn_body_details,btn_general_settings,btn_signout;
	String[]menuarray;
	ListView list_menu_item;
	LipberryApplication appInstance;
	JsonParser jsonParser;

	ProgressBar progressbar_loading;
	WebView webview_contact_us;
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
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_contact_us,
				container, false);

		webview_contact_us=(WebView) v.findViewById(R.id.webview_contact_us);
		progressbar_loading=(ProgressBar) v.findViewById(R.id.progressbar_loading);
		progressbar_loading.setVisibility(View.GONE);
		webview_contact_us.setVisibility(View.VISIBLE);
		WebSettings webSettings = webview_contact_us.getSettings();
		webSettings.setJavaScriptEnabled(true);
//		webview_contact_us.setWebViewClient(new Callback());
//		webview_contact_us.loadUrl(getActivity().getResources().getString(R.string.txt_contact_us_url));
		webview_contact_us.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressbar_loading.setVisibility(View.GONE);
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				progressbar_loading.setVisibility(View.VISIBLE);
			
			}
		
		});
		webview_contact_us.loadUrl(getActivity().getResources().getString(R.string.txt_contact_us_url));

		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.txt_contact_us));
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}

	private class Callback extends WebViewClient{  
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			progressbar_loading.setVisibility(View.GONE);
			return (false);
		
		}
		
		
	}
}

