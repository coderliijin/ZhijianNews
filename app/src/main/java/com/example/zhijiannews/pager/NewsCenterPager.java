package com.example.zhijiannews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhijiannews.MainActivity;
import com.example.zhijiannews.base.BasePager;
import com.example.zhijiannews.base.MenuDetailBasepager;
import com.example.zhijiannews.domain.NewsCenterPagerBean;
import com.example.zhijiannews.domain.NewsCenterPagerBean2;
import com.example.zhijiannews.fragment.LeftFragment;
import com.example.zhijiannews.menudetailpager.InteracMenuDetailPager;
import com.example.zhijiannews.menudetailpager.NewsMenuDetailPager;
import com.example.zhijiannews.menudetailpager.PhotosMenuDetailPager;
import com.example.zhijiannews.menudetailpager.TopicMenuDetailPager;
import com.example.zhijiannews.utils.CacheUtils;
import com.example.zhijiannews.utils.Constants;
import com.example.zhijiannews.utils.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
        //得到缓存数据
        String savejson = CacheUtils.getString(context, Constants.NEWSCENTER_PAGER_URL);
        if (!TextUtils.isEmpty(savejson)) {
            processData(savejson);
        }
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
                //缓存数据
                CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, result);
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

    /**
     * 解析json数据和显示数据
     *
     * @param result
     */
    private void processData(String result) {
        NewsCenterPagerBean bean = parsedJson(result);
        // NewsCenterPagerBean2 bean2 = parsedJson2(result);
//        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        // LogUtil.e("使用Gson解析json数据成功-title==" + title);
//        String title2 = bean2.getData().get(0).getChildren().get(1).getTitle();;
//        LogUtil.e("使用Gson解析json数据成功NewsCenterPagerBean2-title2-------------------------==" + title2);

        //给左侧菜单传递数据
        data = bean.getData();

        MainActivity mainActivity = (MainActivity) context;
        //得到左侧菜单
        LeftFragment leftFragment = mainActivity.getLeftFragment();

        //添加详情页面
        detaiBasePagers = new ArrayList<>();
        detaiBasePagers.add(new NewsMenuDetailPager(context, data.get(0)));//新闻详情页面
        detaiBasePagers.add(new TopicMenuDetailPager(context));//专题详情页面
        detaiBasePagers.add(new PhotosMenuDetailPager(context));//图组详情页面
        detaiBasePagers.add(new InteracMenuDetailPager(context));//互动详情页面

        //把数据传递给左侧菜单
        leftFragment.setdata(data);

    }

    /**
     * 使用Android系统自带的API解析json数据
     *
     * @param json
     * @return
     */
    private NewsCenterPagerBean2 parsedJson2(String json) {
        NewsCenterPagerBean2 bean2 = new NewsCenterPagerBean2();
        try {
            JSONObject object = new JSONObject(json);
            int retcode = object.optInt("retcode");
            bean2.setRetcode(retcode);

            JSONArray data = object.optJSONArray("data");

            if (data != null && data.length() > 0) {

                List<NewsCenterPagerBean2.DetailPagerData> detailPagerDatas = new ArrayList<>();
                //设置列表数据
                bean2.setData(detailPagerDatas);

                //for循环，
                for (int i = 0; i < data.length(); i++) {

                    JSONObject jsonObject = (JSONObject) data.get(i);

                    NewsCenterPagerBean2.DetailPagerData detailPagerData = new NewsCenterPagerBean2.DetailPagerData();
                    //添加到集合中
                    detailPagerDatas.add(detailPagerData);
                    int id = jsonObject.optInt("id");
                    detailPagerData.setId(id);
                    int type = jsonObject.optInt("type");
                    detailPagerData.setType(type);
                    String title = jsonObject.optString("title");
                    detailPagerData.setTitle(title);
                    String url = jsonObject.optString("url");
                    detailPagerData.setUrl(url);
                    String url1 = jsonObject.optString("url1");
                    detailPagerData.setUrl1(url1);
                    String dayurl = jsonObject.optString("dayurl");
                    detailPagerData.setDayurl(dayurl);
                    String excurl = jsonObject.optString("excurl");
                    detailPagerData.setExcurl(excurl);
                    String weekurl = jsonObject.optString("weekurl");
                    detailPagerData.setWeekurl(weekurl);

                    JSONArray children = jsonObject.optJSONArray("children");
                    if (children != null && children.length() > 0) {
                        List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> childrenDatas = new ArrayList<>();
                        //设置集合-ChildrenData
                        detailPagerData.setChildren(childrenDatas);

                        for (int j = 0; j < children.length(); j++) {
                            JSONObject childrenitem = (JSONObject) children.get(j);

                            NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData = new NewsCenterPagerBean2.DetailPagerData.ChildrenData();
                            //添加到集合中
                            childrenDatas.add(childrenData);

                            int childId = childrenitem.optInt("id");
                            childrenData.setId(childId);
                            String childTitle = childrenitem.optString("title");
                            childrenData.setTitle(childTitle);
                            String childUrl = childrenitem.optString("url");
                            childrenData.setUrl(childUrl);
                            int childType = childrenitem.optInt("type");
                            childrenData.setType(childType);
                        }

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(json, NewsCenterPagerBean2.class);
    }

    /**
     * 解析json数据：1,使用系统的API解析json；2,使用第三方框架解析json数据，例如Gson,fastjson
     *
     * @param json
     * @return
     */
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
