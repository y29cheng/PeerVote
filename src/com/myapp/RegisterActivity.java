package com.myapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
    		} else if (!email.matches("(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*")) {
    			message = "Please provide your valid email.";
    		} else if (!passwd1.equals(passwd2) || passwd1.equals("")) {
    			message = "Passwords don't match.";
    		} else {
    			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    			postParameters.add(new BasicNameValuePair("username", username));
    			postParameters.add(new BasicNameValuePair("email", email));
    			postParameters.add(new BasicNameValuePair("password", passwd1));
				String result = CustomHttpClient.executeHttpPost("http://teamwiki.phpfogapp.com/register.php", postParameters);
				if (result.equals("0\n")) {
					Intent intent = new Intent(this, LoginActivity.class);
					startActivity(intent);
					finish();
					return;
				} else if (result.equals("1\n")) {
					message = "Account exists.";
				} else if (result.equals("2\n")) {
					message = "Register error";
				} else {
					message = "Application has encountered a problem.";
				}
    		}
    		showAlertDialog(message);
    	}
    	if (v.getId() == R.id.textView6) {
    		Intent intent = new Intent(this, LoginActivity.class);
    		startActivity(intent);
    		finish();
    	}
    }
    
    private void showAlertDialog(String message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {}
		       });
		AlertDialog alert = builder.create();
		alert.show();
    }
}