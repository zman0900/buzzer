package com.cse694;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
		Log.d("Buzzer","Showing login view");
		
		View btnLogin = (Button)findViewById(R.id.loginButton);
		btnLogin.setOnClickListener(this);
		View btnCancel = (Button)findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(this);
        View btnRegister = (Button)findViewById(R.id.registerButton);
        btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
  		case R.id.cancelButton:
	    	finish();
    		break;
  		case R.id.loginButton:
  			Log.d("Buzzer","Perform login");
  			break;
  		case R.id.registerButton:
  			Log.d("Buzzer","Show register view");
  			break;
		}
	}
}
