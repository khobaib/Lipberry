package com.lipberry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.lipberry.fragment.FragmentInbox;
import com.lipberry.fragment.FragmentWriteTopic;
import com.lipberry.fragment.HomeTabFragment;
import com.lipberry.fragment.WriteTopicTabFragment;
import com.lipberry.fragment.CategoryTabFragment;
import com.lipberry.fragment.IneractionTabFragment;
import com.lipberry.fragment.InboxTabFragment;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.fragment.TabFragment;
import com.lipberry.model.ImageScale;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends FragmentActivity {
	public static Typeface tp;

	public  String photofromcamera;
	public  FragmentTabHost mTabHost;
	public  String drectory;
	public  String drectorythumb;
	private static final String LIST_STATE = "listState";
	private Parcelable mListState = null;
	ProgressDialog pd;
	public TextView text_notification_no_fromactivity;
	public ImageView img_cat_icon;

	public TabFragment activeFragment;
	FragmentWriteTopic writetopic;
	public ViewGroup mTabsPlaceHoler;
	int count=0;
	public ListView ProductList;
	JsonParser jsonParser;
	LipberryApplication appInstance;
	TextView text_notification_no;
	boolean galary;
	public Button backbuttonoftab;
	public TextView welcome_title;

	//	
	//	 @Override
	//	    protected void onRestoreInstanceState(Bundle state) {
	//	        super.onRestoreInstanceState(state);
	//	        mListState = state.getParcelable(LIST_STATE);
	//	    }
	//	 
	//	 @Override
	//	    protected void onSaveInstanceState(Bundle state) {
	//	        super.onSaveInstanceState(state);
	//	        mListState = ProductList.onSaveInstanceState();
	//	        state.putParcelable(LIST_STATE, mListState);
	//	    }
	//	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		writetopic=new FragmentWriteTopic();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication) getApplication();
		setContentView(R.layout.main);
		img_cat_icon=(ImageView) findViewById(R.id.img_cat_icon);
		welcome_title=(TextView) findViewById(R.id.welcome_title);
		backbuttonoftab=(Button) findViewById(R.id.backbuttonoftab);
		backbuttonoftab.setVisibility(View.GONE);
		welcome_title.setTypeface(Utility.getTypeface1(HomeActivity.this));
		setTabs();
		mTabHost.setCurrentTab(4);

	}
	private void setTabs() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(),
				android.R.id.tabcontent);
		mTabsPlaceHoler = (TabWidget) findViewById(android.R.id.tabs);
		addTab("Write Topic", R.drawable.lunknown, WriteTopicTabFragment.class);
		addTab("Inbox", R.drawable.linbox,InboxTabFragment.class);
		addTab("Interaction", R.drawable.linteraction, IneractionTabFragment.class);
		addTab("Categories", R.drawable.lcategory, CategoryTabFragment.class);
		addTab("Home", R.drawable.lhome, HomeTabFragment.class);
		addTab("Menu", R.drawable.lmenu, MenuTabFragment.class);

	}

	private void addTab(String labelId, int drawableId, Class<?> c) {



		//	if(labelId!="Inbox"){
		FragmentTabHost.TabSpec spec = mTabHost.newTabSpec(labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, mTabsPlaceHoler, false);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		text_notification_no=(TextView) tabIndicator.findViewById(R.id.text_notification_no);




		if(labelId.equals("Interaction")){

			text_notification_no_fromactivity=text_notification_no;
			text_notification_no_fromactivity.setVisibility(View.GONE);
			getnotificationcount();

		}
		else{
			text_notification_no.setVisibility(View.GONE);
		}
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		//	spec.setContent(intent)
		//	mTabHost.addTab(spec, c, null);

		//			if(labelId!="Inbox"){
		spec.setIndicator(tabIndicator);
		//	spec.setContent(intent)
		mTabHost.addTab(spec, c, null);
		//			}
		//			else{
		//				Intent intent = new Intent().setClass(HomeActivity.this, LoginActivity.class);
		//				// LocalActivityManager mLocalActivityManager = new LocalActivityManager(HomeActivity.this, false);
		//				 //FragmentManager actvityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
		//			
		//					 mTabHost.setup(this,null ,
		//								android.R.id.tabcontent);
		//				spec.setContent(intent);
		//				mTabHost.addTab(spec);
		//			}
		//	//	}
		//		//04-11 19:12:03.771: E/AndroidRuntime(17512): 	at 

		//	/*	else{
		//			/*
		//			
		//			FragmentTabHost.TabSpec spec = mTabHost
		//					.newTabSpec(labelId)
		//					.setIndicator("Videos",
		//							getResources().getDrawable(R.drawable.linbox))
		//							.setContent(intent);*/
		//			
		//			//tabHost.addTab(spec);
		//			
		//			
		//			Intent intent = new Intent().setClass(this, FragmentInbox.class);
		//		FragmentTabHost.TabSpec spec = mTabHost.newTabSpec(labelId);
		//			View tabIndicator = LayoutInflater.from(this).inflate(
		//					R.layout.tab_indicator, mTabsPlaceHoler, false);
		//			ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		//			text_notification_no=(TextView) tabIndicator.findViewById(R.id.text_notification_no);
		//			text_notification_no.setVisibility(View.GONE);
		//			icon.setImageResource(drawableId);
		//			spec.setIndicator(tabIndicator);
		//			//spec.setContent(intent);
		//			mTabHost.addTab(spec, c, null);
		//		//	mTabHost.addt
		//		//	mTabHost.addTab(spec);
		//			//mTabHost.addTab(tabSpec)
		//
		//		}*/



	}

	public void getnotificationcount(){

		if(Constants.isOnline(HomeActivity.this)){
			new AsyncTaskGetNotificationCount().execute();
		}
		else{
			Toast.makeText(HomeActivity.this, getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}

	}



	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	public void setTabSelection() {
		// TODO Auto-generated method stub
		mTabHost.setCurrentTab(1);
	}

	public void setTabSelection0() {
		// TODO Auto-generated method stub
		mTabHost.setCurrentTab(4);
	}
	@Override
	public void onBackPressed() {
		activeFragment.onBackPressed();
	}
	public void close() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				HomeActivity.this);
		alertDialogBuilder.setTitle("Lipberry");
		alertDialogBuilder
		.setMessage("Would you like to exit?")
		.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				Intent intent=new  Intent(HomeActivity.this, LoginActivity.class);
				finisssh();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void finisssh(){
		super.onBackPressed();
	}


	public void captureimage(boolean from){
		galary=from;
		createfolder();
		final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Take Photo"))
				{

					photofromcamera=System.currentTimeMillis()+".jpg";
					Constants.drectory=drectory;
					Constants.photofromcamera=photofromcamera;
					final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(drectory, photofromcamera);
					Log.e("pos", drectory+photofromcamera+" " +f.exists());
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					//getIntent().getSerializableExtra("MyClass");
					startActivityForResult(intent, 1);
				}
				else if (options[item].equals("Choose from Gallery"))
				{
					photofromcamera=System.currentTimeMillis()+".jpg";
					Constants.drectory=drectory;
					Constants.photofromcamera=photofromcamera;
					Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 3);



					//	Intent intent = new Intent(Intent.ACTION_PICK);
					//intent.setType("image/*");
					// intent.setAction(Intent.ACTION_GET_CONTENT);
					// Intent intent = new Intent(Intent.ACTION_PICK,
					// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					// startActivityForResult(Intent.createChooser(intent,
					//  TODO "Select Picture"),
					//	startActivityForResult(intent, 3);

				}
				else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();

	}
	//04-11 15:34:23.314: E/AndroidRuntime(19338): java.lang.RuntimeException: Unable to resume activity {com.lipberry/com.lipberry.HomeActivity}: java.lang.RuntimeException: Failure delivering result ResultInfo{who=null, request=3, result=-1, data=Intent { dat=content://media/external/images/media/484 }} to activity {com.lipberry/com.lipberry.HomeActivity}: java.lang.NullPointerException


	//04-08 16:49:31.972: E/AndroidRuntime(13691): Caused by: java.lang.RuntimeException: 

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		drectory=Constants.drectory;
		photofromcamera=Constants.photofromcamera;

		Log.e("error", ""+requestCode+" "+RESULT_OK);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				try
				{

					String filepath = drectory+"/"+photofromcamera;

					File file=new File(filepath);

					if(file.exists()){
						ImageScale scaleimage=new ImageScale();
						Bitmap photo = scaleimage.decodeImagetoUpload(file.getAbsolutePath());
						Log.e("bitmap ", ""+photo.getHeight()+"   "+photo.getWidth());
						Bitmap p=Bitmap.createScaledBitmap(photo, photo.getWidth()/6, photo.getHeight()/6,true);
						file.delete();
						ByteArrayOutputStream bytes = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.PNG, 80, bytes);


						File f = new File(filepath);
						f.createNewFile();
						FileOutputStream fo = new FileOutputStream(f);
						fo.write(bytes.toByteArray());
						fo.close();


						String thumnilpath=drectorythumb+"/"+photofromcamera;
						Log.e("path", thumnilpath);
						//	photo=Bitmap.createScaledBitmap(photo, photo.getWidth()/6, photo.getHeight()/600,true);
						Log.e("path", "a  "+ p);
						bytes = new ByteArrayOutputStream();
						p.compress(Bitmap.CompressFormat.PNG, 80, bytes);

						File filethumb = new File(thumnilpath);
						filethumb.createNewFile();
						FileOutputStream fothumb = new FileOutputStream(filethumb);
						fothumb.write(bytes.toByteArray());
						fothumb.close();


					}

				}
				catch(Exception e)
				{
					Log.e("Could not save", e.toString());
				}
			}


			else if (requestCode == 3) {
				try {
					File sd = Environment.getExternalStorageDirectory();
					File data1 = Environment.getDataDirectory();

					if (sd.canWrite()) {
						Uri selectedImage = data.getData();
						String[] filePath = { MediaStore.Images.Media.DATA };
						Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
						c.moveToFirst();
						int columnIndex = c.getColumnIndex(filePath[0]);
						String picturePath = c.getString(columnIndex);
						c.close();
						File dn= new File(drectory);
						File source= new File(picturePath);
						File destination= new File(dn,photofromcamera);
						FileChannel src = new FileInputStream(source).getChannel();
						FileChannel dst = new FileOutputStream(destination).getChannel();
						dst.transferFrom(src, 0, src.size());

						src.close();
						dst.close();

						String filepath = drectory+"/"+photofromcamera;
						File file=new File(filepath);
						if(file.exists()){
							ImageScale scaleimage=new ImageScale();
							Bitmap photo = scaleimage.decodeImagetoUpload(file.getAbsolutePath());
							Log.e("bitmap ", ""+photo.getHeight()+"   "+photo.getWidth());

							file.delete();
							ByteArrayOutputStream bytes = new ByteArrayOutputStream();
							photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
							File f = new File(filepath);
							f.createNewFile();
							FileOutputStream fo = new FileOutputStream(f);
							fo.write(bytes.toByteArray());
							fo.close();


							String thumnilpath=drectorythumb+"/"+photofromcamera;
							photo=Bitmap.createScaledBitmap(photo, photo.getWidth()/6, photo.getHeight()/6,false);
							bytes = new ByteArrayOutputStream();
							photo.compress(Bitmap.CompressFormat.PNG, 80, bytes);
							File filethumb = new File(thumnilpath);
							filethumb.createNewFile();
							FileOutputStream fothumb = new FileOutputStream(filethumb);
							fothumb.write(bytes.toByteArray());
							fothumb.close();

						}

					} 

				}catch (Exception e) {}
			}



			if(!galary){
				Log.e("directory", "notnull "+drectory+"/"+photofromcamera+"   "+writetopic);
				Toast.makeText(HomeActivity.this,"You have selected an image",
						Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(HomeActivity.this,"You have selected an image",
						Toast.LENGTH_SHORT).show();
			}
		}
		else{

			Toast.makeText(HomeActivity.this,"Failed to select an image",
					Toast.LENGTH_SHORT).show();
		}
	}   
	public void createfolder(){
		String newFolder = "/Lipberryfinal";
		String thumb="/Lipberrythumb";
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		drectory= extStorageDirectory + newFolder;
		drectorythumb=extStorageDirectory + thumb;
		File myNewFolder = new File(drectory);
		myNewFolder.mkdir();
		File myNewFolderthumb = new File(drectorythumb);
		myNewFolderthumb.mkdir();
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

	private class AsyncTaskGetNotificationCount extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String postdata = loginObj.toString();
				String url = Constants.baseurl+"account/unreadnotifications/";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, 
						null,
						postdata, null);
				return response;
			} catch (JSONException e) {                
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			Log.e("count", result.getjObj().toString());

			if(Constants.isOnline(HomeActivity.this)){
				ScheduledExecutorService scheduler =
						Executors.newSingleThreadScheduledExecutor();

				scheduler.schedule(new Runnable() {
					public void run() {
						getnotificationcount();
					}
				},10, TimeUnit.SECONDS);
				//04-10 16:21:54.517: E/count(9003): {"unread_count":9,"status":"success"}

				try {
					String status=result.getjObj().getString("status");
					if(status.equals("success")){
						String unread_count=result.getjObj().getString("unread_count");
						try {
							count=Integer.parseInt(unread_count);
							Constants.notificationcount=count;
							if(count>0){
								runonUI();
							}
							else{
								text_notification_no_fromactivity.setVisibility(View.GONE);
							}
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


	public void runonUI(){
		HomeActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				text_notification_no_fromactivity.setVisibility(View.VISIBLE);
				text_notification_no_fromactivity.setText(""+count);
			}
		});
	}


}

