package com.yado.btbut;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainHandlerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// show notification
		new MyNotification(this);
		
		// get todo action
		String todo = getIntent().getStringExtra("todo");

		// get remapping state
		GlobalState appState = ((GlobalState) this.getApplicationContext());
		boolean remap = appState.getRemap();

		if (todo.equals("CallHandle")) {
			if (remap) {
				new MyNotification(this);
				// http://stackoverflow.com/questions/3907062/action-media-button-does-not-work-on-real-device

				// http://stackoverflow.com/questions/4212992/how-can-i-check-if-an-app-running-in-android
				ActivityManager activityManager = (ActivityManager) getSystemService("activity");
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

				// 3xprevious Smart AudioBook Player
				for (int ii = 0; ii < 3; ii++) {
					Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
					if (!packageToControl.equals("")) {
						i.setPackage(packageToControl);
					}

					i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
							KeyEvent.ACTION_DOWN,
							KeyEvent.KEYCODE_MEDIA_PREVIOUS));
					this.sendBroadcast(i, null);

					i.putExtra(Intent.EXTRA_KEY_EVENT,
							new KeyEvent(KeyEvent.ACTION_UP,
									KeyEvent.KEYCODE_MEDIA_PREVIOUS));
					this.sendBroadcast(i, null);
				}

				/*
				 * i.putExtra(Intent.EXTRA_KEY_EVENT, new
				 * KeyEvent(KeyEvent.ACTION_DOWN,
				 * KeyEvent.KEYCODE_HEADSETHOOK)); context.sendBroadcast(i,
				 * null);
				 * 
				 * i.putExtra(Intent.EXTRA_KEY_EVENT, new
				 * KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
				 * context.sendBroadcast(i, null);
				 */

				/*
				 * NEEDS to be started on real activity... try some other time
				 * //
				 * http://stackoverflow.com/questions/6756768/turn-off-screen-
				 * on-android //
				 * http://stackoverflow.com/questions/10687535/how-
				 * can-i-change-the
				 * -brightness-of-the-screen-in-broadcastreceiver
				 * WindowManager.LayoutParams params = ((Activity)
				 * context).getWindow().getAttributes(); // params.flags |=
				 * LayoutParams.FLAG_KEEP_SCREEN_ON; params.screenBrightness =
				 * 0; ((Activity) context).getWindow().setAttributes(params);
				 */

				/*
				 * Toast toast; toast = Toast.makeText(context, "play/pause",
				 * Toast.LENGTH_SHORT); toast.show();
				 */

				/*
				 * Intent turnScreenOffIntent = new Intent(context,
				 * TurnOffDisplayActivity.class);
				 * turnScreenOffIntent.putExtra("DO", "toggle_remap");
				 * turnScreenOffIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * context.startActivity(turnScreenOffIntent);
				 */
			}
		} else if (todo.equals("VoiceCommandHandle")) {
			// http://stackoverflow.com/questions/4212992/how-can-i-check-if-an-app-running-in-android
			ActivityManager activityManager = (ActivityManager) getSystemService("activity");
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

			Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
			if (!packageToControl.equals("")) {
				i.setPackage(packageToControl);
			}

			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
					KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			this.sendBroadcast(i, null);

			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
					KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			this.sendBroadcast(i, null);
		}
		finish();
	}
}
