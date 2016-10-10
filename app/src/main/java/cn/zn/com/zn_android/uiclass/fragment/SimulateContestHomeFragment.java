package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.JoFragmentPagerAdapter;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/6/27 0027.
 */
public class SimulateContestHomeFragment extends BaseFragment {


    @Bind(R.id.tab_title)
    TabLayout mTabTitle;
    @Bind(R.id.vp_content)
    ViewPager mVpContent;
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_simulate_contest);
    }

    @Override
    protected void initView(View view) {
        mToolbarTitle.setText(getString(R.string.simulate_contest_title));
        mIvLeftmenu.setVisibility(View.GONE);
        if (mVpContent != null) {
            setupViewPager();
        }
        fragmentChanage();
    }

    @Override
    protected void initEvent() {

    }


    public void setupViewPager() {
        mVpContent.setOffscreenPageLimit(1);
        JoFragmentPagerAdapter adapter = new JoFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ContestHomeFragment.newInstance("home", "home"), getString(R.string.contest_home));
        adapter.addFragment(ContestDynamicFragment.newInstance("dynamic", "dynamic"), getString(R.string.contest_dynamic));
        mVpContent.setAdapter(adapter);
    }

    /**
     * 切换两个fragment的按钮
     */
    private void fragmentChanage() {
        mTabTitle.setTabMode(TabLayout.MODE_FIXED);
        mTabTitle.addTab(mTabTitle.newTab().setText(R.string.contest_home), true);
        mTabTitle.addTab(mTabTitle.newTab().setText(R.string.contest_dynamic));
        mTabTitle.setupWithViewPager(mVpContent);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SimulateContestHomeFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SimulateContestHomeFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
