package com.example.zhijiannews.menudetailpager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.zhijiannews.MainActivity;
import com.example.zhijiannews.R;
import com.example.zhijiannews.base.MenuDetailBasepager;
import com.example.zhijiannews.domain.NewsCenterPagerBean;
import com.example.zhijiannews.menudetailpager.tabdetailpager.TabDetailPager;
import com.example.zhijiannews.utils.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class NewsMenuDetailPager extends MenuDetailBasepager {

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;
    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;
    private List<NewsCenterPagerBean.DataBean.ChildrenBean> children;
    private List<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {

        super(context);
        children = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.newsmenu_detail_pager, null);
        x.view().inject(this, view);
        //向右箭头点击下一个页面
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        LogUtil.e("新闻详情页面数据被初始化。。。");
        tabDetailPagers = new ArrayList<>();
        //准备新闻详情页面数据
        for (int i = 0; i < children.size(); i++) {
            TabDetailPager tabDetailPager = new TabDetailPager(context, children.get(i));
            tabDetailPagers.add(tabDetailPager);
        }
        //设置适配器
        viewPager.setAdapter(new myNewsMenuDetailPagerAdapter());
        //关联viewpager
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager.addOnPageChangeListener(new myPageChangeListener());
    }

    private class myNewsMenuDetailPagerAdapter extends PagerAdapter {
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootview = tabDetailPager.rootview;
            tabDetailPager.initData();//初始化数据
            container.addView(rootview);
            return rootview;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private class myPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                //SlidingMenu可以滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            } else {
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 根据传人的参数设置是否让SlidingMenu可以滑动
     */
    private void isEnableSlidingMenu(int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }
}
