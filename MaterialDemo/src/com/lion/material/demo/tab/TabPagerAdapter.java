package com.lion.material.demo.tab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lion.material.demo.R;

public class TabPagerAdapter extends FragmentPagerAdapter {
	private String[] mTitles;
	private final String[] mContent = new String[] { "One", "Two", "Three" };

	private Context mContext;

	public TabPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.mContext = context;
		mTitles = mContext.getResources().getStringArray(R.array.title);
	}

	@Override
	public Fragment getItem(int position) {
		return TabFragment.newInstance(mContent[position % mContent.length]);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}
}