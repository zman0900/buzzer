package com.cse694;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BuzzerActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View btnLogout = (Button)findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(this);
        
        // Check for login
        if (!LoginActivity.isLoggedIn(this)) {
        	finish();
        	startActivity(new Intent(BuzzerActivity.this,LoginActivity.class));
        }
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