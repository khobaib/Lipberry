package com.lipberry;

import java.util.HashMap;

import com.lipberry.html.AppUtil;
import com.lipberry.html.ImageDownloader;
import com.lipberry.html.MyTagHandler;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;


public class ShowHtmlText {
	TextView tvContent;
	 HashMap<String, Drawable> mImageCache;
	 Activity activity;
	 DisplayMetrics metrics;
	public ShowHtmlText(TextView  tvContent, Activity activity){
		this.tvContent=tvContent;
		 mImageCache = new HashMap<String, Drawable>();	
		 this.activity=activity;
		 metrics = new DisplayMetrics();
		 activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
         
	}
	public void updateImages(final boolean downloadImages, final String textHtml) {
        if (textHtml == null) return;
        Log.i("check", "1");
        Spanned spanned = Html.fromHtml(textHtml,
                new Html.ImageGetter() {
                    @SuppressWarnings("unchecked")
					@Override
                    public Drawable getDrawable(final String source) {
                    	  Log.i("check", "1");

                         Drawable drawable = mImageCache.get(source);
                        if (drawable != null) {
                            return drawable;
                        } else if (downloadImages) {
                            new ImageDownloader(new ImageDownloader.ImageDownloadListener() {
                                @Override
                                public void onImageDownloadComplete(byte[] bitmapData) {
                                    try {
										Drawable drawable = new BitmapDrawable(activity.getResources(),
										        BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length));

										drawable = AppUtil.setDownloadedImageMetrices(drawable, metrics, 0.75);
										mImageCache.put(source, drawable);
										updateImages(false, textHtml);
									} catch (Exception e) {
										e.printStackTrace();
									}
                                }

                                @Override
                                public void onImageDownloadFailed(Exception ex) {
                                }
                            }).execute(source);
                        }
                        return null;
                    }
                }, new MyTagHandler());
        tvContent.setText(spanned);
    }
}
