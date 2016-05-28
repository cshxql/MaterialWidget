package com.lion.demo.material.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.lion.demo.material.R;
import com.lion.material.widget.LFrameLayout;

import java.util.Random;


public class LFrameLayoutActivity extends BaseActivity implements
		OnClickListener {
	private FrameLayout mFrameLayout1;
	private LFrameLayout mFrameLayout2;
	private LFrameLayout mFrameLayout3;
	private LFrameLayout mFrameLayout4;
	private LFrameLayout mHeaderFrameLayout;

	private ImageView mPauseImageView;
	private ImageView mPlayImageView;

	private Random mRandom = new Random();
	private int[] randomColor = new int[] { 0x1d000000, 0x1d00ff00, 0xff00ffff,
			0xffffff00, 0x1dff0000 };

	private boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lframelayout);
		initView();
	}

	private void initView() {
		findViewById(R.id.header_left).setOnClickListener(this);
		mHeaderFrameLayout = (LFrameLayout) findViewById(R.id.header_framelayout);
		mFrameLayout1 = (FrameLayout) findViewById(R.id.framelayout1);
		mFrameLayout2 = (LFrameLayout) findViewById(R.id.framelayout2);
		mFrameLayout3 = (LFrameLayout) findViewById(R.id.framelayout3);
		mFrameLayout4 = (LFrameLayout) findViewById(R.id.framelayout4);
		mHeaderFrameLayout.setOnClickListener(this);
		mFrameLayout1.setOnClickListener(this);
		mFrameLayout2.setOnClickListener(this);
		mFrameLayout3.setOnClickListener(this);
		mFrameLayout4.setOnClickListener(this);

		mPauseImageView = (ImageView) findViewById(R.id.header_pause);
		mPlayImageView = (ImageView) findViewById(R.id.header_play);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_left:
			finish();
			break;
		case R.id.framelayout1:
			Toast.makeText(mContext, R.string.lframelayout_toast_system_button,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.framelayout2:
			break;
		case R.id.framelayout3:
			break;
		case R.id.framelayout4:
			if (v instanceof LFrameLayout) {
				((LFrameLayout) v).setColor(randomColor[mRandom
						.nextInt(randomColor.length)]);
			}
			break;
		case R.id.header_framelayout:
			isPlaying = !isPlaying;
			mPauseImageView.setVisibility(isPlaying ? View.VISIBLE : View.GONE);
			mPlayImageView.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
