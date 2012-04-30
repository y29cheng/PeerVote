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

public class LoginActivity extends Activity {
	
	public static String username = null;
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                LoginActivity.this.finish();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}

	public void logInListener(View v) {
		if (v.getId() == R.id.button2) {
			String username = (((EditText) findViewById(R.id.editText5)).getText()).toString();
			String password = (((EditText) findViewById(R.id.editText6)).getText()).toString();
			String message = null;
			if (username.equals("")) {
				message = "Please enter username.";
				showAlertDialog(message);
			} else if (password.equals("")) {
				message = "Please enter password.";
				showAlertDialog(message);
			} else  {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("username", username));
				postParameters.add(new BasicNameValuePair("password", password));
				String result = CustomHttpClient.executeHttpPost("http://teamwiki.phpfogapp.com/login.php", postParameters);
				if (result.equals("1\n")) {
					LoginActivity.username = username;
					Intent intent = new Intent(this, IndexActivity.class);
					startActivity(intent);
					finish();
				} else {
					message = "Log in failed.";
					showAlertDialog(message);
				}
			}
		}		
	}
	
	public void quitListener(View v) {
		if (v.getId() != R.id.textView12) return;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                LoginActivity.this.finish();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
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
