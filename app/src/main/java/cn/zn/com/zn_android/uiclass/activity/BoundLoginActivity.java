package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.JoFragmentPagerAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.uiclass.fragment.BoundFragment;
import cn.zn.com.zn_android.uiclass.fragment.BoundRegisterFragment;
import cn.zn.com.zn_android.manage.Constants;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/5/13 0013.
 * explain: 第三方登陆的账号绑定界面
 */
public class BoundLoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tab_title)
    TabLayout mTabTitle;
    @Bind(R.id.vp_bound)
    ViewPager mVpBound;
    private String ucode = "";
    private int type = Constants.WEI_XING;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_bound_login);
    }

    /**
     * 在主线程拿到数据
     */
    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String && event.getType() != null) {
            ucode = (String) event.getObject();
            type = event.getType();
            Log.i(TAG, "onEventMainThread: " + ucode);
            Log.i(TAG, "onEventMainThread: " + type);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BoundLoginActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BoundLoginActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
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
    protected void initView() {
        mToolbarTitle.setText("第三方登录");
        if (mVpBound != null) {
            setupViewPager();
        }
        mTabTitle.setFocusable(true);
        mTabTitle.setupWithViewPager(mVpBound);
    }

    private void setupViewPager() {
        mVpBound.setOffscreenPageLimit(2);
        JoFragmentPagerAdapter adapter = new JoFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BoundFragment.newInstance(ucode, type), "账号绑定");
        adapter.addFragment(BoundRegisterFragment.newInstance(ucode, type), "账号注册");
        mVpBound.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
//            case R.id.:
//                break;
//            case :
//                break;
//            default:
//                break;
        }
    }
}
