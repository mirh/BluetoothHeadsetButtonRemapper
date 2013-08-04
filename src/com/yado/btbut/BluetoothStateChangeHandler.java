package com.yado.btbut;

// http://android-developers.blogspot.de/2013/05/handling-phone-call-requests-right-way.html


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


public class BluetoothStateChangeHandler extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		int currStatus = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
		int prevStatus = intent.getIntExtra(BluetoothProfile.EXTRA_PREVIOUS_STATE, -1);

		GlobalState appState = ((GlobalState)context.getApplicationContext());
		
		int mId = 1423;
		
		if (currStatus == BluetoothProfile.STATE_CONNECTED)
		{
			// set remapping status to true
			appState.setRemap(true);

			// show notification that button remapping is active
			NotificationCompat.Builder mBuilder =
					new NotificationCompat.Builder(context)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("Bluetooth Button")
			.setContentText("Button remap active");

			Intent resultIntent = new Intent(context, RemapToggle.class);

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(BtBut.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
					stackBuilder.getPendingIntent(
							0,
							PendingIntent.FLAG_UPDATE_CURRENT
							);
			mBuilder.setContentIntent(resultPendingIntent);

			NotificationManager mNotificationManager =
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(mId, mBuilder.build());
		}

		if (currStatus == BluetoothProfile.STATE_DISCONNECTED && prevStatus == BluetoothProfile.STATE_CONNECTED)
		{
			// set remapping status to false
			appState.setRemap(false);

			// clear notification
			NotificationManager mNotificationManager =
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.cancel(mId);

			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
			if (mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.disable(); 
			}
		}

		/*		
		Toast toast;
		toast = Toast.makeText(context, Integer.toString(currStatus), Toast.LENGTH_SHORT);
		toast.show();
		 */
	}
}

