package com.uplooking.meihaoshiguang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

public class MyGridView extends GridView{

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	//���Ƕ��listview����scrollview��ʹ��ʱ�����ܻ���
	//ֱ��ȫ������չ�������������������
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
