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


public class SplashActivity extends Activity {
	int stateofbackpress=0;
	UserCred usercred;
	EditText e_uname,e_pass,et_email;
	Button b_signin,b_signup,bt_forgotpass,bt_enter;
	String username,password,email;
	JsonParser jsonParser;
	ProgressDialog pd;
	LipberryApplication appInstance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication) getApplication();
		
		
		setContentView(R.layout.splash);
		Handler handler=new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(!appInstance.isRememberMe()){
					gosplash2();
				}
				else{
					gosplash2();
					if(Constants.isOnline(SplashActivity.this)){
						username=appInstance.getUserCred().getUsername();
						password=appInstance.getUserCred().getPassword(); 
						signin();
					}
					else{
						Toast.makeText(SplashActivity.this, getResources().getString(R.string.Toast_check_internet), 10000).show();
						Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
						startActivity(intent);

					}
				}

			}
		},2000);
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

	public void goLoginpage(View view){
		Intent  intent=new Intent(SplashActivity.this, LoginActivity.class);
		startActivity(intent);
	}
	public void goRegisterscreen(View view){
		Intent intent=new Intent(SplashActivity.this, SignupActivity.class);
		startActivity(intent);
	}

	public void signin(){
		if(Constants.isOnline(SplashActivity.this)){
			if  (!Constants.namecheck(username)) {
				if(username.length()<3){
					Toast.makeText(SplashActivity.this, " Username  can't be less than 3 characcter",
							10000).show();
				}

				else if(username.length()>10){
					Toast.makeText(SplashActivity.this, " Username  can't be more than 10 characcter",
							10000).show();
				}
				else{
					Toast.makeText(SplashActivity.this, " Username  must  start with later and continue with" +
							" only later,number and dashes ",
							10000).show();
				}

			}

			else if(password.trim().equals("")){
				Toast.makeText(SplashActivity.this, "Please enter password",
						10000).show();
			}
			// TODO Auto-generated method stub
			else{
				pd=ProgressDialog.show(SplashActivity.this, "Lipberry",
						"Signing in", true);
				new  AsyncTaskLogin().execute();
			}

		}

		else{
			Toast.makeText(SplashActivity.this, getResources().getString(R.string.Toast_check_internet), 10000).show();
		}

	}


	private class AsyncTaskLogin extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("username", username);
				loginObj.put("password", password);
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
				appInstance.setUserCred(usercred);
				appInstance.setRememberMe(true);
				Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
				startActivity(intent);
			}
			else{
				String descrip=job.getString("description");
				Toast.makeText(SplashActivity.this,descrip, 10000).show();
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
