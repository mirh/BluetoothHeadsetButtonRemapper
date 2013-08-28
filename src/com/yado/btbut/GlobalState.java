package com.yado.btbut;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class GlobalState extends Application {

	public boolean getRemap() {
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		return sharedPref.getBoolean(getString(R.string.button_remapping_key),
				true);
	}

	public void setRemap(boolean newRemap) {
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.button_remapping_key), newRemap);
		editor.commit();
	}

	public void toggleRemap() {
		if (!getRemap()) {
			setRemap(true);
		} else {
			setRemap(false);
		}
	}

	public void setBluetoothConnected(boolean newBluetoothConnected) {
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.bluetooth_connected_key),
				newBluetoothConnected);
		editor.commit();
	}

	public boolean getBluetoothConnected() {
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		return sharedPref.getBoolean(
				getString(R.string.bluetooth_connected_key), true);
	}

	public boolean getPlayState() {
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		return sharedPref.getBoolean(getString(R.string.play_state_key), true);
	}

	public void setPlayState(boolean newPlayState) {
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.play_state_key), newPlayState);
		editor.commit();
	}
	
	public boolean getTtsInit() {
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		return sharedPref.getBoolean(getString(R.string.tts_init_key), true);
	}

	public void setTtsInit(boolean newTtsInit) {
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.tts_init_key), newTtsInit);
		editor.commit();
	}
}