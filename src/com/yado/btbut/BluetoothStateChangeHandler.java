package com.yado.btbut;

// http://android-developers.blogspot.de/2013/05/handling-phone-call-requests-right-way.html
// http://stackoverflow.com/questions/10516722/android-notification-with-buttons-on-it

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothStateChangeHandler extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		int currStatus = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
		int prevStatus = intent.getIntExtra(
				BluetoothProfile.EXTRA_PREVIOUS_STATE, -1);

		GlobalState appState = ((GlobalState) context.getApplicationContext());

		if (currStatus == BluetoothProfile.STATE_CONNECTED) {
			// set remapping status to true
			appState.setRemap(true);
			appState.setBluetoothConnected(true);

			// notify user over TTS
			Intent ServiceIntent = new Intent(context, TtsService.class);
			ServiceIntent.putExtra("todo", "startup");
			context.startService(ServiceIntent);
			
			new MyNotification(context);
		}

		if (currStatus == BluetoothProfile.STATE_DISCONNECTED
				&& prevStatus == BluetoothProfile.STATE_CONNECTED) {
			// set remapping status
			appState.setRemap(false);
			appState.setBluetoothConnected(false);
			appState.setPlayState(false);
			new MyNotification(context);

			// turn off blutooth
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			if (mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.disable();
			}
		}
	}
}
