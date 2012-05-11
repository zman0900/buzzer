package com.cse694;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class BuzzerActivity extends MapActivity implements OnClickListener {

	private static final int MAP_ZOOM = 18;

	private MapView mapView;
	private MapController mapController;
	private MyLocationOverlay myLocationOverlay;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("buzzer", "Buzzer called onCreate");
		setContentView(R.layout.main);

		// Check for login
		if (!LoginActivity.isLoggedIn(this)) {
			Log.i("buzzer", "Not logged in");
			finish();
			startActivity(new Intent(BuzzerActivity.this, LoginActivity.class));
		}

		View btnLogout = findViewById(R.id.logoutButton);
		btnLogout.setOnClickListener(this);
		View btnRecenter = findViewById(R.id.recenterButton);
		btnRecenter.setOnClickListener(this);

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(MAP_ZOOM);

		// create an overlay that shows our current location
		myLocationOverlay = new MyLocationOverlay(this, mapView);

		// add this overlay to the MapView and refresh it
		mapView.getOverlays().add(myLocationOverlay);
		mapView.postInvalidate();
		
		// Center map on first location fix
		myLocationOverlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				recenterMap();
			}
		});

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
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("buzzer", "Buzzer called onResume");
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
		mapController.setZoom(MAP_ZOOM);
		GeoPoint myGeoPoint = new GeoPoint(
				(int) (myLocationOverlay.getLastFix().getLatitude() * 1000000),
				(int) (myLocationOverlay.getLastFix().getLongitude() * 1000000));
		mapController.animateTo(myGeoPoint);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logoutButton:
			LoginActivity.logout(this);
			finish();
			startActivity(new Intent(BuzzerActivity.this, LoginActivity.class));
			break;
		case R.id.recenterButton:
			recenterMap();
			break;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}