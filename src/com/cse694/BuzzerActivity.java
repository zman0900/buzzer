package com.cse694;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BuzzerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Check for login
        // if (!logged in) {
        	startActivity(new Intent("com.cse694.LoginActivity"));
    }
}