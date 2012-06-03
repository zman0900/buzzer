package com.cse694;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class UserChecksIn extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private PartySizes partySize = PartySizes.ONE_TWO; // default party size
	private Restaurant restaurant;
	private boolean checkedIn = false;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("buzzer", "Checkin called onCreate");
		setContentView(R.layout.checkin);

		String idStr = this.getIntent().getStringExtra(
				"com.cse694.buzzer.RestaurantId");
		Integer id = Integer.parseInt(idStr);
		restaurant = Restaurant.getRestaurantById(this, id);
		if (restaurant == null) {
			Log.d("buzzer", "Restaurant was null!");
			finish();
		}

		TextView restName = (TextView) findViewById(R.id.restaurantName);
		restName.setText(restaurant.getName());
		TextView description = (TextView) findViewById(R.id.description);
		description.setText(restaurant.getDescription());
		TextView info = (TextView) findViewById(R.id.infoText);

		Button btnCancel = (Button) findViewById(R.id.cancelButtonCHK);
		btnCancel.setOnClickListener(this);
		Button btnRegister = (Button) findViewById(R.id.checkInButton);
		btnRegister.setOnClickListener(this);
		RadioGroup radio = (RadioGroup) findViewById(R.id.radioGroup);
		radio.setOnCheckedChangeListener(this);
		
		User user = User.getCurrentUser(this);
		if (user.getCheckedInAtId() != null && user.getCheckedInAtId() == id) {
			// User already checked in here
			checkedIn = true;
			radio.setVisibility(View.GONE);
			TextView message = (TextView) findViewById(R.id.message);
			message.setText(R.string.alreadyCheckedIn);
			btnRegister.setText(R.string.cancelCheckinBtn);
			info.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("buzzer", "Checkin called onSaveInstanceState");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("buzzer", "Checkin called onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("buzzer", "Checkin called onResume");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("buzzer", "Checkin called onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("buzzer", "Checkin called onDestroy");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelButtonCHK:
			Log.d("buzzer", "Canceled checkIn screen");
			finish();
			break;
		case R.id.checkInButton:
			User user = User.getCurrentUser(this);
			if (checkedIn) {
				Log.d("buzzer", "Canceling checkin");
				user.cancelCheckin(this);
				finish();
			} else {
				Log.d("buzzer", "Checking in");
				Toast.makeText(
						this,
						"Checking in " + partySize.getNum() + " guests at "
								+ restaurant.getName() + "...",
						Toast.LENGTH_LONG).show();
				// This gets called when checkin is complete to close view
				Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						finish();
					}
				};
				user.checkIn(restaurant.getId(), partySize, this, handler);
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Log.i("Buzzer", "radio button changed; checkedId = " + checkedId);
		switch (checkedId) {
		case R.id.twoPeople:
			this.partySize = PartySizes.ONE_TWO;
			break;
		case R.id.fourPeople:
			this.partySize = PartySizes.THREE_FOUR;
			break;
		case R.id.sixPeople:
			this.partySize = PartySizes.FIVE_SIX;
			break;
		case R.id.sevenPlus:
			this.partySize = PartySizes.SEVEN_PLUS;
			break;
		}
	}

}
