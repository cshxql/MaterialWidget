package com.lion.demo.material.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lion.demo.material.R;
import com.lion.material.widget.LImageButton;

public class LImageButtonActivity extends BaseActivity implements
		OnClickListener {
	private ImageButton imageButton1;
	private LImageButton imageButton2;
	private LImageButton imageButton3;
	private LImageButton imageButton4;

	// private Random mRandom = new Random();
	// private int[] randomColor = new int[] { 0x1d000000, 0x1d00ff00,
	// 0xff00ffff,
	// 0xffffff00, 0x1dff0000 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_limagebutton);
		initView();
	}

	private void initView() {
		findViewById(R.id.header_left).setOnClickListener(this);
		imageButton1 = (ImageButton) findViewById(R.id.imagebutton1);
		imageButton2 = (LImageButton) findViewById(R.id.imagebutton2);
		imageButton3 = (LImageButton) findViewById(R.id.imagebutton3);
		imageButton4 = (LImageButton) findViewById(R.id.imagebutton4);
		imageButton1.setOnClickListener(this);
		imageButton2.setOnClickListener(this);
		imageButton3.setOnClickListener(this);
		imageButton4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_left:
			finish();
			break;
		case R.id.imagebutton1:
			Toast.makeText(mContext, R.string.limagebutton_toast_system_button,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.imagebutton2:
		case R.id.imagebutton3:
		case R.id.imagebutton4:
//			if (v instanceof LImageButton) {
//				((LImageButton) v).setColor(randomColor[mRandom
//						.nextInt(randomColor.length)]);
//			}
			break;
		default:
			break;
		}
	}

}
