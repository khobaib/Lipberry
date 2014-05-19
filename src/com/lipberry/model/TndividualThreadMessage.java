package com.lipberry.model;

public class TndividualThreadMessage {
	String parent_id="",article_title="",subject="",to_nickname="",child_flag="",from_nickname="",article_url="",read_flag="",message="",id="",
	category_prefix="",article_id="",article_flag="",to_id="",from_id="",created_at="",from_username="",from_avatar="",to_avatar="";
	
	public TndividualThreadMessage(String parent_id,String article_title,String subject,String to_nickname,String child_flag,String from_nickname,
			String article_url,String read_flag,String message,String id,String category_prefix,String article_id,
			String article_flag,String to_id,
			String from_id,String created_at,String from_username,String from_avatar,String to_avatar){
		this.article_title=article_title;
		this.subject=subject;
		this.to_nickname=to_nickname;
		this.child_flag=child_flag;
		this.from_nickname=from_nickname;
		this.article_url=article_url;
		this.read_flag=read_flag;
		this.message=message;
		this.id=id;
		this.category_prefix=category_prefix;
		this.article_id=article_id;
		this.article_flag=article_flag;
		this.to_id=to_id;
		this.from_id=from_id;
		this.created_at=created_at;
		this.from_username=from_username;
		this.from_avatar=from_avatar;
		this.to_avatar=to_avatar;
		this.parent_id=parent_id;
		
	}
	public String getParent_id(){
		return this.parent_id;
		
	}
	public void setParent_flag(String parent_id){
		this.parent_id=parent_id;
		
	}
	
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
