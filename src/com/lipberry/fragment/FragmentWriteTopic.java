
package com.lipberry.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.lipberry.adapter.NothingSelectedSpinnerAdapter;
import com.lipberry.model.Categories;
import com.lipberry.model.Commentslist;
import com.lipberry.model.ImageScale;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;

@SuppressLint("NewApi")
public class FragmentWriteTopic extends Fragment {
    EditText txt_topic,txt_text,txt_tag;
	Button btn_select_photo,btn_go;
	WriteTopicTabFragment parent;
	
	Spinner spinner_category;
	Button btn_add_more_photo;
	int selsectedspinnerposition=0;
	FragmentActivity activity;
	Bitmap scaledBmp;
	//04-07 21:13:52.159: E/write(13138): {"status":"success","description":"Success add article"}
	Bitmap bitmap;
	ProgressDialog pd;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	ArrayList<String>catnamelist;
	ArrayList< Categories>categorylist;
	ImageScale bitmapimage;
	public  String photofromcamera;
	public  String drectory;
	String title,category_id,category_prefix,body,photo,video;
	@SuppressLint("NewApi")
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=getActivity();
		jsonParser=new JsonParser();
		categorylist=new ArrayList<Categories>();
		catnamelist=new ArrayList<String>();
		appInstance = (LipberryApplication) getActivity().getApplication();
	}
	@Override
	public void onPause() {
		selsectedspinnerposition=0;
		super.onPause();
		
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_write_topic,
				container, false);
			txt_topic=(EditText) v.findViewById(R.id.txt_topic);
			txt_text=(EditText) v.findViewById(R.id.txt_text);
			btn_add_more_photo=(Button) v.findViewById(R.id.btn_add_more_photo);
			btn_add_more_photo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					loadphotoforgalary();
				}
			});
			txt_tag=(EditText) v.findViewById(R.id.txt_tag);
			btn_go=(Button) v.findViewById(R.id.btn_go);
			btn_go.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startwritetopic();
				}
			});
			btn_select_photo=(Button) v.findViewById(R.id.btn_select_photo);
			btn_select_photo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loadphoto();
				}
			});
			spinner_category=(Spinner) v.findViewById(R.id.spinner_category);
			((HomeActivity)getActivity()).welcome_title.setText(R.string.txt_write_topic);
			if(Constants.isOnline(getActivity())){
				pd=ProgressDialog.show(activity, "Lipberry",
						"Retreving categories", true);
				new AsyncTaskgetCategories().execute();
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
			
			
		return v;
	}
	
	public void loadphotoforgalary(){
		((HomeActivity)getActivity()).captureimage(this,true);
	}
	public void loadphoto(){
		((HomeActivity)getActivity()).captureimage(this,false);
	}
	public void imagecapturesccessfullforgalry(String imageuri){
		Toast.makeText(getActivity(),"You have selected an image for galary",
				Toast.LENGTH_SHORT).show();
		String filepath = imageuri;
		bitmapimage =new ImageScale();
		bitmap=bitmapimage.decodeImage(filepath);
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(activity,"Lipberry",
					"Uploading photo", true);
			new AsyncTaskAddGalaryImage().execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
	}
	public void imagecapturesccessfull(String imageuri){
		Toast.makeText(getActivity(),"You have selected an image",
				Toast.LENGTH_SHORT).show();
		String filepath = imageuri;
		bitmapimage =new ImageScale();
		bitmap=bitmapimage.decodeImage(filepath);
	}
	public void imagecapturefaillure(String imageuri){
		Toast.makeText(getActivity(),"Failed to select an image",
				Toast.LENGTH_SHORT).show();
		drectory=imageuri;
	}
	
	private class AsyncTaskgetCategories extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"category/categorylist";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
				return response;
			} catch (JSONException e) { 
			
				return null;
			}
		}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if(pd!=null){
				if((pd.isShowing())){
						pd.dismiss();
				}
			}
			JSONObject res=result.getjObj();
			try {
				String status=res.getString("status");
				if(status.equals("success")){
					JSONArray jarraArray=res.getJSONArray("categorylist");
					categorylist.clear();
					for(int i=0;i<jarraArray.length();i++){
						JSONObject job=jarraArray.getJSONObject(i);
						Categories cat=new Categories();
						cat=cat.parsecaCategories(job);
						categorylist.add(cat);
					}
					if(categorylist.size()>0){
						catnamelist.clear();
						for(int i=0;i<categorylist.size();i++){
							catnamelist.add(categorylist.get(i).getName());
						}
						generateSpinner();
					}
					else{
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_nocat_found),
								Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_nocat_found),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_nocat_found),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void generateSpinner() {
			ArrayAdapter< String>adapter = new ArrayAdapter<String>(activity,
					android.R.layout.simple_spinner_dropdown_item, catnamelist);
			spinner_category.setAdapter(adapter);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_category.setOnItemSelectedListener(new OnItemSelectedListener(){

				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, 
						long arg3){
					selsectedspinnerposition=position;
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					selsectedspinnerposition=0;
				}
			});
	  }
	  
	public void startwritetopic(){
		//String title,category_id,category_prefix,body,photo,video;
		title=txt_topic.getText().toString();
		body=txt_text.getText().toString();
		body=txt_text.getText().toString();
		if(title.trim().equals("")){
			Toast.makeText(getActivity(), "Please enter article title",Toast.LENGTH_SHORT).show();
		}
		else if(body.trim().equals("")){
			Toast.makeText(getActivity(), "Please enter article text",Toast.LENGTH_SHORT).show();
		}
		else if(selsectedspinnerposition==-1){
			Toast.makeText(getActivity(), "Please select category",Toast.LENGTH_SHORT).show();
		}
		
		else{
			
			if(Constants.isOnline(getActivity())){
				pd=ProgressDialog.show(activity,"Lipberry",
						"Writing topic", true);
				new AsyncTaskWriteTopic().execute();
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	private class AsyncTaskWriteTopic extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("title", title);
				loginObj.put("category_id", categorylist.get(selsectedspinnerposition).getId());
				loginObj.put("category_prefix", categorylist.get(selsectedspinnerposition).getPrefix());
				loginObj.put("body", body);
				if(bitmap!=null){
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG,60, bao);
					byte[] ba = bao.toByteArray();
					Log.e("BITMAP SIZE in asynctask", "bitmap size after compress = "
							+ ba.length);
					String base64Str = Base64.encodeBytes(ba);
					loginObj.put("photo",  base64Str);
				}
				else{
					Log.e("BITMAP SIZE in asynctask", "bitmap size after compress = "
							);
				}
				
				loginObj.put("video", "https://www.youtube.com/watch?v=u9L1QP6foCo");
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"article/addarticle/";
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
			if(pd!=null){
				if((pd.isShowing())){
						pd.dismiss();
				}
			}
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				bitmap=null;
				Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
//				if(status.equals("success")){
//				//	04-07 21:13:52.159: E/write(13138): {"status":"success","description":"Success add article"}
//
//				}
//				else{
//					String description=jobj.getString("message");
//					
//				}
			} catch (JSONException e) {
			}
		}
	}
	
	private class AsyncTaskAddGalaryImage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("category_prefix", categorylist.get(selsectedspinnerposition).getPrefix());
				
				if(bitmap!=null){
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG,60, bao);
					byte[] ba = bao.toByteArray();
					Log.e("BITMAP SIZE in asynctask", "bitmap size after compress = "
							+ ba.length);
					String base64Str = Base64.encodeBytes(ba);
					loginObj.put("pic_data",  base64Str);
				}
				else{
					Log.e("BITMAP SIZE in asynctask", "bitmap size after compress = "
							);
				}
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"article/addgallery/"+"1169";
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
			if(pd!=null){
				if((pd.isShowing())){
						pd.dismiss();
				}
			}
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				bitmap=null;
				Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();

			} catch (JSONException e) {
			}
		}
	}

}

