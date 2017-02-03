package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.JoFragmentPagerAdapter;
import cn.zn.com.zn_android.uiclass.fragment.OrdersAllFragment;

/**
 * 我的订单
 *
 * Created by Jolly on 2016/12/7.
 */

public class MyOrdersActivity extends BaseActivity {
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tl_orders)
    TabLayout mTlOrders;
    @Bind(R.id.vp_orders)
    ViewPager mVpOrders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_my_orders);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);       //统计时长
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.my_orders);
        setupViewPager();
        mTlOrders.setupWithViewPager(mVpOrders);
        mTlOrders.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_bar_color));
    }

    @Override
    protected void initEvent() {

    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    private void setupViewPager() {
        OrdersAllFragment allFragment = new OrdersAllFragment();
        OrdersAllFragment payedFragment = new OrdersAllFragment();
        allFragment.setType(1);
        payedFragment.setType(2);
        JoFragmentPagerAdapter adapter = new JoFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(allFragment, getString(R.string.all));
        adapter.addFragment(payedFragment, getString(R.string.wait_for_pay));
        mVpOrders.setAdapter(adapter);
    }

}
