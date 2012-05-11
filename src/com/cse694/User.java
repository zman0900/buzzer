package com.cse694;
public class User {
	private String email = "";
	private String party_name = "";
	private String password = "";
	private Restaurant checked_in_at = null;

	public User() {
	}

	public boolean register(String email, String party_name, String password) {
		this.email = email;
		this.party_name = party_name;
		this.password = password;
		return true;
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
