package com.yado.btbut;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

// http://developer.plantronics.com/blogs/Cary/2012/11/26/plugging-into-plantronics-headset-sensor-events-via-android
// http://stackoverflow.com/questions/15872941/how-to-control-music-player-using-voice

public class BtButActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		startService(new Intent(this, BtButService.class));
		super.onCreate(savedInstanceState);
		new MyNotification(this);

		GlobalState appState = ((GlobalState) this.getApplicationContext());
		File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.yado.btbut/");
		if (!(dir.exists() && dir.isDirectory())) {
			appState.setTtsInit(false);
		}
		
		if (appState.getTtsInit() != true) {
			Intent ServiceIntent = new Intent(this, TtsService.class);
			ServiceIntent.putExtra("todo", "generate");
			startService(ServiceIntent);
		}

		finish();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
