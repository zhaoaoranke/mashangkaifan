package com.uplooking.meihaoshiguang.tools;

import android.content.Context;

public class DensityUtil {
	/**
	 * dipת����
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context,float dpValue){
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dpValue*scale+0.5f);
	}
	/**
	 * ����תdip
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context,float pxValue){
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue/scale+0.5f);
	}
	
}
