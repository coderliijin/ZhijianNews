package com.example.zhijiannews.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.zhijiannews.CustomView.NoScrollViewPager;
import com.example.zhijiannews.MainActivity;
import com.example.zhijiannews.R;
import com.example.zhijiannews.base.BaseFragment;
import com.example.zhijiannews.base.BasePager;
import com.example.zhijiannews.pager.GovaffairPager;
import com.example.zhijiannews.pager.HomePager;
import com.example.zhijiannews.pager.NewsCenterPager;
import com.example.zhijiannews.pager.SettingPager;
import com.example.zhijiannews.pager.SmartServicePager;
import com.example.zhijiannews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ContentFragment extends BaseFragment {


    NoScrollViewPager viewpager;

    RadioGroup rgMain;
    private List<BasePager> basePagers;

    @Override
    public View initView() {
        LogUtil.e("正文Fragemnt视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment, null);
        viewpager = view.findViewById(R.id.viewpager);
        rgMain = view.findViewById(R.id.rg_main);
        return view;

    }

    @Override
    public void initData() {
        LogUtil.e("正文Fragment数据被初始化了");

        //初始化五个页面
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));//主页面
        basePagers.add(new NewsCenterPager(context));//新闻中心
        basePagers.add(new SmartServicePager(context));//商城
        basePagers.add(new GovaffairPager(context));//购物车
        basePagers.add(new SettingPager(context));//设置

        viewpager.setAdapter(new ContentFragmentAdapter());
        rgMain.setOnCheckedChangeListener(new myOnCheckedChangeListener());

        //监听某个页面被选中，初始化对应页面数据
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        basePagers.get(0).initData();
        //设置模式SlidingMenu不可以滑动
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);

    }

    /*
    得到新闻中心页面
     */
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }


    private class ContentFragmentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position);//各个页面的实例
            View rootView = basePager.rootView;//代表各个子页面
//调用各个页面的initData()
            // basePager.initData();//初始化数据，这样会数据重复加载
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private class myOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home://主页radioButton的id
                    viewpager.setCurrentItem(0, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_newscenter://新闻中心radioButton的id
                    viewpager.setCurrentItem(1, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_smartservice://智慧服务radioButton的id
                    viewpager.setCurrentItem(2, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_govaffair://政要指南的RadioButton的id
                    viewpager.setCurrentItem(3, false);

                    break;
                case R.id.rb_setting://设置中心RadioButton的id
                    viewpager.setCurrentItem(4, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }

        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            basePagers.get(i).initData();
        }

        @Override
        public void onPageScrollStateChanged(int i) {

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
