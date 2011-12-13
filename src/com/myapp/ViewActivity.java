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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewActivity extends Activity {
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, IndexActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);
		try {
			JSONArray votes = IndexActivity.json.getJSONArray("votes");
			final JSONObject vote = votes.getJSONObject(IndexActivity.position);
			TextView tv = (TextView) findViewById(R.id.textView17);
			tv.setText(vote.getString("title"));
			final String index = vote.getString("id");
			String[] options = new String[] {vote.getString("choice1"), vote.getString("choice2"), vote.getString("choice3"), vote.getString("choice4")};
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewActivity.this, android.R.layout.simple_list_item_single_choice, options);
			ListView lv = (ListView) findViewById(R.id.listView2);
			lv.setAdapter(adapter);
			lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					final int choice = position + 1;
					AlertDialog.Builder builder = new AlertDialog.Builder(ViewActivity.this);
					builder.setMessage("Are you sure?")
					       .setCancelable(false)
					       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					    	   public void onClick(DialogInterface dialog, int id) {
					    		   dialog.cancel();
					    		   try {
						    		   String owner = vote.getString("owner");
						    		   if (LoginActivity.username.equals(owner)) {
						    			   showAlertDialog("You created this vote.");
						    			   return;
						    		   }
					    		   } catch (Exception e) {
					        		   Log.e("log_tag", "Error parsing data " + e.toString());
					        	   }
					    		   ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
					    		   postParameters.add(new BasicNameValuePair("index", index));
					    		   postParameters.add(new BasicNameValuePair("choice", ((Integer) choice).toString()));
					    		   postParameters.add(new BasicNameValuePair("username", LoginActivity.username));
					    		   String result = CustomHttpClient.executeHttpPost("http://teamwiki.phpfogapp.com/vote.php", postParameters);
					    		   if ("success\n".equals(result)) {
					    			   Intent intent = new Intent(ViewActivity.this, IndexActivity.class);
					    			   startActivity(intent);
					    			   Toast.makeText(getApplicationContext(), "You vote has been submitted.", Toast.LENGTH_SHORT).show();
					    			   finish();
					    		   } else if ("hasVoted\n".equals(result)) {
					    			   showAlertDialog("You have already voted.");
					    		   } else {
					    			   showAlertDialog("Vote failed, try again later.");
					    		   }
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
			});
		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
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
