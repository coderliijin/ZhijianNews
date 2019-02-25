package com.example.zhijiannews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhijiannews.MainActivity;
import com.example.zhijiannews.base.BasePager;
import com.example.zhijiannews.base.MenuDetailBasepager;
import com.example.zhijiannews.domain.NewsCenterPagerBean;
import com.example.zhijiannews.fragment.LeftFragment;
import com.example.zhijiannews.menudetailpager.InteracMenuDetailPager;
import com.example.zhijiannews.menudetailpager.NewsMenuDetailPager;
import com.example.zhijiannews.menudetailpager.PhotosMenuDetailPager;
import com.example.zhijiannews.menudetailpager.TopicMenuDetailPager;
import com.example.zhijiannews.utils.Constants;
import com.example.zhijiannews.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class NewsCenterPager extends BasePager {
    private List<MenuDetailBasepager> detaiBasePagers;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        LogUtil.e("新闻中心数据被初始化了..");
        //1.设置标题
        tvTitle.setText("我是新闻中心");
        ib_menu.setVisibility(View.VISIBLE);
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);

        //3.把子视图数据添加到BasePager中的FragmentLayout中
        flContent.addView(textView);
        //4.绑定数据
        textView.setText("新闻中心内容");

        //联网请求数据
        getDataFromNet();


        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
            }
        });
    }

    //请求数据
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xUtils3联网请求成功==" + result);
                //解析数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3联网请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xUtils3-onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xUtils3-onFinished");
            }
        });
    }

    List<NewsCenterPagerBean.DataBean> data;

    private void processData(String result) {
        NewsCenterPagerBean bean = parsedJson(result);
        data = bean.getData();
        MainActivity mainActivity = (MainActivity) context;
        //得到左侧菜单
        LeftFragment leftFragment = mainActivity.getLeftFragment();

        //添加详情页面
        detaiBasePagers = new ArrayList<>();
        detaiBasePagers.add(new NewsMenuDetailPager(context));//新闻详情页面
        detaiBasePagers.add(new TopicMenuDetailPager(context));//专题详情页面
        detaiBasePagers.add(new PhotosMenuDetailPager(context));//图组详情页面
        detaiBasePagers.add(new InteracMenuDetailPager(context));//互动详情页面

        //把数据传递给左侧菜单
        leftFragment.setdata(data);

    }

    //解析json数据
    private NewsCenterPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, NewsCenterPagerBean.class);
    }

    public void swichPager(int position) {
        //1.设置标题
        tvTitle.setText(data.get(position).getTitle());
        //2.移除之前内容
        flContent.removeAllViews();
        //3.添加内容
        MenuDetailBasepager detailBasepager = detaiBasePagers.get(position);
        View rootview = detailBasepager.rootview;
        detailBasepager.initData();//初始化数据
        flContent.addView(rootview);
    }
}
