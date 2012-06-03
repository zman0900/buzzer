package com.cse694;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
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
			Integer checkedInAtId, Integer checkinId, Context context) {
		if (db == null) {
			db = new LoginDatabaseHelper(context);
		}
		this.partyName = partyName;
		this.email = email;
		this.password = password;
		this.checkedInAtId = checkedInAtId;
		this.checkinId = checkinId;
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
		Integer checkinId = settings.getInt("checkinId", -1);
		Log.d("buzzer", "In getCurrentUser, checkinId = " + checkinId);
		return new User(party, email, password, checkedInAtId, checkinId, context);
	}

	public static boolean logIn(String email, String password) {
		boolean ans = false;
		if (db.checkUser(email, password)) {
			ans = true;
		}
		return ans;
	}

	public void checkIn(final Integer restaurantId, final PartySizes partySize,
			final Context context, final Handler callWhenDone) {
		final User thisUser = this;
		final String regId = ((BuzzerApplication) context
				.getApplicationContext()).getRegId();
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

				String response = Util.postJson(json.toString(),
						"http://zman0900.no-ip.com:3000/checkins.json");
				if (response != null) {
					try {
						JSONObject respJson = new JSONObject(response);
						int checkinId = respJson.getInt("id");
						Log.d("buzzer", "Checkin id = " + checkinId);

						// Store checked in restarurant id
						thisUser.setCheckedInAtId(restaurantId);
						thisUser.setCheckinId(checkinId);
						SharedPreferences settings = context
								.getSharedPreferences("LoginStore",
										android.content.Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt("checkedInAt", restaurantId);
						editor.putInt("checkinId", checkinId);
						editor.commit();
						
						callWhenDone.sendEmptyMessage(0);
						
						Toast.makeText(context, "Check-in Successful",
								Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						callWhenDone.sendEmptyMessage(0);
						e.printStackTrace();
						Toast.makeText(context, "Check-in failed!",
								Toast.LENGTH_LONG).show();
					}

				} else {
					callWhenDone.sendEmptyMessage(0);
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

	public void cancelCheckin(Context context) {
		final String url = "http://zman0900.no-ip.com:3000/checkins/"
				+ checkinId + ".json";
		Thread t = new Thread() {
			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread

				Util.delete(url);

				Looper.loop(); // Loop in the message queue
			}
		};
		t.start();

		setCheckedInAtId(null);
		setCheckinId(null);
		// Clear seat
		SharedPreferences settings = context.getSharedPreferences("LoginStore",
				android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("checkedInAt", -1);
		editor.putInt("checkinId", -1);
		editor.commit();

		Log.d("buzzer", "Checkin canceled");
	}

	public boolean accept_seat(Restaurant rest, boolean accept, Context context) {
		boolean ans = true;
		// Clear seat either way
		SharedPreferences settings = context.getSharedPreferences("LoginStore",
				android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("checkedInAt", -1);
		editor.putInt("checkinId", -1);
		editor.commit();
		this.checkedInAtId = null;
		this.checkinId = null;
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