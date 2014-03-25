package com.lipberry.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lipberry.R;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ProfileEssentialsfragment extends ListFragment {

	LipberryApplication appInstance;	
	 ProgressDialog pd;
	 TextView textView1;
	 JsonParser jsonParser;

	 
	 
	 
    public ProfileEssentialsfragment() {
        // TODO Auto-generated constructor stub
    }

    public static ProfileEssentialsfragment newInstance(){
        ProfileEssentialsfragment fragment = new ProfileEssentialsfragment();
        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
 Log.i("onActivityCreated", "onActivityCreated");
        jsonParser=new JsonParser();
       
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
				    "Retreving Post", true);
			new AsyncTaskLoadPostFrommyFollowing().execute();
			
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet), 10000).show();
		}
    }



			private class AsyncTaskLoadPostFrommyFollowing extends AsyncTask<Void, Void, ServerResponse> {
					@Override
						protected ServerResponse doInBackground(Void... params) {

							try {
								   appInstance = (LipberryApplication) getActivity().getApplication();
									  
									JSONObject loginObj = new JSONObject();
									loginObj.put("session_id", appInstance.getUserCred().getSession_id());
									loginObj.put("startIndex", "0");
									loginObj.put("endIndex", "3");
									String loginData = loginObj.toString();
									String url =Constants.baseurl+"home/latestposts/";
									//Log.d("", msg)
				                  	ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
											loginData, null);

									Log.d("rtes", response.getjObj().toString());
									//textView1.setText(response.getjObj().toString());
	                
									
									
							//		
	                
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
	          //  setUsercredential(result.getjObj().toString());
	        }
	    }

   

}
