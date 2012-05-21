package com.cse694;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CheckinActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("buzzer", "Checkin called onCreate");
		setContentView(R.layout.checkin);

		TextView restName = (TextView) findViewById(R.id.restaurantName);
		restName.setText(this.getIntent().getStringExtra(
				"com.cse694.buzzer.RestaurantId"));
		TextView checkingIn = (TextView) findViewById(R.id.checkingInAt);
		checkingIn
				.setText("You're checking in at "
						+ this.getIntent()
								.getStringExtra(
										"com.cse694.buzzer.RestaurantID"
												+ ". Please let us know how many people are in your party:"));

		View btnLogin = (Button) findViewById(R.id.loginButton);
		btnLogin.setOnClickListener(this);
		View btnCancel = (Button) findViewById(R.id.cancelButton);
		btnCancel.setOnClickListener(this);
		View btnRegister = (Button) findViewById(R.id.registerButton);
		btnRegister.setOnClickListener(this);
		RadioGroup radio = (RadioGroup) findViewById(R.id.radioGroup);
		radio.setOnClickListener(this);
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
			case R.id.cancelButton :
				Log.i("Buzzer", "Canceled checkIn");
				finish();
				break;
			case R.id.radioGroup :
				Log.i("Buzzer", "Radio group clicked");

			case R.id.checkInButton :
				Log.i("Buzzer", "Checked in");
				User user = User.getCurrentUser(getApplicationContext());

		}
	}

}
