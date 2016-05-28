package com.lion.material.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.lion.material.R;

public class PreferenceCheckBox extends LFrameLayout {
    private Context mContext;
    private LSwitch mCustomSwitch;
    private TextView mTitleTextView;
    private TextView mSummaryTextView;
    private View mDividerView;

    private OnLPreferenceSwitchListener mListener;


    public PreferenceCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PreferenceStyle);
        mTitleTextView.setText(a
                .getString(R.styleable.PreferenceStyle_preference_title));
        String summary = a
                .getString(R.styleable.PreferenceStyle_preference_summary);
        mSummaryTextView.setVisibility(TextUtils.isEmpty(summary) ? View.GONE
                : View.VISIBLE);
        mSummaryTextView.setText(summary);
        mCustomSwitch.setChecked(a.getBoolean(
                R.styleable.PreferenceStyle_preference_checked, false));
        mDividerView
                .setVisibility(a.getBoolean(
                        R.styleable.PreferenceStyle_preference_show_divider,
                        true) ? View.VISIBLE : View.GONE);
        a.recycle();
        mCustomSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mListener != null) {
                    mListener.onLCheckedChanged(PreferenceCheckBox.this, isChecked);
                }
            }
        });
    }

    private void initView() {
        View.inflate(mContext, R.layout.preference_checkbox, this);
        mTitleTextView = (TextView) findViewById(R.id.preference_title);
        mSummaryTextView = (TextView) findViewById(R.id.preference_summary);
        mCustomSwitch = (LSwitch) findViewById(R.id.preference_checkbox);
        mDividerView = findViewById(R.id.preference_divider);
    }

    @Override
    public boolean performClick() {
        mCustomSwitch.performClick();
        return super.performClick();
    }

    public void setOnCheckedChangeListener(OnLPreferenceSwitchListener listener) {
        mListener = listener;

    }

    public void setChecked(boolean checked) {
        mCustomSwitch.setChecked(checked);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mCustomSwitch.setEnabled(enabled);
        mTitleTextView.setEnabled(enabled);
        mSummaryTextView.setEnabled(enabled);
    }

    public void setSummary(int resId) {
        setSummary(mContext.getString(resId));
    }

    public void setSummary(String summary) {
        mSummaryTextView.setVisibility(TextUtils.isEmpty(summary) ? View.GONE
                : View.VISIBLE);
        mSummaryTextView.setText(summary);
    }

    /**
     * check值改变的回调
     */
    public interface OnLPreferenceSwitchListener {
        void onLCheckedChanged(View v, boolean isChecked);
    }

}
