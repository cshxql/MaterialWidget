package com.lion.material.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lion.material.demo.R;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.main_item_lbutton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(mContext,
								LButtonActivity.class));
					}
				});
		findViewById(R.id.main_item_limagebutton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(mContext,
								LImageButtonActivity.class));
					}
				});
		findViewById(R.id.main_item_lframelayout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(mContext,
								LFrameLayoutActivity.class));
					}
				});
		findViewById(R.id.main_item_lpreference).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(mContext,
								LPreferenceActivity.class));
					}
				});
	}

}
