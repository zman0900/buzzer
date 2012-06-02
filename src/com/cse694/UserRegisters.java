package com.cse694;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegisters extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("buzzer", "Register called onCreate");
		setContentView(R.layout.register);

		View btnCancel = (Button) findViewById(R.id.registerCancelButton);
		btnCancel.setOnClickListener(this);
		View btnRegister = (Button) findViewById(R.id.registerButton);
		btnRegister.setOnClickListener(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("buzzer", "Register called onSaveInstanceState");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("buzzer", "Register called onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("buzzer", "Register called onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("buzzer", "Register called onDestroy");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registerCancelButton:
			finish();
			break;
		case R.id.registerButton:
			Log.i("Buzzer", "Perform register");
			register();
			break;
		}
	}

	public void register() {
		// LoginDatabaseHelper db = new LoginDatabaseHelper(
		// getApplicationContext());
		EditText email = (EditText) findViewById(R.id.registerEmail);
		EditText partyName = (EditText) findViewById(R.id.registerPartyName);
		EditText pass = (EditText) findViewById(R.id.registerPassword);
		EditText confPass = (EditText) findViewById(R.id.registerConfirmPassword);

		if (pass.getText().toString().compareTo(confPass.getText().toString()) != 0) {
			// Passwords aren't equal
			Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (email.getText().length() > 6
					&& partyName.getText().length() > 2
					&& pass.getText().length() > 4) {
				User user = new User(partyName.getText().toString(), email
						.getText().toString(), pass.getText().toString(), -1,
						getApplicationContext());
				user.register();
				// db.insertUser(email.getText().toString(), partyName.getText()
				// .toString(), pass.getText().toString());
				finish();
			} else {
				// Fields not filled or too short
				Toast.makeText(this, "Please fill in all fields!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
