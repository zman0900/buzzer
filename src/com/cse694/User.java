package com.cse694;
public class User {
	private String email = "";
	private String party_name = "";
	private PartySizes party_size = PartySizes.UNINIT;
	private Restaurant checked_in_at = null;

	private User() {
	}

	public boolean register(String email, String party_name,
			PartySizes party_size) {
		this.email = email;
		this.party_name = party_name;
		this.party_size = party_size;
		return true;
	}

	public boolean check_in(Restaurant rest) {
		this.checked_in_at = rest;
		/*Notification seat_alert = new Notification(this,party_name
				+ " is requesting a seat at your restaurant, "
				+ rest.toString ());
		seat_alert.send ();*/
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
