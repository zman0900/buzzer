package com.cse694;

import javax.xml.transform.Result;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class CheckInWaitTask extends AsyncTask<Context, Void, Void> {

	private Context context;

	@Override
	protected Void doInBackground(Context... params) {
		Log.i("buzzer","Waiting for seat...");
		this.context = params[0];
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(Void...result) {
		Log.i("buzzer", "Done waiting.");
		new AlertDialog.Builder(context).setTitle("Your seat is ready!")
				.setMessage("Please either accept or reject your seat.").show();		
	}

}