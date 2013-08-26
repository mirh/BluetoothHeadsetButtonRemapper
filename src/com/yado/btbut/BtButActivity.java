package com.yado.btbut;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

// http://developer.plantronics.com/blogs/Cary/2012/11/26/plugging-into-plantronics-headset-sensor-events-via-android
// http://stackoverflow.com/questions/15872941/how-to-control-music-player-using-voice

public class BtButActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		startService(new Intent(this, BtButService.class));
		super.onCreate(savedInstanceState);
		new MyNotification(this);
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
