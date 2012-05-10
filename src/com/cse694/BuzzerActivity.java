package com.cse694;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BuzzerActivity extends MapActivity implements OnClickListener,
		LocationListener {

	private MapController mapController;
	private LocationManager locManager;

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

		MapView mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();

		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		GeoPoint initGeoPoint = new GeoPoint((int) (locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER)
				.getLatitude() * 1000000), (int) (locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER)
				.getLongitude() * 1000000));
		mapController.setZoom(16);
		mapController.animateTo(initGeoPoint);
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
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("buzzer", "Buzzer called onResume");
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = locationManager.getBestProvider(criteria, true);
		Log.d("buzzer", "Providers: " + locationManager.getAllProviders());
		Log.d("buzzer", "Best provider: " + provider);
		locationManager.requestLocationUpdates(provider, 60000, 1000, this);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logoutButton:
			LoginActivity.logout(this);
			finish();
			startActivity(new Intent(BuzzerActivity.this, LoginActivity.class));
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
		// TODO Auto-generated method stub
		Log.d("buzzer", "Location Changed: " + location);
		GeoPoint myGeoPoint = new GeoPoint(
				(int) (location.getLatitude() * 1000000),
				(int) (location.getLongitude() * 1000000));

		mapController.animateTo(myGeoPoint);
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
}