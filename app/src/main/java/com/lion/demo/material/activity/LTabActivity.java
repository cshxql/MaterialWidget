package com.lion.demo.material.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.lion.demo.material.R;
import com.lion.demo.material.tab.TabPagerAdapter;
import com.lion.material.widget.LTabIndicator;


public class LTabActivity extends BaseActivity {
	private LTabIndicator lineTabIndicator;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_ltab);
		findViews();
	}

	private void findViews() {
		findViewById(R.id.header_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		lineTabIndicator = (LTabIndicator) findViewById(R.id.tab_indicator);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new TabPagerAdapter(this,
				getSupportFragmentManager()));
		lineTabIndicator.setViewPager(viewPager);
	}
}
