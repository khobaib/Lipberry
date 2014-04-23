package com.lipberry.utility;

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
		int width = displaymetrics.widthPixels;
		return width;
	}
}
