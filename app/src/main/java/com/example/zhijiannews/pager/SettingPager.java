package com.example.zhijiannews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.zhijiannews.base.BasePager;
import com.example.zhijiannews.utils.LogUtil;

public class SettingPager extends BasePager {
    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        LogUtil.e("设置中心数据被初始化了..");
        //1.设置标题
        tvTitle.setText("我是设置");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        //3.把子视图数据添加到BaseFragment中的FragmentLayout中
        flContent.addView(textView);
        //4.绑定数据
        textView.setText("设置内容");
    }
}
