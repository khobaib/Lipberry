package com.lipberry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lipberry.model.UserCred;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.splunk.mint.Mint;

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
		Mint.initAndStartSession(this, "761a56f9");
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication) getApplication();
		setContentView(R.layout.splash2);
		txt_title=(TextView) findViewById(R.id.txt_title);
		txt_title.setTypeface(Utility.getTypeface2(Splash2Activity.this));

	}
	@Override
	protected void onResume() {
		super.onResume();
	}


	public void goLoginpage(View view){
		Intent  intent=new Intent(Splash2Activity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	public void goRegisterscreen(View view){
		Intent intent=new Intent(Splash2Activity.this, SignupActivity.class);
		startActivity(intent);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

}
