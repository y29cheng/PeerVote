package com.myapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.util.Log;

public class CustomHttpClient {
	public static final int HTTP_TIMEOUT = 10000;
	private static HttpClient mHttpClient; 
	
	private static HttpClient getHttpClient() {  
		if (mHttpClient == null) {  
			mHttpClient = new DefaultHttpClient();  
			final HttpParams params = mHttpClient.getParams();  
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);  
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);  
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);  
		}  
		return mHttpClient;  
	}
	
	public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) {
		String result = "";
		InputStream is = null;
		try {
			HttpClient client = getHttpClient();  
			HttpPost request = new HttpPost(url);  
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);  
			request.setEntity(formEntity);  
			HttpResponse response = client.execute(request);
			is = response.getEntity().getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.equals("")) continue;
				sb.append(line + "\n");
			}
			reader.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error reading lines" + e.toString());
		}
		return result;
	}
	
	public static JSONObject getJSONfromURL(String url) {
		InputStream is = null;
		String result = "";
		JSONObject jArray = null; 
		try {
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(url);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.equals("")) continue;
				sb.append(line + "\n");
			}
			reader.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result" + e.toString());
		}
		try {
			jArray = new JSONObject(result);
		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data" + e.toString());
		}
		return jArray;
	}
}
