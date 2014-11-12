package com.lipberry.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.lipberry.model.ServerResponse;
import com.lipberry.utility.Constants;

public class JsonParser {

	static InputStream          is   = null;
	static JSONObject           jObj = null;
	static String               json = "";

	private static final String TAG  = JsonParser.class.getSimpleName();
	public JsonParser() {

	}

	public ServerResponse retrieveServerData(int reqType, String url, List<NameValuePair> urlParams, String content, String appToken) {
		int status = 0;
		StringBuilder sb = null;
		Constants.debugLog(url, "a  "+content);
		if (urlParams != null) {
			String paramString = URLEncodedUtils.format(urlParams, "utf-8");
			url += "?" + paramString;            
		}
		try {
			URL urltoconnect = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urltoconnect
					.openConnection();
			connection.setRequestProperty("Content-Type","application/json"); 
			connection.setDoOutput(true);
			HttpResponse httpResponse = null;
			if(reqType == Constants.REQUEST_TYPE_GET){
				connection.setRequestMethod("GET");
				connection.connect();
				int statusCode = connection.getResponseCode();
			} else if(reqType == Constants.REQUEST_TYPE_POST){
				connection.setRequestMethod("POST");
				String str =  content;
				byte[] outputInBytes = str.getBytes("UTF-8");
				OutputStream os = connection.getOutputStream();
				os.write( outputInBytes );    
				os.close();
				connection.connect();
				int statusCode = connection.getResponseCode();
			} else if(reqType == Constants.REQUEST_TYPE_PUT){
				connection.setRequestProperty("Content-Type","application/json"); 
				connection.setRequestMethod("POST");
				connection.connect();
				int statusCode = connection.getResponseCode();
			} else if(reqType == Constants.REQUEST_TYPE_DELETE){
				connection.setRequestProperty("Content-Type","application/json"); 
				connection.setRequestMethod("POST");
				connection.connect();
				int statusCode = connection.getResponseCode();
		}
			status = connection.getResponseCode();
			is = connection.getInputStream();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
		}
		ServerResponse response=new ServerResponse(jObj, status);
		Constants.debugLog(url, response.getjObj().toString());
		return response;
	}

}
