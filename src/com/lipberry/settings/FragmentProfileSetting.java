
package com.lipberry.settings;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.SignupActivity;
import com.lipberry.adapter.NothingSelectedSpinnerAdapter;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.model.City;
import com.lipberry.model.Country;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.SingleMember;
import com.lipberry.model.UserCred;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
@SuppressLint("NewApi")
public class FragmentProfileSetting extends Fragment {
	public MenuTabFragment parent;
	String[]menuarray;
	ListView list_menu_item;
	Spinner  s_city,s_country;
	int selectedcityposition=-1;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	TextView t_city;
	ImageLoader imageLoader;
	ImageView img_profile;
	int selectedcountryposition=-1;
	ProgressDialog pd;
	Button bt_update_profile;
	ArrayList<Country>countrylist;
	ArrayList<String>allcountryname;
	ArrayList<City>citylist;
	ArrayList<String>allcityname;
	int state=0;
	DisplayImageOptions defaultOptions;
	ArrayAdapter<String> adapter ;
	EditText e_nickname,et_new_pass,et_email,et_site_url,et_brief;
	Button btn_change_photo;
	SingleMember singleMember;
	String  nickname,email,country_id="",city_id="",brief,password,siteurl;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appInstance = (LipberryApplication) getActivity().getApplication();
		jsonParser=new JsonParser();
		 defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(false).cacheOnDisc(false).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext()).defaultDisplayImageOptions(
						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);
		countrylist=new ArrayList<Country>();
		allcountryname=new ArrayList<String>();
		citylist=new ArrayList<City>();
		allcityname=new ArrayList<String>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_profile_setting,
				container, false);
		img_profile=(ImageView) v.findViewById(R.id.img_profile);
		btn_change_photo=(Button) v.findViewById(R.id.btn_change_photo);
		s_city=(Spinner) v.findViewById(R.id.s_city);
		s_country=(Spinner) v.findViewById(R.id.s_country);
		t_city=(TextView) v.findViewById(R.id.t_city);
		bt_update_profile=(Button) v.findViewById(R.id.bt_update_profile);
		e_nickname=(EditText) v.findViewById(R.id.e_nickname);
		et_new_pass=(EditText) v.findViewById(R.id.et_new_pass);
		et_email=(EditText) v.findViewById(R.id.et_email);
		et_site_url=(EditText) v.findViewById(R.id.et_site_url);
		et_brief=(EditText) v.findViewById(R.id.et_brief);
		
		t_city.setTypeface(Utility.getTypeface2(getActivity()));
		e_nickname.setTypeface(Utility.getTypeface2(getActivity()));
		et_new_pass.setTypeface(Utility.getTypeface2(getActivity()));
		et_email.setTypeface(Utility.getTypeface2(getActivity()));
		et_site_url.setTypeface(Utility.getTypeface2(getActivity()));
		et_brief.setTypeface(Utility.getTypeface2(getActivity()));
		
		btn_change_photo.setTypeface(Utility.getTypeface1(getActivity()));
		bt_update_profile.setTypeface(Utility.getTypeface1(getActivity()));
		
		img_profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.startFragmentImageSetting(singleMember);
			}
		});
		btn_change_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.startFragmentImageSetting(singleMember);
			}
		});
		bt_update_profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//String  nickname,email,country_id,city_id,brief,password;
				nickname=e_nickname.getText().toString();
				password=et_new_pass.getText().toString();
				brief=et_brief.getText().toString();
				email=et_email.getText().toString();
				siteurl=et_site_url.getText().toString();
				if((!nickname.equals(""))||(!password.equals(""))||(!brief.equals(""))||(!email.equals(""))||(!siteurl.equals(""))||
						(selectedcityposition!=-1)||(selectedcountryposition!=-1)){
					if(!email.equals("")){
						if(Constants.isValidEmail(email)){
							//AsyncTasksetUpdateProfile
							//	AsyncTasksetUpdateProfile 
							pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
									getActivity().getResources().getString(R.string.txt_please_wait), false);
							new AsyncTasksetUpdateProfile().execute();
						}
						else{
							Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_please_enter_email), Toast.LENGTH_SHORT).show();
						}
					}
					else{
						pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
								getActivity().getResources().getString(R.string.txt_please_wait), false);
						new AsyncTasksetUpdateProfile().execute();
					}

				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_please_required_field), Toast.LENGTH_SHORT).show();
				}
			}
		});
		if(Constants.isOnline(getActivity())){

			pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), false);
			new AsyncTaskGetCountry().execute();
		}
		else{
			Toast.makeText(getActivity(), getResources().
					getString(R.string.Toast_check_internet), 10000).show();
		}
		//imageLoader.displayImage(appInstance.getUserCred().get, imageView)
		t_city.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Constants.isOnline(getActivity())){
					if(selectedcountryposition==-1){
						Toast.makeText(getActivity(),  getActivity().getResources().getString(R.string.txt_please_select_country_first),
								10000).show();

					}else{
						pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
								getActivity().getResources().getString(R.string.txt_retreiving_country), false);
						new AsyncTaskGetCity().execute();
					}
				}
				else{
					Toast.makeText(getActivity(), getResources().
							getString(R.string.Toast_check_internet), 10000).show();
				}
			}
		});
		return v;
	}


	private class AsyncTasksetUpdateProfile extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				if(selectedcountryposition!=-1){
					loginObj.put("country_id",countrylist.get(selectedcountryposition));
				}
				if(selectedcityposition!=-1){
					loginObj.put("city_id",citylist.get(selectedcityposition));
				}
				if(selectedcityposition!=-1){
					loginObj.put("city_id",citylist.get(selectedcityposition));
				}
				if(!password.equals("")){
					loginObj.put("password",password);
				}
				if(!siteurl.equals("")){
					loginObj.put("siteurl", siteurl);
				}
				if(!nickname.equals("")){
					loginObj.put("nickname", nickname);
				}
				if(!brief.equals("")){
					loginObj.put("brief", brief);
				}
				if(Constants.isValidEmail(email)){
					loginObj.put("email", email);
				}
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"settings/profilesettings/";
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
				if(status.equals("success")){
					setusercred();
				}
				else{
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void setusercred(){
		UserCred ucred=appInstance.getUserCred();
		if(!password.equals("")){
			ucred.setPassword(password);
		}

		if(!email.equals("")){
			ucred.setEmail(email);
		}
		if(!brief.equals("")){
			ucred.setbBrief(brief);
		}
		if(!city_id.equals("")){
			ucred.setCity(city_id);
		}
		if(!country_id.equals("")){
			Log.e("country_id", country_id);
			ucred.setCountry(country_id);
		}
		if(!brief.equals("")){
			ucred.setbBrief(brief);
		}
		if(!siteurl.equals("")){
			ucred.setSiteUrl(brief);
		}
		appInstance.setUserCred(ucred);

	}
	private class AsyncTaskGetCity extends AsyncTask<Void, Void, ServerResponse> {

		@Override
		protected ServerResponse doInBackground(Void... params) {
			String url =Constants.baseurl+"ajax/city.php?country_id="+countrylist.get(selectedcountryposition).getId();
			ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, url, null,
					null, null);
			return response;
		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			Log.d("serverreponse", result.getjObj().toString());
			if(pd!=null){
				if(pd.isShowing()){
					pd.dismiss();
				}
				
			}
			
			try {
				String city=result.getjObj().getString("city_list");
				loadcitylist(city);

			} catch (JSONException e) {
				Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_not_available), 10000).show();
			}  
		}
	}
	public void  loadcitylist(String result){

		JSONObject json_data;
		try {
			JSONArray jsonArray = new JSONArray(result);
			citylist.clear();
			allcityname.clear();
			for (int i = 0; i < jsonArray.length(); i++) {
				json_data = jsonArray.getJSONObject(i);
				String id = json_data.getString("city_id");
				String name = json_data.getString("city_name");
				City city=new City(id, name);
				citylist.add(city);
				allcityname.add(name);

			}
			if(allcityname.size()>0){
				t_city.setVisibility(View.GONE);
				s_city.setVisibility(View.VISIBLE);

				setcity();
			}
			else{
				Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_no_city_found), 10000).show();
				t_city.setVisibility(View.VISIBLE);
				s_city.setVisibility(View.GONE);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
			

	}
	private void setcity(){
		

		ArrayAdapter  adapter2 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, allcityname);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		s_city.setAdapter( new NothingSelectedSpinnerAdapter(
//				adapter2, R.drawable.contact_spinner_row_nothing_selected_city,getActivity()));
		s_city.setAdapter(adapter2);
		s_city.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, 
					long arg3){
				selectedcityposition=position-1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				selectedcityposition=-1;
			}
		});
		if(state==0){
			int inexforcity=10;
			for (int i=0;i<citylist.size();i++){
				if(citylist.get(i).getId().equals(appInstance.getUserCred().getCity())){
					inexforcity=i;
				}
			}
			s_city.setSelection(20);
			selectedcityposition=inexforcity;
		}
	
	}

	private class AsyncTaskGetCountry extends AsyncTask<Void, Void, ServerResponse> {

		@Override
		protected ServerResponse doInBackground(Void... params) {
			String url =Constants.baseurl+"ajax/country.php?";
			ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, url, null,
					null, null);
			return response;
		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
					try {
						s_country.setVisibility(View.VISIBLE);
						String country=result.getjObj().getString("country_list");
						loadcountrylist(country);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				new AsyncTaskGetSinleMember().execute();
		}
	}

	public void loadcountrylist(String result){

		JSONObject json_data;
		try {
			JSONArray jsonArray = new JSONArray(result);
			countrylist.clear();
			allcountryname.clear();
			for (int i = 0; i < jsonArray.length(); i++) {
				json_data = jsonArray.getJSONObject(i);
				String id = json_data.getString("id");
				String name = json_data.getString("name");
				Country contr=new Country(id, name);
				countrylist.add(contr);
				allcountryname.add(name);
				
			}
			setcountry();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.txt_my_page));
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
		if(	Constants.MESSAGESETTINGSTATE){
			parent.startFragmentSetting();
		}
	}

	private void setcountry(){

		Log.e("tag", "1");
		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_item, allcountryname);
		Log.e("tag", "2");

		int position=0;
		Log.e("tag", "3");

		for (int i=0;i<countrylist.size();i++){
			if(countrylist.get(i).getId().equals(appInstance.getUserCred().getCountry())){
				position=i;
			}
		}
		Log.e("tag", "4");


		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Log.e("tag", "5");

		//s_country.setAdapter(adapter);
	s_country.setAdapter(
			
				new NothingSelectedSpinnerAdapter(
						adapter,
						R.drawable.contact_spinner_row_nothing_selected_country,
					getActivity()));
		Log.e("tag", "6");

		s_country.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, 
					long arg3){
				
				selectedcountryposition=position-1;
				t_city.setVisibility(View.VISIBLE);
				s_city.setVisibility(View.GONE);
				state=1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				selectedcountryposition=-1;
			}
		});
		s_country.setSelection(position);
		selectedcountryposition=position;
		t_city.setVisibility(View.VISIBLE);
		s_city.setVisibility(View.GONE);
		new AsyncTaskGetCity().execute();
		}

	private class AsyncTaskGetSinleMember extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			String url =Constants.baseurl+"account/findmemberbyid/"+appInstance.getUserCred().getId()+"/";
			ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, url, null,
					null, null);
			return response;
		}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if(pd!=null){
				if(pd.isShowing()){
					pd.dismiss();
				}
			}
			Log.e("responses", result.getjObj().toString());
			setMemberObject(result.getjObj().toString());
		}
	}
	public void setMemberObject(String  respnse){
		try {
			Log.i("serverreponse", respnse);
			JSONObject jobj=new JSONObject(respnse);
			String  status=jobj.getString("status");
			if(status.equals("success")){
				singleMember  =SingleMember.parseSingleMember(jobj);
				ImageLoader.getInstance().getMemoryCache().clear();
				ImageLoader.getInstance().getDiscCache().clear();
				ImageLoader.getInstance().displayImage(singleMember.getAvatar(), img_profile);
				e_nickname.setText(singleMember.getNickname());
				et_email.setText(singleMember.getEmail());
				et_site_url.setText(singleMember.getSiteurl());
				et_brief.setText(singleMember.getBrief());
				
			}
			else{
//				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_member_found),
//						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
//			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_member_found),
//					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}

