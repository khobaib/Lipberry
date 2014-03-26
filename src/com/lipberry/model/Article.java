package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class Article {
	private ArrayList<LikeMember>likemember_array;
	private String article_id;
	private String article_title;
	private String article_photo;
	private String article_description;
	private String article_url;
	private String like_count;
	private String comment_count;
	private String like_url;
	private String dislike_url;
	private String comment_url;
	String member_id="";
	String member_photo="";
	String userAlreadylikeThis="";
	String likemember_text="";
	String created_at="";
	
	private String member_username;
	private  String category_name;
	private String article_category_url;
	
	public Article(ArrayList<LikeMember>likedmemberlist,String article_id,String article_photo,String article_url,
			String article_title,String article_description,String like_count,String comment_count,String like_url
			,String dislike_url,String comment_url,String member_id,String member_photo,String userAlreadylikeThis,
			String likemember_text,String created_at){
		this.userAlreadylikeThis=userAlreadylikeThis;
		this.likemember_text=likemember_text;
		this.created_at=created_at;
		this.likemember_array=likedmemberlist;
		this.article_id=article_id;
		this.article_photo=article_photo;
		this.article_url=article_url;
		this.article_title=article_title;
		this.article_description=article_description;
		this.like_count=like_count;
		this.comment_count=comment_count;
		this.like_url=like_url;
		this.dislike_url=dislike_url;
		this.comment_url=comment_url;
		this.member_id=member_id;
		this.member_photo=member_photo;
		//this.category_name=category_name;
	}
	public String getArticle_category_url(){
		return this.article_category_url;
	}
	
	public String getMember_username(){
		return this.member_username;
	}
	
	public String getCategory_name(){
		return this.category_name;
	}
	
	
	
	public String getCreated_at(){
		return this.created_at;
	}
	
	public String getLikemember_text(){
		return this.likemember_text;
	}
	
	public void setUserAlreadylikeThis(String likestring){
	 this.userAlreadylikeThis=likestring;
	}
	
	public String getUserAlreadylikeThis(){
		return this.userAlreadylikeThis;
	}
	
	
	
	public String getMember_id(){
		return this.member_id;
	}
	
	public String getMember_photo(){
		return this.member_photo;
	}
	
	public String getComment_url(){
		return this.comment_url;
	}
	
	
	public String getDislike_url(){
		return this.dislike_url;
	}
	
	public String getLike_url(){
		return this.like_url;
	}
	
	
	public String getComment_count(){
		return this.comment_count;
	}
	
	public String getLike_count(){
		return this.like_count;
	}
	public String getArticle_description(){
		return this.article_description;
	}
	public String getArticle_title(){
		return this.article_title;
	}
	
	public String getArticle_url(){
		return this.article_url;
	}
	
	public String getArticle_photo(){
		return this.article_photo;
	}
	
	public String getArticle_id(){
		return this.article_id;
	}
	
	public ArrayList<LikeMember>getLikedmemberlist(){
		return this.likemember_array;
		
	}
	
}
