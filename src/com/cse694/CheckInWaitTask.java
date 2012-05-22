package com.cse694;

import javax.xml.transform.Result;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class CheckInWaitTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private String restName;

	public CheckInWaitTask(Context context, String restName) {
		this.context = context;
		this.restName = restName;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.i("buzzer","Waiting for seat...");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected Void onPostExecute(Result result) {
		Log.i("buzzer", "Done waiting.");
//		new AlertDialog.Builder(context).setTitle("Your seat is ready!")
//				.setMessage("Please either accept or reject your seat.").show();
		CharSequence msg = "Your seat at "+restName+" is ready!";
		Toast.makeText(
				context,
				msg,
				Toast.LENGTH_SHORT).show();
		return null;
	}

}