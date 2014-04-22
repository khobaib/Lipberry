package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class ArticleDetails {
	ArrayList<ArticleGallery>article_gallery;
	private String member_username;
	private  String category_title;
	String title="";
	private String invalid_email;
	private String created_at;
	private String video;
	private String comment_count="0";
	private String member;
	private String follow_flag;
	private String member_email;
	private String photo;
	private String category;
	private String short_url;
	private String update_visitcounter_url;
	private String member_name;
	ArrayList<LikeMember>likemember_array;
	private String like_url;
	private String article_gallery_flag;
	private String body;
	private String comment_url;
	private String member_avatar;
	private String visit_counter;
	private String likemember_text;
	private String id;
	private String dislike_url;
	String member_id="";
	String abuse_url="";
	public static  ArticleDetails getArticleDetails(JSONObject joObject){
		String res=joObject.toString();
		Gson gson = new Gson();
		ArticleDetails object = gson.fromJson(res, ArticleDetails.class);
		return object;
	}
	public String getBody(){
		return this.body;
	}

	public String getComment_url(){
		return this.comment_url;
	}
	public ArrayList<ArticleGallery> getArticle_gallery(){
		return this.article_gallery;
	}
	public String getMember_avatar(){
		return this.member_avatar;
	}
	public String getVisit_counter(){
		return this.visit_counter;
	}
	public String getLikemember_text(){
		return this.likemember_text;
	}
	public String getId(){
		return this.id;
	}
	public String getDislike_url(){
		return this.dislike_url;
	}
	public String getMember_id(){
		return this.member_id;
	}
	public String getAbuse_url(){
		return this.abuse_url;
	}
	public String getCategory_title(){
		return this.category_title;
	}
	public String getMember_username(){
		return this.member_username;
	}
	public String getTitle(){
		return this.title;
	}
	public String getInvalid_email(){
		return this.invalid_email;
	}
	public String getTags_array(){
		return null;
	}
	public String getCommentlist_url(){
		return this.comment_count;
	}
	public String getCreated_at(){
		return this.created_at;
	}
	public String getVideo(){
		return this.video;
	}
	public String getComment_count(){
		return this.comment_count;
	}

	public String getMember(){
		return this.member;
	}
	public String getLike_url(){
		return this.like_url;
	}
	public String getArticle_gallery_flag(){
		return this.article_gallery_flag;
	}
	public String getFollow_flag(){
		return this.follow_flag;
	}
	public String getMember_email(){
		return this.member_email;
	}
	public String getPhoto(){
		return this.photo;
	}
	public String getCategory(){
		return this.category;
	}
	public String getShort_url(){
		return this.short_url;
	}
	public String getUpdate_visitcounter_url(){
		return this.update_visitcounter_url;
	}
	public String getMember_name(){
		return this.member_name;
	}
	public ArrayList<LikeMember>getLikedmemberlist(){
		return this.likemember_array;
	}

}
