package com.lion.material.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity {
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		if (getRequestedOrientation() != orientation) {
			setRequestedOrientation(orientation);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mContext = this;
	}

	private boolean mCalled = false;

	@Override
	public void setRequestedOrientation(int requestedOrientation) {
		if (!mCalled) {
			super.setRequestedOrientation(requestedOrientation);
			mCalled = true;
		}
	}


}
