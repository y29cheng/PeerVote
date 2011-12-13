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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity {

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(AddActivity.this, IndexActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		final EditText title = (EditText) findViewById(R.id.editText12);
		final EditText choice1 = (EditText) findViewById(R.id.editText13);
		final EditText choice2 = (EditText) findViewById(R.id.editText14);
		final EditText choice3 = (EditText) findViewById(R.id.editText15);
		final EditText choice4 = (EditText) findViewById(R.id.editText16);
		Button submit = (Button) findViewById(R.id.button4);
		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String title_s = title.getText().toString();
				String choice1_s = choice1.getText().toString();
				String choice2_s = choice2.getText().toString();
				String choice3_s = choice3.getText().toString();
				String choice4_s = choice4.getText().toString();
				if ("".equals(title_s) || "".equals(choice1_s) || "".equals(choice2_s) || "".equals(choice3_s) || "".equals(choice4_s)) {
					showAlertDialog("You can't leave empty fields.");
					return;
				}
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("owner", LoginActivity.username));
		   		postParameters.add(new BasicNameValuePair("title", title_s));
		   		postParameters.add(new BasicNameValuePair("choice1", choice1_s));
		   		postParameters.add(new BasicNameValuePair("choice2", choice2_s));
		   		postParameters.add(new BasicNameValuePair("choice3", choice3_s));
		   		postParameters.add(new BasicNameValuePair("choice4", choice4_s));
		   		String result = CustomHttpClient.executeHttpPost("http://teamwiki.phpfogapp.com/add.php", postParameters);
				if ("1\n".equals(result)) {
					Intent intent = new Intent(AddActivity.this, IndexActivity.class);
					startActivity(intent);
	        		Toast.makeText(getApplicationContext(), "Your vote has been added.", Toast.LENGTH_SHORT).show();
	        		finish();
				} else {
					showAlertDialog(result);
				}
			}
		});
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
