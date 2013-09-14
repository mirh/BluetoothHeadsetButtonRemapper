package com.yado.btbut;

// http://android-developers.blogspot.de/2013/05/handling-phone-call-requests-right-way.html

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class CallHandler extends BroadcastReceiver {

	DevicePolicyManager deviceManger;
	ComponentName compName;

	@Override
	public void onReceive(Context context, Intent intent) {
		// check if remapping is activated by user
		GlobalState appState = ((GlobalState) context.getApplicationContext());
		boolean remap = appState.getRemap();

		if (remap) {
			deviceManger = (DevicePolicyManager) context
					.getSystemService(Context.DEVICE_POLICY_SERVICE);
			compName = new ComponentName(context, MyAdmin.class);
			boolean active = deviceManger.isAdminActive(compName);
			

			// start button event handling activity
			Intent ServiceIntent = new Intent(context, MainHandlerService.class);
			ServiceIntent.putExtra("todo", "CallHandle");
			context.startService(ServiceIntent);

			this.abortBroadcast();
			if (active) {
//				deviceManger.lockNow();
			}
			// cancel the call broadcast
			setResultData(null);
		}
	}
}
/*
 * //
 * http://mobile.tutsplus.com/tutorials/android/android-sdk-using-the-text-to-
 * speech-engine/ private void speakWords(String speech) { Intent checkTTSIntent
 * = new Intent();
 * checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
 * startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE); }
 * 
 * 
 * @Override public void onInit(int status) { // TODO Auto-generated method stub
 * 
 * }
 * 
 * //act on result of TTS data check protected void onActivityResult(int
 * requestCode, int resultCode, Intent data) { if (requestCode ==
 * MY_DATA_CHECK_CODE) { if (resultCode ==
 * TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) { //the user has the necessary
 * data - create the TTS myTTS = new TextToSpeech(this, this); } else { //no
 * data - install it now Intent installTTSIntent = new Intent();
 * installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
 * startActivity(installTTSIntent); } } }
 */

