package com.uplooking.meihaoshiguang.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail.RecipeStep;
import com.uplooking.meihaoshiguang.tools.BitmapHelper;
import com.uplooking.meihaoshiguang.view.SlideableView;
import  com.uplooking.meihaoshiguang.R;

public class RecipeStepActivity extends Activity 
implements OnPageChangeListener, OnClickListener{
	private ViewPager mViewPager;
	private View mBack;
	private TextView mCurPagerTv,mTotalPagerTv;
	private ResponceRecipeDetail recipeDetail;
	private BitmapUtils mBitmapUtils;
	//跟"多说几句"相关的控件
	private SlideableView mSlideableView;
	private CheckedTextView mTipsBtn;
	private TextView mTipsTv;
	private int mScrollHeight;//记录滑动位置的变量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recipe_step);
		recipeDetail = (ResponceRecipeDetail) getIntent().getSerializableExtra("object");
		mBitmapUtils = BitmapHelper.getBitmapUtils(getApplicationContext());
		initViews();
	}

	/**
	 * 初始化views
	 */
	private void initViews() {
		mBack = findViewById(R.id.backImg);
		mBack.setOnClickListener(this);
		mCurPagerTv = (TextView) findViewById(R.id.currentPageTv);
		mTotalPagerTv = (TextView) findViewById(R.id.totalPageTv);
		mTotalPagerTv.setText(recipeDetail.getSteps().size()+"");
		if(!TextUtils.isEmpty(recipeDetail.getTips())){
			mSlideableView = (SlideableView) findViewById(R.id.recipe_step_slideableview);
			mTipsBtn = (CheckedTextView) findViewById(R.id.recipe_step_tipsBtn);
			mTipsTv = (TextView) findViewById(R.id.recipe_step_tipsTv);
			mSlideableView.setVisibility(View.VISIBLE);
			mTipsTv.setText(Html.fromHtml(recipeDetail.getTips()));
			mTipsBtn.setOnClickListener(this);
		}
		initViewPager();

	}
	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		List<View> views = new ArrayList<View>();
		List<RecipeStep> steps = recipeDetail.getSteps();
		for (RecipeStep recipeStep : steps) {
			View view = LayoutInflater.from(this).inflate(R.layout.recipe_step_cusom_pager, null);
			//设置页面上的TextView
			TextView tv = (TextView) view.findViewById(R.id.recipe_step_pager_content);
			tv.setText(Html.fromHtml(recipeStep.getTitle()));
			views.add(view);
			
		}
		RecipeStepPagerAdapter pagerAdapter = new RecipeStepPagerAdapter(views);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		//获取到上个界面传过来的item位置
		int position = getIntent().getIntExtra("position", 0);
		//设置当前页是第几页
		mViewPager.setCurrentItem(position);
		mCurPagerTv.setText(position+1+"");
		mViewPager.setOnTouchListener(onTouchListener);
	}
	//ViewPager的onTouchListener
	//此监听是实现了当滑动ViewPager的时候,把mSlideableView收起来
	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			if(event.getAction() == MotionEvent.ACTION_DOWN 
					&& !TextUtils.isEmpty(recipeDetail.getTips())){
				if(!mTipsBtn.isChecked()){
					mTipsBtn.toggle();
					mSlideableView.smoothScrollTo(0, -mScrollHeight, 600);
				}
			}
			return false;
		}
	};

	private class RecipeStepPagerAdapter extends PagerAdapter{
		private List<View> views;
		public RecipeStepPagerAdapter(List<View> views) {
			super();
			this.views = views;
		}
		@Override
		public int getCount() {
			return views.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = views.get(position);
			ImageView img = (ImageView) v.findViewById(R.id.recipe_step_pager_img);
			mBitmapUtils.display(img, recipeDetail.getSteps().get(position).getImg());
			container.addView(v);
			return v;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View v = views.get(position);
			container.removeView(v);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	@Override
	public void onPageSelected(int arg0) {
		mCurPagerTv.setText(arg0+1+"");
	}
	
	/**
	 * onWindowFocusChanged这个方法里面可以在界面一打开就获取到控件的宽高
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			if(!TextUtils.isEmpty(recipeDetail.getTips()) && mScrollHeight == 0){
			
				mScrollHeight = mSlideableView.getHeight() - mTipsBtn.getHeight();
				//一出来，先滑动mScrollHeight距离，保证mSlideableView收缩下去而按钮留在界面
				mSlideableView.scrollTo(0, -mScrollHeight);
				mTipsBtn.setChecked(true);
			}
		}
	}
	@Override
	public void onClick(View v) {
		
		if(v == mBack){
			onBackPressed();
		}else if(v == mTipsBtn){
			if(mTipsBtn.isChecked()){
				mSlideableView.smoothScrollTo(0, 0, 0);
			}else{
				mSlideableView.smoothScrollTo(0, -mScrollHeight, 0);
			}
			mTipsBtn.toggle();
		}
	}
}
