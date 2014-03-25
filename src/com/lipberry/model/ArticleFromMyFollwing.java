package com.lipberry.model;

import java.util.ArrayList;

public class ArticleFromMyFollwing {

	private String status;
	private String message;
	ArrayList<Article> articlelist;
	
	public ArticleFromMyFollwing(String status,String message){
		this.status=status;
		this.message=message;
				
	}
	
	public ArticleFromMyFollwing(ArrayList<Article> articlelist,String status,String message){
		this.status=status;
		this.message=message;
		this.articlelist=articlelist;
				
	}
	
	public String getMessage(){
		
		return this.message;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public  ArrayList<Article> getArticlelist(){
		return this.articlelist;
	}
	
	
}
