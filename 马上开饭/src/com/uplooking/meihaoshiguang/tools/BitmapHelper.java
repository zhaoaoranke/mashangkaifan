package com.uplooking.meihaoshiguang.tools;

import android.content.Context;
import com.lidroid.xutils.BitmapUtils;

public class BitmapHelper {
    private BitmapHelper() {
    }

    private static BitmapUtils bitmapUtils;

    /**
     * BitmapUtils不是单例�? 根据�?要重载多个获取实例的方法
     *
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(appContext);
        }
        return bitmapUtils;
    }
}
