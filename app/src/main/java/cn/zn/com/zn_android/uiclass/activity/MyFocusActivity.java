package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.JoFragmentPagerAdapter;
import cn.zn.com.zn_android.uiclass.fragment.FocusPersonFragment;
import cn.zn.com.zn_android.uiclass.fragment.FocusRoomFragment;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

public class MyFocusActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
//    @Bind(R.id.tl_focus)
//    TabLayout mTlFocus;
//    @Bind(R.id.vp_focus)
//    ViewPager mVpFocus;

//    private FocusPersonFragment personFragment = FocusPersonFragment.newInstance();
//    private FocusRoomFragment roomFragment = FocusRoomFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_my_focus);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.my_focus));

//        setupViewPager();
//        mTlFocus.setupWithViewPager(mVpFocus);
//        mTlFocus.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_bar_color));
    }

    @Override
    protected void initEvent() {
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

//    private void setupViewPager() {
//        JoFragmentPagerAdapter adapter = new JoFragmentPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(roomFragment, getString(R.string.tab_live));
//        adapter.addFragment(personFragment, getString(R.string.tab_person));
//        mVpFocus.setAdapter(adapter);
//    }

}
