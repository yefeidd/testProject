package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.JoFragmentPagerAdapter;
import cn.zn.com.zn_android.uiclass.fragment.MyActivitiesFragment;
import cn.zn.com.zn_android.uiclass.fragment.MyBonusesFragment;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * 我的参与
 * Created by Jolly on 2016/7/4 0004.
 */
public class MyInvolvementActivity extends BaseActivity {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tl_involve)
    TabLayout mTlInvolve;
    @Bind(R.id.vp_involvement)
    ViewPager mVpInvolvement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_my_involvement);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.my_activitis));
        if (mVpInvolvement != null) {
            setupViewPager(mVpInvolvement);
        }

        mTlInvolve.setFocusable(true);
        mTlInvolve.setupWithViewPager(mVpInvolvement);
        Log.d("current tab is -->", mTlInvolve.getSelectedTabPosition() + "    ");
        mTlInvolve.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_bar_color));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupViewPager(ViewPager mVpChat) {
        mVpChat.setOffscreenPageLimit(2);
        JoFragmentPagerAdapter adapter = new JoFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MyActivitiesFragment.newInstance(), "我的活动");
        adapter.addFragment(MyBonusesFragment.newInstance(), "我的奖励");
        mVpChat.setAdapter(adapter);
    }
}
