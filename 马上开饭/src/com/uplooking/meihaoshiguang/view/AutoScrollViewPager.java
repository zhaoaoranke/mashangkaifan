package com.uplooking.meihaoshiguang.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.BitmapUtils;
import com.uplooking.meihaoshiguang.R;
import com.uplooking.meihaoshiguang.activity.MainActivity;
import com.uplooking.meihaoshiguang.activity.RecipeDetailActivity;
import com.uplooking.meihaoshiguang.entity.ResponceHome.HeadObject.HeadRecipe;

import com.uplooking.meihaoshiguang.tools.BitmapHelper;
import com.uplooking.meihaoshiguang.tools.IntentUtil;

public class AutoScrollViewPager extends ViewPager {
	private Timer timer;
	private TimerTask task;

	// �Զ����һ��������
	private OnScollChangeListener onScollChangeListener;

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			setCurrentItem(getCurrentItem() + 1, false);
			return false;
		}
	});

	public AutoScrollViewPager(Context context) {
		super(context, null);
		timer = new Timer();
	}

	public AutoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		timer = new Timer();
	}

	// l��ǰˮƽ������
	// t��ǰ��ֱ������
	// oldl��ǰˮƽ������
	// oldtǰ��ֱ��������Դ
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		if (onScollChangeListener != null) {
			onScollChangeListener.scroll(l);
		}

	}

	public void setOnScollChangeListener(
			OnScollChangeListener onScollChangeListener) {
		this.onScollChangeListener = onScollChangeListener;
	}

	// 1.AutoScrollViewPager����һ�������ӿ�
	// 2.��ĳ����ʵ������ӿڣ�����������ʵ������
	// 3.���ʵ���������IndicatorView��move����
	// 4.��ִ�еķ���д��AutoScrollViewPager��onScrollChanged��д��д��д�����
	// �ǵ�Ҫִ��super�ķ�������Ҫ�ƻ�ԭ�����߼�
	// 5.����ÿ�η�ҳʱ��AutoScrollViewPager��ִ������onScrollChanged��
	// Ҳ��ִ����IndicatorView��move
	public interface OnScollChangeListener {
		void scroll(int x);
	}

	// 20
	// 5ҳ
	@Override
	public void setAdapter(PagerAdapter adapter) {
		super.setAdapter(adapter);
		int curPage = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2)
				% ((AutoSlidingPagerAdapter) adapter).getViews().size();
		setCurrentItem(curPage, false);
		startSliding();
	}

	/**
	 * ��ʼ�Զ�����
	 */
	public void startSliding() {
		if (task == null) {
			task = new TimerTask() {

				@Override
				public void run() {
					handler.sendEmptyMessage(0);
				}
			};
			timer.schedule(task, 3000, 3000);
		}
	}

	/**
	 * ֹͣ�Զ�����
	 */
	public void stopSliding() {
		handler.removeMessages(0);
		if (task != null) {
			task.cancel();
			task = null;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			stopSliding();
			MainActivity aty = (MainActivity) getContext();
			aty.setIntercept(false);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			startSliding();
			aty = (MainActivity) getContext();
			aty.setIntercept(true);
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			startSliding();
			MainActivity aty = (MainActivity) getContext();
			aty.setIntercept(true);
			break;
		}
		return super.onTouchEvent(ev);
	}

	public class AutoSlidingPagerAdapter extends PagerAdapter {
		private List<ImageView> views;

		private List<HeadRecipe> recipes;
		private BitmapUtils bitmapUtils;
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Integer i = (Integer) v.getTag();
				HeadRecipe recipe = recipes.get(i);
				Intent intent = new Intent(getContext(),
						RecipeDetailActivity.class);
				intent.putExtra("recipe_id", recipe.recipeId);
				IntentUtil.startActivity(getContext(), intent);
				
		
			}
		};

		public AutoSlidingPagerAdapter(List<HeadRecipe> recipes) {
			super();
			this.recipes = recipes;
			views = new ArrayList<ImageView>();
			for (int i = 0; i < recipes.size(); i++) {
				ImageView img = new ImageView(getContext());
				img.setScaleType(ScaleType.CENTER_CROP);
				img.setTag(i);
				img.setOnClickListener(onClickListener);
				views.add(img);
			}
			// �˴����õĵ���ģʽ,ֱ��newÿ�ζ�Ҫnewһ��������Դ
			// bitmapUtils=new BitmapUtils(getContext()
			// .getApplicationContext());
			bitmapUtils = BitmapHelper.getBitmapUtils(getContext()
					.getApplicationContext());
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = views.get(position % views.size());
			container.removeView(view);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = views.get(position % views.size());
			// URL url=new URL(recipes.get(position).img);
			// view.setImageURI(url);
			bitmapUtils.display(view, recipes.get(position % views.size()).img);
			container.addView(view);
			// view.setImageURI(Uri.parse(recipes.get(position%views.size()).img));

			return view;
		}

		public List<ImageView> getViews() {
			return views;
		}

		public HeadRecipe getItem(int position) {
			return recipes.get(position % recipes.size());
		}
	}
}
