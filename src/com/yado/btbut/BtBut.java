package com.yado.btbut;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.os.Bundle;

// http://developer.plantronics.com/blogs/Cary/2012/11/26/plugging-into-plantronics-headset-sensor-events-via-android
// http://stackoverflow.com/questions/15872941/how-to-control-music-player-using-voice

public class BtBut extends Activity {

	Intent enableBtIntent;
	BluetoothAdapter mBluetoothAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
		startService(new Intent(this, BtButService.class));
		super.onCreate(savedInstanceState);
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
