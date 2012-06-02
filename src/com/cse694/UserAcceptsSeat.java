package com.cse694;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserAcceptsSeat extends Activity implements OnClickListener {

	private Restaurant rest;
	private User currentUser;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("buzzer","UserAcceptsSeat called onCreate");
        setContentView(R.layout.acceptseat);
		
		View btnAccept = (Button)findViewById(R.id.acceptSeatBtn);
		btnAccept.setOnClickListener(this);
		View btnDeclinel = (Button)findViewById(R.id.declineSeatBtn);
        btnDeclinel.setOnClickListener(this);
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
