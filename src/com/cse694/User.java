package com.cse694;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
	private String email;
	private String partyName;
	private String password;
	private Integer checkedInAtId;
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

	public boolean check_in(Integer restaurantId, PartySizes partySize,
			Context context) {
		this.checkedInAtId = restaurantId;
		// Store checked in restarurant id
		SharedPreferences settings = context.getSharedPreferences("LoginStore",
				android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("checkedInAt", restaurantId);
		editor.commit();
		return true;
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
}