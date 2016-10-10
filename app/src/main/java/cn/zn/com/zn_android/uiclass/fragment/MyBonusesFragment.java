package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.AlertListAdapter;
import cn.zn.com.zn_android.model.bean.MyBonusesBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.uiclass.activity.BonusAnnounceActivity;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 我的奖励
 * Created by Jolly on 2016/7/4 0004.
 */
public class MyBonusesFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.xlv_bonuses)
    XListView mXlvBonuses;

    private List<MarketImp> bonusesList = new ArrayList<>();

    public static MyBonusesFragment newInstance() {
        return new MyBonusesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_my_bonuses);
    }

    @Override
    protected void initView(View view) {
        getData();
        AlertListAdapter adapter = new AlertListAdapter(_mContext, R.layout.item_alert, bonusesList);
        mXlvBonuses.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        mXlvBonuses.setOnItemClickListener(this);
    }

    private void getData() {
        for (int i = 0; i < 10; i ++) {
            MyBonusesBean bean = new MyBonusesBean("我的奖励" + i);

            bonusesList.add(bean);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(_mActivity, BonusAnnounceActivity.class));
    }
}
