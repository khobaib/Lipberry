package com.lipberry.model;

public class LikeMember {
	private String foreign_key_article_id;
	private String likemember_nickname;
	private String likemember_url;
	private String likemember_id;
	public LikeMember(String likemember_nickname,String likemember_url,String foreign_key_article_id){
		this.likemember_nickname=likemember_nickname;
		this.likemember_url=likemember_url;
		this.foreign_key_article_id=foreign_key_article_id;
	}
	public LikeMember(String likemember_nickname,String likemember_url,
			String foreign_key_article_id,String likemember_id){
		this.likemember_nickname=likemember_nickname;
		this.likemember_url=likemember_url;
		this.foreign_key_article_id=foreign_key_article_id;
		this.likemember_id=likemember_id;
	}
	public String  getLikemember_id(){
		return this.likemember_id;
	}
	public void setForeign_key_article_id(String foreign_key_article_id){
		this.foreign_key_article_id=foreign_key_article_id;
	}
	public LikeMember(String likemember_nickname,String likemember_url){
		this.likemember_nickname=likemember_nickname;
		this.likemember_url=likemember_url;
	}
	public String getForeign_key_article_id(){
		return this.foreign_key_article_id;
	}
	public String getNickname(){
		return likemember_nickname;
	}
	public String getMemberUrl(){
		return this.likemember_url;
	}
}
