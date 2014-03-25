package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class MemberList {
 private String status;
	private ArrayList<Member>memberstofollow=new ArrayList<Member>();
	
	public MemberList (){
		
	}
	public MemberList (String status,ArrayList<Member>memberstofollow){
		 this.memberstofollow=memberstofollow;
		 this.status=status;
	}
	
	public static  MemberList getMemberlist(JSONObject joObject){
		String res=joObject.toString();
		Gson gson = new Gson();
		MemberList object = gson.fromJson(res, MemberList.class);
		return object;
	}
	
	
	
	
	public ArrayList<Member> getMemberlist(){
		return this.memberstofollow;
	}
	public String getStatus(){
		return this.status;
	}
}
