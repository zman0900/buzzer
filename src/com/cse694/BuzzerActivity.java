package com.cse694;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BuzzerActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("buzzer","Buzzer called onCreate");
        setContentView(R.layout.main);
        
        View btnLogout = (Button)findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(this);
        
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
}