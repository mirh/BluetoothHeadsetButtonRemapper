package com.yado.btbut;

// http://stackoverflow.com/questions/10516722/android-notification-with-buttons-on-it

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyNotification extends Notification {

	private Context context;
	private NotificationManager mNotificationManager;
	private String buttonText;
	private CharSequence tickerText;

	public MyNotification(Context context) {
		super();

		GlobalState appState = ((GlobalState) context.getApplicationContext());
		boolean remapState = appState.getRemap();
		boolean bluetoothConnected = appState.getBluetoothConnected();

		this.context = context;
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (bluetoothConnected) {
			if (remapState) {
				buttonText = "Disable remapping";
				tickerText = "Remapping enabled";
			} else {
				buttonText = "Enable remapping";
				tickerText = "Remapping disabled";
			}
			RemoteViews contentView = new RemoteViews(context.getPackageName(),
					R.layout.not_layout);
			contentView.setTextViewText(R.id.toggle_remap, buttonText);
			contentView.setTextViewText(R.id.change_player, "change player");
			setListeners(contentView);

			Notification.Builder builder = new Notification.Builder(context);
			Notification notification = builder.build();
			notification.when = System.currentTimeMillis();
			notification.tickerText = tickerText;
			notification.icon = R.drawable.ic_launcher;
			notification.contentView = contentView;
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			mNotificationManager.notify(
					context.getResources().getInteger(R.integer.mId),
					notification);
		} else {
			mNotificationManager.cancel(context.getResources().getInteger(
					R.integer.mId));
		}
	}

	public void setListeners(RemoteViews view) {
		Intent remapIntent = new Intent(context, RemapToggleActivity.class);
		remapIntent.putExtra("DO", "toggle_remap");
		PendingIntent pRemapIntent = PendingIntent.getActivity(context, 0,
				remapIntent, 0);
		view.setOnClickPendingIntent(R.id.toggle_remap, pRemapIntent);

		Intent playerIntent = new Intent(context, RemapToggleActivity.class);
		playerIntent.putExtra("DO", "change_player");
		PendingIntent pPlayerIntent = PendingIntent.getActivity(context, 1,
				playerIntent, 0);
		view.setOnClickPendingIntent(R.id.change_player, pPlayerIntent);
	}
}
