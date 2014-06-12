package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;

public class Commentslist {
	ArrayList<Comments>comments_list=new ArrayList<Comments>();
	String status;
	public String getStatus(){
		return this.status;
	}
	public ArrayList<Comments> getCommentslist(){
		return this.comments_list;
	}
	public  static Commentslist getCommentsListInstance(JSONObject joObject){
		String res=joObject.toString();
		Gson gson = new Gson();
		Commentslist  commentslist=gson.fromJson(res, Commentslist.class);
		return commentslist;
	}
}
