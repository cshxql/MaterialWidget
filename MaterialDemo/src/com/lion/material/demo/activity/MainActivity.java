package com.lion.material.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.lion.material.demo.R;
import com.lion.material.widget.LDrawerButton;

public class MainActivity extends BaseActivity implements OnClickListener {
	private String TAG = "MainActivity";
	private DrawerLayout mDrawerLayout;
	private View mLeftGravityView;
	private LDrawerButton drawerButtonLeft;
	private LDrawerButton drawerButtonRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// init left headerButton
		drawerButtonLeft = (LDrawerButton) findViewById(R.id.header_left);
		drawerButtonLeft.setOnClickListener(this);
		// init right headerButton
		drawerButtonRight = (LDrawerButton) findViewById(R.id.header_right);
		drawerButtonRight.setOnClickListener(this);
		mLeftGravityView = findViewById(R.id.main_menu_left);
		findViewById(R.id.main_add).setOnClickListener(this);
		findViewById(R.id.main_item_lbutton).setOnClickListener(this);
		findViewById(R.id.main_item_limagebutton).setOnClickListener(this);
		findViewById(R.id.main_item_lframelayout).setOnClickListener(this);
		findViewById(R.id.main_item_lpreference).setOnClickListener(this);
		findViewById(R.id.main_item_ldialog).setOnClickListener(this);
		findViewById(R.id.main_item_ltab).setOnClickListener(this);

		mDrawerLayout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int drawerStatus) {
			}

			@Override
			public void onDrawerSlide(View v, float progress) {
				if (v == mLeftGravityView) {
					drawerButtonLeft.onDrag(
							mDrawerLayout.isDrawerOpen(Gravity.LEFT), progress);
				} else {
					drawerButtonRight.onDrag(
							mDrawerLayout.isDrawerOpen(Gravity.RIGHT), progress);
				}

			}

			@Override
			public void onDrawerOpened(View v) {
			}

			@Override
			public void onDrawerClosed(View v) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_add:
		case R.id.header_left:
			if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
			}
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			} else {
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
			break;
		case R.id.header_right:
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
			if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
			} else {
				mDrawerLayout.openDrawer(Gravity.RIGHT);
			}
			break;
		case R.id.main_item_lbutton:
			startActivity(new Intent(mContext, LButtonActivity.class));
			break;
		case R.id.main_item_limagebutton:
			startActivity(new Intent(mContext, LImageButtonActivity.class));
			break;
		case R.id.main_item_lframelayout:
			startActivity(new Intent(mContext, LFrameLayoutActivity.class));
			break;
		case R.id.main_item_lpreference:
			startActivity(new Intent(mContext, LPreferenceActivity.class));
			break;
		case R.id.main_item_ldialog:
			startActivity(new Intent(mContext, LDialogActivity.class));
			break;
		case R.id.main_item_ltab:
			startActivity(new Intent(mContext, LTabActivity.class));
			break;
		default:
			break;
		}
	}
}
