package com.yado.btbut;

// http://android-developers.blogspot.de/2013/05/handling-phone-call-requests-right-way.html

import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

public class CallHandler extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		GlobalState appState = ((GlobalState)context.getApplicationContext());
		int btState = appState.getState();

		if (btState == BluetoothProfile.STATE_CONNECTED) 
		{
			// play pause Smart AudioBook Player
			Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);     
			i.setPackage("ak.alizandro.smartaudiobookplayer");
	
			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			context.sendBroadcast(i, null);
	
			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			context.sendBroadcast(i, null);
			/*
			Toast toast;
			toast = Toast.makeText(context, "play/pause", Toast.LENGTH_SHORT);
			toast.show();
			*/
			// cancel the broadcast
			setResultData(null);
		}
	}
}
