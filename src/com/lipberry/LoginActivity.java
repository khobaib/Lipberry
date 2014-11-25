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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {
	int stateofbackpress=0;
	String l;
	UserCred usercred;
	EditText e_uname,e_pass,et_email;
	Button b_signin,bt_forgotpass,bt_enter;
	String username,password,email;
	JsonParser jsonParser;
	ProgressDialog pd;
	LipberryApplication appInstance;
	TextView txt_title,tv_sign_in;
	String sk;
	Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication) getApplication();
		initview();
	}
	public void initview(){
		stateofbackpress=0;
		setContentView(R.layout.login);
		txt_title=(TextView) findViewById(R.id.txt_title);
		e_pass=(EditText) findViewById(R.id.e_pass);
		tv_sign_in=(TextView) findViewById(R.id.tv_sign_in);
		tv_sign_in.setTypeface(Utility.getTypeface1(LoginActivity.this));
		e_uname=(EditText) findViewById(R.id.e_uname);
		b_signin=(Button) findViewById(R.id.b_signin);
		bt_forgotpass=(Button) findViewById(R.id.bt_forgotpass);
		txt_title.setTypeface(Utility.getTypeface2(LoginActivity.this));
		bt_forgotpass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				forgotpass();
			}
		});
		bt_forgotpass.setTypeface(Utility.getTypeface1(LoginActivity.this));
		b_signin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				username=e_uname.getText().toString();
				password=e_pass.getText().toString(); 
				signin();
			}
		});
		
		e_pass.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		        	username=e_uname.getText().toString();
					password=e_pass.getText().toString(); 
					signin();
		          // Perform action on key press
		          return true;
		        }
		        return false;
		    }
		});
		e_uname.addTextChangedListener(new TextWatcher() {          
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
            	e_uname.setBackgroundResource(R.drawable.rounded_edittext);

            }                       
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub                          
            }                       
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub                          

            }
        });

		e_pass.addTextChangedListener(new TextWatcher() {          
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
            	e_pass.setBackgroundResource(R.drawable.lbackgound_bottombar);

            }                       
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub                          
            }                       
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub                          

            }
        });


	}
	public void signin(){

		if(Constants.isOnline(LoginActivity.this)){
			if(Constants.isValidEmail(username)){
				pd=ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.app_name_arabic),
						getResources().getString(R.string.txt_signing_in), false);
				new  AsyncTaskLogin().execute();
				
			}
			else{
				if  (!Constants.namecheck(username)) {
					 
	            	e_uname.setBackgroundResource(R.drawable.rounded_text_nofield);

					if(username.length()<3){
						
						Toast.makeText(LoginActivity.this, getResources().getString(R.string.txt_uname_cant_lessthan),
								Toast.LENGTH_SHORT).show();
					}

					else if(username.length()>10){
						Toast.makeText(LoginActivity.this, getResources().getString(R.string.txt_uname_cant_more),
								10000).show();
						
					}
					else{
						Toast.makeText(LoginActivity.this,getResources().getString(R.string.txt_uname_spec),
								10000).show();
					}
				}
				
				else if(password.trim().length()<6){
	            	e_pass.setBackgroundResource(R.drawable.rounded_txt_forgotpass);

					Toast.makeText(LoginActivity.this, getResources().getString(R.string.et_signup_password),
							10000).show();
				}
				else{
					pd=ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.app_name_arabic),
							getResources().getString(R.string.txt_signing_in), false);
					new  AsyncTaskLogin().execute();
					
				}
				
			}
		}

		else{
			Toast.makeText(LoginActivity.this, getResources().getString(R.string.Toast_check_internet), 10000).show();
		}

	}
	public void forgotpass(){
		stateofbackpress=1;
		setContentView(R.layout.forgotpass);
		//et_email,bt_enter,email
		bt_enter=(Button) findViewById(R.id.bt_enter);
		et_email=(EditText) findViewById(R.id.et_email);
		et_email.addTextChangedListener(new TextWatcher() {          
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
            	et_email.setBackgroundResource(R.drawable.rounded_edittext);

            }                       
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub                          
            }                       
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub                          

            }
        });
		bt_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				email=et_email.getText().toString();
				if(Constants.isOnline(LoginActivity.this)){
					if  (!Constants.isValidEmail(email)) {
		            	et_email.setBackgroundResource(R.drawable.rounded_text_nofield);

						Toast.makeText(LoginActivity.this,  getResources().getString(R.string.txt_please_enter_email),
								10000).show();
					}
					else{
						pd=ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.app_name_arabic),
								getResources().getString(R.string.txt_loading), true);
						new  AsyncTaskForgotPass().execute();
					}
				}

				else{
					Toast.makeText(LoginActivity.this, getResources().getString(R.string.Toast_check_internet), 10000).show();
					Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
					startActivity(intent);	 
				}
			}
		});
	}


	private class AsyncTaskForgotPass extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("email", email);
				String loginData = loginObj.toString();
				String url = "http://lipberry.com/API/account/forgetpassword";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
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

			if(pd.isShowing()&&(pd!=null)){
				pd.dismiss();
			}

			JSONObject job=result.getjObj();
			try {
				String msz=job.getString("message");
				Toast.makeText(LoginActivity.this, msz, 10000).show();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class AsyncTaskLogin extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				byte[] ba = username.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("username", username);
				ba=password.getBytes();
				base64Str=Base64.encodeBytes(ba);
				//base64Str=Utility.getEncodedpassword(base64Str);
				loginObj.put("password", base64Str);
				String loginData = loginObj.toString();
				String url = Constants.baseurl+"account/login";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
				return response;
			} catch (JSONException e) {                
				e.printStackTrace();
				return null;
			}
		}
		//06-11 22:08:17.219: D/JsonParser(5138): sb = ï»¿ 	{"status":"success","message":"email sent successfully to activate your account"}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			Log.e("result", result.getjObj().toString());
			setUsercredential(result.getjObj().toString());
		}
	}
	//06-08 22:52:41.306: E/result(30555): {"status":"failure","description":"خطأ في معلومات تسجيل الدخول. حاول مرة اخرى"}


	public void setUsercredential(String result){

		usercred=new UserCred();
		try {
			JSONObject  job=new JSONObject (result);
			String descrip=job.getString("description");
			String status=job.getString("status");
			if(status.equals("success")){
				usercred=usercred.parseUserCred(job);
				usercred.checknull();
				usercred.setPassword(password);
				appInstance.setUserCred(usercred);
				appInstance.setRememberMe(true);
				Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				this.getParent().finish();
			}
			else{
				if(descrip.equals("Inactive email")){
					 resenlinktomail();
				}
				else{
					Toast.makeText(LoginActivity.this,descrip, Toast.LENGTH_SHORT).show();

				}
			}


		} catch (Exception e) {
		}
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if(stateofbackpress==0){
			
				Intent  intent=new Intent(LoginActivity.this, Splash2Activity.class);
				startActivity(intent);
				finish();
			
		}
		else{
			initview();
		}

	}
	public void resenlinktomail() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				LoginActivity.this);
		alertDialogBuilder.setTitle(getResources().getString(R.string.app_name_arabic));
		alertDialogBuilder
		.setMessage(getResources().getString(R.string.txt_resend_aciviation))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.txt_yes),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				ResendActivation();
			}
		})
		.setNegativeButton(getResources().getString(R.string.txt_no),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	public void ResendActivation(){
		 if(Constants.isOnline(LoginActivity.this)){
			
				pd=ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.app_name_arabic),
						getResources().getString(R.string.txt_loading), true);
				new  AsyncTaskCallResend().execute();
		}

		else{
			Toast.makeText(LoginActivity.this, getResources().getString(R.string.Toast_check_internet), 10000).show();
				 
		}
	}
	
	private class AsyncTaskCallResend extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				byte[] ba = username.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("username", username);
				ba=password.getBytes();
				base64Str=Base64.encodeBytes(ba);
				loginObj.put("password", base64Str);
				String loginData = loginObj.toString();
				String url = Constants.baseurl+"account/resendactivation";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
				return response;
			} catch (JSONException e) {                
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			Log.e("result", result.getjObj().toString());
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			try {
				JSONObject  job=result.getjObj();
				String descrip=job.getString("message");
				String status=job.getString("status");
				if(status.equals("success")){
					
					Toast.makeText(LoginActivity.this,getResources().getString(R.string.txt_mszsendto_email), Toast.LENGTH_SHORT).show();
				}
				else{
			
						Toast.makeText(LoginActivity.this,descrip, Toast.LENGTH_SHORT).show();

				}


			} catch (Exception e) {
			}
			super.onPostExecute(result);
			
			
		}
	}


}