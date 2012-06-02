package com.cse694;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This class is used to store the shared information in this app. In this case
 * it's the shared preferences
 */
public class BuzzerApplication extends Application {
	
	private SharedPreferences prefs;
	
	@Override
	public void onCreate() {
		super.onCreate();
		// get the shared preferences for the app
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}

	/**
	 * @return regId or null
	 * 
	 * This function returns the regId string if it's present, null if not
	 */
	public String getRegId() {
		return prefs.getString("regId", null);
	}

	/**
	 * @param regId - null or a String representing the registration id
	 * 
	 * This function can set or clear the regId preference. If null is received 
	 * then the preference is cleared, or else is it set
	 */
	public void setRegId(String regId) {
		SharedPreferences.Editor editor = prefs.edit();
		if (regId == null)
			editor.remove("regId");
		else 
			editor.putString("regId", regId);
		editor.commit();
	}

}
