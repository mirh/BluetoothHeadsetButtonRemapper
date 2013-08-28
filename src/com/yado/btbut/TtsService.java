package com.yado.btbut;

import java.util.Calendar;
import java.util.Locale;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

public class TtsService extends IntentService implements OnInitListener {

	private String todoTTS;

	public TtsService() {
		super("TtsService");
	}

	// TTS object
	private TextToSpeech myTTS;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String todo = intent.getStringExtra("todo");
		todoTTS = todo;
		myTTS = new TextToSpeech(this, this); // this gives an error if called
												// in onHandleIntent...
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// setup TTS
	public void onInit(int initStatus) {
		// http://android-developers.blogspot.de/2009/09/introduction-to-text-to-speech-in.html
		// check for successful instantiation
		if (initStatus == TextToSpeech.SUCCESS) {
			myTTS.setLanguage(Locale.ENGLISH);

			if (todoTTS.equals("time")) {
				Calendar cal = Calendar.getInstance();
				int minute = cal.get(Calendar.MINUTE);
				int hourofday = cal.get(Calendar.HOUR_OF_DAY);
				// http://forum.wordreference.com/showthread.php?t=1666181
				String tospeak = "It's " + Integer.toString(hourofday) + " "
						+ Integer.toString(minute) + ".";
				myTTS.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null);
			} else if (todoTTS.equals("startup")) {
				String tospeak = "Remapping active.";
				myTTS.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null);
			}
		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}

	@Override
	public void onDestroy() {
		myTTS.shutdown();
		super.onDestroy();
	}
}
