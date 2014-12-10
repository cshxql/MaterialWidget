package com.lion.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.lion.material.R;
import com.lion.material.extra.MaterialStyle;
import com.lion.material.widget.LButton;

public class DrawerButton extends LButton {
	private final String TAG = "DrawerButton";

	private Paint mLinePaint = new Paint();
	private int mCenterX;
	private int mCenterY;
	private int mStrokeWidth;
	private int mLineSplit;// split between line
	private int mLineHalfWidth;// Half width of line
	private int mSplitHalfHeight;// Half height of line
	private int mLineLeftBase;// Left base pointX of line
	private int mLineRightBase;// Right base pointX of line
	private int mLine1TopBase;// line1 base pointY
	private int mLine2TopBase;// line2 base pointY
	private int mLine3TopBase;// line3 base pointY
	private float mLineLeftOffset;

	private float mLeftOffsetY;
	private float mRightOffsetY;
	private int mCurrentDegrees = 0;
	private boolean mIsLeftGravity = true;
	private int mBaseDegrees = 0;

	public DrawerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mLinePaint.setColor(0xffffffff);
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.LButtonStyle);
			mIsLeftGravity = a.getInt(R.styleable.LButtonStyle_widget_type,
					MaterialStyle.TYPE_HEADBAR_BACK_BTN) != MaterialStyle.TYPE_HEADBAR_RIGHT_BTN;
			mBaseDegrees = mIsLeftGravity ? 0 : 180;
			a.recycle();
		}
	}

	private void initDrawer() {
		if (getWidth() == 0 || getHeight() == 0)
			return;
		mCenterX = getWidth() / 2;
		mCenterY = getHeight() / 2;
		mStrokeWidth = getHeight() / 35;
		mLinePaint.setStrokeWidth(mStrokeWidth);
		mLineSplit = mStrokeWidth * 3;// getHeight() / 15
		mLineHalfWidth = (int) (getHeight() / 4.5f);
		mLineLeftBase = mCenterX - mLineHalfWidth;
		mLineRightBase = mCenterX + mLineHalfWidth;

		mLine1TopBase = mCenterY - mLineSplit - mStrokeWidth;
		mLine2TopBase = mCenterY;
		mLine3TopBase = mCenterY + mLineSplit + mStrokeWidth;
		mSplitHalfHeight = mLine3TopBase - mLine2TopBase;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mCenterX == 0 || mCenterY == 0) {
			initDrawer();
		}
		canvas.rotate(mCurrentDegrees, mCenterX, mCenterY);

		canvas.drawLine(mLineLeftBase + mLineLeftOffset, mLine1TopBase
				- mLeftOffsetY, mLineRightBase, mLine1TopBase + mRightOffsetY,
				mLinePaint);
		canvas.drawLine(mLineLeftBase + mLineLeftOffset / 3, mLine2TopBase,
				mLineRightBase, mLine2TopBase, mLinePaint);
		canvas.drawLine(mLineLeftBase + mLineLeftOffset, mLine3TopBase
				+ mLeftOffsetY, mLineRightBase, mLine3TopBase - mRightOffsetY,
				mLinePaint);
	}

	public void onDrag(boolean isOpened, float progress) {
		if (progress < 0) {
			progress = 0;
		} else if (progress > 1) {
			progress = 1;
		}
		mLineLeftOffset = (mLineHalfWidth * progress);
		mLeftOffsetY = (mSplitHalfHeight / 1.5f * progress);
		mRightOffsetY = (mSplitHalfHeight * progress);
		float degreesProgress = isOpened ? (1 - progress) : progress;
		mCurrentDegrees = (int) (180 * degreesProgress) + (isOpened ? 180 : 0)
				+ mBaseDegrees;
		invalidate();
	}

}
