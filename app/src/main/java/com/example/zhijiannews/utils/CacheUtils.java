package com.example.zhijiannews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zhijiannews.SplashActivity;
import com.example.zhijiannews.activity.GuideActivity;

public class CacheUtils {
    /**
     * 得到缓存值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 保存软件参数
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();


    }
}
