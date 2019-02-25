package com.example.zhijiannews.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhijiannews.base.MenuDetailBasepager;
import com.example.zhijiannews.utils.LogUtil;

public class PhotosMenuDetailPager extends MenuDetailBasepager {
    private TextView textView;

    public PhotosMenuDetailPager(Context context) {

        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        LogUtil.e("图组详情页面数据被初始化。。。");
        textView.setText("图组详情页面内容");

    }
}
