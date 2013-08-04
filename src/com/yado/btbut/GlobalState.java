package com.yado.btbut;

import android.app.Application;

public class GlobalState extends Application {

	private boolean remap;

	public boolean getRemap() {
		return remap;
	}

	public void setRemap(boolean newRemap) {
		this.remap = newRemap;
	}
	
	public void toggleRemap()
	{
		if (remap == false) {
			remap = true;
		}
		else
		{
			remap = false;
		}
	}
}