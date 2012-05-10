package com.cse694;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BuzzerActivity extends MapActivity implements OnClickListener {
	
	private MapController mapController;
	
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("buzzer","Buzzer called onCreate");
        setContentView(R.layout.main);
        
        View btnLogout = (Button)findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(this);
        
        MapView mapView = (MapView)findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapController = mapView.getController();
        
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
		Log.d("buzzer","Buzzer called onPause");
	}
    
    @Override
	protected void onStop() {
    	super.onStop();
		Log.d("buzzer","Buzzer called onStop");
	}
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d("buzzer","Buzzer called onDestroy");
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logoutButton:
			LoginActivity.logout(this);
			finish();
			startActivity(new Intent(BuzzerActivity.this,LoginActivity.class));
			break;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}