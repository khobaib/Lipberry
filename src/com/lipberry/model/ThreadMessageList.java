package com.lipberry.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

public class ThreadMessageList {
		ArrayList<TndividualThreadMessage>inbox_message=new ArrayList<TndividualThreadMessage>();
		public ArrayList<TndividualThreadMessage> getIndividualThreadlist(){
			return this.inbox_message;
		}
		
		public static  ThreadMessageList getList(JSONObject joObject){
			String res=joObject.toString();
			ThreadMessageList object;
			Gson gson = new Gson();
			object = gson.fromJson(res, ThreadMessageList.class);
			try {
				String a=joObject.getString("inbox_message");
				JSONArray jsonArray=new JSONArray(a);
				for(int i=0;i<object.getIndividualThreadlist().size();i++){
					JSONObject childjobject=jsonArray.getJSONObject(i);
					String to_nickname=childjobject.getString("to-nickname");
					object.getIndividualThreadlist().get(i).setTo_nickname(to_nickname);
				}
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object;
		}


}
