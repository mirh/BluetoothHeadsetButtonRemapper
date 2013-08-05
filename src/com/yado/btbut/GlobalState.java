package com.yado.btbut;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class GlobalState extends Application {

	public boolean getRemap() {
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		return sharedPref.getBoolean(getString(R.string.preference_key), true);
	}

	public void setRemap(boolean newRemap) {
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.preference_key), newRemap);
		editor.commit();
	}

	public void toggleRemap()
	{
		if (!getRemap()) {
			setRemap(true);
		}
		else {
			setRemap(false);
		}
	}
}