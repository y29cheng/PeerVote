package com.myapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Entity;
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
	
	public static boolean executeHttpPost(String baseUrl, String apiKey, ArrayList<NameValuePair> postParameters) throws Exception {
		int statusCode;
		String result = "";
		InputStream is = null;
		HttpClient client = getHttpClient();  
		HttpPost request = new HttpPost(baseUrl + "apiKey=" + apiKey);  
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);  
		request.setEntity(formEntity);  
		HttpResponse response = client.execute(request);
		statusCode = response.getStatusLine().getStatusCode();
		return statusCode == 200 ? true : false;
	}
	
	public static boolean executeHttpPut(String baseUrl, String apiKey, JSONObject query, String jsonData) throws Exception {
		int statusCode;
		HttpClient client = getHttpClient();
		String url = baseUrl + "q={";
		JSONArray keys = query.names();
		for (int idx = 0; idx < keys.length(); idx++) {
			String key = keys.get(idx).toString();
			String value = query.get(key).toString();
			url += idx > 0 ? "\"" + key + "\": " + value + "," : "\"" + key + "\": " + value;
		}
		url += "}" + "&apiKey=" + apiKey;
		HttpPut httpput = new HttpPut(url);
		httpput.addHeader("Accept", "application/json");
		httpput.addHeader("Content-Type", "application/json");
		StringEntity entity = new StringEntity(jsonData, "UTF-8");
		entity.setContentType("application/json");
		httpput.setEntity(entity);
		HttpResponse response = client.execute(httpput);
		statusCode = response.getStatusLine().getStatusCode();
		return statusCode == 200 ? true : false;
	}
	
	public static JSONArray executeHttpGet(String baseUrl, String apiKey) {
		InputStream is = null;
		String result = "";
		JSONArray jArray = null;
		try {
			HttpClient client = getHttpClient();
			HttpGet httpget = new HttpGet(baseUrl + "apiKey=" + apiKey);
			HttpResponse response = client.execute(httpget);
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
			jArray = new JSONArray(result);
		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data" + e.toString());
		}
		return jArray;
	}
	
//	public static JSONArray getJSONfromURL() {
//		InputStream is = null;
//		String result = "";
//		JSONArray jArray = null; 
//		try {
//			HttpClient client = getHttpClient();
//			HttpPost request = new HttpPost(baseUrl + "apiKey=" + apiKey);
//			HttpResponse response = client.execute(request);
//			HttpEntity entity = response.getEntity();
//			is = entity.getContent();
//		} catch (Exception e) {
//			Log.e("log_tag", "Error in http connection" + e.toString());
//		}
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				if (line.equals("")) continue;
//				sb.append(line + "\n");
//			}
//			reader.close();
//			result = sb.toString();
//		} catch (Exception e) {
//			Log.e("log_tag", "Error converting result" + e.toString());
//		}
//		try {
//			jArray = new JSONArray(result);
//		} catch (Exception e) {
//			Log.e("log_tag", "Error parsing data" + e.toString());
//		}
//		return jArray;
//	}
}
