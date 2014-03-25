package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class ArticleList {
 private String status;
	private ArrayList<Article>article_list=new ArrayList<Article>();
	
	public ArticleList (){
		
	}
	public ArticleList (String status,ArrayList<Article>article_list){
		 this.article_list=article_list;
		 this.status=status;
	}
	
	public static  ArticleList getArticlelist(JSONObject joObject){
		String res=joObject.toString();
		Gson gson = new Gson();
		ArticleList object = gson.fromJson(res, ArticleList.class);
		return object;
	}
	
	
	

	
	public ArrayList<Article> getArticlelist(){
		return this.article_list;
	}
	public String getStatus(){
		return this.status;
	}
}
