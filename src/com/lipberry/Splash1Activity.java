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
import com.lipberry.utility.Base64;
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


public class Splash1Activity extends Activity {
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
		Log.e("password", appInstance.getUserCred().getPassword());
		setContentView(R.layout.splash);
		txt_title=(TextView) findViewById(R.id.txt_title);
		txt_title.setTypeface(Utility.getTypeface2(Splash1Activity.this));
		Utility.getDeviceWidth(Splash1Activity.this);
		Handler handler=new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(!appInstance.isRememberMe()){
					Intent intent=new Intent(Splash1Activity.this,Splash2Activity.class);
					startActivity(intent);
					finish();
				}
				else{
					if(Constants.isOnline(Splash1Activity.this)){
						username=appInstance.getUserCred().getUsername();
						password=appInstance.getUserCred().getPassword(); 
						system_notification=appInstance.getUserCred().getSystem_notification();
						weekly_newsletter=appInstance.getUserCred().getWeekly_newsletter();
						direct_msz_mail=appInstance.getUserCred().getDirect_msz_mail();
						allow_direct_msz=appInstance.getUserCred().getAllow_direct_msz();
						stop_push_new_message=appInstance.getUserCred().getStop_push_new_message();
						signin();
					}
					else{
						Toast.makeText(Splash1Activity.this, getResources().getString(R.string.Toast_check_internet), Toast.LENGTH_SHORT).show();
						Intent intent=new Intent(Splash1Activity.this, HomeActivity.class);
						startActivity(intent);
						finish();

					}
				}

			}
		},4000);
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}



	public void gosplash2(){
		stateofbackpress=0;
		setContentView(R.layout.splash2);

	}
	public void signin(){
		if(Constants.isOnline(Splash1Activity.this)){
			if(Constants.isValidEmail(username)){
				pd=ProgressDialog.show(Splash1Activity.this, getResources().getString(R.string.app_name_arabic),
						getResources().getString(R.string.txt_signing_in), false);
				new  AsyncTaskLogin().execute();
				
			}
			else{
				if  (!Constants.namecheck(username)) {
					 

					if(username.length()<3){
						Toast.makeText(Splash1Activity.this, getResources().getString(R.string.txt_uname_cant_lessthan),
								Toast.LENGTH_SHORT).show();
					}

					else if(username.length()>10){
						Toast.makeText(Splash1Activity.this, getResources().getString(R.string.txt_uname_cant_more),
								Toast.LENGTH_SHORT).show();
						
					}
					else{
						Toast.makeText(Splash1Activity.this, getResources().getString(R.string.txt_uname_spec) ,
								Toast.LENGTH_SHORT).show();
					}
				}

				else if(password.trim().equals("")){
					Toast.makeText(Splash1Activity.this,getResources().getString(R.string.txt_please_enter_password),
							Toast.LENGTH_SHORT).show();
				}
				else{
					
					pd=ProgressDialog.show(Splash1Activity.this, getResources().getString(R.string.app_name_arabic),
							getResources().getString(R.string.txt_signing_in), false);
					new  AsyncTaskLogin().execute();
					
				}
				
			}
		}

		else{
			Toast.makeText(Splash1Activity.this, getResources().getString(R.string.Toast_check_internet), 10000).show();
		}
	}


	private class AsyncTaskLogin extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				byte[] ba = username.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("username", base64Str);
				ba=password.getBytes();
				base64Str=Base64.encodeBytes(ba);
				loginObj.put("password", base64Str);
				String loginData = loginObj.toString();
				String url = Constants.baseurl+"account/login";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, 
						null,
						loginData, null);
				return response;
			} catch (JSONException e) {                
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			setUsercredential(result.getjObj().toString());
		}
	}


	public void setUsercredential(String result){
		usercred=new UserCred();
		try {
			JSONObject  job=new JSONObject (result);
			String status=job.getString("status");
			if(status.equals("success")){
				usercred=usercred.parseUserCred(job);
				usercred.checknull();
				usercred.setPassword(password);
				usercred.setSystem_notification(system_notification);
				usercred.setWeekly_newsletter(weekly_newsletter);
				usercred.setDirect_msz_mail(direct_msz_mail);
				usercred.setAllow_direct_msz(allow_direct_msz);
				usercred.setStop_push_new_message(stop_push_new_message);
				appInstance.setUserCred(usercred);
				appInstance.setRememberMe(true);
				Intent intent=new Intent(Splash1Activity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
			else{
				String descrip=job.getString("description");
				Toast.makeText(Splash1Activity.this,descrip, Toast.LENGTH_SHORT).show();
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

}
