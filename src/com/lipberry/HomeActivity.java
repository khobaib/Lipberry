package com.lipberry;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.lipberry.fragment.CategoryTabFragment;
import com.lipberry.fragment.FragmentWriteTopic;
import com.lipberry.fragment.HomeTabFragment;
import com.lipberry.fragment.InboxTabFragment;
import com.lipberry.fragment.IneractionTabFragment;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.fragment.ReclickableTabHost;
import com.lipberry.fragment.TabFragment;
import com.lipberry.fragment.WriteTopicTabFragment;
import com.lipberry.model.ImageScale;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.settings.FragmentImageSetting;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;

public class HomeActivity extends FragmentActivity {
	public static Typeface tp;
	public  String photofromcamera;
	public  ReclickableTabHost mTabHost;
	public  String drectory;
	public  String drectorythumb;
	private static final String LIST_STATE = "listState";
	private Parcelable mListState = null;
	ProgressDialog pd;
	public TextView text_notification_no_fromactivity;
	public ImageView img_cat_icon;
	File photo;
	public TabFragment activeFragment;
	FragmentWriteTopic writetopic;
	public ViewGroup mTabsPlaceHoler;
	int count=0;
	public ListView ProductList;
	public  RelativeLayout topBar;
	AsyncTask<Void, Void, Void> mRegisterTask;
	JsonParser jsonParser;
	LipberryApplication appInstance;
	TextView text_notification_no;
	boolean galary;
	public Button backbuttonoftab;
	public TextView welcome_title;
	FragmentImageSetting imgsetting;
	String a="normal";
	Timer myTimer ;
	String kkr;
	int k=4;
	MyTimerTask myTimerTask;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("HomeActivity","onCreate");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication) getApplication();
		setContentView(R.layout.main);
		img_cat_icon=(ImageView) findViewById(R.id.img_cat_icon);
		welcome_title=(TextView) findViewById(R.id.welcome_title);
		backbuttonoftab=(Button) findViewById(R.id.backbuttonoftab);
		backbuttonoftab.setVisibility(View.GONE);
		topBar=(RelativeLayout) findViewById(R.id.topBar);
		welcome_title.setTypeface(Utility.getTypeface1(HomeActivity.this));
		setTabs();
		try {
			a=getIntent().getExtras().getString("type");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(a.equals("normal")){
			k=4;
			//mTabHost.setCurrentTab(4);
		}
		else if(a.equals("inbox message")){
			k=1;
			//mTabHost.setCurrentTab(1);

		}
		else{
			k=2;
		}
		mTabHost.setCurrentTab(k);

//		Handler handler=new Handler();
//		handler.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				writetopic=new FragmentWriteTopic();
//
//			}
//		},4000);
		Constants.homeActivity=HomeActivity.this;
		if(k==4){
			newgetGCMDeviceID();
		}
		
	}
	
	
	private class MyTimerTask extends TimerTask {
	    @Override
	    public void run() {
	        runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	               //code to get and send location information
	            	if(Constants.isOnline(HomeActivity.this)){
	        			new AsyncTaskGetNotificationCount().execute();
	        		}
	            }
	        });
	    }
	}
	
	public void setTabs() {
		mTabHost = (ReclickableTabHost) findViewById(android.R.id.tabhost);

		mTabHost.setup(this, getSupportFragmentManager(),
				android.R.id.tabcontent);
		mTabsPlaceHoler = (TabWidget) findViewById(android.R.id.tabs);
		addTab(getResources().getString(R.string.txt_write_topic), R.drawable.lunknown, WriteTopicTabFragment.class);
		addTab(getResources().getString(R.string.txt_inbox), R.drawable.linbox,InboxTabFragment.class);
		addTab(getResources().getString(R.string.txt_interaction), R.drawable.linteraction, IneractionTabFragment.class);
		addTab("Categories", R.drawable.lcategory, CategoryTabFragment.class);
		addTab("Home", R.drawable.lhome, HomeTabFragment.class);
		addTab("Menu", R.drawable.lmenu, MenuTabFragment.class);
		int i=mTabHost.getCurrentTab();
	}
	

	private void addTab(String labelId, int drawableId, Class<?> c) {
		FragmentTabHost.TabSpec spec = mTabHost.newTabSpec(labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, mTabsPlaceHoler, false);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		text_notification_no=(TextView) tabIndicator.findViewById(R.id.text_notification_no);
		if(labelId.equals(getResources().getString(R.string.txt_interaction))){
			text_notification_no_fromactivity=text_notification_no;
			text_notification_no_fromactivity.setVisibility(View.GONE);
			getnotificationcount();

		}
		else{
			text_notification_no.setVisibility(View.GONE);
		}
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setIndicator(tabIndicator);
		mTabHost.addTab(spec, c, null);
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
		Log.e("HomeActivity","onStart");
	}
	@Override
	protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	//myTimer.cancel();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("HomeActivity","onResume");
        if(Constants.pushnotificationcalllive){
        	Constants.pushnotificationcalllive=false;
        	String type=Constants.type;
        	int callno;
        	
        	
        }
