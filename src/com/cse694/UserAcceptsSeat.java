package com.cse694;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserAcceptsSeat extends Activity implements OnClickListener {

	private Restaurant rest;
	private User currentUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("buzzer", "UserAcceptsSeat called onCreate");
		setContentView(R.layout.acceptseat);

		View btnAccept = (Button) findViewById(R.id.acceptSeatBtn);
		btnAccept.setOnClickListener(this);
		View btnDecline = (Button) findViewById(R.id.declineSeatBtn);
		btnDecline.setOnClickListener(this);
		currentUser = User.getCurrentUser(this);
		int restId = currentUser.getCheckedInAtId();
		rest = Restaurant.getRestaurantById(this, restId);
		TextView seatNotifyText = (TextView) this
				.findViewById(R.id.seatNotifyLabel);
		seatNotifyText
				.setText("Your table at " + rest.getName() + " is ready!");
	}
	@Override
	public void onClick(View v) {
		Log.d("buzzer", "userAcceptsSeat called onClick");
		final boolean accept = (v.getId() == R.id.acceptSeatBtn);
		if (!accept) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Are you sure?");
			alertDialog
					.setMessage("Are you sure you want to cancel your table?");
			alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					currentUser.accept_seat(rest, accept,
							getApplicationContext());
					Toast.makeText(
							getApplicationContext(),
							"You have forfeited your table for at "
									+ rest.getName() + ".", Toast.LENGTH_LONG)
							.show();
					finish();
				}
			});
			alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// return to acceptSeat view, without doing anything
				}
			});
			alertDialog.show();
		} else {
			currentUser.accept_seat(rest, accept, this);
			Toast.makeText(
					getApplicationContext(),
					"You have accepted your table for at " + rest.getName()
							+ ". Please make your way to the restaurant.",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}
}
