package com.lion.material.dialog;

import static android.view.View.MeasureSpec.EXACTLY;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class LDialogLayout extends LinearLayout {
	private float mMaxWidthWeight = 0.9f;
	private float mMinWidthWeight = 0.55f;
	// private float mMaxHeightWeight = 0.8f;
	private DisplayMetrics metrics;

	public LDialogLayout(Context context) {
		super(context);
	}

	public LDialogLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		metrics = getContext().getResources().getDisplayMetrics();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		float widthWeight = metrics.widthPixels < metrics.heightPixels ? mMaxWidthWeight
				: mMinWidthWeight;
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(
				(int) (metrics.widthPixels * widthWeight), EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// if (getMeasuredHeight() > metrics.heightPixels * mMaxHeightWeight) {
		// super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
		// (int) (metrics.heightPixels * mMaxHeightWeight), EXACTLY));
		// }
	}
}
