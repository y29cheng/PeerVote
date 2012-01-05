package com.myapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {

	static final int CUSTOM_DIALOG_ID = 0;
	static String customText;
	static int index;
	int clickCounter = 0;
	
	ArrayAdapter<String> adapter;
	ArrayList<String> inputs = new ArrayList<String>();
	
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
			JSONArray votes = IndexActivity.json;
 		   	final JSONObject vote = votes.getJSONObject(IndexActivity.position);
 		   	final String id = vote.getJSONObject("_id").getString("$oid");
 		   	final String created = vote.getString("created");
 		   	final int choices = vote.getInt("choices");
 		   	for (int i = 0; i < choices + 1; i++) {
 		   		if (i == 0) {
 		   			inputs.add(vote.getString("title"));
 		   		} else {
 		   			inputs.add(vote.getString("choice"+i));
 		   		}
 		   	}
 		   	adapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_list_item_1, inputs);
 		   	final ListView lv = (ListView) findViewById(R.id.listView4);
 		   	lv.setAdapter(adapter);
 		   	lv.setOnItemLongClickListener(new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					index = position;
					customText = ((TextView) view).getText().toString();
					showDialog(CUSTOM_DIALOG_ID);
					return true;
				}
			});
 		   	Button addRow = (Button) findViewById(R.id.button8);
 			addRow.setOnClickListener(new OnClickListener() {
 				public void onClick(View v) {
 					if (choices + 1 + clickCounter == 0) {
 						inputs.add("enter title here");
 					} else {
 						inputs.add("enter choice" + (choices + 1 + clickCounter) + " here");
 					}
 					adapter.notifyDataSetChanged();
 					clickCounter++;
 				}
 			});
 			Button deleteRow = (Button) findViewById(R.id.button9);
 			deleteRow.setOnClickListener(new OnClickListener() {
 				public void onClick(View v) {
 					if (choices + 1 + clickCounter == 0) return;
 					clickCounter--;
 					inputs.remove(choices + 1 + clickCounter);
 					adapter.notifyDataSetChanged();
 				}
 			});
 		   	Button save = (Button) findViewById(R.id.button10);
 		   	save.setOnClickListener(new OnClickListener() {
 		   		public void onClick(View v) {
 		   			if (choices + 1 + clickCounter < 3) {
 		   				showAlertDialog("Your vote has at least 3 rows.");
 		   				return;
 		   			}
 		   			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
 		   			postParameters.add(new BasicNameValuePair("id", id));
 		   			postParameters.add(new BasicNameValuePair("owner", LoginActivity.username));
 		   			postParameters.add(new BasicNameValuePair("counter", ((Integer) (choices + 1 + clickCounter)).toString()));
 		   			postParameters.add(new BasicNameValuePair("created", created));
 		   			for (int i = 0; i < choices + 1 + clickCounter; i++) {
 		   				if (i == 0) {
 		   					postParameters.add(new BasicNameValuePair("title", inputs.get(i)));
 		   				} else {
 		   					postParameters.add(new BasicNameValuePair("choice"+i, inputs.get(i)));
 		   				}
 		   			}
 		   			String result = CustomHttpClient.executeHttpPost("http://teamwiki.phpfogapp.com/edit.php", postParameters);
 		   			if ("success\n".equals(result)) {
 		   				Intent intent = new Intent(EditActivity.this, IndexActivity.class);
 		   				startActivity(intent);
 		   				Toast.makeText(getApplicationContext(), "You vote has been editted", Toast.LENGTH_SHORT).show();
 		   				finish();
 		   			} else {
 		   				showAlertDialog("You vote is not saved");
 		   			}
 				}
 			});
		} catch (Exception e) {
			showAlertDialog("Error reading json data.");
		}
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case CUSTOM_DIALOG_ID:
			AlertDialog.Builder builder;
			AlertDialog alertDialog;

			Context mContext = EditActivity.this;
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup) findViewById(R.id.layout_root));

			final EditText et = (EditText) layout.findViewById(R.id.editText12);
			et.setText(customText);

			builder = new AlertDialog.Builder(mContext);
			builder.setView(layout);
			builder.setMessage("Enter your vote text here");
			builder.setCancelable(false);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					inputs.set(index, et.getText().toString());
					adapter.notifyDataSetChanged();
					removeDialog(CUSTOM_DIALOG_ID);
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					removeDialog(CUSTOM_DIALOG_ID);
				}
			});
			alertDialog = builder.create();
			dialog = alertDialog;
			break;
		default:
			dialog = null;
		}
		return dialog;
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
