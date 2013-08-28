package com.yado.btbut;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
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
		// TODO Auto-generated method stub
		return null;
	}

	// setup TTS
	public void onInit(int initStatus) {
		// http://android-developers.blogspot.de/2009/09/introduction-to-text-to-speech-in.html
		// check for successful instantiation
		if (initStatus == TextToSpeech.SUCCESS) {
			myTTS.setLanguage(Locale.ENGLISH);
			
			String destFileName;
			String destDirName = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.yado.btbut/";

			if (todoTTS.equals("time")) {
				Calendar cal = Calendar.getInstance();
				int minute = cal.get(Calendar.MINUTE);
				int hourofday = cal.get(Calendar.HOUR_OF_DAY);
				// http://forum.wordreference.com/showthread.php?t=1666181
				String tospeak = "It's " + Integer.toString(hourofday) + " "
						+ Integer.toString(minute) + ".";
				/*
				myTTS.addSpeech(Integer.toString(hourofday), destDirName + Integer.toString(hourofday) + ".wav");
				myTTS.speak(Integer.toString(hourofday), TextToSpeech.QUEUE_ADD, null);
				
				myTTS.addSpeech(Integer.toString(minute), destDirName + Integer.toString(minute) + ".wav");
				myTTS.speak(Integer.toString(minute), TextToSpeech.QUEUE_ADD, null);
				*/
				myTTS.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null);
			} else if (todoTTS.equals("startup")) {
				String tospeak = "Remapping active.";
				/*
				myTTS.addSpeech(tospeak, destDirName + "remapping_active.wav");
				myTTS.speak(tospeak, TextToSpeech.QUEUE_ADD, null);
				*/
				myTTS.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null);
			} else if (todoTTS.equals("generate")) {
				HashMap<String, String> myHashRender = new HashMap<String, String>();
				myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
						getString(R.string.remapping_active_text));
				
				// have the object build the directory structure, if needed.
				File soundsDirectory = new File(destDirName);
				soundsDirectory.mkdirs();
				
				destFileName = "remapping_active.wav";
				
				if (myTTS.synthesizeToFile(
						getString(R.string.remapping_active_text),
						myHashRender, destDirName + destFileName) == TextToSpeech.SUCCESS)
				{
					Toast.makeText(this, "success", Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(this, "no oooo success", Toast.LENGTH_LONG).show();
				}

				for (int i = 0; i < 60; i++) {
					destFileName = Integer.toString(i) + ".wav";

					if (i % 10 == 0) {
						Toast.makeText(this, "Generating voice file " + Integer.toString(i) + " of 61.", Toast.LENGTH_LONG).show();
					}
					
					if (i == 0) {
						myTTS.synthesizeToFile("oh",
								myHashRender, destDirName + destFileName);
					} else {
						myTTS.synthesizeToFile(Integer.toString(i),
								myHashRender, destDirName + destFileName);
					}
				}
				GlobalState appState = ((GlobalState) this.getApplicationContext());
				appState.setTtsInit(true);
				Toast.makeText(this, "Finished generating voice files.", Toast.LENGTH_LONG).show();
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
