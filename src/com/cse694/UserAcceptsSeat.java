package com.cse694;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class UserAcceptsSeat extends Activity implements OnClickListener {

	private Restaurant rest;
	private User currentUser;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		currentUser = User.getCurrentUser(this);
		int restId = currentUser.getCheckedInAtId();
		rest = Restaurant.getRestaurantById(this, restId);
		TextView seatNotifyText = (TextView) this.findViewById(R.id.seatNotifyLabel);
		seatNotifyText.setText("Your table at "+rest.getName()+ " is ready!");
	}
	@Override
	public void onClick(View v) {
		boolean accept = (v.getId() == R.id.acceptSeatBtn);
		currentUser.accept_seat(rest, accept, this);
		finish();
	}

}
