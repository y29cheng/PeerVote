package com.myapp;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

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
				CommonUtils.showAlertDialog(message, this);
			} else if (password.equals("")) {
				message = "Please enter password.";
				CommonUtils.showAlertDialog(message, this);
			} else  {
//				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//				postParameters.add(new BasicNameValuePair("username", username));
//				postParameters.add(new BasicNameValuePair("password", password));
//				String result = CustomHttpClient.executeHttpPost("http://teamwiki.phpfogapp.com/login.php", postParameters);
//				if (result.equals("1\n")) {
//					LoginActivity.username = username;
//					Intent intent = new Intent(this, IndexActivity.class);
//					startActivity(intent);
//					finish();
//				} else {
//					message = "Log in failed.";
//					showAlertDialog(message);
//				}
				try {
					CommonUtils.openDBConnection();
					DBCollection coll = CommonUtils.db.getCollection("logins");
					BasicDBObject query = new BasicDBObject("username", username);
					DBObject obj = coll.findOne(query);
					String fingerprint = obj.get("fingerprint").toString();
					String salt = obj.get("salt").toString();
					try {
						if (CommonUtils.validatePassword(password, salt, fingerprint)) {
							LoginActivity.username = username;
							Intent intent = new Intent(this, IndexActivity.class);
							startActivity(intent);
							finish();
						} else {
							message = "Invalid username or password.";
							CommonUtils.showAlertDialog(message, this);
						}
					} catch (Exception e) {
						message = "Encountered an internal problem.";
						CommonUtils.showAlertDialog(message, this);
					}
					
					CommonUtils.closeDBConnection();
					
				} catch (UnknownHostException e) {
					message = "Can not establish connection to remote host.";
					CommonUtils.showAlertDialog(message, this);
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
	
    
    
}
