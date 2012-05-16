package com.cse694;

import android.content.Context;

public class User {
	private String email;
	private String partyName;
	private String password;
	private Restaurant checked_in_at;
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
	
	public static boolean logIn(String email, String password) {
		boolean ans = false;
		if (db.checkUser(email, password)) {
			ans = true;
		}
		return ans;
	}

	public boolean check_in(Restaurant rest, int party_size) {
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