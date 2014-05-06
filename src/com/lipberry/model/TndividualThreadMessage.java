package com.lipberry.model;

public class TndividualThreadMessage {
	String article_title,subject,to_nickname,child_flag,from_nickname,article_url,read_flag,message,id,
	category_prefix,article_id,article_flag,to_id,from_id,created_at,from_username,from_avatar,to_avatar;
	
	public String  getTo_avatar(){
		return this.to_avatar;
	}
	public String  getFrom_avatar(){
		return this.from_avatar;
	}
	public String  getFrom_username(){
		return this.from_username;
	}
	public String  getCreated_at(){
		return this.created_at;
	}
	public String  getFrom_id(){
		return this.from_id;
	}
	public String  getTo_id(){
		return this.to_id;
	}
	public String  getArticle_flag(){
		return this.article_flag;
	}
	public String  getArticle_id(){
		return this.article_id;
	}
	public String  getCategory_prefix(){
		return this.category_prefix;
	}
	public String  getId(){
		return this.id;
	}
	public String  getMessage(){
		return this.message;
	}
	public String  getRead_flag(){
		return this.read_flag;
	}
	public String  getArticle_url(){
		return this.article_url;
	}
	public String  getFrom_nickname(){
		return this.from_nickname;
	}
	public String  getChild_flag(){
		return this.child_flag;
	}
	public String  getTo_nickname(){
		return this.to_nickname;
	}
	public String  getSubject(){
		return this.subject;
	}
	
	public String  getArticle_title(){
		return this.article_title;
	}
	public void setTo_nickname(String to_nickname){
		this.to_nickname=to_nickname;
	}
	

}
