package com.lipberry.model;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Comments {

	String created_at,member_url,comment_id,member_avatar,member_name,
			likecount,abusecomment_url,likecomment_url,replyon_url,comment;
	
	
	public String getComment(){
		return this.comment;
	}
	public String getReplyon_url(){
		return this.replyon_url;
	}
	public String getLikecomment_url(){
		return this.likecomment_url;
	}
	public String getAbusecomment_url(){
		return this.abusecomment_url;
	}
	
	public String getLikecount(){
		return this.likecount;
	}
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
