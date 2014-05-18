package com.lipberry.model;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SingleMember {
	String  id,name,nickname,username,email,password,country,city,fromwhere_uknow,marital_status,
	birth_date,working_woman,beauty_money,tall,weight,avatar,created_at,administrator,blocked,points,
	allow_follow,brief,siteurl,youtube,instagram,telephone,number_of_followers,number_of_following,
	publicpage_visit,already_following;
	public SingleMember(){

	}
	public SingleMember(String  id,String name,String nickname,String username,String email,String password,
			String country,String city,String fromwhere_uknow,String marital_status,String birth_date,
			String working_woman,String beauty_money,String tall,String weight,String avatar,String created_at,
			String administrator,String blocked,String points,String allow_follow,String brief,String siteurl,
			String youtube,String instagram,String telephone){

		this.id=id;
		this.name=name;
		this.nickname=nickname;
		this.username=username;
		this.email=email;
		this.password=password;
		this.country=country;
		this.city=city;
		this.fromwhere_uknow=fromwhere_uknow;
		this.marital_status=marital_status;
		this.birth_date=birth_date;
		this.working_woman=working_woman;
		this.beauty_money=beauty_money;
		this.tall=tall;
		this.weight=weight;
		this.avatar=avatar;
		this.created_at=created_at;
		this.administrator=administrator;
		this.blocked=blocked;
		this.points=points;
		this.allow_follow=allow_follow;
		this.brief=brief;
		this.siteurl=siteurl;
		this.youtube=youtube;
		this.instagram=instagram;
		this.telephone=telephone;


	}

	public static SingleMember parseSingleMember(JSONObject userObj){
		SingleMember singleMember = new SingleMember();
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		if(userObj != null){
			String jsonString = userObj.toString();
			singleMember = gson.fromJson(jsonString, SingleMember.class);
		}



		return singleMember;
	}

	public String getPublicpage_visit(){
		return this.publicpage_visit;
	}

	public String getNumber_of_following(){
		return this.number_of_following;
	}

	public String getNumber_of_followers(){
		return this.number_of_followers;
	}


	public String getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getNickname(){
		return this.nickname;
	}

	public String getUsername(){
		return this.username;
	}

	public String getEmail(){
		return this.email;
	}


	public String getPassword(){
		return this.password;
	}

	public String getCountry(){
		return this.country;
	}

	public String getCity(){
		return this.city;
	}


	public String getFromwhere_uknow(){
		return this.fromwhere_uknow;
	}

	public String getMarital_status(){
		return this.marital_status;
	}
	public String getBirth_date(){
		return this.birth_date;
	}

	public String getWorking_woman(){
		return this.working_woman;
	}

	public String getBeauty_money(){
		return this.beauty_money;
	}

	public String getTall(){
		return this.tall;
	}

	public String getWeight(){
		return this.weight;
	}

	public String getAvatar(){
		return this.avatar;
	}

	public String getCreated_at(){
		return this.created_at;
	}

	public String getAdministrator(){
		return this.administrator;
	}

	public String getBlocked(){
		return this.blocked;
	}

	public String getPoints(){
		return this.points;
	}
	public String getAlready_followin(){
		return this.already_following;
	}

	public String getBrief(){
		return this.brief;
	}
	

	public String getSiteurl(){
		return this.siteurl;
	}

	public String getYoutube(){
		return this.youtube;
	}

	public String getInstagram(){
		return this.instagram;
	}

	public String getTelephone(){
		return this.telephone;
	}

}
