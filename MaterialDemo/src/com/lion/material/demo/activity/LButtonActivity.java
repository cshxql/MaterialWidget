package com.lion.material.demo.activity;

import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.lion.material.demo.R;
import com.lion.material.widget.LButton;

public class LButtonActivity extends BaseActivity implements OnClickListener {
	private Button button1;
	private LButton button2;
	private LButton button3;
	private LButton button4;

	private Random mRandom = new Random();
	private int[] randomColor = new int[] { 0x1d000000, 0x1d00ff00, 0xff00ffff,
			0xffffff00, 0x1dff0000 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lbutton);

		initView();
	}

	private void initView() {
		findViewById(R.id.header_left).setOnClickListener(this);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (LButton) findViewById(R.id.button2);
		button3 = (LButton) findViewById(R.id.button3);
		button4 = (LButton) findViewById(R.id.button4);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_left:
			finish();
			break;
		case R.id.button1:
			Toast.makeText(mContext, R.string.lbutton_toast_system_button,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.button2:
		case R.id.button3:
			button2.setEnabled(!button2.isEnabled());
			button2.setText(button2.isEnabled() ? R.string.lbutton_enabled
					: R.string.lbutton_disabled);
			break;
		case R.id.button4:
			if (v instanceof LButton) {
				int fullColor = randomColor[mRandom.nextInt(randomColor.length)];
				((LButton) v).setColor(fullColor);
			}
			break;
		default:
			break;
		}
	}

}
