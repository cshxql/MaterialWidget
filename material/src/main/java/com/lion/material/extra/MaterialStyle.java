package com.lion.material.extra;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

import com.lion.material.R;

public class MaterialStyle {
	private final int STATUS_NORMAL = 0;
	// hand up will change to fast status
	private final int STATUS_ANIM_FAST = 1;
	// hand down and go on pressed is slowly status
	private final int STATUS_ANIM_SLOWLY = 2;
	// when time out,changed to pressed status
	private final int STATUS_PRESSED = 3;
	// when animation over will change to normal status
	private final int STATUS_TO_NORMAL = 4;
	private int status = STATUS_NORMAL;

	private final int SHAPE_MODE_EQUAL = 0;
	private final int SHAPE_MODE_WIDTH = 1;
	private final int SHAPE_MODE_HEIGHT = 2;
	private int mAnimMode = SHAPE_MODE_EQUAL;

	private int ANIM_FAST_TIME = 125;
	private final int ANIM_SLOW_TIME = 200;
	private final int ANIM_SLEEP_TIME = 10;

	// normal status (no touch, no pressed) paint
	// private Paint mBackgroundPaint = new Paint();
	private Drawable mBackgroundDrawable;
	private int mBackgroundColor = 0;
	private int mAnimColorAlpha = 0;// current alpha
	private int mAnimColorAlphaMax = (int) (255 * 0.06f);// max alpha
	private int mAnimColorAlphaSlowSpeed;// long press alpha animation speed
	private int mAnimColorAlphaFastSpeed;// click alpha animation speed
	private int mAnimColorAlphaToNormalSpeed;// change to normal alpha animation
												// speed
	private Path mPath = new Path();
	// paint for bottom (alpha will change)
	private Paint mAnimBottomPaint = new Paint();
	// paint for top circle(color and alpha will not change)
	private Paint mCirclePaint = new Paint();

	private float mCirRSpeedSlow;// long press circle radius speed
	private float mCirRSpeedFast;// click circle radius speed
	private float mCircleRadius; // current circle radius speed
	private float mCircleRadiusMax;// max circle radius speed

	private int animSlowTimes;
	private int animFastTimes;

	private float actionDownX;// touch down x position
	private float actionDownY;// touch down y position
	private int mBackgroundWidth;
	private int mBackgroundHeight;

	//private OnClickListener mClickListener;

	/**
	 * there have 3 type: TYPE_HEADBAR_CENTER_BTN is normal button shape,could
	 * be circle TYPE_HEADBAR_BACK_BTN like back button in header bar
	 * TYPE_HEADBAR_RIGHT_BTN like setting button in header bar
	 */
	public static final int TYPE_NORMAL_BTN = 0;
	public static final int TYPE_HEADBAR_BACK_BTN = 1;
	public static final int TYPE_HEADBAR_RIGHT_BTN = 2;
	public static final int TYPE_HEADER_CENTER_BTN = 3;

	private int mAnimFullColor = -1;

