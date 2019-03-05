package com.example.zhijiannews.utils;

import android.content.Context;
import android.content.SharedPreferences;

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

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
}
