package com.example.zhijiannews;

import android.app.Application;

import org.xutils.x;

public class ZhijianNewsApplication extends Application {
    /**
     * 所有组件被创建之前执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(true);
        x.Ext.init(this);
    }
}
