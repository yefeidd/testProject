package cn.zn.com.zn_android.uiclass.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.MarketPageAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.service.RefreshDataService;
import cn.zn.com.zn_android.uiclass.activity.EditOptionalStockActivity;
import cn.zn.com.zn_android.uiclass.activity.MarketSearchActivity;
import cn.zn.com.zn_android.uiclass.page.BaseMarketPage;
import cn.zn.com.zn_android.uiclass.page.BasePage;
import cn.zn.com.zn_android.uiclass.page.HKStockPage;
import cn.zn.com.zn_android.uiclass.page.SSPage;
import cn.zn.com.zn_android.uiclass.page.SelfSelectStockPage;
import cn.zn.com.zn_android.utils.UIUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/6/27 0027.
 * email: m15267280642@163.com
 * explain:
 */
public class MarketFragment extends BaseFragment {
    @Bind(R.id.tab_title)
    TabLayout mTabTitle;
    @Bind(R.id.vp_market_list)
    ViewPager mVpMarketList;

    @Bind(R.id.tv_leftMenu)
    TextView mTvLeftMenu;
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    private List<BasePage> pageList;
    private String[] mTabStrs;
    private MarketPageAdapter marketPageAdapter;

    LocalBroadcastManager lbm;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RefreshDataService.TAG)) {
//                Log.d("mark", "MarketFragment网络状态已经改变\n" + intent.getStringExtra("mark"));

                if (isHidden()) {
                    return;
                }
                if (pageList.size() > 2) {
                    switch (mTabTitle.getSelectedTabPosition()) {
                        case 1:
                            Log.d(TAG, "onReceive: 1  " + TAG + "   " + isHidden());
                            pageList.get(1).queryIndexData();
                            break;
                        case 2:
                            Log.d(TAG, "onReceive: 2  " + TAG + "   " + isHidden());
                            pageList.get(2).queryIndexData();
                            break;
                    }
                }
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged: " + hidden);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _setLayoutRes(R.layout.fragment_market);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        lbm = LocalBroadcastManager.getInstance(_mContext);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        /**
         * 初始化titlebar
         */
        mIvLeftmenu.setVisibility(View.GONE);
        mTvLeftMenu.setVisibility(View.VISIBLE);
        mIbSearch.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        mToolbarTitle.setText(getString(R.string.market));
        initPage();
        marketPageAdapter = new MarketPageAdapter(_mActivity);
        marketPageAdapter.setPageList(pageList);
        marketPageAdapter.setTabNames(mTabStrs);
        mVpMarketList.setAdapter(marketPageAdapter);
        viewChange();
    }

    /**
     * 初始化三个page页面的数据。
     */
    private void initPage() {
        mTabStrs = UIUtil.getStringArray(R.array.market_tab_titles);
        pageList = new ArrayList<>();
        for (int i = 0; i < mTabStrs.length; i++) {
            if (0 == i) {
                SelfSelectStockPage selfSelectStockPage = new SelfSelectStockPage(_mActivity);
                selfSelectStockPage.initData();
                pageList.add(selfSelectStockPage);
                continue;
            }
            if (2 == i) {
                HKStockPage hkStockPage = new HKStockPage(_mActivity);
                hkStockPage.initData();
                pageList.add(hkStockPage);
                continue;
            }
            if (1 == i) {
                SSPage ssPage = new SSPage(_mActivity);
                ssPage.initData();
                pageList.add(ssPage);
                continue;
            }
            BaseMarketPage marketPage = new BaseMarketPage(_mActivity);
            marketPage.initData();
            marketPage.setText(mTabStrs[i]);
            pageList.add(marketPage);
        }
    }


    /**
     * 设置tabLayout标题和viewpager的关联
     */
    private void viewChange() {
        //设置TabLayout的模式,这里主要是用来显示tab展示的情况的
        //TabLayout.MODE_FIXED          各tab平分整个工具栏,如果不设置，则默认就是这个值
        //TabLayout.MODE_SCROLLABLE     适用于多tab的，也就是有滚动条的，一行显示不下这些tab可以用这个
        //                              当然了，你要是想做点特别的，像知乎里就使用的这种效果
        mTabTitle.setTabMode(TabLayout.MODE_FIXED);

        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);

        //为TabLayout添加tab名称
        for (int i = 0; i < mTabStrs.length; i++) {
            if (i == 0) {
                mTabTitle.addTab(mTabTitle.newTab().setText(mTabStrs[i]), true);
                continue;
            }
            mTabTitle.addTab(mTabTitle.newTab().setText(mTabStrs[i]));
        }
        //将tabLayout与viewpager连起来
        mTabTitle.setupWithViewPager(mVpMarketList);
    }

    @Override
    protected void initEvent() {
        mTvLeftMenu.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType(((SelfSelectStockPage) pageList.get(0)).getSelfDataList()));
            startActivity(new Intent(_mContext, EditOptionalStockActivity.class));
        });
        mIbSearch.setOnClickListener(v -> {
            startActivity(new Intent(_mActivity, MarketSearchActivity.class));
        });

        mVpMarketList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position) {
                    mTvLeftMenu.setVisibility(View.VISIBLE);
                    pageList.get(0).queryIndexData();
                } else {
                    mTvLeftMenu.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabTitle.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (0 == tab.getPosition()) {
                    mTvLeftMenu.setVisibility(View.VISIBLE);
                    pageList.get(0).queryIndexData();
                } else {
                    mTvLeftMenu.setVisibility(View.GONE);
                }
                mVpMarketList.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(RefreshDataService.TAG);
        lbm.registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义

        if (pageList.size() > 2) {
            switch (mTabTitle.getSelectedTabPosition()) {
                case 1:
                    Log.d(TAG, "onReceive: 1  " + TAG + "   " + isHidden());
                    pageList.get(1).queryIndexData();
                    break;
                case 2:
                    Log.d(TAG, "onReceive: 2  " + TAG + "   " + isHidden());
                    pageList.get(2).queryIndexData();
                    break;
                case 0:
                    pageList.get(0).queryIndexData();
                    break;
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        lbm.unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
