package com.uplooking.meihaoshiguang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

public class MyGridView extends GridView{

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	//解决嵌套listview或者scrollview等使用时，不能滑动
	//直接全部数据展开，活动则用最外层的套用
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
