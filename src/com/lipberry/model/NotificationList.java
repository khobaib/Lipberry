package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;

public class NotificationList {
	private String status;
	private ArrayList<Notifications>notifications_list=new ArrayList<Notifications>();
	
	public static  NotificationList getNotificationList(JSONObject joObject){
		String res=joObject.toString();
		Gson gson = new Gson();
		NotificationList object = gson.fromJson(res, NotificationList.class);
		return object;
	}
	public ArrayList<Notifications> getnotificationslist(){
		return this.notifications_list;
	}
	

}
