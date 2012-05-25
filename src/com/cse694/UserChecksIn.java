package com.cse694;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
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
					"Checking in " + partySize.getNum() + " guests at "
							+ restaurant.getName() + "...", Toast.LENGTH_LONG)
					.show();
			// CheckInWaitTask checkInWaitTask = new CheckInWaitTask();
			// checkInWaitTask.execute(this);
			sendJson();
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

	protected void sendJson() {
		final String regId = ((BuzzerApplication) this.getApplicationContext())
				.getRegId();
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
			Toast.makeText(this,
					"Your device is not connected to the internet!",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (regId == null) {
			Toast.makeText(
					this,
					"Your device could not register for push notifications, please try again",
					Toast.LENGTH_LONG).show();
			return;
		}
		Thread t = new Thread() {
			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the
									// child Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				HttpResponse response;
				JSONObject json = new JSONObject();
				try {
					HttpPost post = new HttpPost(
							"http://zman0900.no-ip.com:3000/checkins.json");
					json.put("restaurant_id", restaurant.getId());
					json.put("device_reg", regId);
					StringEntity se = new StringEntity(json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);
					response = client.execute(post);
					/* Checking response */
					if (response != null) {
						// Assume success
						Toast.makeText(getApplicationContext(),
								"Check-in Successful", Toast.LENGTH_LONG)
								.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Check-in failed!",
							Toast.LENGTH_LONG).show();
				}
				Looper.loop(); // Loop in the message queue
			}
		};
		t.start();
		finish();
	}
}
