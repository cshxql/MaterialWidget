package com.lion.demo.material.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.lion.demo.material.R;
import com.lion.material.widget.PreferenceCheckBox;
import com.lion.material.widget.PreferenceNormal;


public class LPreferenceActivity extends BaseActivity implements
		OnClickListener {
	private PreferenceCheckBox mPreferenceCheckBox;
	private PreferenceNormal mPreferenceNormal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lpreference);
		initView();
	}

	private void initView() {
		findViewById(R.id.header_left).setOnClickListener(this);
		mPreferenceCheckBox = (PreferenceCheckBox) findViewById(R.id.lpreference_checkbox);
		mPreferenceCheckBox
				.setOnCheckedChangeListener(new PreferenceCheckBox.OnLPreferenceSwitchListener() {

					@Override
					public void onLCheckedChanged(View v, boolean isChecked) {
						// Toast.makeText(mContext, "checked-->" + isChecked,
						// Toast.LENGTH_SHORT).show();
						mPreferenceCheckBox.setSummary("status:" + isChecked);
					}
				});
		mPreferenceNormal = (PreferenceNormal) findViewById(R.id.lpreference_normal);
		mPreferenceNormal.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_left:
			finish();
			break;
		case R.id.lpreference_normal:
			Toast.makeText(mContext, R.string.lpreference_toast_normal_click,
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

}
