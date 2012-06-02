package com.cse694;

import java.util.Iterator;
import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class UserChoosesRestaurant extends MapActivity implements
		OnClickListener {

	private static final int MAP_ZOOM = 18;

	private MapView mapView;
	private MapController mapController;
	private GoodLocationOverlay myLocationOverlay;

	private class GoodLocationOverlay extends MyLocationOverlay {

		public GoodLocationOverlay(Context context, MapView mapView) {
			super(context, mapView);
		}

		@Override
		public boolean onTap(GeoPoint p, MapView map) {
			// LoginDatabaseHelper db = new
			// LoginDatabaseHelper(BuzzerActivity.this);
			boolean ans = false;
			if (super.onTap(p, map)) {
				SharedPreferences settings = getSharedPreferences("LoginStore",
						MODE_PRIVATE);
				String onTapText = settings.getString("name", "You Fail!");
				Toast.makeText(UserChoosesRestaurant.this, onTapText,
						Toast.LENGTH_SHORT).show();
				ans = true;
			}
			return ans;
		}
	}

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("buzzer", "Buzzer called onCreate");
		setContentView(R.layout.main);

		// Check for login
		if (!UserLogsIn.isLoggedIn(this)) {
			Log.i("buzzer", "Not logged in");
			finish();
			startActivity(new Intent(UserChoosesRestaurant.this,
					UserLogsIn.class));
		}

		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
			Toast.makeText(this,
					"Your device is not connected to the internet!",
					Toast.LENGTH_LONG).show();
		}

		// Check for c2dm registration
		if (((BuzzerApplication) this.getApplicationContext()).getRegId() == null) {
			// Create registration intent
			Intent regIntent = new Intent(
					"com.google.android.c2dm.intent.REGISTER");
			// Identify your app
			regIntent.putExtra("app", PendingIntent.getBroadcast(
					UserChoosesRestaurant.this, 0, new Intent(), 0));
			// Identify role account server will use to send
			regIntent.putExtra("sender", "buzzerappc2dm@gmail.com");
			// Start the registration process
			startService(regIntent);
		}

		View btnLogout = findViewById(R.id.logoutButton);
		btnLogout.setOnClickListener(this);
		View btnRecenter = findViewById(R.id.recenterButton);
		btnRecenter.setOnClickListener(this);

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(MAP_ZOOM);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("buzzer", "Buzzer called onSaveInstanceState");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("buzzer", "Buzzer called onPause");
		// Don't crash in the emulator
		if (myLocationOverlay != null) {
			myLocationOverlay.disableMyLocation();
		}

		// Remove overlays
		mapView.getOverlays().clear();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("buzzer", "Buzzer called onResume");

		// create an overlay that shows our current location
		if (myLocationOverlay == null) {
			myLocationOverlay = new GoodLocationOverlay(this, mapView);
			// Center map on first location fix
			myLocationOverlay.runOnFirstFix(new Runnable() {
				@Override
				public void run() {
					recenterMap();
				}
			});
		}

		// add location overlay to the MapView and refresh it
		mapView.getOverlays().add(myLocationOverlay);
		mapView.postInvalidate();

		// Add restaurant overlays
		addOverlays();

		// Don't crash in the emulator
		if (myLocationOverlay != null) {
			myLocationOverlay.enableMyLocation();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("buzzer", "Buzzer called onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("buzzer", "Buzzer called onDestroy");
	}

	private void recenterMap() {
		try {
			mapController.setZoom(MAP_ZOOM);
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (myLocationOverlay.getLastFix().getLatitude() * 1000000),
					(int) (myLocationOverlay.getLastFix().getLongitude() * 1000000));
			mapController.animateTo(myGeoPoint);
		} catch (NullPointerException e) {
			Toast.makeText(this, "Location services not available.",
					Toast.LENGTH_SHORT).show();
			Log.i("buzzer",
					"NullPointerException caught in UserChoosesRestaurant.recenterMap()");
		}
	}

	private void addOverlays() {
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.mappin);
		RestaurantItemizedOverlay itemizedoverlay = new RestaurantItemizedOverlay(
				drawable, this);
		User user = User.getCurrentUser(this);
		List<Restaurant> restaurants = Restaurant.getAllRestaurants(this);
		Iterator<Restaurant> restIterator = restaurants.iterator();
		while (restIterator.hasNext()) {
			Restaurant rest = restIterator.next();
			OverlayItem restOverlay = new OverlayItem(new GeoPoint(
					rest.getLatitude(), rest.getLongitude()), rest.getName(),
					String.valueOf(rest.getId()));
			if (rest.getId() == user.getCheckedInAtId()) {
				Drawable icon = this.getResources().getDrawable(
						R.drawable.mappin_green);
				icon.setBounds(-1 * icon.getIntrinsicWidth(),
						-1 * icon.getIntrinsicHeight(), 0, 0);
				restOverlay.setMarker(icon);
			}
			itemizedoverlay.addOverlay(restOverlay);
		}
		mapOverlays.add(itemizedoverlay);
		mapView.postInvalidate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logoutButton:
			UserLogsIn.logout(this);
			finish();
			startActivity(new Intent(UserChoosesRestaurant.this,
					UserLogsIn.class));
			break;
		case R.id.recenterButton:
			recenterMap();
			break;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}