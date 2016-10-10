package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * Created by Jolly on 2016/7/1 0001.
 */
public class PushActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.cb_alert)
    CheckBox mCbAlert;
    @Bind(R.id.ll_alert)
    LinearLayout mLlAlert;
    @Bind(R.id.cb_news)
    CheckBox mCbNews;
    @Bind(R.id.ll_news)
    LinearLayout mLlNews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_push);
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
        mToolbarTitle.setText(getString(R.string.push));

    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mLlAlert.setOnClickListener(this);
        mLlNews.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_alert:
                mCbAlert.setChecked(!mCbAlert.isChecked());
                break;
            case R.id.ll_news:
                mCbNews.setChecked(!mCbNews.isChecked());
                break;
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }
}
