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
		
	
		Log.e("reg id", "reg "+regid);
		Log.e("reg id", "tok "+this.token);
		this.regid=regid;
		jsonParser = new JsonParser();
		this.token=token;
//		pDialog = new ProgressDialog(activity);
//		  pDialog.setMessage("Registration...");
//          pDialog.setIndeterminate(true);
//          pDialog.setCancelable(true);
//          pDialog.show();
		
	}

	@Override
	protected String doInBackground(Void... params) {
		
			String res;
			res = "dfgfrg";
			try {
				
				Log.d("test", "Start");
				String url = Constants.baseurl + "GCM/register/";//+regid;

      
				   JSONObject loginObj = new JSONObject();
				
				   
				   loginObj.put("session_id",Utility.token);
				   loginObj.put("regId",regid);
				   loginObj.put("deviceId", Utility.SENDER_ID);
				   String  loginData = loginObj.toString();
				   ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url,
				           null, loginData, token);
				   res=response.getjObj().toString();
				  
				   
				//   int reqType, String url, List<NameValuePair> urlParams, String content, String appToken
				   
				   if(response.getStatus() == 200){
				       Log.d(">>>><<<<", "success in retrieving response in login");
				       JSONObject responseObj = response.getjObj();
				     Log.d("resut", responseObj.toString());
				   }
				 
				   Log.d("resut", response.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return res;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
//		if((pDialog!=null)&&(pDialog.isShowing())){
//			pDialog.cancel();
//		}
//		

	}
	
	
	
}
