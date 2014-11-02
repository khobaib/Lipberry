package com.lipberry;

import org.json.JSONException;
import org.json.JSONObject;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


public class AsynctaskSendregIdToServer extends AsyncTask<Void, Void, String> {

	Activity activity;
	JsonParser jsonParser;
	String token;
	String profile_info;
	String regid;
	private ProgressDialog pDialog;
	public AsynctaskSendregIdToServer(String regid,String token) {
		this.regid=regid;
		jsonParser = new JsonParser();
		this.token=token;
	}

	@Override
	protected String doInBackground(Void... params) {

		String res;
		res = "dfgfrg";
		try {
			String url = Constants.baseurl + "GCM/register/";//+regid;
			JSONObject loginObj = new JSONObject();
			loginObj.put("session_id",Utility.token);
			loginObj.put("regId",regid);
			loginObj.put("deviceId", Utility.DEVICE_ID);
			String  loginData = loginObj.toString();
			ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url,
					null, loginData, token);
			res=response.getjObj().toString();
			if(response.getStatus() == 200){
				Log.d(">>>><<<<", "success in retrieving response in login");
				JSONObject responseObj = response.getjObj();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
}
