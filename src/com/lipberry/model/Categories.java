package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Categories {
	
/*03-22 16:09:48.290: D/rtes(29194): {
 * 		"status":"success",
 * 		"categorylist":[
 * 			{
 * 				"URL":"http:\/\/www.lipberry.com\/API\/category\/beauty\/1\/",
 * 				"name":"مكياج"},
 * 			{"URL":"http:\/\/www.lipberry.com\/API\/category\/beauty\/5\/","name":"أزياء و موضة"},
 * 			{"URL":"http:\/\/www.lipberry.com\/API\/category\/beauty\/2\/","name":"البشرة"},
 * 			{"URL":"http:\/\/www.lipberry.com\/API\/category\/beauty\/3\/","name":"الشعر"},
 * 			{"URL":"http:\/\/www.lipberry.com\/API\/category\/beauty\/8\/","name":"الغذاء الصحي والرشاقة"},
 * 			{"URL":"http:\/\/www.lipberry.com\/API\/category\/shexp\/2\/","name":"هل لديك سؤال للمجربات؟"}
 *]}
*/
	ArrayList<Categories>arrayList;
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
    
    

  /*  public static ArrayList<Categories> parsecaCategoriesList(JSONArray userObj){
    	Categories categories = new Categories();
        ArrayList<Categories>categorieslist=new ArrayList<Categories>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        if(userObj != null){
            String jsonString = userObj.toString();
            categorieslist = gson.fromJson(jsonString, Categories.class);
        }
       
        
        return categories;
    } */
  
    public String getUrl() {
        return this.URL;
    }
    
    
    public String getName() {
        return name;
    }
    

    
   
   
   
}
