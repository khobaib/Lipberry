package com.lipberry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.UserCred;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Splash2Activity extends Activity {
	int stateofbackpress=0;
	UserCred usercred;
	EditText e_uname,e_pass,et_email;
	Button b_signin,b_signup,bt_forgotpass,bt_enter;
	String username,password,email;
	JsonParser jsonParser;
	ProgressDialog pd;
	LipberryApplication appInstance;
	boolean system_notification,weekly_newsletter,direct_msz_mail,allow_direct_msz,stop_push_new_message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication) getApplication();
		setContentView(R.layout.splash2);
//		Handler handler=new Handler();
//		handler.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				if(!appInstance.isRememberMe()){
//					gosplash2();
//				}
//				else{
//					gosplash2();
//					if(Constants.isOnline(Splash2Activity.this)){
//						username=appInstance.getUserCred().getUsername();
//						password=appInstance.getUserCred().getPassword(); 
//						system_notification=appInstance.getUserCred().getSystem_notification();
//						weekly_newsletter=appInstance.getUserCred().getWeekly_newsletter();
//						direct_msz_mail=appInstance.getUserCred().getDirect_msz_mail();
//						allow_direct_msz=appInstance.getUserCred().getAllow_direct_msz();
//						stop_push_new_message=appInstance.getUserCred().getStop_push_new_message();
//						signin();
//					}
//					else{
//						Toast.makeText(Splash2Activity.this, getResources().getString(R.string.Toast_check_internet), 10000).show();
//						Intent intent=new Intent(Splash2Activity.this, HomeActivity.class);
//						startActivity(intent);
//						finish();
//
//					}
//				}
//
//			}
//		},4000);
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}



//	public void gosplash2(){
//		stateofbackpress=0;
//		setContentView(R.layout.splash2);
//
//	}

	public void goLoginpage(View view){
		Intent  intent=new Intent(Splash2Activity.this, LoginActivity.class);
		startActivity(intent);
	//	finish();
	}
	public void goRegisterscreen(View view){
		Intent intent=new Intent(Splash2Activity.this, SignupActivity.class);
		startActivity(intent);
	//	finish();
	}

	

	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

}
