package com.yado.btbut;

// http://android-developers.blogspot.de/2013/05/handling-phone-call-requests-right-way.html

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.WindowManager;

public class CallHandler extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		GlobalState appState = ((GlobalState) context.getApplicationContext());
		boolean remap = appState.getRemap();

		if (remap) {
			new MyNotification(context);
			// http://stackoverflow.com/questions/3907062/action-media-button-does-not-work-on-real-device

			// http://stackoverflow.com/questions/4212992/how-can-i-check-if-an-app-running-in-android
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService("activity");
			List<RunningAppProcessInfo> pkgAppsList = activityManager
					.getRunningAppProcesses();
			String packageToControl = "ak.alizandro.smartaudiobookplayer";
			for (int i = 0; i < pkgAppsList.size(); i++) {
				if (pkgAppsList.get(i).processName
						.equals("com.hyperionics.fbreader.plugin.tts_plus")) {
					if (pkgAppsList.get(i).importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
						packageToControl = "com.hyperionics.fbreader.plugin.tts_plus";
					}
				}
			}

			// play pause Smart AudioBook Player
			Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
			if (!packageToControl.equals("")) {
				i.setPackage(packageToControl);
			}

			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
					KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			context.sendBroadcast(i, null);

			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
					KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			context.sendBroadcast(i, null);

			/*
			 * i.putExtra(Intent.EXTRA_KEY_EVENT, new
			 * KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
			 * context.sendBroadcast(i, null);
			 * 
			 * i.putExtra(Intent.EXTRA_KEY_EVENT, new
			 * KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
			 * context.sendBroadcast(i, null);
			 */

		    /* NEEDS to be started on real activity... try some other time
		    // http://stackoverflow.com/questions/6756768/turn-off-screen-on-android
		    // http://stackoverflow.com/questions/10687535/how-can-i-change-the-brightness-of-the-screen-in-broadcastreceiver
		    WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
		    // params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
		    params.screenBrightness = 0;
		    ((Activity) context).getWindow().setAttributes(params);
		    */
			
			/*
			 * Toast toast; toast = Toast.makeText(context, "play/pause",
			 * Toast.LENGTH_SHORT); toast.show();
			 */
			// cancel the broadcast
			setResultData(null);
		}
	}
}
