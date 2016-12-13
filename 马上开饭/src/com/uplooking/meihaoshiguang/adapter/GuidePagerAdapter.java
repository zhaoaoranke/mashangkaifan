package com.uplooking.meihaoshiguang.adapter;

import java.util.List;

import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class GuidePagerAdapter extends PagerAdapter{
	private List<View> views;
	public GuidePagerAdapter(List<View> views) {
		super();
		this.views = views;
		
	}
	//分页的数量
	@Override
	public int getCount() {
		return views.size();
	}
	
	//用于判断是否由对象生成界面
	//该函数用来判断instantiateItem(ViewGroup, int)
	//函数所返回来的Key与一个页面视图是否是代表的同一个视图
	//(即它俩是否是对应的，对应的表示同一个View)
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	//创建每页的视图
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = views.get(position);
		container.addView(view);
		return view;
	}
	//删除每页的视图
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = views.get(position);
		container.removeView(view);
	}
	
}
