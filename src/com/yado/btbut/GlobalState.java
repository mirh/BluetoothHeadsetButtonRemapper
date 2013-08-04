package com.yado.btbut;

import android.app.Application;

public class GlobalState extends Application {

	private int state;

	public int getState() {
		return state;
	}

	public void setState(int newState) {
		this.state = newState;
	}
}