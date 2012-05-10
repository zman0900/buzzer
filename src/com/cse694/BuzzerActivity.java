package com.cse694;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.content.*;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BuzzerActivity extends MapActivity implements OnClickListener {

	private MapController mapController;
	private LocationManager locManager;
	private LocationListener locListener;

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));

			mapController.animateTo(myGeoPoint);
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}
	/** Called when the activity is first created. */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("buzzer","Buzzer called onCreate");
        setContentView(R.layout.main);
        
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new MyLocationListener();
        
        locManager.requestLocationUpdates(
        	    LocationManager.GPS_PROVIDER,
        	    0,
        	    0,
        	    locListener);
        
        GeoPoint initGeoPoint = new GeoPoint(
        		   (int)(locManager.getLastKnownLocation(
        		    LocationManager.GPS_PROVIDER)
        		    .getLatitude()*1000000),
        		   (int)(locManager.getLastKnownLocation(
        		    LocationManager.GPS_PROVIDER)
        		    .getLongitude()*1000000));
        
        mapController.animateTo(initGeoPoint);
        
        View btnLogout = (Button)findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(this);
        
        MapView mapView = (MapView)findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapController = mapView.getController();
        mapController.setCenter(initGeoPoint);
        
        // Check for login
        if (!LoginActivity.isLoggedIn(this)) {
        	Log.i("buzzer","Not logged in");
        	finish();
        	startActivity(new Intent(BuzzerActivity.this,LoginActivity.class));
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
			case R.id.logoutButton :
				LoginActivity.logout(this);
				finish();
				startActivity(new Intent(BuzzerActivity.this,
						LoginActivity.class));
				break;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}