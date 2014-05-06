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
import com.lipberry.utility.Utility;

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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Splash2Activity extends Activity {
	int stateofbackpress=0;
	UserCred usercred;
	EditText e_uname,e_pass,et_email;
	Button b_signin,b_signup,bt_forgotpass,bt_enter;
	String username,password,email;
	JsonParser jsonParser;
	ProgressDialog pd;
	TextView txt_title;
	LipberryApplication appInstance;
	boolean system_notification,weekly_newsletter,direct_msz_mail,allow_direct_msz,stop_push_new_message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication) getApplication();
		setContentView(R.layout.splash2);
		txt_title=(TextView) findViewById(R.id.txt_title);
		txt_title.setTypeface(Utility.getTypeface2(Splash2Activity.this));

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


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
