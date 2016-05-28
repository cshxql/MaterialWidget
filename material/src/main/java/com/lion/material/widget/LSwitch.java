package com.lion.material.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ToggleButton;

//public class LSwitch extends CheckBox {
// public class LSwitch extends Switch {
public class LSwitch extends ToggleButton {
    public static int screenHeight;

    private Paint mPaintChecked = new Paint();
    private Paint mPaintNotChecked = new Paint();
    private Paint mPaintAnim = new Paint();
    private Paint mPaintLine = new Paint();
    private Paint mPaintTemp = new Paint();

    private PaintFlagsDrawFilter drawFilter = new PaintFlagsDrawFilter(0,
            Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private RectF mRectFOff;
    private RectF mRectFOn;

    private float mPostionX;
    private float offsetSpeed = 5;
    private float scaleSpeed = 0.01f;

    private final int STATUS_NORMAL = 0;
    private final int STATUS_ANIM_TO_OFF = 1;
    private final int STATUS_ANIM_TO_ON = 2;
    private final int STATUS_MOVE_BY_TOUCH = 3;

    private int status = STATUS_NORMAL;
    private final int ANIM_TIME = 100;

    private final int mCheckedColor = 0xaa09314B;
    private final int mNotCheckedColor = 0xff868384;
    private final int mAnimCheckedColor = 0x6609314B;
    private final int mAnimNotCheckedColor = 0x66868384;
//	private final int mCheckedColor = 0xff03a9f4;
//	private final int mNotCheckedColor = 0xff868384;
//	private final int mAnimCheckedColor = 0x6603a9f4;
//	private final int mAnimNotCheckedColor = 0x66868384;

    private int mWarpWidth;
    private int mWarpHeight;

    public LSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(0x00000000);
        // this.setBackgroundDrawable(null);
        // this.setButtonDrawable(null);
        if (LSwitch.screenHeight <= 0) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            LSwitch.screenHeight = wm.getDefaultDisplay().getHeight();
        }
        mWarpHeight = (int) (60 * LSwitch.screenHeight / 1280f);
        mWarpWidth = mWarpHeight * 2;
    }

    private void init() {
        if (getHeight() == 0 || getWidth() == 0) {
            return;
        }
        int reallyHeight = getHeight();
        int reallyWidth = getWidth();
        int contentWidth = getWidth();
        int contentHeight = getWidth();
        int leftRightEmpty = 0;
        int topBottomEmpty = 0;
        float padding = mWarpHeight * 0.1f;
        float circleRadius;
        float offsetMax;
        if (reallyWidth >= 2 * reallyHeight) {// 填满高度
            contentWidth = 2 * reallyHeight;
            circleRadius = reallyHeight - 2 * padding;
            leftRightEmpty = (reallyWidth - contentWidth) / 2;
            offsetMax = contentWidth - circleRadius - padding * 2;
            offsetSpeed = (contentWidth - offsetMax - mPostionX)
                    / (ANIM_TIME / 15f);
        } else {// 填满宽度
            contentHeight = reallyWidth / 2;
            circleRadius = contentHeight - 2 * padding;
            topBottomEmpty = (reallyHeight - contentHeight) / 2;
            offsetMax = reallyWidth - circleRadius - padding * 2;
            offsetSpeed = (reallyWidth - offsetMax - mPostionX)
                    / (ANIM_TIME / 15f);
        }
        mRectFOff = new RectF();
        mRectFOff.left = padding + leftRightEmpty;
        mRectFOff.top = padding + topBottomEmpty;
        mRectFOff.right = mRectFOff.left + circleRadius;
        mRectFOff.bottom = mRectFOff.top + circleRadius;
        mPostionX = mRectFOff.left;

        mRectFOn = new RectF();
        mRectFOn.left = mRectFOff.left + offsetMax;
        mRectFOn.top = mRectFOff.top;
        mRectFOn.right = mRectFOff.right + offsetMax;
        mRectFOn.bottom = mRectFOff.bottom;

        mPaintChecked.setColor(mCheckedColor);
        mPaintChecked.setStyle(Style.FILL);
        mPaintNotChecked.setColor(mNotCheckedColor);
        mPaintNotChecked.setStyle(Style.STROKE);
        mPaintNotChecked.setStrokeWidth(mRectFOff.width() * 0.1f);
        mPaintTemp.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mPaintAnim.setStyle(Style.STROKE);
        mPaintLine.setStyle(Style.STROKE);
        mPaintLine.setStrokeWidth(mRectFOff.width() * 0.1f);

        scaleSpeed = (mScaleMax - mScaleMin) / (ANIM_TIME / 15f);
    }

    long lastTime;

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        if (mRectFOff == null) {
            init();
            lastTime = System.currentTimeMillis();
        }
        // Log.e(TAG, "onDraw-time-->" + (System.currentTimeMillis() -
        // lastTime));
        lastTime = System.currentTimeMillis();
        canvas.setDrawFilter(drawFilter);
        mPaintLine.setColor(isChecked() ? mAnimCheckedColor
                : mAnimNotCheckedColor);
        if (status == STATUS_NORMAL) {
            if (isChecked()) {
                canvas.drawCircle(mRectFOn.centerX(), mRectFOn.centerY(),
                        mRectFOn.width() / 2, mPaintChecked);
                canvas.drawLine(mRectFOff.left, mRectFOff.centerY(),
                        mRectFOn.left, mRectFOff.centerY(), mPaintLine);
                mPostionX = mRectFOn.left;
            } else {
                canvas.drawCircle(mRectFOff.centerX(), mRectFOff.centerY(),
                        mRectFOff.width() / 2 * (1 - 0.1f), mPaintNotChecked);
                canvas.drawLine(mRectFOff.right, mRectFOff.centerY(),
                        mRectFOn.right, mRectFOff.centerY(), mPaintLine);
                mPostionX = mRectFOff.left;
            }
        } else {
            mPaintAnim.setColor(isChecked() ? mCheckedColor : mNotCheckedColor);

            canvas.drawCircle(mPostionX + mRectFOff.width() / 2,
                    mRectFOff.centerY(), mRectFOff.width() / 2
                            * (1 - mStrokeWidthScale), mPaintAnim);
            canvas.drawLine(mRectFOff.left, mRectFOff.centerY(), mPostionX,
                    mRectFOff.centerY(), mPaintLine);
            canvas.drawLine(mPostionX + mRectFOff.width(), mRectFOff.centerY(),
                    mRectFOn.right, mRectFOff.centerY(), mPaintLine);
        }

        move();
    }

    private float mScaleMax = 0.5f;
    private float mScaleMin = 0.1f;
    private float mStrokeWidthScale = 0;

    private void move() {
        if (status == STATUS_NORMAL || status == STATUS_MOVE_BY_TOUCH) {
            return;
        }
        switch (status) {
            case STATUS_ANIM_TO_OFF:
                mPostionX -= offsetSpeed;
                if (mPostionX <= mRectFOff.left) {
                    mPostionX = mRectFOff.left;
                }
                mStrokeWidthScale -= scaleSpeed;
                if (mStrokeWidthScale <= mScaleMin) {
                    mStrokeWidthScale = mScaleMin;
                }
                if (mPostionX <= mRectFOff.left && mStrokeWidthScale <= mScaleMin) {
                    status = STATUS_NORMAL;
                }
                // mPaintAnim.setColor(mCheckedColor);
                break;
            case STATUS_ANIM_TO_ON:
                mPostionX += offsetSpeed;
                if (mPostionX >= mRectFOn.left) {
                    mPostionX = mRectFOn.left;
                }
                mStrokeWidthScale += scaleSpeed;
                if (mStrokeWidthScale >= mScaleMax) {
                    mStrokeWidthScale = mScaleMax;
                }
                if (mPostionX >= mRectFOn.left && mStrokeWidthScale >= mScaleMax) {
                    status = STATUS_NORMAL;
                }
                // mPaintAnim.setColor(mNotCheckedColor);
                break;

            default:
                break;
        }
        mPaintAnim.setStrokeWidth(mRectFOff.width() * mStrokeWidthScale);
        if (mPostionX <= mRectFOff.left) {
            mPostionX = mRectFOff.left;
        } else if (mPostionX >= mRectFOn.left) {
            mPostionX = mRectFOn.left;
        }
        invalidate();
    }

    private void moveByTouch(float offset) {
        mPostionX += offset;
        if (mPostionX <= mRectFOff.left) {
            mPostionX = mRectFOff.left;
        } else if (mPostionX >= mRectFOn.left) {
            mPostionX = mRectFOn.left;
        }
    }

    @Override
    public boolean performClick() {
        status = isChecked() ? STATUS_ANIM_TO_OFF : STATUS_ANIM_TO_ON;
        return super.performClick();
    }

    private boolean canMove = false;
    private float mLastX;
    private boolean needSuperClick = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isEnabled())
            return true;
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                canMove = false;
                if (isChecked() && (ev.getX() > getWidth() / 2)
                        || (!isChecked() && (ev.getX() < getWidth() / 2))) {
                    mStrokeWidthScale = isChecked() ? mScaleMax : mScaleMin;
                    mPaintAnim
                            .setStrokeWidth(mRectFOff.width() * mStrokeWidthScale);
                    canMove = true;
                    status = STATUS_MOVE_BY_TOUCH;
                    mLastX = ev.getX();
                    invalidate();
                    // return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (canMove) {
                    moveByTouch(ev.getX() - mLastX);
                    mLastX = ev.getX();
                    invalidate();
                    if (needSuperClick) {
                        if ((isChecked() && mPostionX <= ((getWidth() - mRectFOff
                                .width()) / 2))
                                || (!isChecked() && mPostionX >= ((getWidth() - mRectFOff
                                .width()) / 2))) {
                            needSuperClick = false;
                        }
                    }
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (canMove) {
                    moveByTouch(ev.getX() - mLastX);
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (canMove) {
                    if (isChecked()) {
                        if (mPostionX <= (getWidth() - mRectFOff.width()) / 2) {
                            setChecked(false);
                            status = STATUS_ANIM_TO_OFF;
                        } else {
                            status = STATUS_ANIM_TO_ON;
                        }
                    } else {
                        if ((mPostionX >= (getWidth() - mRectFOff.width()) / 2)) {
                            setChecked(true);
                            status = STATUS_ANIM_TO_ON;

                        } else {
                            status = STATUS_ANIM_TO_OFF;
                        }
                    }
                    // status = isChecked() ? STATUS_ANIM_TO_OFF :
                    // STATUS_ANIM_TO_ON;
                    invalidate();
                    if (!needSuperClick) {
                        needSuperClick = true;
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        // return true;
        return super.onTouchEvent(ev);
    }

    @Override
    public void setChecked(boolean checked) {
        status = checked ? STATUS_ANIM_TO_ON : STATUS_ANIM_TO_OFF;
        super.setChecked(checked);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mRectFOff == null) {
            init();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = widthSize;
        int height = heightSize;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                switch (heightMode) {
                    case MeasureSpec.EXACTLY:
                        height = heightSize;
                        break;
                    case MeasureSpec.AT_MOST:
                        height = Math.max(Math.min(mWarpHeight, heightSize),
                                widthSize / 2);
                        break;
                    case MeasureSpec.UNSPECIFIED:
                        height = Math.min(mWarpHeight, widthSize / 2);
                        break;

                    default:
                        break;
                }
                break;
            case MeasureSpec.AT_MOST:
                switch (heightMode) {
                    case MeasureSpec.EXACTLY:
                        width = Math.max(Math.min(mWarpWidth, widthSize),
                                heightSize * 2);
                        height = heightSize;
                        break;
                    case MeasureSpec.AT_MOST:
                        width = Math.min(mWarpWidth, widthSize);
                        height = Math.min(mWarpHeight, heightSize);
                        break;
                    case MeasureSpec.UNSPECIFIED:
                        width = Math.min(mWarpWidth, widthSize);
                        height = mWarpHeight;
                        if (width > height * 2) {
                            width = height * 2;
                        } else {
                            height = width / 2;
                        }
                        break;

                    default:
                        break;
                }
                break;
            case MeasureSpec.UNSPECIFIED:
                switch (heightMode) {
                    case MeasureSpec.EXACTLY:
                        width = Math.min(mWarpWidth, heightSize * 2);
                        height = heightSize;
                        break;
                    case MeasureSpec.AT_MOST:
                        width = mWarpWidth;
                        height = Math.min(mWarpHeight, heightSize);
                        if (width > height * 2) {
                            width = height * 2;
                        } else {
                            height = width / 2;
                        }
                        break;
                    case MeasureSpec.UNSPECIFIED:
                        width = mWarpWidth;
                        height = mWarpHeight;
                        if (width > height * 2) {
                            width = height * 2;
                        } else {
                            height = width / 2;
                        }
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }
        setMeasuredDimension(width, height);
    }
}
