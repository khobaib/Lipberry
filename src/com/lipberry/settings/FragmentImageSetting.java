
package com.lipberry.settings;

import java.io.ByteArrayOutputStream;
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
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
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
import com.lipberry.fragment.FragmentMyCountriesPost;
import com.lipberry.fragment.FragmentMyFollwerPost;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.model.City;
import com.lipberry.model.Commentslist;
import com.lipberry.model.Country;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.SingleMember;
import com.lipberry.model.UserCred;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
@SuppressLint({ "NewApi", "ValidFragment" })
public class FragmentImageSetting extends Fragment {
	public MenuTabFragment parent;
	String[]menuarray;
	ListView list_menu_item;
	Spinner  s_city,s_country;
	int selectedcityposition=-1;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	TextView t_city;
	Bitmap bitmap;
	ImageLoader imageLoader;
	ImageView iv_profile_pic;
	Button b_take_pic,b_go_gallery,b_update;
	int selectedcountryposition=-1;
	ProgressDialog pd;
	public SingleMember singleMember;
	public FragmentImageSetting(SingleMember singleMember){
		this.singleMember=singleMember;
	}
	

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appInstance = (LipberryApplication) getActivity().getApplication();
		jsonParser=new JsonParser();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext()).defaultDisplayImageOptions(
						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.upload_pic,
				container, false);

		b_take_pic=(Button) v.findViewById(R.id.b_take_pic);
		b_go_gallery=(Button) v.findViewById(R.id.b_go_gallery);
		b_update=(Button) v.findViewById(R.id.b_update);
		iv_profile_pic=(ImageView) v.findViewById(R.id.iv_profile_pic);
		imageLoader.displayImage(singleMember.getAvatar(), iv_profile_pic);
		b_take_pic.setTypeface(Utility.getTypeface2(getActivity()));
		b_go_gallery.setTypeface(Utility.getTypeface2(getActivity()));
		b_update.setTypeface(Utility.getTypeface2(getActivity()));

		b_take_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.IMAGEPAGECALLED=true;
				((HomeActivity)getActivity()).opeencamera(FragmentImageSetting.this);
			}
		});

		b_go_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.IMAGEPAGECALLED=true;

				((HomeActivity)getActivity()).opengalary(FragmentImageSetting.this);
			}
		});
		b_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(bitmap!=null){
					if(Constants.isOnline(getActivity())){
						pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
								getActivity().getResources().getString(R.string.txt_please_wait), false);
						new AsyncTaskUploadImage().execute();
					}
					else{
						
					}

				}
				else{
					Toast.makeText(getActivity(),  getActivity().getResources().getString(R.string.txt_please_selct_img),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		return v;
	}

	public void onimageloadingSuccessfull(Bitmap bitmap){
		Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_you_have_selcect_image), Toast.LENGTH_SHORT).show();
		iv_profile_pic.setImageBitmap(bitmap);
		this.bitmap=bitmap;
		Log.e("size", ""+bitmap.getHeight()+"  "+bitmap.getWidth());
	}
	public void onimageloadingFailed(){
		Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_failed_to_select_an_image), Toast.LENGTH_SHORT).show();
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

	private class AsyncTaskUploadImage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());

				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG,100, bao);
				byte[] ba = bao.toByteArray();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("pic_data",  base64Str);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"settings/changemypicture/";
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
			JSONObject jobj=result.getjObj();
			if(pd!=null){
				if(pd.isShowing()){
					pd.cancel();
				}
			}
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				if(status.equals("success")){
					parent.onBackPressed();
					Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_upload_success), Toast.LENGTH_SHORT).show();

				}
				else{
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();

				}
				
			} catch (JSONException e) {
				
			}
		}
	}
}

