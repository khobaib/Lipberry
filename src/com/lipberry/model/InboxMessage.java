package com.lipberry.model;

public class InboxMessage {
	String message,from_id,subject,message_url,message_id,thread_count,from_username,from_nickname,read_flag,to_nickname,article_title,child_flag,
	article_url,category_prefix,article_id,article_flag,to_id,created_at;
	public String getMessage(){
		return this.message;
	}
	public String getFrom_id(){
		return this.from_id;
	}
	public String getSubject(){
		return this.subject;
	}
	public String getMessage_url(){
		return this.message_url;
	}
	public String getMessage_id(){
		return this.message_id;
	}
	public String getThread_count(){
		return this.thread_count;
	}
	public String getFrom_username(){
		return this.from_username;
	}
	public String getFrom_nickname(){
		return this.from_nickname;
	}
	public String getRead_flag(){
		return this.read_flag;
	}
	public String getTo_nickname(){
		return this.to_nickname;
	}
	public void setTo_nickname(String to_nickname){
		this.to_nickname=to_nickname;
	}
	public String getChild_flag(){
		return this.child_flag;
	}
	public String getArticle_url(){
		return this.article_url;
	}
	public String getCategory_prefix(){
		return this.category_prefix;
	}
	public String getArticle_id(){
		return this.article_id;
	}
	public String getArticle_flag(){
		return this.article_flag;
	}
	public String getTo_id(){
		return this.to_id;
	}
	public String getCreated_at(){
		return this.created_at;
	}

	
}
