package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemberForSendMessage {
	private String nickname, id, username, stop_privateMessages, avatar;

	/**
	 * @param nickname
	 * @param id
	 * @param username
	 * @param avatar
	 */
	public MemberForSendMessage(String nickname, String id, String username, String avatar) {
		super();
		this.nickname = nickname;
		this.id = id;
		this.username = username;
		this.stop_privateMessages = "";
		this.avatar = avatar;
	}

	public String getNickName() {
		return this.nickname;
	}

	public String getStopPrivateMsz() {
		return this.stop_privateMessages;
	}

	public String getId() {
		return this.id;
	}

	public String getUserName() {
		return this.username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public static ArrayList<MemberForSendMessage> parseMemberList(JSONArray memberListJSONArray) {
		ArrayList<MemberForSendMessage> memList = new ArrayList<MemberForSendMessage>();
		try {
			for (int i = 0; i < memberListJSONArray.length(); i++) {
				JSONObject mj = memberListJSONArray.getJSONObject(i);
				MemberForSendMessage m = new MemberForSendMessage(mj.getString("nickname"), mj.getString("id"),
						mj.getString("username"), mj.getString("avatar"));
				memList.add(m);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return memList;
	}
}
