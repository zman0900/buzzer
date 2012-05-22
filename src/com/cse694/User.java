package com.cse694;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
	private String email;
	private String partyName;
	private String password;
	public String checked_in_at;
	private static LoginDatabaseHelper db;
	

	public User(String partyName, String email, String password, Context context) {
		if (db == null) {
			db = new LoginDatabaseHelper(context);
		}
		this.partyName = partyName;
		this.email = email;
		this.password = password;
	}

	public boolean register() {
		db.insertUser(email, partyName, password);
		return true;
	}
	
	public static User getCurrentUser(Context context) {
		SharedPreferences settings = context.getSharedPreferences("LoginStore", android.content.Context.MODE_PRIVATE);
		String party = settings.getString("name", "You Fail!");
		String email = settings.getString("email", "You Fail!");
		String password = settings.getString("password", "You Fail!");
		return new User(party, email, password, context);
	}
	
	public static boolean logIn(String email, String password) {
		boolean ans = false;
		if (db.checkUser(email, password)) {
			ans = true;
		}
		return ans;
	}

	public boolean check_in(String rest, PartySizes partySize) {
		this.checked_in_at = rest;
		return true;
	}

	public boolean accept_seat(Restaurant rest, Notification seat_alert) {
		boolean ans;
		if (/* user accepts */true) {
			ans = true;
		} else {
			ans = false;
		}
		this.checked_in_at = null;
		return ans;
	}
}