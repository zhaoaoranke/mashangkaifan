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
	//��ҳ������
	@Override
	public int getCount() {
		return views.size();
	}
	
	//�����ж��Ƿ��ɶ������ɽ���
	//�ú��������ж�instantiateItem(ViewGroup, int)
	//��������������Key��һ��ҳ����ͼ�Ƿ��Ǵ����ͬһ����ͼ
	//(�������Ƿ��Ƕ�Ӧ�ģ���Ӧ�ı�ʾͬһ��View)
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	//����ÿҳ����ͼ
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = views.get(position);
		container.addView(view);
		return view;
	}
	//ɾ��ÿҳ����ͼ
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = views.get(position);
		container.removeView(view);
	}
	
}
