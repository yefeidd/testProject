package cn.zn.com.zn_android.uiclass.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.manage.CookieManger;
import cn.zn.com.zn_android.manage.PersistentCookieStore;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import de.greenrobot.event.EventBus;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by zjs on 2016/9/27 0027.
 * email: m15267280642@163.com
 * explain:
 */

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.wv_sign_up)
    X5WebView mWvSignUp;
    @Bind(R.id.pb_load)
    ProgressBar mPbLoad;
    private PresentScorePresenter preseter;
    private String contestID = "0";
    private String shareContent = Constants.signUpShareContent;
    private String shareTitle = Constants.signUpShareTitle;
    private String mUrl = Constants.signUpShareUrl;
    UMImage image = new UMImage(SignUpActivity.this, Constants.iconResourece);
    private boolean flag = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_sign_up);
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
        mToolbarTitle.setText("大赛报名");
        preseter = new PresentScorePresenter(this);
    }


    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        initWebData();
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

    protected void initWebData() {
        //初始化webview的图片加载
        intWebViewImageLoader();
        mWvSignUp.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWvSignUp.getSettings().setJavaScriptEnabled(true);
        mWvSignUp.getSettings().setDatabaseEnabled(true);
        mWvSignUp.getSettings().setDomStorageEnabled(true);
        mWvSignUp.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String str = url;
                String[] strs = str.split("/");
                String s = strs[strs.length - 1];
                if ("login".equals(s)) {
                    _Activity.startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    return true;
                } else if ("reg".equals(s)) {
                    _Activity.startActivity(new Intent(SignUpActivity.this, RegisterActivity.class));
                    return true;
                } else if ("sina".equals(s)) {
                    setShareAction(SHARE_MEDIA.SINA);
                    return true;
                } else if ("wechat".equals(s)) {
                    setShareAction(SHARE_MEDIA.WEIXIN);
                    return true;
                } else if ("friends".equals(s)) {
                    setShareAction(SHARE_MEDIA.WEIXIN_CIRCLE);
                    return true;
                } else if ("qq".equals(s)) {
                    setShareAction(SHARE_MEDIA.QQ);
                    return true;
                } else if ("jiaoyi".equals(s)) {
                    _Activity.startActivity(new Intent(SignUpActivity.this, ImitateFryActivity.class));
                    return true;
                }
                return false;
            }

            @SuppressLint("NewApi")
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                syncCookie(url);
                return super.shouldInterceptRequest(view, url);
            }


            /**
             * 5.0以下
             * @param view
             * @param url
             * @return
             */
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                syncCookie(url);
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
            }
        });


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


    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(_Activity, platform + " 分享成功", Toast.LENGTH_SHORT).show();
            flag = false;
            //赠送积分
            preseter.presentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(_Activity, platform + " 分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (flag) {
                Toast.makeText(_Activity, platform + " 分享取消", Toast.LENGTH_SHORT).show();
            } else {
                flag = true;
            }
        }
    };

    /**
     * 设置三方跳转的时候的dialog
     */
    private void setDialog() {
        JoDialog dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);
        Config.dialog = dialog;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            this.finish();
            return true;
        }
        return false;
    }
    /**
     * 分享的动作
     *
     * @param shareAction
     */
    private void setShareAction(SHARE_MEDIA shareAction) {
        setDialog();
        if (null == image) {
            image = new UMImage(_Activity, BitmapFactory.decodeResource(_Activity.getResources(), R.drawable.share_icon));
        }
        new ShareAction(_Activity)
                .setPlatform(shareAction)
                .setCallback(shareListener)
                .withText(shareContent)
                .withTitle(shareTitle)
                .withTargetUrl(mUrl)
                .withMedia(image)
                .share();
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
            CookieSyncManager.createInstance(this);
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
     * 重写activityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
