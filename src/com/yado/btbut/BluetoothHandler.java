package com.yado.btbut;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class BluetoothHandler extends Handler {

	private Activity mContext;
	Handler handler = new Handler();

	@Override
	public void handleMessage(Message msg) {

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("i m in handler");
				Toast.makeText(mContext, "this is toast", Toast.LENGTH_SHORT)
						.show();
			}
		}, 1000);
	}
}