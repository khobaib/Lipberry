package com.lipberry.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.lipberry.html.AppUtil;
import com.lipberry.html.ImageDownloader;
import com.lipberry.html.MyTagHandler;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

public class Utility {
	public static String DEVICE_ID;
	public static final String EXTRA_MESSAGE = "message";
	public static String token;
	// public static final String SENDER_ID = "326205185342";
	 public static final String SENDER_ID ="975975527814";//"631054890646";// 
	 public static final String DISPLAY_MESSAGE_ACTION =
	            "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";
	public static String getFormattedTime(String dateTime) {
		if (dateTime == null)
			return null;

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = dateFormat.parse(dateTime);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			long millis = c.getTimeInMillis();
			if (isToday(millis)) {
				return formatTOdayTime(millis);
			} 
			else if (getDaysago(millis)<3){
				if(getDaysago(millis)==1)return "yesterday";
				return getDaysago(millis)+" days ago";
			}
			
			else {
				return "more than 3 days ago";
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}
	
//	public static String getFormattedTime2(String dateTime) {
//		if (dateTime == null)
//			return null;
//
//		SimpleDateFormat dateFormat = new SimpleDateFormat(
//				"yyyy-MM-dd HH:mm:ss");
//		Date date = new Date();
//		try {
//			date = dateFormat.parse(dateTime);
//			int a=date.getDate();
////			Calendar c = Calendar.getInstance();
////			c.setTime(date);
////			long millis = c.getTimeInMillis();
////			if (isToday(millis)) {
////				return formatTOdayTime(millis);
////			} 
////			else if (getDaysago(millis)<3){
////				if(getDaysago(millis)==1)return "yesterday";
////				return getDaysago(millis)+" days ago";
////			}
////			
////			else {
////				return "more than 3 days ago";
////			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
	public static int getDaysago(long millis) {
		Calendar today = Calendar.getInstance();
		today = clearTimes(today);
		
		float daysago=((today.getTimeInMillis()-millis)/(24*60*60*1000));
		int dayagoinint=(int) daysago;
		if(daysago>(float)dayagoinint){
			dayagoinint++;
		}

		return dayagoinint;
	}
	public static String formatTOdayTime(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a");
		dateFormat.setCalendar(cal);
		return dateFormat.format(cal.getTime());
	}
	public static Boolean isToday(long millis) {
		Calendar today = Calendar.getInstance();
		today = clearTimes(today);

		if (millis > today.getTimeInMillis())
			return true;
		return false;
	}
	
	private static Calendar clearTimes(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	
    public static Typeface getTypeface1(Activity activity){
    	Typeface tp = Typeface.createFromAsset(activity.getAssets(), "InsanBold.ttf");
    	return tp;
    }
    
    public static Typeface getTypeface2(Activity activity){
    	Typeface tp = Typeface.createFromAsset(activity.getAssets(), "InsanRegular.ttf");
    	return tp;
    }
    public static Typeface getTypeface3(Activity activity){
    	Typeface tp = Typeface.createFromAsset(activity.getAssets(), "InsanThin.ttf");
    	return tp;
    }
	public static int getDeviceHeight(Activity activity){
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		return height;
	}

	public static int getDeviceWidth(Activity activity){
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		Log.e("test", ""+displaymetrics.density+" "+displaymetrics.densityDpi+"  "+displaymetrics.scaledDensity);
		int width = displaymetrics.widthPixels;
		return width;
	}
	
	public static float getDpi(Activity activity){
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.density;
	}

}
