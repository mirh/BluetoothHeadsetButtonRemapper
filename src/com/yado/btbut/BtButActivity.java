package com.yado.btbut;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

// http://developer.plantronics.com/blogs/Cary/2012/11/26/plugging-into-plantronics-headset-sensor-events-via-android
// http://stackoverflow.com/questions/15872941/how-to-control-music-player-using-voice

public class BtButActivity extends Activity {

	DevicePolicyManager deviceManger;
	ActivityManager activityManager;
	ComponentName compName;
	static final int RESULT_ENABLE = 1;
	// BroadcastReceiver ScreenStateBroadCast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// startService(new Intent(this, BtButService.class));
		super.onCreate(savedInstanceState);

		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			GlobalState appState = ((GlobalState) getApplicationContext());
			appState.setBluetoothConnected(false);
		}
		new MyNotification(this);

		deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		compName = new ComponentName(this, MyAdmin.class);

		boolean active = deviceManger.isAdminActive(compName);
		if (!active) {
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					"This is necessary to keep the screen off when handling Bluetooth headset call last number.");
			startActivityForResult(intent, RESULT_ENABLE);
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
