package com.lipberry.model;

public class InboxMessage {
	String message="",from_id="",subject="",message_url="",message_id="",thread_count="",from_username="",from_nickname="",
			read_flag="",to_nickname="",article_title="",child_flag="",from_avatar,to_avatar="",
	article_url="",category_prefix="",article_id="",article_flag="",to_id="",created_at="";
	
	public InboxMessage(String message,String from_id,String subject,String message_url,String message_id,String thread_count,
			String from_username,String from_nickname,String read_flag,String to_nickname,String article_title,String child_flag,
			String from_avatar,String to_avatar,String article_url,String category_prefix,String article_id,String article_flag,
			String to_id,String created_at){
		this.message=message;
		this.from_id=from_id;
		this.subject=subject;
		this.message_url=message_url;
		this.message_id=message_id;
		this.thread_count=thread_count;
		this.from_username=from_username;
		this.from_nickname=from_nickname;
		this.read_flag=read_flag;
		this.to_nickname=to_nickname;
		this.article_title=article_title;
		this.child_flag=child_flag;
		this.from_avatar=from_avatar;
		this.to_avatar=to_avatar;
		this.article_url=article_url;
		this.category_prefix=category_prefix;
		this.article_id=article_id;
		this.article_flag=article_flag;
		this.to_id=to_id;
		this.created_at=created_at;
		
	}
	public String getTo_avatar(){
		return this.to_avatar;
	}
	public String getFrom_avatar(){
		return this.from_avatar;
	}
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
	public String getArticle_Title(){
		return this.article_title;
	}

	
}
