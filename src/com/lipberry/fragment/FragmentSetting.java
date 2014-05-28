
package com.lipberry.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.lipberry.HomeActivity;
import com.lipberry.LoginActivity;
import com.lipberry.R;
import com.lipberry.Splash2Activity;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
@SuppressLint("NewApi")
public class FragmentSetting extends Fragment {
	MenuTabFragment parent;
	Button btn_body_details,btn_general_settings,btn_signout;
	String[]menuarray;
	ListView list_menu_item;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	ProgressDialog pd;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appInstance = (LipberryApplication)getActivity().getApplication();
		jsonParser=new JsonParser();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_setting,
				container, false);
		btn_signout=(Button) v.findViewById(R.id.btn_signout);
		btn_body_details=(Button) v.findViewById(R.id.btn_body_details);
		btn_general_settings=(Button) v.findViewById(R.id.btn_general_settings);
		btn_signout.setTypeface(Utility.getTypeface1(getActivity()));
		btn_body_details.setTypeface(Utility.getTypeface1(getActivity()));
		btn_general_settings.setTypeface(Utility.getTypeface1(getActivity()));
		btn_signout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(Constants.isOnline(getActivity())){
					pd=ProgressDialog.show(getActivity(), getResources().getString(R.string.app_name_arabic),
							getResources().getString(R.string.txt_signout), true);
					new AsyncTaskSaveMessageSetting().execute();
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
				
			/* */
			}
		});
		btn_general_settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.startFragmentProfileSetting();
			}
		});
		btn_body_details.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				parent.startFragmentMessageSetting();
			}
		});
		if(	Constants.MESSAGESETTINGSTATE){
			parent.startFragmentMessageSetting();
		}
		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.txt_setting));
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}
	
	private class AsyncTaskSaveMessageSetting extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/logout/";
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
			Log.e("res", result.getjObj().toString());
			if(pd.isShowing()&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject job=result.getjObj();

			try {
				String status=job.getString("status");
				appInstance.setRememberMe(false);
				Intent intent=new Intent(getActivity(), Splash2Activity.class);
				intent.putExtra("fromhome", true);
				getActivity().finish();
				Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_logout_success),
						Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

