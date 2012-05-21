package com.cse694;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class BuzzerActivity extends MapActivity implements OnClickListener {

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
				Toast.makeText(BuzzerActivity.this, onTapText,
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
		myLocationOverlay = new GoodLocationOverlay(this, mapView);

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

		addOverlays();
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
		GeoPoint myGeoPoint = new GeoPoint((int) (myLocationOverlay
				.getLastFix().getLatitude() * 1000000),
				(int) (myLocationOverlay.getLastFix().getLongitude() * 1000000));
		mapController.animateTo(myGeoPoint);
	}

	private void addOverlays() {
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.mappin);
		RestaurantItemizedOverlay itemizedoverlay = new RestaurantItemizedOverlay(
				drawable, this);

		/*
		 * TODO: Remove testing code GeoPoint point = new GeoPoint(19240000,
		 * -99120000); OverlayItem overlayitem = new OverlayItem(point,
		 * "Hola, Mundo!", "I'm in Mexico City!");
		 * itemizedoverlay.addOverlay(overlayitem);
		 * 
		 * GeoPoint point2 = new GeoPoint(35410000, 139460000); OverlayItem
		 * overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!",
		 * "I'm in Japan!"); itemizedoverlay.addOverlay(overlayitem2); // End
		 * testing code
		 */

		try {
			Reader file = new BufferedReader(new FileReader("assets/resDB.txt"));
			int ch = file.read();
			String name = "";
			String info = "";
			String latitudeS = "";
			String longitudeS = "";
			int latitude, longitude;
			GeoPoint thisRestaurant;
			OverlayItem thisItem;
			while (ch != -1) { // while not at EOF
				// read latitude
				while (ch != (int) ',') {
					latitudeS = latitudeS + (char) ch;
					ch = file.read();
				}
				ch = file.read(); // next character
				// read longitude
				while (ch != (int) ',') {
					longitudeS = longitudeS + (char) ch;
					ch = file.read();
				}
				ch = file.read(); // next character
				latitude = Integer.parseInt(latitudeS);
				longitude = Integer.parseInt(longitudeS);
				// read restaurant name
				while (ch != (int) ',') {
					name = name + (char) ch;
					ch = file.read();
				}
				ch = file.read(); // next character
				// read short description (to end of line)
				while (ch != (int) System.getProperty("line.separator").charAt(
						0)) {
					info = info + (char) ch;
					ch = file.read();
				}
				// read through newline, if it is a multi-character sequence
				// also skips multiple newlines
				String cha = "" + (char) ch;
				while (System.getProperty("line.separator").contains(cha)) {
					cha = "" + (char) file.read();
				}
				thisRestaurant = new GeoPoint(latitude, longitude);
				thisItem = new OverlayItem(thisRestaurant, name, info);
				itemizedoverlay.addOverlay(thisItem);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mapOverlays.add(itemizedoverlay);
		mapView.postInvalidate();
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
			case R.id.recenterButton :
				recenterMap();
				break;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}