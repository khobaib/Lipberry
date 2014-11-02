
package com.lipberry;



import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;

public class GCMIntentService extends GCMBaseIntentService {

	@SuppressWarnings("hiding")
	private static final String TAG = "GCMIntentService";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	LipberryApplication appInstance;

	Context context;
	public GCMIntentService() {
		super(Utility.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		this.context=context;
		boolean registered = ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			ServerUtilities.unregister(context, registrationId);
		} else {
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "-------------------------------------------- Received message");

		handleMessage(context, intent);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		//   String message = getString(R.string.gcm_deleted, total);
		//        Utility.displayMessage(context, message);
		// notifies user
		//        generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		//        Utility.displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}


	private void handleMessage(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {           
			String receivedMsg = "" + (String) extras.get("message");
			String type=""+(String)extras.get("type");
			appInstance = (LipberryApplication) getApplication();
			String rememberMeFlag = appInstance.getUserCred().getPush_new_msz();
			boolean foregroud=false;
			try {
				 foregroud = new ForegroundCheckTask().execute(context).get();
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			}
			if(rememberMeFlag.equals("0")){
				String from_username=""+(String)extras.get("from_username");
				if(!foregroud){
					if((appInstance.getUserCred().getUsername()==null)||(appInstance.getUserCred().getUsername().equals(""))){
						
					}
					else{
						if(!appInstance.getUserCred().getUsername().equals(from_username)){
							sendNotificationOnlive(receivedMsg,type,foregroud);
						}	
					}
					
				 }
				 else{
					 if((appInstance.getUserCred().getUsername()==null)||(appInstance.getUserCred().getUsername().equals(""))){
							
						}
						else{
							if(!appInstance.getUserCred().getUsername().equals(from_username)){
								sendNotificationOnlive(receivedMsg,type,foregroud);
							}	
						}
				 }

			}
		}
	}
private void sendNotificationOnlive(String msg,String type,boolean foregroud) {
		Intent myIntent;
		int MY_NOTIFICATION_ID=1;
		myIntent = new Intent(this, WebViewActtivity.class);
		myIntent.putExtra("type", type);
		myIntent.putExtra("foregroud", foregroud);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				this, 
				0, 
				myIntent, 
				PendingIntent.FLAG_CANCEL_CURRENT);
		Notification  myNotification = new NotificationCompat.Builder(this)
		.setContentTitle("Lipberry")
		.setContentText(msg)
		.setTicker("Notification!")
		.setWhen(System.currentTimeMillis())
		.setContentIntent(pendingIntent)
		.setDefaults(Notification.DEFAULT_SOUND)
		.setAutoCancel(true)
		.setSmallIcon(R.drawable.ic_launcher)
		.build();
		NotificationManager  notificationManager = 
				(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
	}
	class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {
		@Override
   	  protected Boolean doInBackground(Context... params) {
   	    final Context context = params[0].getApplicationContext();
   	    return isAppOnForeground(context);
   	  }
   	  private boolean isAppOnForeground(Context context) {
   	    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
   	    List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
   	    if (appProcesses == null) {
   	      return false;
   	    }
   	    final String packageName = context.getPackageName();
   	    for (RunningAppProcessInfo appProcess : appProcesses) {
   	      if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
   	        return true;
   	      }
   	    }
   	    return false;
   	  }
   	}



}
