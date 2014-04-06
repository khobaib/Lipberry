package com.lipberry.asyntask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lipberry.SplashActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;




public class AsynctaskLogin extends AsyncTask<Void, Void, String> {
	Activity activity;
	SplashActivity  lisenar;
    String username,password;
    public AsynctaskLogin(Activity activity,SplashActivity  lisenar, String  username, String password) {
		this.activity = activity;
		this.lisenar=lisenar;
		this.username=username;
		this.password=password;
	}

	@Override
	protected String doInBackground(Void... params) {
		return"";
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
	}
}
