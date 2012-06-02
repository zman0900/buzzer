package com.cse694;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserLogsIn extends Activity implements OnClickListener {
	
	public static final String LOGIN_STORE = "LoginStore";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("buzzer","Login called onCreate");
        setContentView(R.layout.login);
		
		View btnLogin = (Button)findViewById(R.id.loginButton);
		btnLogin.setOnClickListener(this);
		View btnCancel = (Button)findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(this);
        View btnRegister = (Button)findViewById(R.id.registerButton);
        btnRegister.setOnClickListener(this);
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
    	Log.d("buzzer", "Login called onSaveInstanceState");
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("buzzer","Login called onPause");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d("buzzer","Login called onStop");
	}
	
	@Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d("buzzer","Login called onDestroy");
    }


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
  		case R.id.cancelButton:
	    	finish();
    		break;
  		case R.id.loginButton:
  			Log.i("Buzzer","Perform login");
  			checkLoginInfo();
  			break;
  		case R.id.registerButton:
  			Log.i("Buzzer","Show register view");
  			startActivity(new Intent(UserLogsIn.this,UserRegisters.class));
  			break;
		}
	}
	
	private void checkLoginInfo() {
		LoginDatabaseHelper db = new LoginDatabaseHelper(getApplicationContext());
		EditText email = (EditText)findViewById(R.id.email);
		EditText password = (EditText)findViewById(R.id.password);
		if (db.checkUser(email.getText().toString(), password.getText().toString())) {
			// Valid login
			SharedPreferences settings = getSharedPreferences(LOGIN_STORE, MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("email", email.getText().toString());
			editor.putString("password", password.getText().toString());
			editor.putString("name", db.getNameFromEmail(email.getText().toString()));
			editor.putInt("checkedInAt", -1);
			editor.putBoolean("loggedin", true);
			editor.commit();
			
			finish();
			startActivity(new Intent(UserLogsIn.this,UserChoosesRestaurant.class));
		} else {
			Toast.makeText(this, "Login not valid!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public static Boolean isLoggedIn(Context context) {
		SharedPreferences settings = context.getSharedPreferences(LOGIN_STORE, MODE_PRIVATE);
        return settings.getBoolean("loggedin", false);
	}
	
	public static void logout(Context context) {
		SharedPreferences settings = context.getSharedPreferences(LOGIN_STORE, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}
}
