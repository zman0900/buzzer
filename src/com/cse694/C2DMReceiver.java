package com.cse694;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Most of this class is copied with modifications from
 * http://www.eecs.berkeley.
 * edu/~liuisaac/site/blog/201109/simple-android-c2dm.html
 * 
 * playNotificationSound() copied from Google's Chrome2Phone app
 */
public class C2DMReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		// The action for registration reply is
		// com.google.android.c2dm.intent.REGISTRATION
		if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {
			Log.d("buzzer", "Received C2DM Registration Packet");
			// Call the handleRegistration function to handle registration
			handleRegistration(context, intent);

			// The action for data reply is
			// com.google.android.c2dm.intent.RECEIVE
		} else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
			Log.d("buzzer", "Received C2DM Data Packet");
			// Call the handleData function to handle c2dm packet
			handleData(context, intent);
		}
	}

	private void handleRegistration(Context context, Intent intent) {
		// These strings are sent back by google
		String regId = intent.getStringExtra("registration_id");
		String error = intent.getStringExtra("error");
		String unregistered = intent.getStringExtra("unregistered");

		if (error != null) {
			// If there is an error, then we log the error
			Log.e("buzzer", String.format("Received error: %s\n", error));
			if (error.equals("ACCOUNT MISSING")) {
				// ACCOUNT MISSING is sent back when the device doesn't have a
				// google account registered
				Toast.makeText(context,
						"Please add a google account to your device.",
						Toast.LENGTH_LONG).show();
			} else {
				// Other errors
				Toast.makeText(context, "Registration Error: " + error,
						Toast.LENGTH_LONG).show();
			}
		} else if (unregistered != null) {
			// This is returned when you are unregistering your device from c2dm
			Log.d("buzzer", String.format("Unregistered: %s\n", unregistered));
			Toast.makeText(context, "Unregistered: " + unregistered,
					Toast.LENGTH_LONG).show();

			// Clear the shared prefs
			((BuzzerApplication) context.getApplicationContext())
					.setRegId(null);
			// Update our Home Activity
			// updateHome(context);

		} else if (regId != null) {
			// You will get a regId if nothing goes wrong and you tried to
			// register a device
			Log.d("buzzer", String.format("Got regId: %s", regId));

			// Store it into shared prefs
			((BuzzerApplication) context.getApplicationContext())
					.setRegId(regId);
			// Update our Home Activity
			// updateHome(context);
		}
	}

	private void handleData(Context context, Intent intent) {
		String app_name = (String) context.getText(R.string.app_name);
		String message = intent.getStringExtra("message");
		String restaurantId = intent.getStringExtra("restaurant_id");
		Log.d("buzzer", "C2DM Message: " + message + " Restaurant id: "
				+ restaurantId);

		// Use the Notification manager to send notification
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Create a notification using android stat_notify_chat icon.
		Notification notification = new Notification(
				android.R.drawable.stat_notify_chat, app_name + ": " + message,
				0);

		// Create a pending intent to call the Activity when the
		// notification is clicked
		// TODO: Create a new activity to display when user's seat is ready
		PendingIntent pendingIntent = PendingIntent.getActivity(context, -1,
				new Intent(context, UserAcceptsSeat.class),
				PendingIntent.FLAG_UPDATE_CURRENT); //
		notification.when = System.currentTimeMillis();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// Set the notification and register the pending intent to it
		notification.setLatestEventInfo(context, app_name, message,
				pendingIntent); //

		// Trigger the notification
		notificationManager.notify(0, notification);
		playNotificationSound(context);
	}

	public static void playNotificationSound(Context context) {
		Uri uri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		if (uri != null) {
			Ringtone rt = RingtoneManager.getRingtone(context, uri);
			if (rt != null) {
				rt.setStreamType(AudioManager.STREAM_NOTIFICATION);
				rt.play();
			}
		}
	}

}
