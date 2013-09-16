package com.yado.btbut;

// http://developer.android.com/guide/topics/ui/dialogs.html#AlertDialog

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class PlayerChooserDialogFragment extends DialogFragment {
	public interface NoticeDialogListener {
		public void onDialogClick(String packageName);

		public void onDialogCanceled();
	}

	NoticeDialogListener mListener;
	String[] apps;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (NoticeDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		/*
		 * final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		 * mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		 * 
		 * //
		 * http://stackoverflow.com/questions/4586684/how-to-get-a-list-of-installed
		 * -media-players PackageManager pm = getActivity().getPackageManager();
		 * Intent mainIntent = new Intent(Intent.ACTION_VIEW); Uri uri =
		 * Uri.withAppendedPath
		 * (MediaStore.Audio.Media.INTERNAL_CONTENT_URI,"1"); //
		 * mainIntent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI); //
		 * mainIntent
		 * .setDataAndType(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
		 * "media/*"); mainIntent.setType("media/*"); final List<ResolveInfo>
		 * pkgAppsList = pm.queryIntentActivities( mainIntent, 0);
		 */

		PackageManager packageManager = getActivity().getPackageManager();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.withAppendedPath(
				MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "1");
		intent.setData(uri);
		List<ResolveInfo> pkgAppsList;
		pkgAppsList = packageManager.queryIntentActivities(intent, 0);

		/*
		apps = new String[pkgAppsList.size()];
		for (int i = 0; i < pkgAppsList.size(); i++) {
			apps[i] = pkgAppsList.get(i).activityInfo.name;
			/*
			 * try { apps[i] = (String)
			 * pm.getApplicationLabel(pm.getApplicationInfo
			 * (pkgAppsList.get(i).activityInfo.processName,
			 * PackageManager.GET_META_DATA)); } catch (NameNotFoundException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
		// }
		
		apps = new String[5];
		apps[0] = "ak.alizandro.smartaudiobookplayer";
		apps[1] = "com.hyperionics.fbreader.plugin.tts_plus";
		apps[2] = "com.hyperionics.avar";
		apps[3] = "tunein.player";
		apps[4] = "com.google.android.music";

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose media app to control");
		builder.setCancelable(false);
		builder.setItems(apps, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogClick(apps[which]);
				dialog.dismiss();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		mListener.onDialogCanceled();
	}
}