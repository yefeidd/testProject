package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.uiclass.fragment.TacticsFragment;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 我的策略
 */
public class MyTacticsActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.fl_tactics)
    FrameLayout mFlTactics;


    private String tid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_my_tactics);
    }

    public void onEventMainThread(AnyEventType event) {
        tid = (String) event.getObject();
    }

    @Override
    protected void initView() {
        if (tid.equals("")) {
            mToolbarTitle.setText(getString(R.string.my_tactics));
        } else {
            mToolbarTitle.setText(getString(R.string.tactics));
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_tactics, TacticsFragment.newInstance(tid, ""));
        transaction.commit();
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

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }


}
