package com.lipberry.model;

public class InboxMessage {
	String message,from_id,subject,message_url,message_id,thread_count,from_username,from_nickname,read_flag;
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
}
