package com.cse694;

import java.util.Calendar;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BuzzerActivity extends MapActivity implements OnClickListener,
		LocationListener, Listener {

	private static final int LOC_UPDATE_INTERVAL = 60000;
	private static final int LOC_UPDATE_DIST = 1000;
	private static final int MAP_ZOOM = 18;

	private MapView mapView;
	private MapController mapController;
	private LocationManager locManager;
	private boolean gpsEnabled = false;
	private Location lastLocation;
	private GeoPoint lastMapCenter;
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

		View btnLogout = (Button) findViewById(R.id.logoutButton);
		btnLogout.setOnClickListener(this);
		View btnRecenter = (Button) findViewById(R.id.recenterButton);
		btnRecenter.setOnClickListener(this);

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(MAP_ZOOM);

		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		lastLocation = locManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (lastLocation != null) {
			recenterMap();

			// create an overlay that shows our current location
            myLocationOverlay = new MyLocationOverlay(this, mapView);
            
            // add this overlay to the MapView and refresh it
            mapView.getOverlays().add(myLocationOverlay);
            mapView.postInvalidate();
		}

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
		disableLocation();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("buzzer", "Buzzer called onResume");
		enableLocation();
		myLocationOverlay.enableMyLocation();
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

	private void enableLocation() {
		Log.d("buzzer", "Enabling location...");
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				LOC_UPDATE_INTERVAL, LOC_UPDATE_DIST, this);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				LOC_UPDATE_INTERVAL, LOC_UPDATE_DIST, this);
		locManager.addGpsStatusListener(this);
	}

	private void disableLocation() {
		locManager.removeUpdates(this);
		locManager.removeGpsStatusListener(this);
		Log.d("buzzer", "Location disabled");
	}

	private void recenterMap() {
		mapController.setZoom(MAP_ZOOM);
		GeoPoint myGeoPoint = new GeoPoint(
				(int) (lastLocation.getLatitude() * 1000000),
				(int) (lastLocation.getLongitude() * 1000000));
		mapController.animateTo(myGeoPoint);
		lastMapCenter = myGeoPoint;
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

	@Override
	public void onLocationChanged(Location location) {
		Log.d("buzzer", "Location Changed: " + location);

		if (location.getProvider() == LocationManager.NETWORK_PROVIDER
				&& gpsEnabled) {
			if (lastLocation != null
					&& location.getAccuracy() >= lastLocation.getAccuracy()) {
				// don't update if accuracy from network is worse
				Log.d("buzzer", "\tNot updating since network is less accurate");
				return;
			}
			long now = Calendar.getInstance().getTimeInMillis();
			if (lastLocation != null
					&& lastLocation.getProvider() == LocationManager.GPS_PROVIDER
					&& (now - lastLocation.getTime()) < LOC_UPDATE_INTERVAL) {
				// don't update if gps location is not old
				Log.d("buzzer",
						"\tNot updating since last location is gps and not old");
				return;
			}
		}

		lastLocation = location;

		// Don't move map if user has scrolled
		if (lastMapCenter.equals(mapView.getMapCenter())) {
			recenterMap();
		} else {
			Log.d("buzzer","Not moving map since user scrolled");
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("buzzer", "Location provider disabled: " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("buzzer", "Location provider enabled: " + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.d("buzzer", "Location status changed: " + status + " provider: "
				+ provider);
	}

	@Override
	public void onGpsStatusChanged(int event) {
		if (event == GpsStatus.GPS_EVENT_STARTED) {
			gpsEnabled = true;
		} else if (event == GpsStatus.GPS_EVENT_STOPPED) {
			gpsEnabled = false;
		}
	}
}