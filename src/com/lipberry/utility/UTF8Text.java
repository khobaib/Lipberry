/***
 * Copyright 2014 - Wave2Find Inc. All rights reserved.
 **/
package com.lipberry.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * To encode UTF-8 Strings of non-ASCI languages.
 * @author alan
 */
public class UTF8Text {

    private String s;
    
    private UTF8Text(){}
    
    public UTF8Text(String s,boolean encript) {
    	if(encript){
    		 try {
    	            if(s==null){
    	                this.s=null;
    	                return;
    	            }
    	            this.s = Base64.encodeBytes(s.getBytes("UTF-8"), Base64.NO_OPTIONS);
    	        } catch (UnsupportedEncodingException ex) {
    	            this.s = s;
    	        } catch (IOException e) {
    	            this.s = s;
    	        } 
    	}
    	else{
    		this.s=s;
    	}
       
    }
    
    
    public String getStringencripted(){
    	return this.s;
    }
    
    public String getText(){
        System.out.println("s = " + s);
        try {
            if(s==null){return "??";}
            String s1 = new String(Base64.decode(s, Base64.NO_OPTIONS), "UTF-8");
            System.out.println("s1 = " + s1);
            return new String(Base64.decode(s, Base64.NO_OPTIONS), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return s;
        } catch (IOException e) {
            return s;
        }          
    }
    
}