//		Log.e(" onResume", " onResume");
//		 myTimer = new Timer();
//		 myTimerTask= new MyTimerTask();
//		 myTimer.scheduleAtFixedRate(myTimerTask, 0, 10000); 
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
		alertDialogBuilder.setTitle(getResources().getString(R.string.app_name_arabic));
		alertDialogBuilder
		.setMessage(getResources().getString(R.string.txt_exit))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.txt_yes),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				Intent intent=new  Intent(HomeActivity.this, LoginActivity.class);
				finisssh();
				System.exit(0);
			}
		})
		.setNegativeButton(getResources().getString(R.string.txt_no),new DialogInterface.OnClickListener() {
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
		final CharSequence[] options = { getResources().getString(R.string.txt_take_photo),  getResources().getString(R.string.txt_from_gallery),
				getResources().getString(R.string.txt_from_cancel)};

		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		builder.setTitle(getResources().getString(R.string.app_name_arabic));
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals( getResources().getString(R.string.txt_take_photo)))
				{

					photofromcamera=System.currentTimeMillis()+".jpg";
					Constants.drectory=drectory;
					Constants.photofromcamera=photofromcamera;
					final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(drectory, photofromcamera);
					Log.e("pos", drectory+photofromcamera+" " +f.exists());
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, 1);
				}
				else if (options[item].equals(getResources().getString(R.string.txt_from_gallery)))
				{
					photofromcamera=System.currentTimeMillis()+".jpg";
					Constants.drectory=drectory;
					Constants.photofromcamera=photofromcamera;
					Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 3);

				}
				else if (options[item].equals(getResources().getString(R.string.txt_from_cancel))) {
					dialog.dismiss();
				}
			}
		});
		builder.show();

	}

	public void opeencamera(FragmentImageSetting imgsetting){
		this.imgsetting=imgsetting;
		photofromcamera=System.currentTimeMillis()+".jpg";
		photo = new File(Environment.getExternalStorageDirectory(),photofromcamera);
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		startActivityForResult(intent, 4);
	}
	public void opengalary(FragmentImageSetting imgsetting){
		photofromcamera=System.currentTimeMillis()+".jpg";
		photo = new File(Environment.getExternalStorageDirectory(),photofromcamera);
		this.imgsetting=imgsetting;
		Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, 5);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("request code", "q "+requestCode);
		drectory=Constants.drectory;
		photofromcamera=Constants.photofromcamera;

		if (resultCode == RESULT_OK) {

			if (requestCode == 4){
				try {

					String filepath =Environment.getExternalStorageDirectory()+"/"+photofromcamera;
					if(photo.exists()){
						ImageScale scaleimage=new ImageScale();
						Bitmap bitmap = scaleimage.decodeImageForProfile( photo.getAbsolutePath());
						imgsetting.onimageloadingSuccessfull(bitmap);

					}
					else{
						imgsetting.onimageloadingFailed();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					imgsetting.onimageloadingFailed();
					e.printStackTrace();
				}


			}

			else if (requestCode == 1) {
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

			else if(requestCode == 5){
				try {

					String filepath =Environment.getExternalStorageDirectory()+"/"+photofromcamera;
					File sd = Environment.getExternalStorageDirectory();
					if (sd.canWrite()){
						Uri selectedImage = data.getData();
						String[] filePath = { MediaStore.Images.Media.DATA };
						Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
						c.moveToFirst();
						int columnIndex = c.getColumnIndex(filePath[0]);
						String picturePath = c.getString(columnIndex);
						c.close();
						File source= new File(picturePath);
						FileChannel src = new FileInputStream(source).getChannel();
						FileChannel dst = new FileOutputStream(photo).getChannel();
						dst.transferFrom(src, 0, src.size());
						src.close();
						dst.close();
						ImageScale scaleimage=new ImageScale();
						Bitmap bitmap = scaleimage.decodeImageForProfile( photo.getAbsolutePath());
						imgsetting.onimageloadingSuccessfull(bitmap);


					}
					else{
						imgsetting.onimageloadingFailed();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					imgsetting.onimageloadingFailed();
					e.printStackTrace();
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


			if((requestCode==4)||(requestCode==5)){

			}
			else{
				if(!galary){
					Log.e("directory", "notnull "+drectory+"/"+photofromcamera+"   "+writetopic);
					Toast.makeText(HomeActivity.this,getResources().getString(R.string.txt_you_have_selcect_image),
							Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(HomeActivity.this,getResources().getString(R.string.txt_you_have_selcect_image),
							Toast.LENGTH_SHORT).show();
				}
			}

		}
		else{

			Toast.makeText(HomeActivity.this,getResources().getString(R.string.txt_failed_to_select_an_image),
					Toast.LENGTH_SHORT).show();
		}
	}   
	public void createfolder(){
		String newFolder = "/Lipberryfinal1";
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
	
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String newMessage = intent.getExtras().getString(Utility.EXTRA_MESSAGE);
		}
	};
	
	private void newgetGCMDeviceID() {
		if(Constants.isOnline(HomeActivity.this)){
			Log.e("Gcm", "1");
			Utility.token=appInstance.getUserCred().getSession_id();
			Log.e("Gcm", "11");
			GCMRegistrar.checkDevice(HomeActivity.this);
			Log.e("Gcm", "12");
			GCMRegistrar.checkManifest(HomeActivity.this);
			Log.e("Gcm", "2");

			registerReceiver(mHandleMessageReceiver, new IntentFilter(Utility.DISPLAY_MESSAGE_ACTION));
			final String regId = GCMRegistrar.getRegistrationId(HomeActivity.this);
			Log.e("Gcm", "3");

			Log.e("reg id", "reg "+regId);
			if (regId.equals("")) {
				GCMRegistrar.register(this, Utility.SENDER_ID);
				GCMRegistrar.getRegistrationId(HomeActivity.this);
			}
			else {
				Log.e("Gcm", "5");

				if (GCMRegistrar.isRegisteredOnServer(HomeActivity.this)) {
					Log.e("Gcm", "6");

				} 
				else {
					final Context context = this;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							Log.e("Gcm", "7");

							boolean registered = ServerUtilities.register(context, regId);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
							Log.e("Gcm", "8");

						}

					};
					mRegisterTask.execute(null, null, null);
				}
			}
		}
	}

}
