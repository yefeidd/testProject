package cn.zn.com.zn_android.uiclass.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ListPageAdapter;
import cn.zn.com.zn_android.adapter.RankingListAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ContributionBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.page.ListPage;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 榜单
 * Created by zjs on 2016/3/25 0025.
 */
public class RankingListActivity extends BaseActivity {
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_history)
    ImageButton mIbHistory;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_layout)
    LinearLayout mToolbarLayout;
    @Bind(R.id.tab_title)
    TabLayout mTabTitle;
    @Bind(R.id.vp_ranking_list)
    ViewPager mVpRankingList;
    private String[] tab_names = null;
    private List<View> listViews;
    private ListPage rankingMonth;
    private ListPage rankingSum;
    private List<ContributionBean.MonthBean> monthList;
    private List<ContributionBean.TotalBean> totalList;
    private RankingListAdapter monthAdapter;
    private RankingListAdapter totalAdapter;
    private String tid = "9898";
    private JoDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_ranking_list);
        ButterKnife.bind(this);

    }

    @Override
    protected void initView() {
        dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);

        tab_names = UIUtil.getStringArray(R.array.ranking_list_tab_names);
        monthList = new ArrayList<>();
        totalList = new ArrayList<>();
        monthAdapter = new RankingListAdapter(this, monthList);
        totalAdapter = new RankingListAdapter(this, totalList);
        //初始化两个页面
        rankingMonth = new ListPage(this);
        rankingMonth.setAdapter(monthAdapter);
        rankingMonth.initData();
        rankingSum = new ListPage(this);
        rankingSum.setAdapter(totalAdapter);
        rankingSum.initData();
        viewChanage();
    }

    public void onEventMainThread(AnyEventType event) {
        tid = (String) event.getObject();
        if (tid.equals("")) {
            tid = "9898";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("RankingListActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        mToolbarTitle.setText(getString(R.string.contribution));
        getContribution();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RankingListActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void viewChanage() {
        listViews = new ArrayList<>();
        listViews.add(rankingMonth.mRootView);
        listViews.add(rankingSum.mRootView);


        //设置TabLayout的模式,这里主要是用来显示tab展示的情况的
        //TabLayout.MODE_FIXED          各tab平分整个工具栏,如果不设置，则默认就是这个值
        //TabLayout.MODE_SCROLLABLE     适用于多tab的，也就是有滚动条的，一行显示不下这些tab可以用这个
        //                              当然了，你要是想做点特别的，像知乎里就使用的这种效果
        mTabTitle.setTabMode(TabLayout.MODE_FIXED);

        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);

        //为TabLayout添加tab名称
        mTabTitle.addTab(mTabTitle.newTab().setText(tab_names[0]), true);
        mTabTitle.addTab(mTabTitle.newTab().setText(tab_names[1]));


        ListPageAdapter pAdapter = new ListPageAdapter(this, listViews, tab_names);
        mVpRankingList.setAdapter(pAdapter);

        //将tabLayout与viewpager连起来
        mTabTitle.setupWithViewPager(mVpRankingList);
    }

    /**
     * 获取贡献榜数据
     *
     * @return
     */
    private void getContribution() {
        AppObservable.bindActivity(this, _apiManager.getService().getContribution(tid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultContribution, Throwable -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Throwable.printStackTrace();
                    ToastUtil.showShort(this, getString(R.string.no_net));
                });
    }

    private void resultContribution(ReturnValue<ContributionBean> returnValue) {
        if (dialog != null) {
            dialog.dismiss();
        }

        if (returnValue != null && returnValue.getMsg().equals("success")) {
            monthList = returnValue.getData().getMonth();
            totalList = returnValue.getData().getTotal();
            monthAdapter.setmContentList(monthList);
            totalAdapter.setmContentList(totalList);
            monthAdapter.notifyDataSetChanged();
            totalAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showShort(this, returnValue.getMsg());
        }
    }

}
