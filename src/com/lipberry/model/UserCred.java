package com.lipberry.model;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class UserCred {
	private boolean system_notification=false;
	private boolean weekly_newsletter=false;
	private boolean direct_msz_mail=false;
	private boolean allow_direct_msz=false;
	private boolean stop_push_new_message=false;

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
	public UserCred() {
	}

	public void setStop_push_new_message(boolean stop_push_new_message) {
		this.stop_push_new_message=stop_push_new_message;
	}

	public boolean getStop_push_new_message() {
		return stop_push_new_message;
	}

	public void setAllow_direct_msz(boolean allow_direct_msz) {
		this.allow_direct_msz=allow_direct_msz;
	}

	public boolean getAllow_direct_msz() {
		return allow_direct_msz;
	}

	public void setDirect_msz_mail(boolean direct_msz_mail) {
		this.direct_msz_mail=direct_msz_mail;
	}

	public boolean getDirect_msz_mail() {
		return direct_msz_mail;
	}

	public void setWeekly_newsletter(boolean weekly_newsletter) {
		this.weekly_newsletter=weekly_newsletter;
	}

	public boolean getWeekly_newsletter() {
		return weekly_newsletter;
	}



	public void setSystem_notification(boolean system_notification) {
		this.system_notification=system_notification;
	}

	public boolean getSystem_notification() {
		return system_notification;
	}

	public UserCred( boolean system_notification,boolean weekly_newsletter,
			boolean direct_msz_mail, boolean allow_direct_msz,boolean stop_push_new_message,
			String administrator,String name, String youtube,String telephone,
			String brief, String city, String country, String id,String twitter,
			String username, String email,String description,String session_id,
			String instagram, String nickname,String countrycity_flag,
			String is_authorized,String siteurl) {
		this.system_notification=system_notification;
		this.weekly_newsletter=weekly_newsletter;
		this.direct_msz_mail=direct_msz_mail;
		this.allow_direct_msz=allow_direct_msz;
		this.stop_push_new_message=stop_push_new_message;
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
