package com.myapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.mongodb.DB;
import com.mongodb.MongoClient;


public class BaseActivity {
	private final static String username = "georgeC";
	private final static char[] password = "hungry plant 147".toCharArray();
	static final String votes_baseUrl = "https://api.mongolab.com/api/1/databases/teamwiki/collections/votes?";
	static final String logins_baseUrl = "https://api.mongolab.com/api/1/databases/teamwiki/collections/logins?";
	static final String apiKey = "4eef72abe4b092a677cdf360";
	static MongoClient mongoClient = null;
	static DB db = null;
	static String SHA = "SHA-256";
	
	public static boolean openDBConnection() {
		if (mongoClient == null) {
			try {
				mongoClient = new MongoClient("dbh54.mongolab.com", 27547);
				db = mongoClient.getDB("teamwiki");
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
	
	public static boolean validatePassword(String passwd, String salt, String fingerprint) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance(SHA);
		byte[] passwdInBytes = passwd.getBytes();
		byte[] saltInBytes = passwd.getBytes();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(passwdInBytes);
		baos.write(saltInBytes);
		byte[] passwdSaltInBytes = baos.toByteArray();
		md.update(passwdSaltInBytes);
		byte[] digest = md.digest();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < digest.length; i++) {
			sb.append((Integer.toString(digest[i] & 0xff + 0x100, 16)).substring(1));
		}
		return fingerprint.equals(sb.toString());
	}
	
	public static void showAlertDialog(String message, Activity act) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {}
		       });
		AlertDialog alert = builder.create();
		alert.show();
    }
	
}
