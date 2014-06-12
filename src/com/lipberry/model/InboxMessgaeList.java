package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;

public class InboxMessgaeList {
	ArrayList<InboxMessage>inbox_list=new ArrayList<InboxMessage>();
	String status="";
	public static InboxMessgaeList getMessageList(JSONObject joObject){
		String res=joObject.toString();
		Gson gson = new Gson();
		InboxMessgaeList object = gson.fromJson(res, InboxMessgaeList.class);
		return object;
	}
	
	public String getStatus(){
		return this.status;
	}
	public ArrayList<InboxMessage> getinboxlist(){
		return this.inbox_list;
	}
}
