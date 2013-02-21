package com.myapp;

import java.lang.Math;

public class BaseActivity {
	static final String votes_baseUrl = "https://api.mongolab.com/api/1/databases/teamwiki/collections/votes?";
	static final String logins_baseUrl = "https://api.mongolab.com/api/1/databases/teamwiki/collections/logins?";
	static final String apiKey = "4eef72abe4b092a677cdf360";
	
	public static String generateSalt(int length) {
		String salt = "";
		for (int i = 0; i < length; i++) {
			salt += 'a' + (char)(Math.random()*('Z' - 'a' + 1));
		}
		return salt;
	}
	
	public static void main(String[] args) {
		System.out.println(BaseActivity.generateSalt(1));
	}
}
