package com.uplooking.meihaoshiguang.lib.sliding_activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.uplooking.meihaoshiguang.MyApplication;
import com.uplooking.meihaoshiguang.R;

public class SlidingActivity extends Activity{
	private SlidingLayout slidingLayout;
	private ImageView img;
	private FrameLayout container;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		slidingLayout = new SlidingLayout(this);
		slidingLayout.setBackgroundColor(0xff444444);
		super.setContentView(slidingLayout);
		container = slidingLayout.getContainer();
		img = slidingLayout.getImg();
		MyApplication app = (MyApplication) getApplication();
		bitmap = app.bitmap;
		if(bitmap != null){
			img.setImageBitmap(bitmap);
		}
	}
	@Override
	public void setContentView(int layoutResID) {
		View view = LayoutInflater.from(this).inflate(layoutResID, null);
		container.addView(view);
	}
	public void setContentView(View view) {
		container.addView(view);
	};
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, 0);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bitmap != null){
			bitmap.recycle();
		}
	}
	@Override
	public void onBackPressed() {
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}
		super.onBackPressed();
		overridePendingTransition(R.anim.anim_slide_activity_close_enter,
				R.anim.anim_slide_activity_close_exit);
	}
}
