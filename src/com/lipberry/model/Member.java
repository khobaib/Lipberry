package com.lipberry.model;

public class Member {
	
	//member name , photo , bio and follow 
		private String member_url,member_nickname,member_bio,member_photo,member_id;
				public Member(String member_url,String member_nickname,String member_bio,String member_photo,String member_id){
					this.member_url=member_url;
					this.member_nickname=member_nickname;
					this.member_bio=member_bio;
					this.member_photo=member_photo;
					this.member_id=member_id;
				}
				public String getMember_id(){
					return this.member_id;
				}
				public String getMember_photo(){
					return this.member_photo;
				}
				public String getMember_url(){
					return this.member_url;
				}
				
				public String getMember_nickname(){
					return this.member_nickname;
				}
				
				public String getMember_bio(){
					return this.member_bio;
				}
 
}
