package com.example.zhijiannews.activity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zhijiannews.MainActivity;
import com.example.zhijiannews.R;
import com.example.zhijiannews.SplashActivity;
import com.example.zhijiannews.utils.CacheUtils;
import com.example.zhijiannews.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    private static final String TAG = GuideActivity.class.getSimpleName();
    private ViewPager viewPager;
    private Button btn_start_main;
    private LinearLayout ll_point_group;
    private int[] ids = new int[]{
            R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3
    };
    private List<ImageView> imageViews = new ArrayList<>();
    /**
     * 两点的间距
     */
    private int leftmax;

    private int widthdpi;

    private ImageView iv_red_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_guide);
        viewPager = findViewById(R.id.viewpager);
        btn_start_main = findViewById(R.id.btn_start_main);
        ll_point_group = findViewById(R.id.ll_point_group);
        iv_red_point = findViewById(R.id.iv_red_point);

        widthdpi = DensityUtil.dip2px(this, 10);
        Log.e(TAG, widthdpi + "--------------");

        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);
            //创建点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            /**
             * 单位是像数
             * 把单位当成dp转成对应的像数
             */
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi, widthdpi);
            if (i != 0) {
                //不包括第0个，所有的点距离左边有10个像数
                params.leftMargin = widthdpi;
            }
            point.setLayoutParams(params);
            //添加到线性布局里面
            ll_point_group.addView(point);

        }
        //设置ViewPager的适配器
        viewPager.setAdapter(new MyPagerAdapter());
        //根据View的生命周期，当视图执行到onLayout或者onDraw的时候，视图的高和宽，边距都有了
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        //得到屏幕滑动的百分比
        viewPager.addOnPageChangeListener(new myOnPageChangeListener());
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.保存曾经进入主页面
                CacheUtils.putBoolean(GuideActivity.this, SplashActivity.START_MAIN, true);
                //2、跳转到主页面
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                //3.关闭引导页面
                finish();

            }
        });
    }

    //另一种加载动态指示点方法
    private void initIndicator() {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
        for (int i = 0; i < ids.length; i++) {
            View view = new View(this);
            view.setId(i);
            view.setBackgroundResource(i == 0 ? R.drawable.dot_focus : R.drawable.dot_normal);
            if (i != 0) {
                lp.leftMargin = 2 * width;
            }
            view.setLayoutParams(lp);
            ll_point_group.addView(view);
        }
    }


    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViews.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private class myOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当页面滑动了会回调这个方法
         *
         * @param position             当前滑动页面的位置
         * @param positionOffset       页面滑动的百分比
         * @param positionOffsetPixels 滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //另一种加载动态指示点方法   
//            for (int i = 0; i < imageViews.size(); i++) {
//                ll_point_group.getChildAt(i).setBackgroundResource(index == i ? R.drawable.dot_focus : R.drawable.dot_normal);
//                
//            }

            //两点间移动的距离 = 屏幕滑动百分比 * 间距
//            int leftmargin = (int) (positionOffset * leftmax);
//            Log.e(TAG,"position=="+position+",positionOffset=="+positionOffset+",positionOffsetPixels=="+positionOffsetPixels);

            //两点间滑动距离对应的坐标 = 原来的起始位置 +  两点间移动的距离
            int leftmargin = (int) (position * leftmax + (positionOffset * leftmax));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftmargin;
            iv_red_point.setLayoutParams(params);
        }

        /**
         * 当页面被选中的时候，回调这个方法
         *
         * @param position 被选中页面的对应的位置
         */
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                btn_start_main.setVisibility(View.VISIBLE);
            } else {
                btn_start_main.setVisibility(View.GONE);
            }
        }

        /**
         * 当ViewPager页面滑动状态发生变化的时候
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            //执行不只一次
            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(MyOnGlobalLayoutListener.this);

//            间距  = 第1个点距离左边的距离 - 第0个点距离左边的距离
            leftmax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
            Log.e(TAG, "leftmax==" + leftmax);
        }
    }
}
