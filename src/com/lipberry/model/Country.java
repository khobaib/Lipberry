package com.lipberry.model;

public class Country {
	private String id,name;
	public Country(String id,String name){
		this.id=id;
		this.name=name;
	}
	public String getId(){
		return id;
	}
	public String getName(){
		return name;
	}
}
