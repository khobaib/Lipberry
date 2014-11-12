package com.lipberry;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.adapter.NothingSelectedSpinnerAdapter;
import com.lipberry.model.City;
import com.lipberry.model.Country;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.Utility;

public class SignupActivity extends Activity {
	Spinner  s_city,s_country,s_kowaboutus;//
	ArrayList<City>citylist;
	ArrayList<String>allcityname;
	int selectedcountryposition=-1;
	int selectedknowposition=-1;
	int selectedcityposition=-1;
	ArrayList<Country>countrylist;
	ArrayList<String>allcountryname;
	boolean usernamecheck=false;
	EditText e_username,e_email,e_name,e_nickname,e_password,e_confirmpass,
	e_activiate_email,e_activiate_key;
	String username,email,password,confirmpass,name,nickname,acivitedemail,
	activiatedkey;
	ProgressDialog pd;
	ArrayAdapter<String> adapter ;
	ArrayList<String>knowList;
	TextView t_country,t_city,t_kowaboutus;
	Button b_register,bt_activiate_user;
	int stateofbackpressed=0;
	JsonParser jsonParser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		jsonParser=new JsonParser();
		countrylist=new ArrayList<Country>();
		allcountryname=new ArrayList<String>();
		citylist=new ArrayList<City>();
		allcityname=new ArrayList<String>();
		knowList=new ArrayList<String>();
		initview();
	}

	public void initview(){
		stateofbackpressed=0;
		setContentView(R.layout.signup);
		t_country=(TextView) findViewById(R.id.t_country);
		t_kowaboutus=(TextView) findViewById(R.id.t_kowaboutus);
		s_country=(Spinner) findViewById(R.id.s_country);
		s_city	=(Spinner) findViewById(R.id.s_city);
		s_kowaboutus=(Spinner) findViewById(R.id.s_kowaboutus);
		b_register=(Button) findViewById(R.id.b_register);
		e_username=(EditText) findViewById(R.id.e_username);
		e_email=(EditText) findViewById(R.id.e_email);
		e_name=(EditText) findViewById(R.id.e_name);
		e_nickname=(EditText) findViewById(R.id.e_nickname);
		e_password=(EditText) findViewById(R.id.e_password);
		e_confirmpass=(EditText) findViewById(R.id.e_confirmpass);
		t_city=(TextView) findViewById(R.id.t_city);
		b_register.setTypeface(Utility.getTypeface1(SignupActivity.this));
		if(Constants.isOnline(SignupActivity.this)){

			pd=ProgressDialog.show(SignupActivity.this,  getResources().getString(R.string.app_name_arabic),
					getResources().getString(R.string.txt_please_wait), false);
			new AsyncTaskGetCountry().execute();
			new AsyncTaskGetknowingReason().execute();

		}
		else{
			Toast.makeText(SignupActivity.this, getResources().
					getString(R.string.Toast_check_internet), 10000).show();
		}


		t_kowaboutus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(Constants.isOnline(SignupActivity.this)){
					pd=ProgressDialog.show(SignupActivity.this, getResources().getString(R.string.app_name_arabic),
							getResources().getString(R.string.txt_please_wait), false);
					new AsyncTaskGetknowingReason().execute();

				}
				else{
					Toast.makeText(SignupActivity.this, getResources().
							getString(R.string.Toast_check_internet), 10000).show();
				}
			}
		});

		t_city.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Constants.isOnline(SignupActivity.this)){
					if(selectedcountryposition==-1){
						Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_please_select_country_first),
								10000).show();

					}else{
						pd=ProgressDialog.show(SignupActivity.this,  getResources().getString(R.string.app_name_arabic),
								getResources().getString(R.string.txt_retreiving_country), true);
						new AsyncTaskGetCity().execute();
					}
				}
				else{
					Toast.makeText(SignupActivity.this, getResources().
							getString(R.string.Toast_check_internet), 10000).show();
				}
			}
		});

		t_country.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Constants.isOnline(SignupActivity.this)){
					t_country.setVisibility(View.GONE);
					s_country.setVisibility(View.VISIBLE);
					pd=ProgressDialog.show(SignupActivity.this, getResources().getString(R.string.app_name_arabic),
							getResources().getString(R.string.txt_please_wait), true);
					new AsyncTaskGetCountry().execute();

				}
				else{
					Toast.makeText(SignupActivity.this, getResources().
							getString(R.string.Toast_check_internet), 10000).show();
				}
			}
		});
		b_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Constants.isOnline(SignupActivity.this)){
					signup();
				}
				else{
					Toast.makeText(SignupActivity.this, getResources().
							getString(R.string.Toast_check_internet), 10000).show();
				}
			}
		});
		configEdittxt();
	}

	public void configEdittxt(){
		e_password.addTextChangedListener(new TextWatcher() {          
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
				e_password.setBackgroundResource(R.drawable.rounded_edittext);

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
		e_email.addTextChangedListener(new TextWatcher() {          
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
				e_email.setBackgroundResource(R.drawable.rounded_edittext);

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
		e_username.addTextChangedListener(new TextWatcher() {          
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
				e_username.setBackgroundResource(R.drawable.rounded_edittext);

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
		e_name.addTextChangedListener(new TextWatcher() {          
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
				e_name.setBackgroundResource(R.drawable.rounded_edittext);

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
		e_nickname.addTextChangedListener(new TextWatcher() {          
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
				e_nickname.setBackgroundResource(R.drawable.rounded_edittext);

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
		e_confirmpass.addTextChangedListener(new TextWatcher() {          
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
				e_confirmpass.setBackgroundResource(R.drawable.rounded_edittext);

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

	private void signup(){
		username=e_username.getText().toString();
		email=e_email.getText().toString();
		password=e_password.getText().toString();
		confirmpass=e_confirmpass.getText().toString();
		name=e_name.getText().toString();
		nickname=e_nickname.getText().toString();
		Log.d("name","1"+ name);

		if  (!Constants.namecheck(e_username.getText().toString())) {

			if(e_username.length()<3){
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_uname_cant_lessthan),
						Toast.LENGTH_SHORT).show();
				e_username.setBackgroundResource(R.drawable.rounded_text_nofield);
			}

			else if(e_username.length()>10){
				e_username.setBackgroundResource(R.drawable.rounded_text_nofield);
				Toast.makeText(SignupActivity.this,  getResources().getString(R.string.txt_uname_cant_more),
						Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_uname_spec),
						Toast.LENGTH_LONG).show();
			}

		}
		else if  (name.trim().equals("")) {
			e_name.setBackgroundResource(R.drawable.rounded_text_nofield);

			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_enter_name),Toast.LENGTH_SHORT).show();
		}
		else if  (nickname.trim().equals("")) {
			e_nickname.setBackgroundResource(R.drawable.rounded_text_nofield);
			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_enter_nicknamename),Toast.LENGTH_SHORT).show();
		}

		else if  ((nickname.length()<3)||(nickname.length()>10)) {
			if(nickname.length()<3){
				e_nickname.setBackgroundResource(R.drawable.rounded_text_nofield);

				Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_condition_nickname),
						Toast.LENGTH_SHORT).show();
			}

			else if(nickname.length()>10){
				e_nickname.setBackgroundResource(R.drawable.rounded_text_nofield);

				Toast.makeText(SignupActivity.this,  getResources().getString(R.string.txt_condition_nicknamemore),
						Toast.LENGTH_SHORT).show();
			}
		}
		else if  (!Constants.isValidEmail(email)) {
			e_email.setBackgroundResource(R.drawable.rounded_text_nofield);

			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_enter_email),
					Toast.LENGTH_SHORT).show();
		}
		else if  (password.trim().equals("")) {
			e_password.setBackgroundResource(R.drawable.rounded_text_nofield);

			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_enter_password),
					Toast.LENGTH_SHORT).show();
		}
		else if  (password.length()<6) {
			e_password.setBackgroundResource(R.drawable.rounded_text_nofield);

			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_password_length),
					Toast.LENGTH_SHORT).show();
		}
		
		else if  (confirmpass.trim().equals("")) {
			e_confirmpass.setBackgroundResource(R.drawable.rounded_text_nofield);

			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_enter_confirm),
					Toast.LENGTH_SHORT).show();
		}
		else if  (selectedcountryposition==-1) {
			s_country.setBackgroundResource(R.drawable.rounded_text_nofield);

			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_select_country),
					Toast.LENGTH_SHORT).show();
		}
		else if  (selectedcityposition==-1) {
			s_city.setBackgroundResource(R.drawable.rounded_text_nofield);
			t_city.setBackgroundResource(R.drawable.rounded_text_nofield);

			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_select_city),
					Toast.LENGTH_SHORT).show();
		}
		else if  (selectedknowposition==-1) {
			s_kowaboutus.setBackgroundResource(R.drawable.rounded_text_nofield);

			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_select_know_us),
					Toast.LENGTH_SHORT).show();
		}

		else if (!(password.equalsIgnoreCase(confirmpass))) {
			e_password.setBackgroundResource(R.drawable.rounded_text_nofield);
			e_confirmpass.setBackgroundResource(R.drawable.rounded_text_nofield);

			Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_match_pass),
					Toast.LENGTH_SHORT).show();
		}

		else {


			pd=ProgressDialog.show(SignupActivity.this, getResources().getString(R.string.app_name),
					getResources().getString(R.string.txt_loading), true);
			new AsyncTaskSignUp().execute();
		}
	}
	private void setcountry(){

		adapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, allcountryname);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s_country.setAdapter(
				new NothingSelectedSpinnerAdapter(
						adapter,
						R.drawable.contact_spinner_row_nothing_selected_country,
						this));
		s_country.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, 
					long arg3){

				selectedcountryposition=position-1;
				t_city.setVisibility(View.VISIBLE);
				s_city.setVisibility(View.GONE);
				s_country.setBackgroundResource(R.drawable.rounded_edittext);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				selectedcountryposition=-1;
			}
		});

	}


	private void setcity(){

		ArrayAdapter<String>  adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, allcityname);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s_city.setAdapter( new NothingSelectedSpinnerAdapter(
				adapter2, R.drawable.contact_spinner_row_nothing_selected_city, this));
		s_city.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, 
					long arg3){
				selectedcityposition=position-1;
				s_city.setBackgroundResource(R.drawable.rounded_edittext);
				t_city.setBackgroundResource(R.drawable.rounded_edittext);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				selectedcityposition=-1;
			}
		}); 
	}


	public void setknowus(){

		ArrayAdapter<String>  adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, knowList);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		s_kowaboutus.setAdapter(
				new NothingSelectedSpinnerAdapter(adapter,
						R.drawable.contact_spinner_row_nothing_selected_knowaboutus, this));
		s_kowaboutus.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
					long arg3){
				selectedknowposition=position-1;
				s_kowaboutus.setBackgroundResource(R.drawable.rounded_edittext);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				selectedknowposition=-1;
			}
		});


	}
	private class AsyncTaskGetknowingReason extends AsyncTask<Void, Void, ServerResponse> {

		@Override
		protected ServerResponse doInBackground(Void... params) {
			String url =Constants.baseurl+"ajax/howyouknowaboutus.php?";
			ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, url, null,
					null, null);

			return response;

		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			Log.d("serverreponse", result.getjObj().toString());
			if((pd!=null)&&(pd.isShowing())){
				pd.dismiss();
			}
			try {
				String know=result.getjObj().getString("how you know about us _list");
				String[] ary = know.split(",");
				for(int i=0;i<ary.length;i++){
					String item=ary[i];
					if(i==0){
						item=item.substring(2, item.length()-1);
					}
					else{
						item=item.substring(1, item.length()-1);
					}
					item=item.replaceAll("\"", "");
					knowList.add(item);
				}
				if(knowList.size()>0){
					s_kowaboutus.setVisibility(View.VISIBLE);
					t_kowaboutus.setVisibility(View.GONE);
					setknowus();
				}
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
			}  
		}
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
			if((pd!=null)&&(pd.isShowing())){
				pd.dismiss();
			}
			try {
				String city=result.getjObj().getString("city_list");
				loadcitylist(city);

			} catch (JSONException e) {
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_not_available), Toast.LENGTH_SHORT).show();
			}  
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
			Log.d("serverreponse", result.getjObj().toString());

			try {
				t_country.setVisibility(View.GONE);
				s_country.setVisibility(View.VISIBLE);
				String country=result.getjObj().getString("country_list");
				loadcountrylist(country);

			} catch (JSONException e) {
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_not_available), Toast.LENGTH_SHORT).show();
			}  



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
				setcountry();
			}

		} catch (JSONException e) {
			e.printStackTrace();
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
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_not_available), Toast.LENGTH_SHORT).show();
				t_city.setVisibility(View.VISIBLE);
				s_city.setVisibility(View.GONE);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class AsyncTaskSignUp extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				byte[] ba = name.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("name",base64Str);
				//				ba = username.getBytes();
				//				base64Str = Base64.encodeBytes(ba);
				loginObj.put("username",username);
				ba = nickname.getBytes();
				base64Str = Base64.encodeBytes(ba);
				loginObj.put("nickname",base64Str);
				ba = password.getBytes();
				base64Str = Base64.encodeBytes(ba);
				//base64Str=Utility.getEncodedpassword(base64Str);
				loginObj.put("password",base64Str);
				loginObj.put("country_id", countrylist.get(selectedcountryposition).getId());
				loginObj.put("city_id", citylist.get(selectedcityposition).getId());
				loginObj.put("email", email);
				loginObj.put("how_to_know", knowList.get(selectedknowposition));
				String loginData = loginObj.toString();
				String url = "http://lipberry.com/API/account/register";
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
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			completesignup( result.getjObj().toString());
		}
	}

	public void completesignup(String result){

		try {
			JSONObject response=new JSONObject(result);
			String  status=response.getString("status");
			String msz=response.getString("message");
			if(status.equals("success")){
//				if(msz.equals("email sent successfully to activate your account")){
//					Toast.makeText(SignupActivity.this, getResources().getString(R.string.txt_emailis_sent_toactiviate), 
//							10000).show();
//				}
				new AlertDialog.Builder(this)
			    .setTitle(getResources().getString(R.string.app_name))
			    .setMessage(getResources().getString(R.string.txt_signup_success3))
			    .setPositiveButton(getResources().getString(R.string.txt_cancelll), new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	dialog.cancel();
			        	Intent intent=new Intent(SignupActivity.this, Splash2Activity.class);
						startActivity(intent);
						finish();
			        }
			     }).show();
				
				
				
			}
			else{
				Toast.makeText(SignupActivity.this, msz, 10000).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
