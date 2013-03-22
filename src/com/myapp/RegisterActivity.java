package com.myapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mongodb.MongoException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends Activity {
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }
    
    public void registerListener(View v) {
    	if (v.getId() == R.id.button1) {
    		String username = (((EditText) findViewById(R.id.editText4)).getText()).toString();
    		String email = (((EditText) findViewById(R.id.editText1)).getText()).toString();
    		String passwd1 = (((EditText) findViewById(R.id.editText2)).getText()).toString();
    		String passwd2 = (((EditText) findViewById(R.id.editText3)).getText()).toString();
    		String message = "";
    		if (username.equals("")) {
    			message = "Please input your username.";
    			CommonUtils.showAlertDialog(message, this);
    		} else if (!passwd1.equals(passwd2) || passwd1.equals("")) {
    			message = "Passwords don't match.";
    			CommonUtils.showAlertDialog(message, this);
    		} else {
//    			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//    			postParameters.add(new BasicNameValuePair("username", username));
//    			postParameters.add(new BasicNameValuePair("email", email));
//    			postParameters.add(new BasicNameValuePair("password", passwd1));
//				String result = CustomHttpClient.executeHttpPost("http://teamwiki.phpfogapp.com/register.php", postParameters);
//				if (result.equals("0\n")) {
//					Intent intent = new Intent(this, LoginActivity.class);
//					startActivity(intent);
//					finish();
//					return;
    			byte[] salt = CommonUtils.generateSalt(5);
    			StringBuilder sb = new StringBuilder();
    			for (int i = 0; i < salt.length; i++) {
    				sb.append(Integer.toString(salt[i] & 0xff + 0x100, 16).substring(1));
    			}
    			String saltString = sb.toString();
    			try {
    				byte[] fingerPrintInBytes = CommonUtils.generateFingerPrint(passwd1, saltString);
	    			sb.delete(0,  sb.length() - 1);
	    			for (int i = 0; i < fingerPrintInBytes.length; i++) {
	    				sb.append(Integer.toString(fingerPrintInBytes[i] & 0xff + 0x100, 16).substring(1));
	    			}
	    			String fingerprint = sb.toString();
    				CommonUtils.insertUserCredentials(username, fingerprint, saltString);
    				Intent intent = new Intent(this, LoginActivity.class);
					startActivity(intent);
					finish();
					return;
    			} catch (NoSuchAlgorithmException e) {
    				message = "Error encountered when hashing user credentials.";
    				CommonUtils.showAlertDialog(message, this);
    			}  catch (UnknownHostException e) {
    				message = e.getMessage();
    				CommonUtils.showAlertDialog(message, this);
    			} catch (IOException e) {
    				message = "Error encountered when hashing user credentials.";
    				CommonUtils.showAlertDialog(message, this);
    			} catch (Exception e) {
    				message = "Error encountered when inserting user credentials.";
    				CommonUtils.showAlertDialog(message, this);
    			}
    		}
    		
    	}
    	if (v.getId() == R.id.textView6) {
    		Intent intent = new Intent(this, LoginActivity.class);
    		startActivity(intent);
    		finish();
    	}
    }
    
}