package com.lipberry.model;

public class Model {
	private String member_avatar;
	private String member_name;
	private String member_url;
	private String comment_id;
	private String created_at;
	private String comment;
	private String likecount;
	private String likecomment_url;
	private String abusecomment_url;
	private String replyon_url;
	public String getReplyon_url(){
		return this.replyon_url;
	}
	public String getAbusecomment_url(){
		return this.abusecomment_url;
	}
	public String getMember_avatar(){
		return this.member_avatar;
	}
	public String getMember_name(){
		return this.member_name;
	}
	public String getMember_url(){
		return this.member_url;
	}
	public String getComment_id(){
		return this.comment_id;
	}
	public String getCreated_at(){
		return this.created_at;
	}
	public String getComment(){
		return this.comment;
	}
	public String getLikecount(){
		return this.likecount;
	}
	public String getLikecomment_url(){
		return this.likecomment_url;
	}
}
