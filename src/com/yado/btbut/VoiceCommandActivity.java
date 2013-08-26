package com.yado.btbut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class VoiceCommandActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// check if remapping is activated by user
		GlobalState appState = ((GlobalState) this.getApplicationContext());
		boolean remap = appState.getRemap();
		
		if (remap) {
			// start button event handling activity
			Intent iMainHandler = new Intent(this, MainHandlerActivity.class);
			iMainHandler.putExtra("todo", "VoiceCommandHandle");
			iMainHandler.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(iMainHandler);
		}
		else
		{
			Intent iVoiceCommand = new Intent(Intent.ACTION_VOICE_COMMAND);
			iVoiceCommand.setPackage("com.google.android.googlequicksearchbox");
			startActivity(iVoiceCommand);
		}
		finish();
	}
}
