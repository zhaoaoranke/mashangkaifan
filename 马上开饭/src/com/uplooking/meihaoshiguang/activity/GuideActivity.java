package com.uplooking.meihaoshiguang.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.uplooking.meihaoshiguang.adapter.GuidePagerAdapter;
import  com.uplooking.meihaoshiguang.R;

public class GuideActivity extends Activity {
	private ViewPager mViewPager;
	private List<View> mViews;
	private int[] mImgIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initViews();
		
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mImgIds = new int[] { R.drawable.guide_1, R.drawable.guide_2,
				R.drawable.guide_3 };
		mViews = new ArrayList<View>();
		for (int i = 0; i < mImgIds.length + 1; i++) {
			ImageView imageView = new ImageView(this);
			if (i != mImgIds.length) {
				imageView.setImageResource(mImgIds[i]);
				imageView.setScaleType(ScaleType.FIT_XY);
			}
			mViews.add(imageView);
		}
		final GuidePagerAdapter adapter = new GuidePagerAdapter(mViews);
		mViewPager.setAdapter(adapter);
		// 监听ViewPager的页面改变
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				Log.e("setOnPageChangeListener","触发");
				// 如果ViewPager选中最后一页，就跳转到主Activity
				if (position == adapter.getCount() - 1) {
					startActivity(new Intent(GuideActivity.this,
							MainActivity.class));
					finish();
					overridePendingTransition(R.anim.activity_enter,
							R.anim.activity_exit);
				}
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				Log.e("onPageScrolled","触发");
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
}
