package com.lion.demo.material.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.lion.demo.material.R;
import com.lion.material.dialog.LAlertDialog;
import com.lion.material.dialog.LDialogListBuilder;


public class LDialogActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
	private int mSelectedIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_ldialog);
		initView();
	}

	private void initView() {
		findViewById(R.id.header_left).setOnClickListener(this);
		findViewById(R.id.ldialog_system).setOnClickListener(this);
		findViewById(R.id.ldialog_normal).setOnClickListener(this);
		findViewById(R.id.ldialog_normal_longmsg).setOnClickListener(this);
		findViewById(R.id.ldialog_list).setOnClickListener(this);
		findViewById(R.id.ldialog_single_choice).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_left:
			finish();
			break;
		case R.id.ldialog_system:
			showSystemDialog();
			break;
		case R.id.ldialog_normal:
			showNormal(false);
			break;
		case R.id.ldialog_normal_longmsg:
			showNormal(true);
			break;
		case R.id.ldialog_list:
			showSingleList(false);
			break;
		case R.id.ldialog_single_choice:
			showSingleList(true);
			break;
		default:
			break;
		}
	}

	private void showSystemDialog() {
		new AlertDialog.Builder(this).setTitle("This is title")
				.setMessage("This is message.").setPositiveButton("OK", null)
				.setNegativeButton("Cancel", null).show();
	}

	private void showNormal(boolean longMsg) {
		String longContent = "1.If you don't make the time to work on creating the life you want, you're eventually going to be forced to spend a lot of time dealing with a life you don't want.\n"
				+ "2.One needs 3 things to be truly happy living in the world: some thing to do, some one to love, some thing to hope for.\n"
				+ "3.Slow the pace of your life and give yourself some time for meditating .It's not death that aman should fear ，but he should fear never live truly. \n";
		String message = longMsg ? longContent : "This is message.";
		new LAlertDialog.Builder(this)
				.setTitle("This is title")
				.setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(mContext, "OK Clicked",
								Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(mContext, "Cancel Clicked",
										Toast.LENGTH_SHORT).show();
							}
						}).show();
	}

	private void showSingleList(boolean chooseMode) {
		final String[] items = new String[] { "Item 1", "Item 2", "Item 3" };
		new LDialogListBuilder(this, mSelectedIndex, chooseMode)
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int position) {
						Toast.makeText(mContext, items[position],
								Toast.LENGTH_SHORT).show();
						mSelectedIndex = position;

					}
				}).showCloseButton(true).setTitle("Title").show();
		// .show();
	}

}
