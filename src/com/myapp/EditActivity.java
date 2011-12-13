package com.myapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends Activity {

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(EditActivity.this, IndexActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		try {
			JSONArray votes = IndexActivity.json.getJSONArray("votes");
 		   	final JSONObject vote = votes.getJSONObject(IndexActivity.position);
 		   	final EditText title = (EditText) findViewById(R.id.editText7);
 		   	final EditText choice1 = (EditText) findViewById(R.id.editText8);
 		   	final EditText choice2 = (EditText) findViewById(R.id.editText9);
 		   	final EditText choice3 = (EditText) findViewById(R.id.editText10);
 		   	final EditText choice4 = (EditText) findViewById(R.id.editText11);
 		   	title.setText(vote.getString("title"));
 		   	choice1.setText(vote.getString("choice1"));
 		   	choice2.setText(vote.getString("choice2"));
 		   	choice3.setText(vote.getString("choice3"));
 		   	choice4.setText(vote.getString("choice4"));
 		   	Button submit = (Button) findViewById(R.id.button3);
 		   	submit.setOnClickListener(new OnClickListener() {
 		   		public void onClick(View v) {
 		   			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
 		   			postParameters.add(new BasicNameValuePair("owner", LoginActivity.username));
 		   			postParameters.add(new BasicNameValuePair("title", title.getText().toString()));
 		   			postParameters.add(new BasicNameValuePair("choice1", choice1.getText().toString()));
 		   			postParameters.add(new BasicNameValuePair("choice2", choice2.getText().toString()));
 		   			postParameters.add(new BasicNameValuePair("choice3", choice3.getText().toString()));
 		   			postParameters.add(new BasicNameValuePair("choice4", choice4.getText().toString()));
 		   			String result = CustomHttpClient.executeHttpPost("http://teamwiki.phpfogapp.com/edit.php", postParameters);
 		   			if ("1\n".equals(result)) {
 		   				Intent intent = new Intent(EditActivity.this, IndexActivity.class);
 		   				startActivity(intent);
 		   				Toast.makeText(getApplicationContext(), "You vote has been editted", Toast.LENGTH_SHORT).show();
 		   				finish();
 		   			} else {
 		   				showAlertDialog(result);
 		   			}
 				}
 			});
		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data" + e.toString());
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
