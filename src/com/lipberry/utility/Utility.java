package com.lipberry.utility;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Utility {

	public static int getDeviceHeight(Activity activity){
		 DisplayMetrics displaymetrics = new DisplayMetrics();
		 activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	       int height = displaymetrics.heightPixels;
	      // int width = displaymetrics.widthPixels;
	       return height;
	}
	
	public static int getDeviceWidth(Activity activity){
		 DisplayMetrics displaymetrics = new DisplayMetrics();
		 activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	      // int height = displaymetrics.heightPixels;
	     int width = displaymetrics.widthPixels;
	       return width;
	}
}
