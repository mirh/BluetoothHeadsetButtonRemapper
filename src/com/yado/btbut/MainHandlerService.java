package com.yado.btbut;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.IntentService;
import android.content.Intent;
import android.view.KeyEvent;

public class MainHandlerService extends IntentService {

	public MainHandlerService() {
		super("MainHandlerService");
		// TODO Auto-generated constructor stub
	}

	// TTS object
	Boolean SmAuBPactive;
	int todoPlayer;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// http://derekknox.com/daklab/2012/04/18/tutorial-how-to-create-invisible-apps-in-android/
		// this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		// // click
		// through-able

		// show notification
		new MyNotification(this);

		// get todo action
		String todo = intent.getStringExtra("todo");

		// get remapping state
		GlobalState appState = ((GlobalState) this.getApplicationContext());
		boolean remap = appState.getRemap();

		if (remap) {
			if (todo.equals("CallHandle")) {
				new MyNotification(this);
				// http://stackoverflow.com/questions/3907062/action-media-button-does-not-work-on-real-device

				// http://stackoverflow.com/questions/4212992/how-can-i-check-if-an-app-running-in-android
				String packageToControl = "ak.alizandro.smartaudiobookplayer";
				ActivityManager activityManager = (ActivityManager) getSystemService("activity");
				List<RunningAppProcessInfo> pkgAppsList = activityManager
						.getRunningAppProcesses();
				/*
				 * SmAuBPactive = false; for (int i = 0; i < pkgAppsList.size();
				 * i++) { if
				 * (pkgAppsList.get(i).processName.equals(packageToControl)) {
				 * if (pkgAppsList.get(i).importance ==
				 * RunningAppProcessInfo.IMPORTANCE_VISIBLE) { SmAuBPactive =
				 * true; } } }
				 */
				/*
				 * for (int i = 0; i < pkgAppsList.size(); i++) { if
				 * (pkgAppsList.get(i).processName.equals(
				 * "com.hyperionics.fbreader.plugin.tts_plus")) { if
				 * (pkgAppsList.get(i).importance ==
				 * RunningAppProcessInfo.IMPORTANCE_VISIBLE) { packageToControl
				 * = "com.hyperionics.fbreader.plugin.tts_plus"; } } }
				 */

				if (!appState.getPlayState()) {
					// start TTS service
					// for now only tell time
					Intent ServiceIntent = new Intent(this, TtsService.class);
					ServiceIntent.putExtra("todo", "time");
					startService(ServiceIntent);
				} else {
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

						i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
								KeyEvent.ACTION_UP,
								KeyEvent.KEYCODE_MEDIA_PREVIOUS));
						this.sendBroadcast(i, null);
						/*
						 * http://stackoverflow.com/questions/6756768/turn-off-
						 * screen -on- android
						 * http://stackoverflow.com/questions
						 * /10687535/how-can-i-
						 * change-the-brightness-of-the-screen
						 * -in-broadcastreceiver WindowManager.LayoutParams
						 * params = getWindow().getAttributes();
						 * params.screenBrightness = 0;
						 * getWindow().setAttributes(params);
						 */
					}
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

				if (appState.getPlayState()) {
					todoPlayer = KeyEvent.KEYCODE_MEDIA_PAUSE;
					appState.setPlayState(false);
				} else {
					todoPlayer = KeyEvent.KEYCODE_MEDIA_PLAY;
					appState.setPlayState(true);
				}

				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
						KeyEvent.ACTION_DOWN, todoPlayer));
				this.sendBroadcast(i, null);

				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
						KeyEvent.ACTION_UP, todoPlayer));
				this.sendBroadcast(i, null);
			}
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}
}
