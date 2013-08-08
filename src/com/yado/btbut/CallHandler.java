package com.yado.btbut;

// http://android-developers.blogspot.de/2013/05/handling-phone-call-requests-right-way.html

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.widget.Toast;

public class CallHandler extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		GlobalState appState = ((GlobalState)context.getApplicationContext());
		boolean remap = appState.getRemap();
		
		if (remap)
		{
			// show notification that button remapping is active
			NotificationCompat.Builder mBuilder =
					new NotificationCompat.Builder(context)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("Bluetooth Button")
			.setContentText("Button remapping active");

			Intent resultIntent = new Intent(context, RemapToggleActivity.class);

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(BtButActivity.class);
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
			mNotificationManager.notify(context.getResources().getInteger(R.integer.mId), mBuilder.build());
			
			// http://stackoverflow.com/questions/3907062/action-media-button-does-not-work-on-real-device
		
			// play pause Smart AudioBook Player
			Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
			i.setPackage("ak.alizandro.smartaudiobookplayer");
			// i.setPackage("com.hyperionics.fbreader.plugin.tts_plus");

			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			context.sendOrderedBroadcast(i, null);

			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			context.sendOrderedBroadcast(i, null);
/*
			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
			context.sendBroadcast(i, null);

			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
			context.sendBroadcast(i, null);
			
			/*
			Toast toast;
			toast = Toast.makeText(context, "play/pause", Toast.LENGTH_SHORT);
			toast.show();
			 */
			// cancel the broadcast
			setResultData(null);
		}
	}
}
