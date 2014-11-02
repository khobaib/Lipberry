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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

public class ResendActiviationEmail extends Activity {
	int stateofbackpress=0;
	UserCred usercred;
	EditText et_uname,et_pass;
	Button bt_enter;
	String username,password;
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
		setContentView(R.layout.resend);
		et_uname=(EditText) findViewById(R.id.et_uname);
		et_pass=(EditText) findViewById(R.id.et_pass);
		bt_enter=(Button) findViewById(R.id.bt_enter);
		et_uname.addTextChangedListener(new TextWatcher() {          
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
            	et_uname.setBackgroundResource(R.drawable.rounded_edittext);

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
		et_pass.addTextChangedListener(new TextWatcher() {          
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
            	et_pass.setBackgroundResource(R.drawable.rounded_edittext);

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
				// TODO Auto-generated method stub
				username=et_uname.getText().toString();
				password=et_pass.getText().toString();
				if(username.trim().equals("")){
					et_uname.setBackgroundResource(R.drawable.rounded_text_nofield);

				}
				else if(password.trim().equals("")){
	            	et_pass.setBackgroundResource(R.drawable.rounded_text_nofield);

				}
				else if(Constants.isOnline(ResendActiviationEmail.this)){
					
						pd=ProgressDialog.show(ResendActiviationEmail.this, getResources().getString(R.string.app_name_arabic),
								getResources().getString(R.string.txt_loading), true);
						new  AsyncTaskCallResend().execute();
				}

				else{
					Toast.makeText(ResendActiviationEmail.this, getResources().getString(R.string.Toast_check_internet), 10000).show();
						 
				}
			}
		});

	
	}
	
	
	private class AsyncTaskCallResend extends AsyncTask<Void, Void, ServerResponse> {
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
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			try {
				JSONObject  job=result.getjObj();
				String descrip=job.getString("message");
				String status=job.getString("status");
				if(status.equals("success")){
					Intent intent=new Intent(ResendActiviationEmail.this, Splash2Activity.class);
					startActivity(intent);
					finish();
					Toast.makeText(ResendActiviationEmail.this,getResources().getString(R.string.txt_mszsendto_email), Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(ResendActiviationEmail.this,descrip, Toast.LENGTH_SHORT).show();
				}


			} catch (Exception e) {
			}
			super.onPostExecute(result);
			
			
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

}
