package com.lipberry.utility;



import com.lipberry.model.UserCred;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class LipberryApplication extends Application {
	private static Context context;
	protected SharedPreferences User;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		User = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static Context getAppContext() {
		return context;        
	}
	public void setFirstTime(Boolean firstTimeFlag){
		Editor editor = User.edit();
		editor.putBoolean(Constants.FIRST_TIME, firstTimeFlag);
		editor.commit();        
	}
	public void setRememberMe(Boolean rememberMeFlag){
		Editor editor = User.edit();
		editor.putBoolean(Constants.REMEMBER_ME, rememberMeFlag);
		editor.commit();        
	}
	public void setUserCred(UserCred userCred){
		Log.e("mail", "  "+userCred.getDirect_msz_mail());
		Editor editor = User.edit();
		editor.putBoolean(Constants.SYSTEM_NOTIFICATION, userCred.getSystem_notification());
		editor.putBoolean(Constants.WEEKLY_NEWSLATER, userCred.getWeekly_newsletter());
		editor.putBoolean(Constants.DIRECT_MSZ_TOMAIL, userCred.getDirect_msz_mail());
		editor.putBoolean(Constants.MEMBERALLOW_TO_SEND_MSZ, userCred.getAllow_direct_msz());
		editor.putBoolean(Constants.STOP_PUSH_MESSAGE, userCred.getStop_push_new_message());
		editor.putString(Constants.ID, userCred.getId());
		editor.putString(Constants.SITEURL, userCred.getSiteurl());
		editor.putString(Constants.CITY, userCred.getCity());
		editor.putString(Constants.USER_NAME, userCred.getUsername());
		editor.putString(Constants.ACCESS_ADMINTRATOR, userCred.getAdministrator());
		editor.putString(Constants.EMAIL, userCred.getEmail());
		editor.putString(Constants.TWITTER, userCred.getTwitter());
		editor.putString(Constants.TELEPHONE, userCred.getTelephone());
		editor.putString(Constants.YOUTUBE, userCred.getYoutube());
		editor.putString(Constants.NAME, userCred.getName());
		editor.putString(Constants.PASSWORD, userCred.getPassword());
		editor.putString(Constants.COUNTRY, userCred.getCountry());
		editor.putString(Constants.COUNTRYCITY_GLAG, userCred.getCountrycity_flag());
		editor.putString(Constants.IS_AUTHORIZED, userCred.getIs_authorized());
		editor.putString(Constants.BRIEF, userCred.getBrief());
		editor.putString(Constants.NICKNAME, userCred.getNickname());
		editor.putString(Constants.DESCRIPTION, userCred.getDescription());
		editor.putString(Constants.SESSION_ID, userCred.getSession_id());
		editor.putString(Constants.INSTAGRAM, userCred.getInstagram());
		editor.commit();
	}


	public UserCred getUserCred(){
		boolean system_notification=User.getBoolean(Constants.SYSTEM_NOTIFICATION, false);
		boolean weekly_newsletter=User.getBoolean(Constants.WEEKLY_NEWSLATER, false);;
		boolean direct_msz_mail=User.getBoolean(Constants.DIRECT_MSZ_TOMAIL, false);;
		boolean allow_direct_msz=User.getBoolean(Constants.MEMBERALLOW_TO_SEND_MSZ, false);;
		boolean stop_push_new_message=User.getBoolean(Constants.STOP_PUSH_MESSAGE, false);;
		String administrator =User.getString(Constants.ACCESS_ADMINTRATOR, "");;
		String name=User.getString(Constants.NAME, "");
		String youtube=User.getString(Constants.YOUTUBE, "");
		String telephone=User.getString(Constants.TELEPHONE, "");
		String brief=User.getString(Constants.BRIEF, "");
		String city=User.getString(Constants.CITY, "");
		String country=User.getString(Constants.COUNTRY, "");
		String id=User.getString(Constants.ID, "");
		String twitter=User.getString(Constants.TWITTER, "");
		String username=User.getString(Constants.USER_NAME, "");
		String email=User.getString(Constants.EMAIL, "");
		String description=User.getString(Constants.DESCRIPTION, "");
		String session_id=User.getString(Constants.SESSION_ID, "");
		String instagram=User.getString(Constants.INSTAGRAM, "");
		String nickname=User.getString(Constants.NICKNAME, "");
		String countrycity_flag=User.getString(Constants.COUNTRYCITY_GLAG, "");
		String is_authorized=User.getString(Constants.IS_AUTHORIZED, "");
		String siteurl=User.getString(Constants.SITEURL, "");
		String password=User.getString(Constants.PASSWORD, "");
		UserCred userCred = new UserCred(system_notification,weekly_newsletter,direct_msz_mail,
				allow_direct_msz,stop_push_new_message,administrator, name, youtube, telephone, 
				brief, city, country, id, twitter, username, email, description, session_id, 
				instagram, nickname, countrycity_flag, is_authorized, siteurl);
		userCred.setPassword(password) ;
		return userCred;
	}
	public boolean isFirstTime(){
		Boolean firstTimeFlag = User.getBoolean(Constants.FIRST_TIME, true);
		return firstTimeFlag;
	}
	public boolean isRememberMe(){
		Boolean rememberMeFlag = User.getBoolean(Constants.REMEMBER_ME, false);
		return rememberMeFlag;
	}
}
