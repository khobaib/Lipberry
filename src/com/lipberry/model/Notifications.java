package com.lipberry.model;

public class Notifications {
	private String message,to_id,from_name,from_id,subject,created_at,from_avatar,from_nickname,from_username,read_flag,article_id,article_section;
	int interaction_types;
	public String getArticle_section(){
		return this.article_section;
	}
	public String getArticle_id(){
		return this.article_id;
	}
	public int getInteraction_types(){
		return this.interaction_types;
	}
	public String getMessage(){
		return this.message;
	}
	public String getTo_id(){
		return this.to_id;
	}
	public String getFrom_name(){
		return this.from_name;
	}
	public String getFrom_id(){
		return this.from_id;
	}
	
	public String getSubject(){
		return this.subject;
	}
	
	public String getCreated_at(){
		return this.created_at;
	}
	
	public String getFrom_avatar(){
		return this.from_avatar;
	}
	public String getFrom_nickname(){
		return this.from_nickname;
	}
	public String getFrom_username(){
		return this.from_username;
	}
	public String geTread_flag(){
		return this.read_flag;
	}
	


}
