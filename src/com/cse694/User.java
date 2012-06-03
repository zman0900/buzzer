package com.cse694;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class User {
	private String email;
	private String partyName;
	private String password;
	private Integer checkedInAtId;
	private Integer checkinId;
	private static LoginDatabaseHelper db;

	public User(String partyName, String email, String password,
			Integer checkedInAtId, Context context) {
		if (db == null) {
			db = new LoginDatabaseHelper(context);
		}
		this.partyName = partyName;
		this.email = email;
		this.password = password;
		this.checkedInAtId = checkedInAtId;
	}

	public boolean register() {
		db.insertUser(email, partyName, password);
		return true;
	}

	public static User getCurrentUser(Context context) {
		SharedPreferences settings = context.getSharedPreferences("LoginStore",
				android.content.Context.MODE_PRIVATE);
		String party = settings.getString("name", "You Fail!");
		String email = settings.getString("email", "You Fail!");
		String password = settings.getString("password", "You Fail!");
		Integer checkedInAtId = settings.getInt("checkedInAt", -1);
		return new User(party, email, password, checkedInAtId, context);
	}

	public static boolean logIn(String email, String password) {
		boolean ans = false;
		if (db.checkUser(email, password)) {
			ans = true;
		}
		return ans;
	}

	public void checkIn(final Integer restaurantId, final PartySizes partySize,
			final Context context) {
		final User thisUser = this;
		final String regId = ((BuzzerApplication) context.getApplicationContext())
				.getRegId();
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
			Toast.makeText(context,
					"Your device is not connected to the internet!",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (regId == null) {
			Toast.makeText(
					context,
					"Your device could not register for push notifications, please try again",
					Toast.LENGTH_LONG).show();
			return;
		}

		Thread t = new Thread() {
			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread
				
				JSONObject json = new JSONObject();
				try {
					json.put("restaurant_id", restaurantId);
					json.put("device_reg", regId);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String response = Util.postJson(json.toString(), "http://zman0900.no-ip.com:3000/checkins.json");
				if (response != null) {
					try {
						JSONObject respJson = new JSONObject(response);
						int checkinId = respJson.getInt("id");
						Log.d("buzzer", "Checkin id = " + checkinId);
						
						// Store checked in restarurant id
						thisUser.setCheckedInAtId(restaurantId);
						thisUser.setCheckinId(checkinId);
						SharedPreferences settings = context.getSharedPreferences("LoginStore",
								android.content.Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt("checkedInAt", restaurantId);
						editor.putInt("checkinId", checkinId);
						editor.commit();
						
						Toast.makeText(context,
								"Check-in Successful", Toast.LENGTH_LONG)
								.show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(context, "Check-in failed!",
								Toast.LENGTH_LONG).show();
					}
					
				} else {
					Log.e("buzzer", "Didn't get a response from checkin");
					Toast.makeText(context, "Check-in failed!",
							Toast.LENGTH_LONG).show();
				}
				
				Looper.loop(); // Loop in the message queue
			}
		};
		t.start();
		
		return;
	}

	public boolean accept_seat(Restaurant rest, boolean accept, Context context) {
		boolean ans = true;
		// Clear seat either way
		SharedPreferences settings = context.getSharedPreferences("LoginStore",
				android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("checkedInAt", -1);
		editor.commit();
		this.checkedInAtId = null;
		if (accept) {
			// Do something if accepting
		} else {
			// Do something else if rejecting
		}
		return ans;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getCheckedInAtId() {
		return checkedInAtId;
	}

	public void setCheckedInAtId(Integer checkedInAtId) {
		this.checkedInAtId = checkedInAtId;
	}

	public Integer getCheckinId() {
		return checkinId;
	}

	public void setCheckinId(Integer checkinId) {
		this.checkinId = checkinId;
	}
}