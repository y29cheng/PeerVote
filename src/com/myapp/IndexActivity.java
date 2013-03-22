package com.myapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class IndexActivity extends Activity implements Runnable {
	
	private ProgressDialog pd;
	static ArrayList<HashMap<String, String>> voteList = null;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
			setContentView(R.layout.index);
			refresh();
		}
	};
	public static JSONArray json = null;
	public static int position;
	
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to log out?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   LoginActivity.username = null;
		        	   Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
		        	   startActivity(intent);
		        	   finish();
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
		pd = ProgressDialog.show(this, "", "Loading, please wait...", false);
		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.index_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh:
				voteList = null;
				startActivity(getIntent());
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void run() {
		if (voteList == null) {
			voteList = new ArrayList<HashMap<String, String>>();
			json = CustomHttpClient.executeHttpGet(CommonUtils.votes_baseUrl, CommonUtils.apiKey);
			try {
				JSONArray votes = json;
				for (int i = 0; i < votes.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject vote = votes.getJSONObject(i);
					String owner = vote.getString("owner");
					String title = vote.getString("title");
					map.put("owner", owner);
					map.put("title", title);
					voteList.add(map);
				}
			} catch (Exception e) {
				showAlertDialog("Error reading json data.");
			}
		}
		handler.sendEmptyMessage(0);
	}
	
	private void refresh() {
		ListAdapter adapter = new SimpleAdapter(this, voteList, R.layout.list, new String[] {"owner", "title"}, new int[] {R.id.textView18, R.id.textView14});
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				IndexActivity.position = position;
				Intent intent = new Intent(IndexActivity.this, ViewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final CharSequence[] items = {"Edit", "Delete"};
				final int pos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
				builder.setTitle("Options");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						String owner = null;
						if (item == 1) {
							dialog.cancel();
							try {
								JSONArray votes = IndexActivity.json;
								JSONObject vote = votes.getJSONObject(pos);
								owner = vote.getString("owner");
								if (!LoginActivity.username.equals(owner)) {
									showAlertDialog("You can't delete other users' vote.");
									return;
								}
							} catch (Exception e) {
								showAlertDialog("Error reading json data.");
								return;
							}
							AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
							builder.setMessage("Are you sure?")
								   .setCancelable(false)
								   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							        	   dialog.cancel();
							        	   try {
							        		   JSONArray votes = IndexActivity.json;
							        		   JSONObject vote = votes.getJSONObject(pos);
//								        	   ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//								        	   if (!vote.getString("owner").equals(LoginActivity.username)) {
//								        		   showAlertDialog("You can't delete other users' vote.");
//								        		   return;
//								        	   }
//								        	   postParameters.add(new BasicNameValuePair("id", vote.getJSONObject("_id").getString("$oid")));
//								        	   String result = CustomHttpClient.executeHttpPut(vote, "[]");
							        		   if (CustomHttpClient.executeHttpPut(CommonUtils.votes_baseUrl, CommonUtils.apiKey, vote, "[]")) {
							        			   voteList = null;
								        		   pd = ProgressDialog.show(IndexActivity.this, "", "Loading, please wait...", false);
								        		   Thread th = new Thread(IndexActivity.this);
								        		   th.start();
								        		   Toast.makeText(getApplicationContext(), "The vote with id " + vote.getJSONObject("_id").getString("$oid") + " has been deleted.", Toast.LENGTH_SHORT).show();
							        		   } else {
							        			   showAlertDialog("Failed to delete the vote.");
							        		   }
//								        	   if ("success\n".equals(result)) {
//								        		   voteList = null;
//								        		   pd = ProgressDialog.show(IndexActivity.this, "", "Loading, please wait...", false);
//								        		   Thread th = new Thread(IndexActivity.this);
//								        		   th.start();
//								        		   Toast.makeText(getApplicationContext(), "The vote with id " + vote.getJSONObject("_id").getString("$oid") + " has been deleted.", Toast.LENGTH_SHORT).show();
//								        	   } else {
//								        		   showAlertDialog("Failed to delete the vote.");
//								        	   }
							        	   } catch (Exception e) {
							        		   showAlertDialog("Error reading json data.");
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
						} else {
							try {
								JSONArray votes = IndexActivity.json;
								JSONObject vote = votes.getJSONObject(pos);
								owner = vote.getString("owner");
								if (!LoginActivity.username.equals(owner)) {
									showAlertDialog("You can't edit other users' vote.");
									return;
								}
								IndexActivity.position = pos;
								Intent intent = new Intent(IndexActivity.this, EditActivity.class);
								startActivity(intent);
								finish();
							} catch (Exception e) {
								showAlertDialog("Error reading json data");
							}
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		});
	}
	
	public void addVoteListener(View v) {
		if (v.getId() != R.id.textView10) return;
		Intent intent = new Intent(IndexActivity.this, AddActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void logOutListener(View v) {
		if (v.getId() != R.id.textView11) return;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to log out?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   LoginActivity.username = null;
		        	   Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
		        	   startActivity(intent);
		        	   finish();
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
