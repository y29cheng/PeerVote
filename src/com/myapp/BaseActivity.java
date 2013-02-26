package com.myapp;

import java.net.UnknownHostException;
import java.security.SecureRandom;

import com.mongodb.DB;
import com.mongodb.MongoClient;


public class BaseActivity {
	private final static String username = "georgeC";
	private final static char[] password = "hungry plant 147".toCharArray();
	static final String votes_baseUrl = "https://api.mongolab.com/api/1/databases/teamwiki/collections/votes?";
	static final String logins_baseUrl = "https://api.mongolab.com/api/1/databases/teamwiki/collections/logins?";
	static final String apiKey = "4eef72abe4b092a677cdf360";
	static MongoClient mongoClient = null;
	
	public static boolean openDBConnection() {
		if (mongoClient == null) {
			try {
				mongoClient = new MongoClient("dbh54.mongolab.com", 27547);
				DB db = mongoClient.getDB("teamwiki");
				db.authenticate(username, password);
				return true;
			} catch (UnknownHostException e) {
				return false;
			}
		}
		return true;
	}
	
	public static void closeDBConnection() {
		if (mongoClient != null) mongoClient.close();
	}
	
	public static byte[] generateSalt(int length) {
		byte[] salt = new byte[length];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		return salt;
	}
	
}
