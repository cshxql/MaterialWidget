package com.lion.material.dialog;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lion.material.R;
import com.lion.material.widget.LButton;
import com.lion.material.widget.LImageButton;

public class LDialogController {
	private final DialogInterface mDialogInterface;
	private final Window mWindow;

	private View mView; // Custom view

	private LButton mButtonPositive; // right Button
	private CharSequence mButtonPositiveText;
	private Message mButtonPositiveMessage;

	private LButton mButtonNegative; // left Button
	private CharSequence mButtonNegativeText;
	private Message mButtonNegativeMessage;

	private ScrollView mScrollView;

	private int mIconId = -1;
	private Drawable mIcon;
	private ImageView mIconView;
	public LImageButton mCloseImageView;

	private TextView mTitleView;
	private CharSequence mTitle; 

	private TextView mMessageView; 
	private CharSequence mMessage; 

	public boolean clickPositiveBtnDismiss; 
	public boolean centerMsg; 

	private Handler mHandler;

	private boolean mShowCloseBtn = false;

	public DialogInterface getDialogInterface() {
		return mDialogInterface;
	}

	View.OnClickListener mButtonHandler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Message m = null;
			if (v == mButtonPositive && mButtonPositiveMessage != null) {
				m = Message.obtain(mButtonPositiveMessage);
			} else if (v == mButtonNegative && mButtonNegativeMessage != null) {
				m = Message.obtain(mButtonNegativeMessage);
			}
			if (m != null) {
				m.sendToTarget();
			}

