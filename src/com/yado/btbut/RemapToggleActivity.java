package com.yado.btbut;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class RemapToggleActivity extends FragmentActivity implements
		PlayerChooserDialogFragment.NoticeDialogListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RemapToggle((String) getIntent().getExtras().get("DO"));
		// finish();
	}

	private void RemapToggle(String action) {
		if (action.equals("toggle_remap")) {
			GlobalState appState = ((GlobalState) getApplicationContext());
			appState.toggleRemap();
			new MyNotification(this);
			finish();
		}

		if (action.equals("change_player")) {
			DialogFragment dialog = new PlayerChooserDialogFragment();
			dialog.show(getFragmentManager(), "NoticeDialogFragment");
			new MyNotification(this);
		}
	}

	@Override
	public void onDialogClick(String packageName) {
		Toast.makeText(this, "Controlled player set to " + packageName,
				Toast.LENGTH_SHORT).show();
		GlobalState appState = ((GlobalState) this.getApplicationContext());
		appState.setAppToControl(packageName);
		finish();
	}

	@Override
	public void onDialogCanceled() {
		finish();
	}
}
