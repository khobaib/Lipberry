package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Categories {
	ArrayList<Categories>arrayList;
	private String id;
	private String prefix;
	private String URL;
	private String name;
	public Categories() {
		// TODO Auto-generated constructor stub
	}
	public Categories( String URL,String name) {
		this.URL=URL;
		this.name=name;
	}
	public static Categories parsecaCategories(JSONObject userObj){
		Categories categories = new Categories();
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		if(userObj != null){
			String jsonString = userObj.toString();
			categories = gson.fromJson(jsonString, Categories.class);
		}
		return categories;
	}
	public String getId() {
		return this.id;
	}
	public String getPrefix() {
		return this.prefix;
	}
	public String getUrl() {
		return this.URL;
	}
	public String getName() {
		return this.name;
	}
}
