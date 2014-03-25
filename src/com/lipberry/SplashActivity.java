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
     
      if( appInstance.getUserCred().getUsername().equalsIgnoreCase("")){
    	  initview();
    	     
      }
      else{
    	  initview();
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

    
    
    
    
    public void initview(){
    	stateofbackpress=0;
    	 setContentView(R.layout.login);
    	 if(appInstance.getUserCred().getUsername().equals("")){
    		 
    	 }
    	 
    	 
    	 
    	 

         e_pass=(EditText) findViewById(R.id.e_pass);
         e_uname=(EditText) findViewById(R.id.e_uname);
         b_signin=(Button) findViewById(R.id.b_signin);
         b_signup=(Button) findViewById(R.id.b_signup);
         bt_forgotpass=(Button) findViewById(R.id.bt_forgotpass);
       
         bt_forgotpass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				forgotpass();
			}
		});
         b_signin.setOnClickListener(new OnClickListener() {

             @Override
             public void onClick(View arg0) {
            		username=e_uname.getText().toString();
            		password=e_pass.getText().toString(); 
            	 signin();
            	}
         });
         b_signup.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				
 				Intent intent=new Intent(SplashActivity.this, SignupActivity.class);
 				startActivity(intent);		
 				
 			}
 		});

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
	    			Toast.makeText(SplashActivity.this, " Username  must  start with later and continue with only later,number and dashes ",
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
    
    
    public void forgotpass(){
    	stateofbackpress=1;
    	setContentView(R.layout.forgotpass);
    	//et_email,bt_enter,email
    	bt_enter=(Button) findViewById(R.id.bt_enter);
    	et_email=(EditText) findViewById(R.id.et_email);
    	
    	bt_enter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				email=et_email.getText().toString();
				
				if(Constants.isOnline(SplashActivity.this)){

					if  (!Constants.isValidEmail(email)) {
						Toast.makeText(SplashActivity.this, "Please enter  email",
								10000).show();
			    	}
					else{
						pd=ProgressDialog.show(SplashActivity.this, "Lipberry",
							    "Loading", true);
						new  AsyncTaskForgotPass().execute();
					}
				 }
       		 
       		 else{
       			 	Toast.makeText(SplashActivity.this, getResources().getString(R.string.Toast_check_internet), 10000).show();
       				Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
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

                //            String content = URLEncodedUtils.format(nameValuePairs, "utf-8");



                String url = "http://lipberry.com/API/account/forgetpassword";
                ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
                        loginData, null);

                Log.d("rtes", response.getjObj().toString());
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
				Toast.makeText(SplashActivity.this, msz, 10000).show();
				
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
                loginObj.put("username", username);
                loginObj.put("password", password);
                String loginData = loginObj.toString();

                //            String content = URLEncodedUtils.format(nameValuePairs, "utf-8");



                String url = "http://lipberry.com/API/account/login";
                ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
                        loginData, null);

                Log.d("rtes", response.getjObj().toString());
                
                
                
                //03-17 13:22:19.255: D/JsonParser(28968): sb = {"status":"success","id":"9683","name":"mahbub","username":"mahbub","nickname":"sumon","email":"mahbubsumon085@gmail.com","country":"3","city":"72","administrator":null,"brief":null,"siteurl":null,"twitter":null,"youtube":null,"instagram":null,"telephone":null,"countrycity_flag":null,"is_authorized":"0","session_id":"0e45c41drj9peoop521pmbugv3","description":"login success"}
               // 03-17 13:22:19.255: D/rtes(28968): {"session_id":"0e45c41drj9peoop521pmbugv3","instagram":null,"nickname":"sumon","status":"success","countrycity_flag":null,"is_authorized":"0","siteurl":null,"city":"72","country":"3","id":"9683","twitter":null,"username":"mahbub","email":"mahbubsumon085@gmail.com","description":"login success","administrator":null,"name":"mahbub","youtube":null,"telephone":null,"brief":null}
              

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
			String descrip=job.getString("description");
			String status=job.getString("status");
			if(status.equals("success")){
				//UserCred parseUserCred
				usercred=usercred.parseUserCred(job);
				usercred.checknull();
				usercred.setPassword(password);
				//Toast.makeText(SplashActivity.this,"pass "+usercred.getPassword(), 10000).show();
				appInstance.setUserCred(usercred);
				
				
				Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
				startActivity(intent);
				
				
				//instagram,countrycity_flag,siteurl,twitter,administrator,youtube,telephone,null
			}
			else{
				Toast.makeText(SplashActivity.this,descrip, 10000).show();
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	
    	if(stateofbackpress==0){
    		super.onBackPressed();
    	}
    	else{
    		initview();
    	}
    	
    }

}
