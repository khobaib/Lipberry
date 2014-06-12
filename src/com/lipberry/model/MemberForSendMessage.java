package com.lipberry.model;

public class MemberForSendMessage {
	String nickname,id,username,stop_privateMessages;
	public String getNickname(){
		return this.nickname;
		
	}
	public String getStopPrivateMsz(){
		return this.stop_privateMessages;
	}
	public String getId(){
		return this.id;
	}
	public String getUsername(){
		return this.username;
	}
}
