package com.example.zhijiannews.base;

import android.content.Context;
import android.view.View;

public abstract class MenuDetailBasepager {
    /*
    上下文
     */
    public final Context context;
    /*
    代表各个详情页面
     */
    public View rootview;

    public MenuDetailBasepager(Context context) {
        this.context = context;
        rootview = initView();
    }

    /*
    抽象方法强制孩子实现该方法，每个页面实现不同效果
     */
    public abstract View initView();

    /*
    子页面需要绑定数据，联网请求数据的时候。重写该方法
     */
    public void initData() {

    }

    ;
}
