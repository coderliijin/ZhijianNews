package com.example.zhijiannews;


import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.zhijiannews.fragment.ContentFragment;
import com.example.zhijiannews.fragment.LeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends SlidingFragmentActivity {
    private int screeWidth;
    private int screeHeight;
    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSlidingMenu();
        //初始化Fragment
        initFragment();
    }

    private void initFragment() {
        //  1.得到fragmentmanager
        FragmentManager fm = getSupportFragmentManager();
        //2、开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //3.替换
        ft.replace(R.id.fl_main_content, new ContentFragment(), MAIN_CONTENT_TAG);
        ft.replace(R.id.fl_leftmenu, new LeftFragment(), LEFTMENU_TAG);
        //提交
        ft.commit();

    }

    private void initSlidingMenu() {
        //1.设置主页面
        setContentView(R.layout.activity_main);

        //2.设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);


        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        //slidingMenu.setSecondaryMenu(R.layout.activity_leftmenu);//设置右侧菜单

        //4.设置显示的模式：左侧菜单+主页，左侧菜单+主页面+右侧菜单；主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);
        //5.设置滑动模式：滑动边缘，全屏滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        DisplayMetrics outmetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outmetrics);
        screeWidth = outmetrics.widthPixels;
        screeHeight = outmetrics.heightPixels;
        //6.设置主页占据的宽度
//        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this, 200));
        slidingMenu.setBehindOffset((int) (screeWidth * 0.625));

    }

    /**
     * 得到左侧菜单Fragment
     *
     * @return
     */
    public LeftFragment getLeftFragment() {

        return (LeftFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);
    }

    /**
     * 得到内容Fragment
     *
     * @return
     */
    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_CONTENT_TAG);
    }
}
