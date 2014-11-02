package com.lipberry.html;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

public class AppUtil {
	public static boolean hasInternet(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	
	 public static Drawable setDownloadedImageMetrices(Drawable drawable,
			   DisplayMetrics metrics, double densityRatio) {
		 		int width, height;
			  int originalWidthScaled = (int) (drawable.getIntrinsicWidth()* metrics.density * densityRatio);
			  int originalHeightScaled = (int) (drawable.getIntrinsicHeight()* metrics.density * densityRatio);
			  if (originalWidthScaled > metrics.widthPixels) {
			   height = drawable.getIntrinsicHeight() * metrics.widthPixels/ drawable.getIntrinsicWidth();
			   width = metrics.widthPixels;
			  } else {
			   height = originalHeightScaled;
			   width = originalWidthScaled;
			  }
			  try {
			   drawable.setBounds(0, 0, width, height);
			  } catch (Exception ex) {
			  }

			  return drawable;
			 }
		
}