			if (!clickPositiveBtnDismiss && v == mButtonPositive) {
				return;
			}
			// Post a message so we dismiss after the above handlers are
			// executed
			mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG,
					mDialogInterface).sendToTarget();
		}
	};

	private static final class ButtonHandler extends Handler {
		// Button clicks have Message.what as the BUTTON{1,2,3} constant
		private static final int MSG_DISMISS_DIALOG = 1;

		private WeakReference<DialogInterface> mDialog;

		public ButtonHandler(DialogInterface dialog) {
			mDialog = new WeakReference<DialogInterface>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				((DialogInterface.OnClickListener) msg.obj).onClick(
						mDialog.get(), msg.what);
				break;

			case MSG_DISMISS_DIALOG:
				((DialogInterface) msg.obj).dismiss();
			}
		}
	}

	public LDialogController(Context context, DialogInterface di, Window window) {
		mDialogInterface = di;
		mWindow = window;
		mHandler = new ButtonHandler(di);
	}

	public void setPositiveButtonEnable(boolean enable) {
		mButtonPositive.setEnabled(enable);
	}

	
	public void installContent() {
		/* We use a custom title so never request a window title */
		mWindow.requestFeature(Window.FEATURE_NO_TITLE);
		mWindow.setContentView(R.layout.material_alert_dialog);
		setupView();
	}

	private void setupView() {
		setupContent();
		boolean hasButtons = setupButtons(); // bottom button
		mWindow.findViewById(R.id.buttonPanel).setVisibility(
				hasButtons ? View.VISIBLE : View.GONE);
		setupTitle(); // title
		FrameLayout custom = (FrameLayout) mWindow.findViewById(R.id.custom);
		if (mView != null) { // custom view
			custom.addView(mView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
		} else {
			custom.setVisibility(View.GONE);
		}

	}

	private boolean setupTitle() {
		boolean hasTitle = true;

		final boolean hasTextTitle = !TextUtils.isEmpty(mTitle);
		mIconView = (ImageView) mWindow.findViewById(R.id.icon);
		mCloseImageView = (LImageButton) mWindow.findViewById(R.id.close);
		mCloseImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialogInterface.dismiss();
			}
		});
		if (hasTextTitle) {
			mTitleView = (TextView) mWindow.findViewById(R.id.alertTitle);
			mTitleView.setText(mTitle);
			if (mIconId > 0) { 
				mIconView.setImageResource(mIconId);
			} else if (mIcon != null) {
				mIconView.setImageDrawable(mIcon);
			} else if (mIconId == 0) {
				mTitleView.setPadding(mIconView.getPaddingLeft(),
						mIconView.getPaddingTop(), mIconView.getPaddingRight(),
						mIconView.getPaddingBottom());
				mIconView.setVisibility(View.GONE);
			}
			mCloseImageView.setVisibility(mShowCloseBtn ? View.VISIBLE
					: View.INVISIBLE);
		} else {
			// Hide the title template
			View titleTemplate = mWindow.findViewById(R.id.title_template);
			titleTemplate.setVisibility(View.GONE);
			mIconView.setVisibility(View.GONE);
			mWindow.findViewById(R.id.topPanel).setVisibility(View.GONE);
			hasTitle = false;

		}
		mWindow.findViewById(R.id.notitle_space).setVisibility(
				hasTitle ? View.GONE : View.VISIBLE);
		return hasTitle;
	}

	
	private void setupContent() {
		mScrollView = (ScrollView) mWindow.findViewById(R.id.scrollView);
		mScrollView.setFocusable(false);

		mMessageView = (TextView) mWindow.findViewById(R.id.message);
		if (mMessage != null) {
			if (centerMsg) {
				mMessageView.setGravity(Gravity.CENTER);
			}
			mMessageView.setText(mMessage);
		} else {
			mMessageView.setVisibility(View.GONE);
			mScrollView.setVisibility(View.GONE);
		}
	}

	public int getMaxListLineNum() {
		return 6;
	}

	
	private boolean setupButtons() {
		int BIT_BUTTON_POSITIVE = 1;
		int BIT_BUTTON_NEGATIVE = 2;
		int whichButtons = 0;
		mButtonPositive = (LButton) mWindow.findViewById(R.id.button1);
		mButtonPositive.setOnClickListener(mButtonHandler);

		if (TextUtils.isEmpty(mButtonPositiveText)) {
			mButtonPositive.setVisibility(View.GONE);
		} else {
			mButtonPositive.setText(mButtonPositiveText);
			mButtonPositive.setVisibility(View.VISIBLE);
			whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
		}

		mButtonNegative = (LButton) mWindow.findViewById(R.id.button2);
		mButtonNegative.setOnClickListener(mButtonHandler);

		if (TextUtils.isEmpty(mButtonNegativeText)) {
			mButtonNegative.setVisibility(View.GONE);
		} else {
			mButtonNegative.setText(mButtonNegativeText);
			mButtonNegative.setVisibility(View.VISIBLE);
			whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
		}

		return whichButtons != 0;

	}

	public void setTitle(CharSequence title) {
		mTitle = title;
		if (mTitleView != null) {
			mTitleView.setText(title);
		}
	}

	public void setClickPositiveBtnDismiss(boolean value) {
		clickPositiveBtnDismiss = value;
	}

	public void setCenterMsg(boolean value) {
		centerMsg = value;
	}

	public void setMessage(CharSequence message) {
		mMessage = message;
		if (mMessageView != null) {
			mMessageView.setText(message);
		}
	}

	public void setButton(int whichButton, CharSequence text,
			DialogInterface.OnClickListener listener, Message msg) {

		if (msg == null && listener != null) {
			msg = mHandler.obtainMessage(whichButton, listener);
		}

		switch (whichButton) {

		case DialogInterface.BUTTON_POSITIVE:
			mButtonPositiveText = text;
			mButtonPositiveMessage = msg;
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			mButtonNegativeText = text;
			mButtonNegativeMessage = msg;
			break;

		default:
			// throw new IllegalArgumentException("Button does not exist");
			break;
		}
	}

	public void setCloseButton(boolean showCloseBtn) {
		this.mShowCloseBtn = showCloseBtn;
	}

	public void setIcon(int resId) {
		mIconId = resId;
		if (mIconView != null) {
			if (resId > 0) {
				mIconView.setImageResource(mIconId);
			} else if (resId == 0) {
				mIconView.setVisibility(View.GONE);
			}
		}
	}

	public void setIcon(Drawable icon) {
		mIcon = icon;
		if ((mIconView != null) && (mIcon != null)) {
			mIconView.setImageDrawable(icon);
		}
	}

	public void setView(View view) {
		mView = view;
	}

	public static class DialogParams {
		public final Context mContext;
		public final LayoutInflater mInflater;

		public boolean visibleCallback;
		public int mIconId = 0;
		public Drawable mIcon;
		public CharSequence mTitle;
		public CharSequence mMessage;
		public CharSequence mPositiveButtonText;
		public DialogInterface.OnClickListener mPositiveButtonListener;
		public DialogInterface.OnClickListener mCloseListener;
		public CharSequence mNegativeButtonText;
		public DialogInterface.OnClickListener mNegativeButtonListener;
		public boolean mCancelable;
		public boolean clickPositiveBtnDismiss;
		public boolean centerMsg;
		public OnCancelListener mOnCancelListener;

		public DialogInterface.OnClickListener mOnClickListener;

		public View mView;

		public DialogParams(Context context) {
			mContext = context;
			mCancelable = true;
			clickPositiveBtnDismiss = true;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void apply(LDialogController control) {
			control.setClickPositiveBtnDismiss(clickPositiveBtnDismiss);
			control.setCenterMsg(centerMsg);
			if (mTitle != null) {
				control.setTitle(mTitle);
			}
			if (mIcon != null) {
				control.setIcon(mIcon);
			}
			control.setCloseButton(visibleCallback);

			if (mIconId >= 0) {
				control.setIcon(mIconId);
			}
			if (mMessage != null) {
				control.setMessage(mMessage);
			}

			if (mPositiveButtonText != null) {
				control.setButton(DialogInterface.BUTTON_POSITIVE,
						mPositiveButtonText, mPositiveButtonListener, null);
			}
			if (mNegativeButtonText != null) {
				control.setButton(DialogInterface.BUTTON_NEGATIVE,
						mNegativeButtonText, mNegativeButtonListener, null);
			}
			if (mView != null) {
				control.setView(mView);
			}
		}

	}
}
