
package com.lipberry.fragment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
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
import android.graphics.BitmapFactory;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.CustomAdaptergrid;
import com.lipberry.adapter.NothingSelectedSpinnerAdapter;
import com.lipberry.model.Categories;
import com.lipberry.model.Commentslist;
import com.lipberry.model.ImageScale;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.UTF8Text;

@SuppressLint("NewApi")
public class FragmentWriteTopic extends Fragment {
	//btn_add_more_photo
	EditText txt_topic,txt_text,txt_tag;
	Button btn_select_photo,btn_go;
	WriteTopicTabFragment parent;
	int pos=1;
	Spinner spinner_category;
	int selsectedspinnerposition=0;
	Activity activity;
	boolean writetopicsuccess=false;
	Bitmap scaledBmp;
	String catagoryid;
	ArrayList<String>galarylist=new ArrayList<String>();
	Bitmap bitmap;
	ProgressDialog pd;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	ArrayList<String>catnamelist;
	ArrayList< Categories>categorylist;
	ImageScale bitmapimage;
	GridView grid_image;
	String title,category_id,category_prefix,body,photo,video;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=getActivity();

		jsonParser=new JsonParser();
		categorylist=new ArrayList<Categories>();
		catnamelist=new ArrayList<String>();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		activity=getActivity();

