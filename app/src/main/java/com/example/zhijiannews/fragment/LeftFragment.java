package com.example.zhijiannews.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhijiannews.MainActivity;
import com.example.zhijiannews.R;
import com.example.zhijiannews.base.BaseFragment;
import com.example.zhijiannews.domain.NewsCenterPagerBean;
import com.example.zhijiannews.pager.NewsCenterPager;
import com.example.zhijiannews.utils.DensityUtil;
import com.example.zhijiannews.utils.LogUtil;

import java.util.List;

public class LeftFragment extends BaseFragment {
    private ListView listView;
    private List<NewsCenterPagerBean.DataBean> data;
    private LeftmenuFragmentAdapter adapter;

    /**
     * 点击的位置
     */
    private int prePosition;

    @Override
    public View initView() {
        LogUtil.e("左侧菜单视图被初始化了");
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context, 40), 0, 0);
        listView.setDividerHeight(0);//设置分割线高度为0
        listView.setCacheColorHint(Color.TRANSPARENT);

        //设置按下listView的item不变色
        listView.setSelector(android.R.color.transparent);
        //设置item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.记录点击的位置，变成红色
                prePosition = position;
                adapter.notifyDataSetChanged();//getCount()-->getView
                //2.把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//关<->开
                //3.切换到对应的详情页面：新闻详情页面，专题详情页面，图组详情页面，互动详情页面
                swichPager(prePosition);
            }
        });
        return listView;
    }
    /*
        左侧菜单接收新闻中心发过来的数据
         */
    public void setdata(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            LogUtil.e("title==" + data.get(i).getTitle());
        }
        //设置适配器
        adapter = new LeftmenuFragmentAdapter();
        listView.setAdapter(adapter);
        //默认跳转到新闻详情页面
        swichPager(prePosition);
    }

    private void swichPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.swichPager(position);
    }

    @Override
    public void initData() {
        super.initData();
    }

    

    private class LeftmenuFragmentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu, null);
            textView.setText(data.get(position).getTitle());
            textView.setEnabled(position == prePosition);
            return textView;
        }
    }
}
