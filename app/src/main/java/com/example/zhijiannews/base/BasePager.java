package com.example.zhijiannews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zhijiannews.R;

/**
 * 作用：基类或者说公共类
 * * HomePager,NewsCenterPager,
 * * SmartServicePager,GovaffairPager
 * * SettingPager都继承BasePager
 */
public class BasePager {
    /**
     * 显示标题
     */

    public TextView tvTitle;
    /**
     * 点击侧滑的
     */

    public ImageButton ib_menu;
    /**
     * 加载各个子页面
     */

    public FrameLayout flContent;
    /**
     * 上下文
     */
    public final Context context;
    /**
     * 视图，代表各个不同的页面
     */
    public View rootView;

    public BasePager(Context context) {
        this.context = context;
        //构造方法一执行，视图就被初始化了
        rootView = initView();
    }

    private View initView() {
        //基类的页面
        View view = View.inflate(context, R.layout.base_pager, null);

        tvTitle = view.findViewById(R.id.tv_title);
        ib_menu = view.findViewById(R.id.ib_menu);
        flContent = view.findViewById(R.id.fl_content);
        return view;
    }

    /**
     * 初始化数据;当孩子需要初始化数据;或者绑定数据;联网请求数据并且绑定的时候，重写该方法
     */
    public void initData() {

    }
}
