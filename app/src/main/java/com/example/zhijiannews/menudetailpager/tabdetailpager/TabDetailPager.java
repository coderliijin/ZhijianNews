package com.example.zhijiannews.menudetailpager.tabdetailpager;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhijiannews.CustomView.HorizontalScrollViewPager;
import com.example.zhijiannews.R;
import com.example.zhijiannews.base.MenuDetailBasepager;
import com.example.zhijiannews.domain.NewsCenterPagerBean;
import com.example.zhijiannews.domain.TabDetailPagerBean;
import com.example.zhijiannews.utils.CacheUtils;
import com.example.zhijiannews.utils.Constants;
import com.example.zhijiannews.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * 作用：页签详情页面
 */
public class TabDetailPager extends MenuDetailBasepager {

    private HorizontalScrollViewPager viewPager;
    private TextView tv_title;
    private ListView listView;

    private LinearLayout linearLayout;
    /**
     * 顶部轮播图数据
     */
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    /**
     * 新闻列表数据集合
     */
    private List<TabDetailPagerBean.DataBean.NewsBean> news;

    private NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;
    private String url;
    private ImageOptions imageOptions;
    /**
     * 之前点高亮显示的位置
     */
    private int prePosition;
    private myListViewAdapter adapter;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.tabdetail_pager, null);
        listView = view.findViewById(R.id.listview);

        View topNewsView = View.inflate(context, R.layout.topnews, null);

        viewPager = topNewsView.findViewById(R.id.viewpager);
        tv_title = topNewsView.findViewById(R.id.tv_title);
        linearLayout = topNewsView.findViewById(R.id.ll_point_group);
        //把顶部轮播图部分视图，以头的形式添加到listview中
        listView.addHeaderView(topNewsView);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();
        String saveJson = CacheUtils.getString(context, url);
        LogUtil.e(childrenBean.getTitle() + "联网地址==" + url);
        if (!TextUtils.isEmpty(saveJson)) {
            //解析数据
            processData(saveJson);
        }
        getDataFromNet();

    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //缓存数据
                CacheUtils.putString(context, url, result);
                LogUtil.e(childrenBean.getTitle() + "页面数据请求成功==" + result);
                //解析与显示数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenBean.getTitle() + "页面数据请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenBean.getTitle() + "页面数据请求onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String result) {
        TabDetailPagerBean bean = parsedJson(result);
        //获取顶部轮播图数据
        topnews = bean.getData().getTopnews();
        //设置适配器
        viewPager.setAdapter(new TabDetailPagerTopNewsAdapter());
        //添加红点
        addPoint();
        //监听页面的变化，设置红点变化和文本变化
        viewPager.addOnPageChangeListener(new myOnPageChangeListener());
        tv_title.setText(topnews.get(prePosition).getTitle());
        //准备listview的数据
        news = bean.getData().getNews();
        //设置适配器
        adapter = new myListViewAdapter();
        listView.setAdapter(adapter);
    }

    /**
     * 新闻列表listview1的适配器
     */
    class myListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            String imagurl = Constants.BASE_URL + newsBean.getListimage();
            //请求图片
            x.image().bind(viewHolder.iv_icon, imagurl, imageOptions);
            //设置标题
            viewHolder.tv_title.setText(newsBean.getTitle());
            //设置时间
            viewHolder.tv_time.setText(newsBean.getPubdate());

            return convertView;
        }
    }

    /**
     * 静态类ViewHolder
     */
    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    /**
     * 轮播图添加红点
     **/
    private void addPoint() {
        //移除所有红点
        linearLayout.removeAllViews();
        for (int i = 0; i < topnews.size(); i++) {
            ImageView imageView = new ImageView(context);
            //设置背景选择器
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(8), DensityUtil.dip2px(8));

            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(8);
            }
            imageView.setLayoutParams(params);
            linearLayout.addView(imageView);
        }
    }

    /**
     * 顶部轮播图的监听器
     */
    private class myOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //1.设置标题
            tv_title.setText(topnews.get(position).getTitle());

            //2设置高亮点
            //取消上一个点高亮
            linearLayout.getChildAt(prePosition).setEnabled(false);
            //设置当前点高亮      
            linearLayout.getChildAt(position).setEnabled(true);
            prePosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 使用Gson解析数据
     */
    private TabDetailPagerBean parsedJson(String result) {
        return new Gson().fromJson(result, TabDetailPagerBean.class);
    }

    /**
     * 顶部轮播图的适配器
     */
    private class TabDetailPagerTopNewsAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView image = new ImageView(context);
            //设置默认背景
            image.setBackgroundResource(R.drawable.home_scroll_default);
            //设置X轴和Y轴拉伸
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            //把图片添加到viewpager中
            container.addView(image);
            TabDetailPagerBean.DataBean.TopnewsBean topnewsItem = topnews.get(position);
            //图片请求地址
            String imageURl = Constants.BASE_URL + topnewsItem.getTopimage();
            //联网请求图片
            x.image().bind(image, imageURl);

            return image;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
