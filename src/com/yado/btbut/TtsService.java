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

	public TtsService() {
		super("TtsService");
	}

	// TTS object
	private TextToSpeech myTTS;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		myTTS = new TextToSpeech(this, this); // this gives an error if called in onHandleIntent...
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// setup TTS
	public void onInit(int initStatus) {
		// check for successful instantiation
		if (initStatus == TextToSpeech.SUCCESS) {
			myTTS.setLanguage(Locale.getDefault());
			Calendar cal = Calendar.getInstance();
			int minute = cal.get(Calendar.MINUTE);
			int hourofday = cal.get(Calendar.HOUR_OF_DAY);
			// http://forum.wordreference.com/showthread.php?t=1666181
			String tospeak = "It's " + Integer.toString(hourofday) + " "
					+ Integer.toString(minute) + ".";
			myTTS.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null);
		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
	}
}
