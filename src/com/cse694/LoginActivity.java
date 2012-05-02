package com.cse694;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LoginActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
		Log.d("Buzzer","Showing login view");
	}
}
