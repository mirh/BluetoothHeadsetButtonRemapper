package com.yado.btbut;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainHandlerActivity extends Activity implements OnInitListener {

	// TTS object
	private TextToSpeech myTTS;
	Boolean SmAuBPlactive;

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

		if (remap) {
			if (todo.equals("CallHandle")) {
				new MyNotification(this);
				// http://stackoverflow.com/questions/3907062/action-media-button-does-not-work-on-real-device

				// http://stackoverflow.com/questions/4212992/how-can-i-check-if-an-app-running-in-android
				ActivityManager activityManager = (ActivityManager) getSystemService("activity");
				List<RunningAppProcessInfo> pkgAppsList = activityManager
						.getRunningAppProcesses();
				String packageToControl = "ak.alizandro.smartaudiobookplayer";
				SmAuBPlactive = false;
				for (int i = 0; i < pkgAppsList.size(); i++) {
					if (pkgAppsList.get(i).processName.equals(packageToControl)) {
						if (pkgAppsList.get(i).importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
							SmAuBPlactive = true;
						}
					}
				}
				
				/*
				for (int i = 0; i < pkgAppsList.size(); i++) {
					if (pkgAppsList.get(i).processName.equals("com.hyperionics.fbreader.plugin.tts_plus")) {
						if (pkgAppsList.get(i).importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
							packageToControl = "com.hyperionics.fbreader.plugin.tts_plus";
						}
					}
				}
				*/

				if (!SmAuBPlactive) {
					myTTS = new TextToSpeech(this, this);
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

				i.putExtra(Intent.EXTRA_KEY_EVENT,
						new KeyEvent(KeyEvent.ACTION_DOWN,
								KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
				this.sendBroadcast(i, null);

				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
						KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
				this.sendBroadcast(i, null);
			}
		}
		// finish();
	}

	// speak the user text
	private void speakWords(String speech) {
		// speak straight away
		myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
	}

	// setup TTS
	public void onInit(int initStatus) {
		// check for successful instantiation
		if (initStatus == TextToSpeech.SUCCESS) {
			myTTS.setLanguage(Locale.getDefault());
			Calendar cal = Calendar.getInstance();
			int minute = cal.get(Calendar.MINUTE);
			int hourofday = cal.get(Calendar.HOUR_OF_DAY);
			String minuteSingPlur;
			if (minute == 1) {
				minuteSingPlur = "minute";
			}
			else {
				minuteSingPlur = "minutes";
			}
			// http://forum.wordreference.com/showthread.php?t=1666181
			// String tospeak = "It's " + Integer.toString(minute) + " " + minuteSingPlur + " past " + Integer.toString(hourofday) + ".";
			String tospeak = "It's " + Integer.toString(hourofday) + " " + Integer.toString(minute) + ".";
			myTTS.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null);
			finish();
		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onDestroy() {
		if (myTTS != null) {
			myTTS.stop();
			myTTS.shutdown();
		}
		super.onDestroy();
	}
}
