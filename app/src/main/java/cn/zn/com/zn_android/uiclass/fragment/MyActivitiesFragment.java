package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.MyActivitiesAdapter;
import cn.zn.com.zn_android.model.bean.ActivityBean;
import cn.zn.com.zn_android.uiclass.activity.ActivityInfoActivity;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 我的活动
 * Created by Jolly on 2016/7/4 0004.
 */
public class MyActivitiesFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.xlv_activities)
    XListView mXlvActivities;

    private List<ActivityBean> data = new ArrayList<>();

    public static MyActivitiesFragment newInstance() {
        return new MyActivitiesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_my_activities);
    }

    @Override
    protected void initView(View view) {
        getData();
        MyActivitiesAdapter adapter = new MyActivitiesAdapter(_mContext, R.layout.item_my_activities, data);
        mXlvActivities.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        mXlvActivities.setOnItemClickListener(this);
    }

    private void getData() {
        for (int i = 0; i < 10; i ++) {
            ActivityBean bean;
            if (i%3 == 0) {
                bean = new ActivityBean("", "【有奖竞猜】猴年第一个交易日沪指收盘点位是环海",
                        "2016-7-20", "正在进行");
            } else if (i%3 == 1) {
                bean = new ActivityBean("", "【有奖竞猜】猴年第一个交易日沪指收盘点位是环海",
                        "2016-7-20", "即将开始");
            } else {
                bean = new ActivityBean("", "【有奖竞猜】猴年第一个交易日沪指收盘点位是环海",
                        "2016-6-20", "已经结束");
            }
            data.add(bean);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(_mContext, ActivityInfoActivity.class));
    }
}
