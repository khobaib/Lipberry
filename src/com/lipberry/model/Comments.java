package com.lipberry.model;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Comments {

	String created_at,member_url,comment_id,member_avatar,member_name;
	public String getCreated_at(){
		return this.created_at;
	}
	
	public String getMember_url(){
		return this.member_url;
	}
	public String getComment_id(){
		return this.comment_id;
	}
	
	public String getMember_avatar(){
		return this.member_avatar;
	}
	public String getMember_name(){
		return this.member_name;
	}
	
	
	public Comments getComments(JSONObject jobj){
		
		Comments comments = new Comments();
        
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        if(jobj != null){
            String jsonString = jobj.toString();
            comments = gson.fromJson(jsonString, Comments.class);
        }
        return comments;
		
	}
	
	
}
