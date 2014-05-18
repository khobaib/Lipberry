package com.lipberry.model;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class UserCred {
	
	private String administrator;
	private String name;
	private String youtube;
	private String telephone;
	private String brief;
	private String city;
	private String country;
	private String id;
	private String twitter;
	private String username="";
	private String email;
	private String description;
	private String session_id;
	private String instagram;
	private String nickname;
	private String countrycity_flag;
	private String is_authorized;
	private String siteurl;
	private String password="";
	private String push_new_msz="0";
	private String weekly_news;
	private String stop_privateMails;
	private String stop_privateMessages;
	private String stop_commentMails;
	private String stop_followerMails;
	private String stop_likeArtMails;

	public UserCred(String push_new_msz,String weekly_news, String stop_privateMails,
			 String stop_privateMessages, String stop_commentMails,String stop_followerMails,String stop_likeArtMails,
			String administrator,String name, String youtube,String telephone,
			String brief, String city, String country, String id,String twitter,
			String username, String email,String description,String session_id,
			String instagram, String nickname,String countrycity_flag,
			String is_authorized,String siteurl) {
		this.push_new_msz=push_new_msz;
		this.stop_likeArtMails=stop_likeArtMails;
		this.weekly_news=weekly_news;
		this.stop_privateMails=stop_privateMails;
		this.stop_privateMessages=stop_privateMessages;
		this.stop_commentMails=stop_commentMails;
		this.stop_followerMails=stop_followerMails;
		this.siteurl=siteurl;
		this.is_authorized=is_authorized;
		this.countrycity_flag=countrycity_flag;
		this.nickname=nickname;
		this.administrator=administrator;
		this.name=name;
		this.youtube=youtube;
		this.telephone=telephone;
		this.brief=brief;
		this.city=city;
		this.country=country;
		this.id=id;
		this.twitter=twitter;
		this.twitter=twitter;
		this.username=username;
		this.email=email;
		this.description=description;
		this.session_id=session_id;
		this.instagram=instagram;

	}
	public String  getPush_new_msz(){
		return this.push_new_msz;
	}
	
	public void  setPush_new_msz(String push_new_msz){
		 this.push_new_msz=push_new_msz;
	}

	
	public void setStop_followerMails(String stop_followerMails) {
		this.stop_followerMails=stop_followerMails;
	}
	public String getStop_followerMails() {
		return stop_followerMails;
	}
	
	public void setStop_likeArtMails(String stop_likeArtMails) {
		this.stop_likeArtMails=stop_likeArtMails;
	}
	public String getStop_likeArtMails() {
		return stop_likeArtMails;
	}
	
	public void setStop_commentMails(String stop_commentMails) {
		this.stop_commentMails=stop_commentMails;
	}
	public String getStop_commentMails() {
		return stop_commentMails;
	}
	
	public void setStop_privateMessages(String stop_privateMessages) {
		this.stop_privateMessages=stop_privateMessages;
	}
	public String getStop_privateMessages() {
		return stop_privateMessages;
	}
	
	
	public void setStop_privateMails(String stop_privateMails) {
		this.stop_privateMails=stop_privateMails;
	}
	public String getStop_privateMails() {
		return stop_privateMails;
	}
	
	
	public void setWeekly_news(String weekly_news) {
		this.weekly_news=weekly_news;
	}
	public String getWeekly_news() {
		return weekly_news;
	}

	
	public UserCred() {
	}
	
	
	public void setCountry(String country){
		this.country=country;
	}
//	public void setCity(String city){
//		this.city=city;
//	}
	public void setCity(String city){
		this.city=city;
	}
	public void setNickname(String nickname){
		this.nickname=nickname;
	}
	public void setSiteUrl(String siteurl){
		this.siteurl=siteurl;
	}
	public void setbBrief(String brief){
		this.brief=brief;
	}

	public void setEmail(String email){
		this.email=email;
	}
	

	
	public static UserCred parseUserCred(JSONObject userObj){
		UserCred userCred = new UserCred();

		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		if(userObj != null){
			String jsonString = userObj.toString();
			userCred = gson.fromJson(jsonString, UserCred.class);
		}
		return userCred;
	}

	public void checknull(){
		if(siteurl==null){
			this.siteurl="";
		}
		if(session_id==null){
			this.session_id="";
		}
		if(countrycity_flag==null){
			this.countrycity_flag="";
		}
		if(twitter==null){
			this.twitter="";
		}

		if(administrator==null){
			this.administrator="";
		}

		if(telephone==null){
			this.telephone="";
		}
		if(youtube==null){
			this.youtube="";
		}

		if(brief==null){
			this.brief="";
		}

	}


	public String getSiteurl() {
		return siteurl;
	}

	public String getIs_authorized() {
		return is_authorized;
	}


	public String getCountrycity_flag() {
		return countrycity_flag;
	}

	public String getNickname() {
		return nickname;
	}

	public String getInstagram() {
		return instagram;
	}
	public String getSession_id() {
		return session_id;
	}

	public String getDescription() {
		return description;
	}


	public String getEmail() {
		return email;
	}

	public String getTwitter() {
		return twitter;
	}

	public String getId() {
		return id;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getBrief() {
		return brief;
	}

	public String getTelephone() {
		return telephone;
	}


	public String getAdministrator() {
		return administrator;
	}


	public String  getName() {
		return name;
	}


	public String getUsername() {
		return username;
	}


	public String getYoutube() {
		return youtube;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public  String getPassword(){
		return password;
	}


}
