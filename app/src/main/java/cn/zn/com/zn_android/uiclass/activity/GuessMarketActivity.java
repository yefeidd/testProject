package cn.zn.com.zn_android.uiclass.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.manage.CookieManger;
import cn.zn.com.zn_android.manage.PersistentCookieStore;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 猜大盘
 * Created by Jolly on 2016/12/29.
 */
public class GuessMarketActivity extends BaseActivity {
    @Bind(R.id.pb_web_load)
    ProgressBar mPbWebLoad;
    @Bind(R.id.wv_detail)
    X5WebView mWvDetail;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    private String mUrl = Constants_api.BASE_URL + Constants_api.GUESS_MARKET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_guess_market);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWvDetail.canGoBack()) {
            mWvDetail.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        mToolbarTitle.setText(R.string.guess_market);
        //初始化webview的图片加载
        intWebViewImageLoader();
        mWvDetail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWvDetail.getSettings().setJavaScriptEnabled(true);
        mWvDetail.getSettings().setDatabaseEnabled(true);
        mWvDetail.getSettings().setDomStorageEnabled(true);

        mWvDetail.setWebViewClient(new WebViewClient() {
            /**
             * 5.0以下
             * @param view
             * @param url
             * @return
             */
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                syncCookie(url);
                return super.shouldInterceptRequest(view, url);//将加好cookie的url传给父类继续执行
            }
        });

        mWvDetail.setWebViewClient(new WebViewClient() {
            @SuppressLint("NewApi")
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                syncCookie(url);
                return super.shouldInterceptRequest(view, url);//因为跟5.0以下的方法返回值是同一个类，所以这里偷懒直接调动4.0方法生成请求
            }
        });

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
        syncCookie(mUrl);
        mWvDetail.loadUrl(mUrl);
    }

    @Override
    protected void initEvent() {

    }

    /**
     * 为webview设置sessionId
     *
     * @param url
     */
    private void syncCookie(String url) {
        CookieManger cookieManger = new CookieManger(RnApplication.getInstance());
        PersistentCookieStore cookieStore = cookieManger.getCookieStore();
        List<Cookie> cookies = cookieStore.get(HttpUrl.parse(Constants_api.BASE_URL));
        try {
            CookieSyncManager.createInstance(mWvDetail.getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
//            StringBuilder sbCookie = new StringBuilder();
//            sbCookie.append(_mApplication.getUserInfo().getSessionID());
//            Log.e(TAG, "syncCookie: " + _mApplication.getUserInfo().getSessionID());
////            ToastUtil.showLong(this, _mApplication.getUserInfo().getSessionID());
//            sbCookie.append(String.format(";domain=%s", ""));
//            sbCookie.append(String.format(";path=%s", ""));
//            String cookieValue = sbCookie.toString();
            String cookieValue = CookieManger.formatCookie(cookies);
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        if (mWvDetail.canGoBack()) {
            mWvDetail.goBack();
        } else {
            finish();
        }
    }

}
