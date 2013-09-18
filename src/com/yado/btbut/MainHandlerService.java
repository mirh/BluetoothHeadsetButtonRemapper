package com.yado.btbut;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.admin.DevicePolicyManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainHandlerService extends IntentService {

	public MainHandlerService() {
		super("MainHandlerService");
	}

	// TTS object
	Boolean SmAuBPactive;
	int todoPlayer;

	Intent i;

	BroadcastReceiver ScreenStateBroadCast;
	DevicePolicyManager deviceManger;
	ComponentName compName;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// http://derekknox.com/daklab/2012/04/18/tutorial-how-to-create-invisible-apps-in-android/
		// this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		// // click
		// through-able

		// show notification
		new MyNotification(this);

		// get screen unlock attempt due to call handler
		ScreenStateBroadCast = new BroadcastReceiver() {
			// When Event is published, onReceive method is called
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
					Log.w("btBut", "on received");
					GlobalState appState = ((GlobalState) context
							.getApplicationContext());
					appState.setScreenState(true);
				} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
					Log.w("btBut", "off received");
					GlobalState appState = ((GlobalState) context
							.getApplicationContext());
					appState.setScreenState(false);
				}
			}
		};

		// start receiving screen on off events
		registerReceiver(ScreenStateBroadCast, new IntentFilter(
				Intent.ACTION_SCREEN_ON));
		registerReceiver(ScreenStateBroadCast, new IntentFilter(
				Intent.ACTION_SCREEN_OFF));

		// get todo action
		String todo = intent.getStringExtra("todo");

		// get remapping state
		GlobalState appState = ((GlobalState) this.getApplicationContext());
		boolean remap = appState.getRemap();

		if (remap) {
			if (todo.equals("CallHandle")) {
				new MyNotification(this);
				// http://stackoverflow.com/questions/3907062/action-media-button-does-not-work-on-real-device

				// unregisterReceiver(ScreenStateBroadCast);

				// http://stackoverflow.com/questions/4212992/how-can-i-check-if-an-app-running-in-android
				/*
				 * String packageToControl =
				 * "ak.alizandro.smartaudiobookplayer"; ActivityManager
				 * activityManager = (ActivityManager)
				 * getSystemService("activity"); List<RunningAppProcessInfo>
				 * pkgAppsList = activityManager .getRunningAppProcesses();
				 */
				String packageToControl = appState.getAppToControl();

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
					for (int ii = 0; ii < 1; ii++) {
						i = new Intent(Intent.ACTION_MEDIA_BUTTON);
						if (!packageToControl.equals("")) {
							i.setPackage(packageToControl);
						}

						Handler handler = new Handler();

						handler.postDelayed(new Runnable() {
							public void run() {
								i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
										KeyEvent.ACTION_DOWN,
										KeyEvent.KEYCODE_MEDIA_PREVIOUS));
								sendBroadcast(i, null);
							}
						}, 200);

						handler.postDelayed(new Runnable() {
							public void run() {
								i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
										KeyEvent.ACTION_UP,
										KeyEvent.KEYCODE_MEDIA_PREVIOUS));
								sendBroadcast(i, null);
							}
						}, 200);
					}
				}
				// check if we are admin to be able to lock screen
				deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
				compName = new ComponentName(this, MyAdmin.class);
				boolean active = deviceManger.isAdminActive(compName);
				if (active && !appState.getScreenState()) {
					deviceManger.lockNow();
					deviceManger.lockNow(); // call it twice, otherwise
											// sometimes the ON receiver is
											// called after the off receiver for
											// some reason...
					appState.setScreenState(false);
				}
			} else if (todo.equals("VoiceCommandHandle")) {
				// http://stackoverflow.com/questions/4212992/how-can-i-check-if-an-app-running-in-android
				ActivityManager activityManager = (ActivityManager) getSystemService("activity");
				List<RunningAppProcessInfo> pkgAppsList = activityManager
						.getRunningAppProcesses();

				String packageToControl = appState.getAppToControl();

				boolean found = false;
				for (int i = 0; i < pkgAppsList.size(); i++) {
					if (pkgAppsList.get(i).processName.equals(packageToControl)) {
						found = true;
						// Toast.makeText(this, "no need to start", Toast.LENGTH_LONG).show();
						/*
						 * if (pkgAppsList.get(i).importance ==
						 * RunningAppProcessInfo.IMPORTANCE_VISIBLE) { Intent
						 * LaunchIntent = getPackageManager()
						 * .getLaunchIntentForPackage(packageToControl);
						 * startActivity(LaunchIntent); }
						 */
					}
				}

				if (!found) {
					Intent LaunchIntent = getPackageManager()
							.getLaunchIntentForPackage(packageToControl);
					startActivity(LaunchIntent);
				}

				i = new Intent(Intent.ACTION_MEDIA_BUTTON);
				if (!packageToControl.equals("")) {
					i.setPackage(packageToControl);
				}

				if (appState.getPlayState()) {
					todoPlayer = KeyEvent.KEYCODE_MEDIA_PAUSE;
					// Toast.makeText(this, "pause", Toast.LENGTH_LONG).show();
					appState.setPlayState(false);
				} else {
					todoPlayer = KeyEvent.KEYCODE_MEDIA_PLAY;
					// Toast.makeText(this, "play", Toast.LENGTH_LONG).show();
					appState.setPlayState(true);
				}
				Handler handler = new Handler();

				handler.postDelayed(new Runnable() {
					public void run() {
						i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
								KeyEvent.ACTION_DOWN, todoPlayer));
						sendBroadcast(i, null);
					}
				}, 200);

				handler.postDelayed(new Runnable() {
					public void run() {
						i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
								KeyEvent.ACTION_UP, todoPlayer));
						sendBroadcast(i, null);
					}
				}, 200);

			} else if (todo.equals("TtsStartup")) {
				// notify user over TTS
				Intent ServiceIntent = new Intent(this, TtsService.class);
				ServiceIntent.putExtra("todo", "startup");
				this.startService(ServiceIntent);
			}
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}
}