		jsonParser=new JsonParser();
		categorylist=new ArrayList<Categories>();
		catnamelist=new ArrayList<String>();
		


	}
	@Override
	public void onPause() {
		selsectedspinnerposition=0;
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadGridview();
	}





	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		appInstance = (LipberryApplication)activity.getApplication();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_write_topic,
				container, false);
		grid_image=(GridView) v.findViewById(R.id.grid_image);
		txt_topic=(EditText) v.findViewById(R.id.txt_topic);
		txt_text=(EditText) v.findViewById(R.id.txt_text);
		//
		txt_tag=(EditText) v.findViewById(R.id.txt_tag);
		btn_go=(Button) v.findViewById(R.id.btn_go);
		btn_go.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(galarylist.size()>0){
					String filepath = galarylist.get(0);
					filepath=filepath.replace("/Lipberrythumb","/Lipberryfinal" );
					bitmap=BitmapFactory.decodeFile(filepath);
				}

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
		((HomeActivity)activity).welcome_title.setText(R.string.txt_write_topic);
		if(Constants.isOnline(activity)){
			pd=ProgressDialog.show(activity, "Lipberry",
					"Retreving categories", true);
			new AsyncTaskgetCategories().execute();
		}
		else{
			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}


		return v;
	}


	public void loadGridview(){

		File file=new File(Environment.getExternalStorageDirectory().toString()+"/Lipberrythumb");
		galarylist =getList(file); 

		if(galarylist.size()>0){
			btn_select_photo.setText("Add more photo for gallery");
			grid_image.setVisibility(View.VISIBLE);
			Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+
					"/Lipberryfinal/"+galarylist.get(0));
			Log.e("sizebitmap", Environment.getExternalStorageDirectory().toString()+
					"/Lipberryfinal/"+galarylist.get(0));
			CustomAdaptergrid adapter=new CustomAdaptergrid(activity, galarylist);
			grid_image.setAdapter(adapter);
			grid_image.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}

			});
			grid_image.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Log.e("name", galarylist.get(arg2));

				}
			});
		}
		else{
			btn_select_photo.setText("Select photo");
			grid_image.setVisibility(View.GONE);
		}

	}

	public void loadphoto(){
		((HomeActivity)activity).captureimage(false);
	}

	private class AsyncTaskgetCategories extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				Log.e("session id","a  " +appInstance.getUserCred().getSession_id());
				loginObj.put("session_id", appInstance. getUserCred().getSession_id());
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
			Log.e("msz", result.getjObj().toString());
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
						Toast.makeText(activity, activity.getResources().getString(R.string.Toast_nocat_found),
								Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(activity, activity.getResources().getString(R.string.Toast_nocat_found),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(activity, activity.getResources().getString(R.string.Toast_nocat_found),
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
			Toast.makeText(activity, "Please enter article text",Toast.LENGTH_SHORT).show();
		}
		else if(selsectedspinnerposition==-1){
			Toast.makeText(activity, "Please select category",Toast.LENGTH_SHORT).show();
		}

		else{

			if(Constants.isOnline(getActivity())){

				pd=new ProgressDialog(getActivity());
				pd.setMessage("Writing topic");
				pd.show();


				new AsyncTaskWriteTopic().execute();
			}
			else{
				Toast.makeText(activity,activity.getResources().getString(R.string.Toast_check_internet),
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
//				
//				byte[] bytes = body.getBytes(Charset.forName("UTF-8"));
//
//				// byte[] utf8 =body.getBytes();
//				String str = new String(bytes, Charset.forName("UTF-8"));
//				body="";
//				for(int a=0;a<bytes.length;a++){
//					body=body.concat(""+bytes[a]);
//				}
//				Log.e("result","abc" +body);
//				//  body=str;
//				//				byte[] basstring = body.getBytes();
//
//				String base64String = Base64.encodeBytes(body.getBytes());
//				try {
//					byte[]  k=Base64.decode(base64String);
//
//					Log.e("decode", "aa "+k.toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				//txt_topic.setText(str);
				//04-21 12:44:36.201: E/base 64(27490): amZ1aWZ1aGYgZ2d0aWcgaXRnamZmIGpndGlnCg==

//04-21 12:40:27.751: E/base 64(27490): 2KfYs9mFINin2YTZhdiz2KrYrtiv2YUg2KjYsdmK2K/ZgyDYp9mE2KXZhNmD2KrYsdmI2YbZig==
				//Log.e("", msg)
				UTF8Text bodytext=new UTF8Text(body,true);
				String base64String=bodytext.getStringencripted();
				loginObj.put("title", title);
				loginObj.put("category_id", categorylist.get(selsectedspinnerposition).getId());
				loginObj.put("category_prefix", categorylist.get(selsectedspinnerposition).getPrefix());
				loginObj.put("body", base64String);
				if(bitmap!=null){
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG,100, bao);
					byte[] ba = bao.toByteArray();
					String base64Str = Base64.encodeBytes(ba);

					Log.e("body", base64Str);
					loginObj.put("photo",  base64Str);
				}
				else{
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
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");

				bitmap=null;
				if(status.equals("success")){
					Constants.writetopicsuccess=true;
					//	Toast.makeText(getActivity(),"Write topic completed", Toast.LENGTH_SHORT).show();
					String article_info=jobj.getString("article_info");
					article_info=article_info.replace("{", "");
					article_info=article_info.replace("}", "");
					catagoryid=article_info.substring(article_info.indexOf(":")+1);
					if(galarylist.size()>1){
						addgalarytoserver();
					}
					else{
						if(pd!=null){
							if((pd.isShowing())){
								pd.dismiss();
							}
						}
						((HomeActivity)activity).mTabHost.setCurrentTab(4);
						Toast.makeText(getActivity(),"Write topic successfull", Toast.LENGTH_SHORT).show();
					}


				}
				else{
					if(pd!=null){
						if((pd.isShowing())){
							pd.dismiss();
						}
					}
					Toast.makeText(getActivity(),"Failed to write topic", Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				if(pd!=null){
					if((pd.isShowing())){
						pd.dismiss();
					}
				}
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
					String base64Str = Base64.encodeBytes(ba);
					loginObj.put("pic_data",  base64Str);
				}
				else{

				}
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"article/addgallery/"+catagoryid;
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
			Log.e("response", result.getjObj().toString());
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				if(status.equals("success")){
					if(pos<galarylist.size()){
						if(Constants.isOnline(activity)){
							String filepath = galarylist.get(pos);
							filepath=filepath.replace("/Lipberrythumb","/Lipberryfinal" );
							//							bitmapimage =new ImageScale();
							//							bitmap=bitmapimage.decodeImage(filepath);
							bitmap=BitmapFactory.decodeFile(filepath);
							new AsyncTaskAddGalaryImage().execute();
							pos++;
						}
						else{
							Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
									Toast.LENGTH_SHORT).show();
							((HomeActivity)activity).mTabHost.setCurrentTab(4);
							String newFolder = "/Lipberryfinal";
							String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
							String drectory= extStorageDirectory + newFolder;
							File myNewFolder = new File(drectory);

							String thumb=extStorageDirectory+"/Lipberrythumb";
							File thumbFolder = new File(thumb);
							deleteDirectory(thumbFolder);
							deleteDirectory(myNewFolder);
							createfolder();
							if(pd!=null){
								if((pd.isShowing())){
									pd.dismiss();
								}
							}
						}
					}
					else{

						Toast.makeText(activity,description, Toast.LENGTH_SHORT).show();
						Constants.writetopicsuccess=false;
						((HomeActivity)activity).mTabHost.setCurrentTab(4);
						String newFolder = "/Lipberryfinal";
						String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
						String drectory= extStorageDirectory + newFolder;

						File myNewFolder = new File(drectory);

						String thumb=extStorageDirectory+"/Lipberrythumb";
						File thumbFolder = new File(thumb);
						deleteDirectory(thumbFolder);
						deleteDirectory(myNewFolder);
						createfolder();
						if(pd!=null){
							if((pd.isShowing())){
								pd.dismiss();
							}
						}
					}

				}
				else{
					((HomeActivity)activity).mTabHost.setCurrentTab(4);
					String newFolder = "/Lipberryfinal";
					String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
					String drectory= extStorageDirectory + newFolder;
					File myNewFolder = new File(drectory);

					String thumb=extStorageDirectory+"/Lipberrythumb";
					File thumbFolder = new File(thumb);
					deleteDirectory(thumbFolder);
					deleteDirectory(myNewFolder);
					createfolder();
					Constants.writetopicsuccess=false;
					if(pd!=null){
						if((pd.isShowing())){
							pd.dismiss();
						}
					}

					Toast.makeText(activity,description, Toast.LENGTH_SHORT).show();
				}


			} catch (JSONException e) {
			}
		}
	}


	private ArrayList<String> getList(File parentDir) {

		ArrayList<String> inFiles = new ArrayList<String>();
		try {
			String[] fileNames = parentDir.list();

			for (String fileName : fileNames) {

				if ((fileName.toLowerCase().endsWith(".jpg"))||(fileName.toLowerCase().endsWith(".png"))) {
					inFiles.add(Environment.getExternalStorageDirectory().toString()+"/Lipberrythumb/"+fileName);

				} 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return inFiles;
	}


	public void addgalarytoserver(){
		pos=0;
		if(Constants.isOnline(activity)){
			String filepath = galarylist.get(pos);
			filepath=filepath.replace("/Lipberrythumb","/Lipberryfinal" );
			//
			//
			//			bitmapimage =new ImageScale();
			//			bitmap=bitmapimage.decodeImage(filepath);
			bitmap=BitmapFactory.decodeFile(filepath);
			new AsyncTaskAddGalaryImage().execute();

			pos++;
		}
		else{
			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}


	}


	public static boolean deleteDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		return( path.delete() );
	}


	public void createfolder(){
		String newFolder = "/Lipberryfinal";
		String thumb="/Lipberrythumb";

		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		String drectory= extStorageDirectory + newFolder;
		String drectorythumb=extStorageDirectory + thumb;
		File myNewFolder = new File(drectory);
		myNewFolder.mkdir();
		File myNewFolderthumb = new File(drectorythumb);
		myNewFolderthumb.mkdir();
	}
}

