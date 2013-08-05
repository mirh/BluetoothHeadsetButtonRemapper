package com.yado.btbut;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class RemapToggle extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GlobalState appState = ((GlobalState)getApplicationContext());
		appState.toggleRemap();
		
		boolean remap = appState.getRemap();
		String notify;
		if (remap) {
			notify = "Button remapping active";
		}
		else {
			notify = "Button remapping inactive";
		}
		
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Bluetooth Button")
		.setContentText(notify);
		
		Intent resultIntent = new Intent(this, RemapToggle.class);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(RemapToggle.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
						);
		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(getResources().getInteger(R.integer.mId), mBuilder.build());
		finish();
	}
}
