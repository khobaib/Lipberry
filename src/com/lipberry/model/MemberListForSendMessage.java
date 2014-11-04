package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;

public class MemberListForSendMessage {
	private String status;
	private ArrayList<MemberForSendMessage> member_list = new ArrayList<MemberForSendMessage>();

	public MemberListForSendMessage(String status, ArrayList<MemberForSendMessage> memList) {
		this.status = status;
		this.member_list = memList;
	}

	public MemberListForSendMessage() {
		this.status = "";
		this.member_list = new ArrayList<MemberForSendMessage>();
	}

	public static MemberListForSendMessage getMemberlist(JSONObject joObject) {
		String res = joObject.toString();
		Gson gson = new Gson();
		MemberListForSendMessage object = gson.fromJson(res, MemberListForSendMessage.class);
		return object;
	}

	public ArrayList<MemberForSendMessage> getMemberlistForSendMessage() {
		return this.member_list;
	}

	public String getStatus() {
		return this.status;
	}
}
