package cn.zn.com.zn_android.uiclass.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;

/**
 * 证牛投顾付费服务使用条款
 * Created by Jolly on 2016/12/30.
 */
public class InvestAdviseRuleActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.pb_web_load)
    ProgressBar mPbWebLoad;
    @Bind(R.id.wv_detail)
    X5WebView mWvDetail;

    private String mUrl = Constants_api.BASE_API_URL + Constants_api.ZNSYTK_URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_invest_advise_rule);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FrameLayout view = (FrameLayout) mWvDetail.getParent();
        view.removeView(mWvDetail);
        mWvDetail.destroy();
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
        mToolbarTitle.setText("证牛投顾付费服务使用条款");

        //初始化webview的图片加载
        intWebViewImageLoader();
        mWvDetail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置Dom仓库可用
        mWvDetail.getSettings().setJavaScriptEnabled(true);
        mWvDetail.getSettings().setDatabaseEnabled(true);
        mWvDetail.getSettings().setDomStorageEnabled(true);

        mWvDetail.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                mPbWebLoad.setProgress(newProgress);
                if (mPbWebLoad != null && newProgress != 100) {
                    mPbWebLoad.setVisibility(View.VISIBLE);
                } else if (mPbWebLoad != null) {
                    mPbWebLoad.setVisibility(View.GONE);
                }
            }

        });
        // 设置cookie
        mWvDetail.loadUrl(mUrl);
    }

    /**
     * 初始化webView的图片加载机制
     */
    public void intWebViewImageLoader() {
        if (Build.VERSION.SDK_INT >= 19) {
            mWvDetail.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWvDetail.getSettings().setLoadsImagesAutomatically(false);
        }
    }

    @Override
    protected void initEvent() {

    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }
}
