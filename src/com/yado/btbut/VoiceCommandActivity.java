package com.yado.btbut;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class VoiceCommandActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GlobalState appState = ((GlobalState) this.getApplicationContext());
		boolean remap = appState.getRemap();

		if (remap) {
			new MyNotification(this);

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

			finish();
		} else {
			Intent i = new Intent(Intent.ACTION_VOICE_COMMAND);
			i.setPackage("com.google.android.googlequicksearchbox");
			startActivity(i);

			finish();
		}
	}
}
