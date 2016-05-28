package com.lion.demo.material.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class BaseActivity extends FragmentActivity {
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
