package cn.zn.com.zn_android.uiclass.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/8/30 0030.
 */
public class NewsDetailActivity extends BaseActivity {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.pb_web_load)
    ProgressBar mPbWebLoad;
    @Bind(R.id.wv)
    X5WebView mWv;

    private String url;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_news_detail);
    }

    public void onEventMainThread(AnyEventType event) {
        if (null != event && event.getObject() instanceof String) {
            url = event.getObject().toString();
            title = event.getTid();
        }
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(title);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mWv.loadUrl(url);
    }

    @Override
    protected void initEvent() {

        mWv.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
                // TODO Auto-generated method stub
                mPbWebLoad.setProgress(newProgress);
                if (mPbWebLoad != null && newProgress != 100) {
                    mPbWebLoad.setVisibility(View.VISIBLE);
                } else if (mPbWebLoad != null) {
                    mPbWebLoad.setVisibility(View.GONE);
                }
            }

        });
    }


    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
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
        LinearLayout view = (LinearLayout) mWv.getParent();
        view.removeView(mWv);
        mWv.destroy();
        EventBus.getDefault().unregister(this);
    }

}
