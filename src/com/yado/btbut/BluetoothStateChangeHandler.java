package com.yado.btbut;

// http://android-developers.blogspot.de/2013/05/handling-phone-call-requests-right-way.html


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class BluetoothStateChangeHandler extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		int currStatus = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
		int prevStatus = intent.getIntExtra(BluetoothProfile.EXTRA_PREVIOUS_STATE, -1);

		GlobalState appState = ((GlobalState)context.getApplicationContext());
		appState.setState(currStatus);

		if (currStatus == BluetoothProfile.STATE_DISCONNECTED && prevStatus == BluetoothProfile.STATE_CONNECTED)
		{
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
			if (mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.disable(); 
			}
		}

		Toast toast;
		toast = Toast.makeText(context, Integer.toString(currStatus), Toast.LENGTH_SHORT);
		toast.show();
	}
}

