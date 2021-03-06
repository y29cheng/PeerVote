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
			JSONArray votes = IndexActivity.json;
			final JSONObject vote = votes.getJSONObject(IndexActivity.position);
			TextView tv = (TextView) findViewById(R.id.textView17);
			tv.setText(vote.getString("title"));
			JSONObject ID = vote.getJSONObject("_id");
			final String index = ID.getString("$oid");
			String[] options = new String[vote.getInt("choices")];
			for (int i = 0; i < vote.getInt("choices"); i++) {
				options[i] = vote.getString("choice" + (i + 1));
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewActivity.this, R.layout.list2, R.id.textView19, options);
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
						    		   JSONArray voters = vote.getJSONArray("voters");
						    		   for (int i = 0; i < voters.length(); i++) {
						    			   if (voters.get(i).equals(LoginActivity.username)) {
						    				   showAlertDialog("You can only vote once.");
						    				   return;
						    			   }
						    		   }
					    		   } catch (Exception e) {
					        		   showAlertDialog("Error reading json data.");
					        		   return;
					        	   }
					    		   ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
					    		   postParameters.add(new BasicNameValuePair("index", index));
					    		   postParameters.add(new BasicNameValuePair("choice", ((Integer) choice).toString()));
					    		   postParameters.add(new BasicNameValuePair("username", LoginActivity.username));
					    		   
					    		   try {
					    			   postParameters.add(new BasicNameValuePair("expire", vote.getString("expire")));
					    			   postParameters.add(new BasicNameValuePair("time", vote.getString("time")));
					    		   } catch (Exception e) {
					    			   postParameters.add(new BasicNameValuePair("expire", "24"));
					    			   postParameters.add(new BasicNameValuePair("time", "1000000"));
					    		   }
					    		   CustomHttpClient.executeHttpGet(CommonUtils.votes_baseUrl, CommonUtils.apiKey);
//					    		   if ("success\n".equals(result)) {
//					    			   Intent intent = new Intent(ViewActivity.this, IndexActivity.class);
//					    			   startActivity(intent);
//					    			   Toast.makeText(getApplicationContext(), "You vote has been submitted.", Toast.LENGTH_SHORT).show();
//					    			   finish();
//					    		   } else if ("expired\n".equals(result)) {
//					    			   showAlertDialog("Expired, vote not counted.");
//					    		   } else {
//					    			   showAlertDialog("Vote failed, try again later. " + result);
//					    		   }
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
			showAlertDialog("Error reading json data.");
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
