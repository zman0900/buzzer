package com.cse694;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
		Log.d("Buzzer","Showing register view");
		
		View btnCancel = (Button)findViewById(R.id.registerCancelButton);
        btnCancel.setOnClickListener(this);
        View btnRegister = (Button)findViewById(R.id.registerButton);
        btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
  		case R.id.registerCancelButton:
	    	finish();
    		break;
  		case R.id.registerButton:
  			Log.d("Buzzer","Perform register");
  			register();
  			break;
		}
	}
	
	public void register() {
		LoginDatabaseHelper db = new LoginDatabaseHelper(getApplicationContext());
		EditText email = (EditText)findViewById(R.id.registerEmail);
		EditText partyName = (EditText)findViewById(R.id.registerPartyName);
		EditText pass = (EditText)findViewById(R.id.registerPassword);
		EditText confPass = (EditText)findViewById(R.id.registerConfirmPassword);
		
		if (pass.getText().toString().compareTo(confPass.getText().toString()) != 0) {
			// Passwords aren't equal
			Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
		} else {
			db.insertUser(email.getText().toString(), partyName.getText().toString(), pass.getText().toString());
			finish();
		}
	}
}
