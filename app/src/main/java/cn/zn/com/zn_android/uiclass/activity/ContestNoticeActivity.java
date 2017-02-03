package cn.zn.com.zn_android.uiclass.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.manage.CookieManger;
import cn.zn.com.zn_android.manage.PersistentCookieStore;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import de.greenrobot.event.EventBus;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by zjs on 2016/9/27 0027.
 * email: m15267280642@163.com
 * explain:
 */

public class ContestNoticeActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.wv_sign_up)
    X5WebView mWvSignUp;
    @Bind(R.id.pb_load)
    ProgressBar mPbLoad;
    private String contestID = "0";
    private String mUrl = "http://www.zhengniu.net/";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_sign_up);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String) {
            mUrl = (String) event.getObject();
            String[] strs = mUrl.split("/");
            contestID = strs[strs.length - 1];
        }
    }

    @Override
    protected void initView() {
        mWvSignUp.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        mIvLeftmenu.setVisibility(View.VISIBLE);
        mToolbarTitle.setText("公告");
    }


    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initWebData();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    protected void initWebData() {
        //初始化webview的图片加载
        intWebViewImageLoader();
//        mWvSignUp.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWvSignUp.getSettings().setJavaScriptEnabled(true);
        mWvSignUp.getSettings().setDatabaseEnabled(true);
        mWvSignUp.getSettings().setDomStorageEnabled(true);


        mWvSignUp.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                mPbLoad.setProgress(newProgress);
                if (mPbLoad != null && newProgress != 100) {
                    mPbLoad.setVisibility(View.VISIBLE);
                } else if (mPbLoad != null) {
                    mPbLoad.setVisibility(View.GONE);
                }
            }

        });
        // 设置cookie
        syncCookie(mUrl);
        mWvSignUp.loadUrl(mUrl);
    }


    /**
     * 初始化webView的图片加载机制
     */
    public void intWebViewImageLoader() {
        if (Build.VERSION.SDK_INT >= 19) {
            mWvSignUp.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWvSignUp.getSettings().setLoadsImagesAutomatically(false);
        }
    }


    @Override
    protected void onDestroy() {
        LinearLayout view = (LinearLayout) mWvSignUp.getParent();
        view.removeView(mWvSignUp);
        mWvSignUp.destroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();

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
            CookieSyncManager.createInstance(mWvSignUp.getContext());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            default:
                break;
        }
    }
}