	private PaintFlagsDrawFilter drawFilter = new PaintFlagsDrawFilter(0,
			Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

	private int type = TYPE_NORMAL_BTN;
	private boolean mNeedDelayClick = true;
	private View mDependView;
	private IMaterial mCallBack;

	@SuppressLint("NewApi")
	public MaterialStyle(View view, AttributeSet attrs) {
		this.mCallBack = (IMaterial) view;
		mDependView = view;
		view.setClickable(true);
		setBackgroundColor(0x00000000);
		animSlowTimes = ANIM_SLOW_TIME / ANIM_SLEEP_TIME;
		animFastTimes = ANIM_FAST_TIME / ANIM_SLEEP_TIME;
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		view.setWillNotDraw(false);
		if (attrs != null) {
			TypedArray a = view.getContext().obtainStyledAttributes(attrs,
					R.styleable.LButtonStyle);
			mNeedDelayClick = a.getBoolean(
					R.styleable.LButtonStyle_widget_delayclick, true);
			mAnimFullColor = a.getInt(
					R.styleable.LButtonStyle_widget_animColor, -1);
			mBackgroundDrawable = a
					.getDrawable(R.styleable.LButtonStyle_widget_background);
			if (mBackgroundDrawable != null) {
				mBackgroundWidth = mBackgroundDrawable.getIntrinsicWidth();
				mBackgroundHeight = mBackgroundDrawable.getIntrinsicHeight();
			}
			// mBackgroundPaint.setColor(a.getInt(
			// R.styleable.LButtonStyle_widget_background, 0x00000000));
			type = a.getInt(R.styleable.LButtonStyle_widget_type, 0);
			a.recycle();
		}
	}

	public void setColor(int fullColor) {
		this.mAnimFullColor = fullColor;
		initColorInfo();
	}

	public void setType(int widgetType) {
		this.type = widgetType;
		init();
	}

	private void init() {

		float width = mDependView.getWidth();
		float heigh = mDependView.getHeight();
		if (width <= 0 || heigh <= 0)
			return;
		// if width is close to height, regard as equale
		boolean inNormalSize = Math.min(width, heigh) / Math.max(width, heigh) > 0.8f;
		if (inNormalSize) {
			mAnimMode = SHAPE_MODE_EQUAL;
		} else if (width > heigh) {
			mAnimMode = SHAPE_MODE_WIDTH;
		} else {
			mAnimMode = SHAPE_MODE_HEIGHT;
		}
		switch (type) {
		case TYPE_HEADBAR_BACK_BTN:
		case TYPE_HEADBAR_RIGHT_BTN:
			mCircleRadius = heigh / 2f;
			mCircleRadiusMax = width;// / 2.5f;
			if (mAnimFullColor == -1) {
				mAnimFullColor = 0x60ffffff;
			}
			break;
		case TYPE_NORMAL_BTN:
			mCircleRadius = Math.min(width, heigh) / 4f;
			if (mAnimMode == SHAPE_MODE_EQUAL) {
				// this is for case:width is close to height
				mCircleRadiusMax = Math.min(width, heigh) / 2;
			} else {
				mCircleRadiusMax = Math.max(width, heigh) / 2;
			}
			if (mAnimFullColor == -1) {
				mAnimFullColor = 0x30000000;
			}
			break;
		case TYPE_HEADER_CENTER_BTN:
			mCircleRadius = Math.min(width, heigh) / 4f;
			mCircleRadiusMax = Math.max(width, heigh) / 2;
			if (mAnimFullColor == -1) {
				mAnimFullColor = 0x60ffffff;
			}
			break;
		}
		mCirRSpeedSlow = (mCircleRadiusMax - mCircleRadius) / animSlowTimes;
		mCirRSpeedFast = (mCircleRadiusMax - mCircleRadius) / animFastTimes;
		initColorInfo();
	}

	private void initColorInfo() {
		// if (mBackGroundPaint.getColor() == 0) {
		mAnimBottomPaint.setColor(Color.rgb(Color.red(mAnimFullColor),
				Color.green(mAnimFullColor), Color.blue(mAnimFullColor)));
		mAnimColorAlphaMax = Color.alpha(mAnimFullColor);
		mCirclePaint.setColor(Color.rgb(Color.red(mAnimFullColor),
				Color.green(mAnimFullColor), Color.blue(mAnimFullColor)));
		mCirclePaint.setAlpha(mAnimColorAlphaMax);
		// } else {
		// mAnimBottomPaint.setColor(Color.rgb(
		// Color.red(255 - mBackGroundPaint.getColor()),
		// Color.green(255 - mBackGroundPaint.getColor()),
		// Color.blue(255 - mBackGroundPaint.getColor())));
		// mAnimColorAlphaMax = Color.alpha(mBackGroundPaint.getColor());
		// }
		mAnimBottomPaint.setAlpha(0);
		mAnimColorAlphaSlowSpeed = (int) (mAnimColorAlphaMax / animSlowTimes);
		mAnimColorAlphaFastSpeed = (int) (mAnimColorAlphaMax / animFastTimes);
		if (mAnimColorAlphaSlowSpeed < 1) {
			mAnimColorAlphaSlowSpeed = 1;
		}
		if (mAnimColorAlphaFastSpeed < 1) {
			mAnimColorAlphaFastSpeed = 1;
		}
		mAnimColorAlphaToNormalSpeed = mAnimColorAlphaFastSpeed;
	}

	private int getWidth() {
		if (mDependView == null)
			return 0;
		return mDependView.getWidth();
	}

	private int getHeight() {
		if (mDependView == null)
			return 0;
		return mDependView.getHeight();
	}

	public void doDraw(Canvas canvas) {
		if (mCircleRadius == 0 || mCircleRadiusMax == 0) {
			init();
		}
		canvas.setDrawFilter(drawFilter);
		canvas.save();
		// Log.e("LButton", "status-->" + status + " Circle.Alpha-->"
		// + mCirclePaint.getAlpha() + " Bottom.Alpha-->"
		// + mAnimBottomPaint.getAlpha() + " Circle.Color-->"
		// + mCirclePaint.getColor() + " bottomColor-->"
		// + mAnimBottomPaint.getColor() + " mCirR-->" + mCircleRadius);
		if (mBackgroundDrawable != null) {
			mBackgroundDrawable.setBounds(0, 0, getWidth(), getHeight());
			mBackgroundDrawable.draw(canvas);
		} else if (mBackgroundColor != 0) {
			canvas.drawColor(mBackgroundColor);
		}
		switch (status) {
		case STATUS_NORMAL:
			break;
		case STATUS_PRESSED:
			switch (type) {
			case TYPE_HEADBAR_BACK_BTN:
				canvas.drawCircle(0, getHeight() / 2, getWidth(),
						mAnimBottomPaint);
				break;
			case TYPE_HEADBAR_RIGHT_BTN:
				canvas.drawCircle(getWidth(), getHeight() / 2, getWidth(),
						mAnimBottomPaint);
				break;
			case TYPE_NORMAL_BTN:
				if (mAnimMode == SHAPE_MODE_EQUAL) {
					canvas.drawCircle(getWidth() / 2, getHeight() / 2,
							Math.min(getWidth(), getHeight()) / 2,
							mAnimBottomPaint);
				} else {
					canvas.drawPaint(mAnimBottomPaint);
				}
				break;
			case TYPE_HEADER_CENTER_BTN:
				canvas.drawCircle(getWidth() / 2, getHeight() / 2,
						Math.max(getWidth(), getHeight()) / 2, mAnimBottomPaint);
				break;
			}
			break;
		case STATUS_ANIM_SLOWLY:
		case STATUS_ANIM_FAST:
			switch (type) {
			case TYPE_HEADBAR_BACK_BTN:
				// if (mBackgroundDrawable == null && mBackgroundColor == 0) {
				mPath.reset();
				mPath.addCircle(0, getHeight() / 2, mCircleRadius,
						Direction.CCW);
				canvas.drawPath(mPath, mCirclePaint);
				canvas.clipPath(mPath, Op.XOR);
				canvas.drawCircle(0, getHeight() / 2, mCircleRadiusMax,
						mAnimBottomPaint);
				// } else {
				// canvas.drawCircle(0, getHeight() / 2, mCircleRadius,
				// mAnimBottomPaint);
				// }
				break;
			case TYPE_HEADBAR_RIGHT_BTN:
				// if (mBackgroundDrawable == null && mBackgroundColor == 0) {
				mPath.reset();
				mPath.addCircle(getWidth(), getHeight() / 2, mCircleRadius,
						Direction.CCW);
				canvas.drawPath(mPath, mCirclePaint);
				canvas.clipPath(mPath, Op.XOR);
				canvas.drawCircle(getWidth(), getHeight() / 2,
						mCircleRadiusMax, mAnimBottomPaint);
				// } else {
				// canvas.drawCircle(getWidth(), getHeight() / 2,
				// mCircleRadius, mAnimBottomPaint);
				// }
				break;
			case TYPE_NORMAL_BTN:
				// if (mBackgroundDrawable == null && mBackgroundColor == 0) {
				mPath.reset();
				mPath.addCircle(actionDownX, actionDownY, mCircleRadius,
						Direction.CCW);
				canvas.drawPath(mPath, mCirclePaint);
				canvas.clipPath(mPath, Op.XOR);
				if (mAnimMode == SHAPE_MODE_EQUAL) {
					canvas.drawCircle(getWidth() / 2, getHeight() / 2,
							mCircleRadiusMax, mAnimBottomPaint);
				} else {
					canvas.drawPaint(mAnimBottomPaint);
				}
				// } else {
				// canvas.drawCircle(actionDownX, actionDownY, mCircleRadius,
				// mAnimBottomPaint);
				// }
				break;
			case TYPE_HEADER_CENTER_BTN:
				// if (mBackgroundDrawable == null && mBackgroundColor == 0) {
				mPath.reset();
				mPath.addCircle(getWidth() / 2, getHeight() / 2, mCircleRadius,
						Direction.CCW);
				canvas.drawPath(mPath, mCirclePaint);
				canvas.clipPath(mPath, Op.XOR);
				canvas.drawCircle(getWidth() / 2, getHeight() / 2,
						mCircleRadiusMax, mAnimBottomPaint);
				// } else {
				// canvas.drawCircle(getWidth() / 2, getHeight() / 2,
				// mCircleRadius, mAnimBottomPaint);
				// }
				break;
			}
			break;
		case STATUS_TO_NORMAL:
			switch (type) {
			case TYPE_HEADBAR_BACK_BTN:
				canvas.drawCircle(0, getHeight() / 2, mCircleRadius,
						mAnimBottomPaint);
				break;
			case TYPE_HEADBAR_RIGHT_BTN:
				canvas.drawCircle(getWidth(), getHeight() / 2, mCircleRadius,
						mAnimBottomPaint);
				break;
			case TYPE_NORMAL_BTN:
				if (mAnimMode == SHAPE_MODE_EQUAL) {
					canvas.drawCircle(getWidth() / 2, getHeight() / 2,
							mCircleRadius, mAnimBottomPaint);
				} else {
					canvas.drawPaint(mAnimBottomPaint);
				}
				break;
			case TYPE_HEADER_CENTER_BTN:
				canvas.drawCircle(getWidth() / 2, getHeight() / 2,
						mCircleRadius, mAnimBottomPaint);
				break;
			}
			break;
		default:
			break;
		}
		canvas.restore();
		move();
	}

	private void move() {
		if (status == STATUS_NORMAL || status == STATUS_PRESSED)
			return;
		if (status == STATUS_ANIM_SLOWLY) {
			mAnimColorAlpha += mAnimColorAlphaSlowSpeed;
			mCircleRadius += mCirRSpeedSlow;
		} else if (status == STATUS_ANIM_FAST) {
			mAnimColorAlpha += mAnimColorAlphaFastSpeed;
			mCircleRadius += mCirRSpeedFast;
		} else if (status == STATUS_TO_NORMAL) {
			mAnimColorAlpha -= mAnimColorAlphaToNormalSpeed;
			mCircleRadius = mCircleRadiusMax;
		}
		if (mAnimColorAlpha >= mAnimColorAlphaMax) {
			mAnimColorAlpha = mAnimColorAlphaMax;
		}
		if (mAnimColorAlpha < 0) {
			mAnimColorAlpha = 0;
		}
		mAnimBottomPaint.setAlpha(mAnimColorAlpha);
		// Log.e(TAG, "mAnimColorAlpha-->" + mAnimColorAlpha);
		// mBottomColor = Color.argb(mAnimColorAlpha, 0, 0, 0);
		if (mCircleRadius > mCircleRadiusMax) {
			mCircleRadius = mCircleRadiusMax;
		}
		if (mAnimColorAlpha <= 0 || mAnimColorAlpha >= mAnimColorAlphaMax) {
			reset();
		}
		mDependView.invalidate();
	}

	private void reset() {
		switch (status) {
		case STATUS_ANIM_FAST:
		case STATUS_PRESSED:
			mCircleRadius = mCircleRadiusMax;
			status = STATUS_TO_NORMAL;
			break;
		case STATUS_ANIM_SLOWLY:
			status = STATUS_PRESSED;
			if (type == TYPE_HEADBAR_BACK_BTN || type == TYPE_HEADBAR_RIGHT_BTN) {
				mCircleRadius = getWidth();
			}
			break;
		case STATUS_TO_NORMAL:
			if (mNeedDelayClick && hasClick) {
				if (mCallBack != null)
					mCallBack.perfirmSuperClick();
				hasClick = false;
			}
			resetStatus();
			break;
		default:
			break;
		}
		mDependView.invalidate();
	}

	private boolean hasClick = false;

	public void resetStatus() {
		status = STATUS_NORMAL;
		hasClick = false;
		mCircleRadius = getHeight() / 4;
		mAnimColorAlpha = 0;
	}

	public void performClick() {
		if (status != STATUS_TO_NORMAL)
			status = STATUS_ANIM_FAST;
		if (!mNeedDelayClick) {
			if (mCallBack != null)
				mCallBack.perfirmSuperClick();
		} else {
			if (status == STATUS_PRESSED) {
				if (mCallBack != null)
					mCallBack.perfirmSuperClick();
			} else {
				hasClick = true;
			}
		}
	}

	public void dispatchKeyEvent() {

	}

	public void onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Log.e(TAG, "onTouchEvent  ACTION_DOWN");
			if (status != STATUS_NORMAL) {
				resetStatus();
			}
			actionDownX = event.getX();
			actionDownY = event.getY();
			switch (mAnimMode) {
			case SHAPE_MODE_EQUAL:
				actionDownX = getWidth() / 2;
				actionDownY = getHeight() / 2;
				break;
			case SHAPE_MODE_WIDTH:
				actionDownY = getHeight() / 2;
				break;
			case SHAPE_MODE_HEIGHT:
				actionDownX = getWidth() / 2;
				break;
			default:
				break;
			}

			status = STATUS_ANIM_SLOWLY;
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			if (status == STATUS_ANIM_SLOWLY) {
				status = STATUS_ANIM_FAST;
			}
		case MotionEvent.ACTION_UP:
			if (status == STATUS_PRESSED) {
				reset();
			} else if (status == STATUS_ANIM_SLOWLY) {
				status = STATUS_ANIM_FAST;
				// resetSpeed();
			}
			break;
		default:
			break;
		}
		mDependView.invalidate();
		//return mClickListener == null ? false : true;
	}

	public boolean needBackgroundMeasure() {
		if (mBackgroundWidth <= 0 || mBackgroundHeight <= 0) {
			return false;
		}
		return true;
	}

	public int[] getMeasureSize(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(mBackgroundWidth, widthSize);
		} else {
			width = mBackgroundWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(mBackgroundHeight, heightSize);
		} else {
			height = mBackgroundHeight;
		}
		return new int[] { width, height };
		// mDependView.setMeasuredDimension(width, height);
	}

	public void setDelayClick(boolean delayClick) {
		this.mNeedDelayClick = delayClick;
	}

	public void setBackground(Drawable background) {
		mBackgroundDrawable = background;
		mBackgroundColor = 0;
		if (mBackgroundDrawable != null) {
			mBackgroundWidth = mBackgroundDrawable.getIntrinsicWidth();
			mBackgroundHeight = mBackgroundDrawable.getIntrinsicHeight();
		}
		mDependView.requestLayout();
		mDependView.invalidate();
	}

	public void setBackgroundColor(int color) {
		// super.setBackgroundColor(color);
		mBackgroundDrawable = null;
		mBackgroundColor = color;
	}

	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (!hasWindowFocus) {
			resetStatus();
		}
	}

	public void onVisibilityChanged(View changedView, int visibility) {
		if (visibility != View.VISIBLE) {
			resetStatus();
		}
	}

	//public void setOnclickListener(OnClickListener listener) {
	//	this.mClickListener = listener;
	//}

}
