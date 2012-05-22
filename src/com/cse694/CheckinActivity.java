package com.cse694;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class CheckinActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private PartySizes partySize = PartySizes.ONE_TWO; // default party size
	private Restaurant restaurant;

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
		TextView checkingIn = (TextView) findViewById(R.id.checkingInAt);
		checkingIn.setText(restaurant.getDescription()
				+ "\n\nPlease let us know how many people are in your party:");

		View btnCancel = (Button) findViewById(R.id.cancelButtonCHK);
		btnCancel.setOnClickListener(this);
		View btnRegister = (Button) findViewById(R.id.checkInButton);
		btnRegister.setOnClickListener(this);
		RadioGroup radio = (RadioGroup) findViewById(R.id.radioGroup);
		radio.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("buzzer", "Checkincalled onSaveInstanceState");
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
			Log.i("Buzzer", "Canceled checkIn");
			finish();
			break;
		case R.id.checkInButton:
			Log.i("Buzzer", "Checked in");
			User user = User.getCurrentUser(getApplicationContext());
			user.check_in(restaurant.getId(), partySize);
			Toast.makeText(
					this,
					"You've checked in 4 guests at " + restaurant.getName()
							+ "!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case 0:
			this.partySize = PartySizes.ONE_TWO;
			break;
		case 1:
			this.partySize = PartySizes.THREE_FOUR;
			break;
		case 2:
			this.partySize = PartySizes.FIVE_SIX;
			break;
		case 3:
			this.partySize = PartySizes.SEVEN_PLUS;
			break;
		default:
			this.partySize = PartySizes.ONE_TWO;
		}
	}

}
