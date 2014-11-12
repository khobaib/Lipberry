package com.lipberry.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.lipberry.HomeActivity;
import com.lipberry.fragment.FragmentWriteTopic;
import com.lipberry.model.Article;
import com.lipberry.model.ArticleDetails;
import com.lipberry.model.ArticleList;

public class Constants {
	
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        java.net.URL url = new java.net.URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	static boolean debugon=true;
	public static void debugLog(String key,String msz){
		if(debugon){
			Log.e(key, msz);	
		}
	}
	public static boolean pushnotificationcalllive=false;
	public static String type;
	public static boolean topicwritesuccess=false;
	public static HomeActivity homeActivity;
	public static boolean IMAGEPAGECALLED=false; 
	public static ArticleList articlelist;
	public static boolean STATECALLPDFORMENU=false;
	public static int GOT_AB_FROM_WRITE_TOPIC;
	public static boolean GO_ARTCLE_PAGE_FROM_MEMBER=false; 
	public static Article ARTICLETOSEE;
	public static ArticleDetails articledetails;
	public static boolean GO_ARTCLE_PAGE=false; 
	public static int from;
	public static String INTER_ARTICLE_ID;
	public static String INTER_MEMBER_ID;
	public static boolean GO_MEMBER_STATE_FROM_IMESSAGE=false; 
	public static boolean GO_MEMBER_STATE_FROM_INTERACTION=false; 
	public static boolean GO_MEMBER_STATE_FROM_SETTING=false; 
	public static boolean MESSAGESETTINGSTATE=false;
	public static boolean catgeory=false;
	public static String caturl="-1";
	public static String caname=""; 
	public static boolean writetopicsuccess=false;
	public static String drectory;
	public static String photofromcamera;
	public static FragmentWriteTopic writetopic;
	public static int notificationcount;
	public static String userid="8150";
	public static String  baseurl="http://lipberry.com/API/";
	public static boolean isOnline(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	public  static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
	public static  boolean namecheck(String name){

		if(((name.length()>10)||(name.length()<3))){
			return false;
		}
		Pattern p = Pattern.compile("[A-Za-z]+([_|A-Za-z|0-9])*");
		return p.matcher(name).matches();
	}
	public static final int REQUEST_TYPE_GET = 1;
	public static final int REQUEST_TYPE_POST = 2;
	public static final int REQUEST_TYPE_PUT = 3;
	public static final int REQUEST_TYPE_DELETE = 4;
	public static final String AVATER = "avater";
	public static final String PUSHNEWMESSAGE = "pushnewmessage1";
	public static final String WEEKLY_NEWS = "weeklynews";
	public static final String STOPPRIVATEMAILS = "stopprivatemail";
	public static final String STOPPRIVATEMESSAGE = "stopprivatmessage";
	public static final String STOPCOMMENTMAIL = "stopcommentmail";
	public static final String STOPFOLLOWERMAIL = "stopfollwemail";
	public static final String STOPLIKEARTMAIL = "stoplikeartmails";
	public static final String SYSTEM_NOTIFICATION = "system_notification1";
	public static final String WEEKLY_NEWSLATER = "weeklynewsletter";
	public static final String DIRECT_MSZ_TOMAIL = "direcmsztomail";
	public static final String MEMBERALLOW_TO_SEND_MSZ= "allowmembertosendmessage";
	public static final String STOP_PUSH_MESSAGE = "stoppushmessage";
	public static final String REMEMBER_ME = "remember_me";
	public static final String FIRST_TIME = "first_time_run";
	public static final String DESCRIPTION = "description";
	public static final String SESSION_ID = "session_id";
	public static final String INSTAGRAM = "instagram"; 
	public static final String NICKNAME = "nickname";
	public static final String COUNTRYCITY_GLAG = "countrycity_flag";
	public static final String IS_AUTHORIZED = "is_authorized";
	public static final String SITEURL = "siteurl";
	public static final String CITY = "city";
	public static final String ID = "id";
	public static final String TWITTER = "twitter"; 
	public static final String EMAIL = "email";
	public static final String ACCESS_ADMINTRATOR = "administrator";
	public static final String USER_NAME = "user_name";
	public static final String PASSWORD = "password";
	public static final String NAME = "name";
	public static final String YOUTUBE = "youtube";
	public static final String TELEPHONE = "telephone";
	public static final String BRIEF = "brief";
	public static final String COUNTRY = "country";
	public static final int ESSENTIAL_EDUCATION_INDEX = 0;
	public static final int ESSENTIAL_ETHNICITY_INDEX = 1;
	public static final int ESSENTIAL_DIET_INDEX = 2;
	public static final int ESSENTIAL_DRINKS_INDEX = 3;
	public static final int ESSENTIAL_SMOKES_INDEX = 4;
	public static final int ESSENTIAL_RELIGION_INDEX = 5;
	public static final int ESSENTIAL_KIDS_INDEX = 6;
	public static final int ESSENTIAL_POLITICS_INDEX = 7;
	public static final int ESSENTIAL_SIGN_INDEX = 8;
	public static final int ESSENTIAL_PROFESSION_INDEX = 9;
	public static final int ESSENTIAL_HOMETOWN_INDEX = 10;
	public static final int ESSENTIAL_LANGUAGES_INDEX = 11;
	public static final int ESSENTIAL_STATIC_FIELD_COUNT = 9;
	public static final int TEMPLATE_ID_SOMETHING_ELSE = 8;
	public static final int FAVORITE_STATUS_STRANGER = 1;
	public static final int FAVORITE_STATUS_FRIEND = 2;
	public static final int FAVORITE_STATUS_SENT = 3;
	public static final int FAVORITE_STATUS_WAITING = 4;
	public static final int RESPONSE_STATUS_CODE_SUCCESS = 200;
	public static final String DISPLAY_MESSAGE_ACTION ="com.sparkzi.DISPLAY_MESSAGE";
	public static final String EXTRA_MESSAGE = "message";
	public static final File APP_DIRECTORY =
			new File(Environment.getExternalStorageDirectory(),"sparkzi");
	public static final String FROM_ACTIVITY = "from_activity";
	public static final int PARENT_ACTIVITY_LOGIN = 101;
	public static final int PARENT_ACTIVITY_PROFILE = 102;
	public static final int RETRIEVE_ALL_CONVERSATIONS = 201;
	public static final int RETRIEVE_SPECIFIC_USER_CONVERSATIONS = 202;

}
