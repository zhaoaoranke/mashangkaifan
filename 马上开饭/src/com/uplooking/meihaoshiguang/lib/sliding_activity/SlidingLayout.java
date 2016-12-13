package com.uplooking.meihaoshiguang.lib.sliding_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

public class SlidingLayout extends ViewGroup{
	/**底部显示上一个界面Bitmap的ImageView**/
	private ImageView img;
	/**放主视图的容器**/
	private FrameLayout container;
	/**拖动控件的帮助类**/
	private ViewDragHelper mDragHelper;
	private GestureDetectorCompat gestureDetector;
	public SlidingLayout(Context context) {
		super(context);
		init(context);
	}

	public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context) {
		img = new ImageView(context);
		addView(img);
		container = new FrameLayout(context);
		addView(container);
		mDragHelper = ViewDragHelper.create(this, dragHelperCallback);
		gestureDetector = new GestureDetectorCompat(context, new YScrollDetector());
	}
	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
			return Math.abs(dy) <= Math.abs(dx);
		}
	}
	private int containerLeft;
	Callback dragHelperCallback = new Callback() {
		//这里可以决定哪个子view可以拖动
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return child == container;
		}
		
		//这里返回可拖动的View应该滑向的位置
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if(left >= getWidth()){
				containerLeft = getWidth();
			}else if(left <= 0){
				containerLeft = containerLeft + (left - containerLeft)/4;
			}else{
				containerLeft = left;
			}
			return containerLeft;
		}
		
		@Override
		public int getViewHorizontalDragRange(View child) {
			return getWidth();
		}
		
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			if(xvel > 0){//如果加速度>0
				if(containerLeft < 0){
					enter();
				}else{
					exit();
				}
			}else if(xvel < 0){
				enter();
			}else if(containerLeft > getWidth()/2){
				exit();
			}else{
				enter();
			}
		}
		/**
		 * 可拖动视图 位置改变了的回调
		 */
		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			containerLeft = left;
			changeImg(left*1f/getWidth());
//			changeContainerView();
//			changeShadeView();
		};
		/**
		 * 可拖动视图 状态改变的回调
		 */
		public void onViewDragStateChanged(int state) {
			if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
				if(containerLeft == getWidth()){
					//关闭Activity
					((Activity)getContext()).finish();
				}
			}
		};
	};
	@SuppressLint("NewApi")
	private void changeImg(float percent){
		/** percent 范围0 ~ 1
		 *  图片缩放从   0.7 ~  1     
		 */
		final int sdkInt = Build.VERSION.SDK_INT;
		float imgScale = 0.9f + percent*0.1f;
		if(sdkInt >= Build.VERSION_CODES.HONEYCOMB){
			img.setPivotX(img.getWidth()/2);
			img.setPivotY(img.getHeight()/2);
			img.setScaleX(imgScale);
			img.setScaleY(imgScale);
			if(percent < 0){
				img.setAlpha(1 + percent*1.2f);
			}else{
				img.setAlpha(0.3f+percent*0.7f);
			}
		}else{
			ViewHelper.setPivotX(img, img.getWidth()/2);
			ViewHelper.setPivotY(img, img.getHeight()/2);
			ViewHelper.setScaleX(img, imgScale);
			ViewHelper.setScaleY(img, imgScale);
			if(percent < 0){
				ViewHelper.setAlpha(img, 1 + percent*1.2f);
			}else{
				ViewHelper.setAlpha(img, 0.3f+percent*0.7f);
			}
		}
	}
	/**
	 * 主视图进入的操作
	 */
	public void enter(){
		mDragHelper.smoothSlideViewTo(container, 0, 0);
		invalidate();
	}
	/**
	 * 主视图退出的操作
	 */
	public void exit(){
		mDragHelper.smoothSlideViewTo(container, getWidth(), 0);
		invalidate();
	}
	@Override
	public void computeScroll() {
		if (mDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mDragHelper.shouldInterceptTouchEvent(ev) && gestureDetector.onTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try{
			mDragHelper.processTouchEvent(event);
		}catch(Exception e){
			
		} 
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for(int i = 0;i < getChildCount();i++){
			getChildAt(i).layout(l, t, r, b);
		}
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		for(int i = 0;i < getChildCount();i++){
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	public FrameLayout getContainer() {
		return container;
	}
	public ImageView getImg(){
		return img;
	}
}
