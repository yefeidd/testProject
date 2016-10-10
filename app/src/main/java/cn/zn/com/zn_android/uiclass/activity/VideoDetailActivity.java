package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import cn.zn.com.zn_android.utils.LogUtils;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/4/6 0006.
 * explain:
 */
public class VideoDetailActivity extends BaseActivity implements View.OnClickListener {
    private final String MARKE = "webViewDidFinishLoad";
    @Bind(R.id.wv_vedio_detail)
    X5WebView mWvVedioDetail;
    @Bind(R.id.ib_back)
    ImageButton mIbBack;
    //    @Bind(R.id.lv_chat)
//    FrameLayout mFlContent;
    @Bind(R.id.pb_load)
    ProgressBar mPbLoad;
    //    @Bind(R.id.refreshText)
//    TextView mRefreshText;
//    @Bind(R.id.refreshPool)
//    RelativeLayout mRefreshPool;
    //    @Bind(R.id.isr_refresh)
//    InterceptSwpRefLayout mIsrRefresh;
    private PresentScorePresenter sharepresenter;
    private String shareContent = Constants.vedioShareContent;
    private String shareTitle = Constants.vedioShareTitle;
    private String mUrl = Constants.vedioShareUrl;
    UMImage image = new UMImage(VideoDetailActivity.this, Constants.iconResourece);
    /**
     * 分享的平台数组
     */
    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    /**
     * 三方平台的分享
     */
    private void societyShare() {
        setDialog();
        new ShareAction(this)
//                .setShareboardclickCallback(shareBoardlistener)
                .setDisplayList(displaylist)
                .withText(shareContent)
                .withTitle(shareTitle)
                .withTargetUrl(mUrl)
                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
        //关闭log和toast
        Config.OpenEditor = true;
//        Log.LOG = false;
        Config.IsToastTip = false;
        Config.dialog.dismiss();

    }


//    /**
//     * 分享点击不同按钮处理不同的事件
//     */
//    private ShareBoardlistener shareBoardlistener = new  ShareBoardlistener() {
//
//        @Override
//        public void onclick(SnsPlatform snsPlatform,SHARE_MEDIA share_media) {
//            if (share_media==null){
//                if (snsPlatform.mKeyword.equals("11")){
//                    Toast.makeText(_Activity,"add button success",Toast.LENGTH_LONG).show();
//                }
//
//            }
//            else {
//                new ShareAction(_Activity).setPlatform(share_media).setCallback(umShareListener)
//                        .withText("多平台分享")
//                        .share();
//            }
//        }
//    };

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


    /**
     * 分享的监听器
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(VideoDetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//            Toast.makeText(VideoDetailActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            sharepresenter.sharePresentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(VideoDetailActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(VideoDetailActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 重写activityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

//    private FrameLayout webViewParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        _setLightSystemBarTheme(false);
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        //注册eventBus
        _setLayoutRes(R.layout.activity_vedio_detail);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        try {
            super.onConfigurationChanged(newConfig);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initView() {

//        if (!(NetUtil.isMOBILEConnected(_mApplication) || NetUtil.isWIFIConnected(_mApplication))) {
//            View not_net_view = UIUtil.inflate(R.layout.layout_not_net);
//            mFlContent.addView(not_net_view);
//            mWvVedioDetail.setVisibility(View.GONE);
//        }
        sharepresenter = new PresentScorePresenter(this);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //设置webview可滚动
        mWvVedioDetail.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
//        mWvVedioDetail.setTitle(mRefreshText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("VideoDetailActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        initData();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("VideoDetailActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    /**
     * 初始化数据,加载页面数据
     */
    protected void initData() {
        //初始化webview的图片加载
        intWebViewImageLoader();
        mWvVedioDetail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置Dom仓库可用
        mWvVedioDetail.getSettings().setJavaScriptEnabled(true);
        mWvVedioDetail.getSettings().setDatabaseEnabled(true);
        mWvVedioDetail.getSettings().setDomStorageEnabled(true);
        mWvVedioDetail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String str = url;
                String s = "";
//            	System.out.println(url+"////222");

                if (str.contains("vid") && !str.contains("video")) {
                    s = str.split("vid")[1];
                    postVideoCollect(s);
                    return true;

                } else if (str.contains(MARKE)) {
                    s = str.substring(str.length() - 1);
                    if (s.equals("1")) {  //登录
                        Intent mIntent = new Intent(VideoDetailActivity.this, LoginActivity.class);
                        startActivity(mIntent);
                        return true;
                    }
                } else {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
            }
        });

        mWvVedioDetail.setWebChromeClient(new WebChromeClient() {
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
        mWvVedioDetail.loadUrl(mUrl);
    }


    /**
     * 初始化webView的图片加载机制
     */
    public void intWebViewImageLoader() {
        if (Build.VERSION.SDK_INT >= 19) {
            mWvVedioDetail.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWvVedioDetail.getSettings().setLoadsImagesAutomatically(false);
        }
    }


    /**
     * 为webview设置sessionId
     *
     * @param url
     */
    private void syncCookie(String url) {
        try {
            CookieSyncManager.createInstance(mWvVedioDetail.getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(_mApplication.getUserInfo().getSessionID());
            sbCookie.append(String.format(";domain=%s", ""));
            sbCookie.append(String.format(";path=%s", ""));
            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    //Web视图
//    private class HelloWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            this.finish();
            return true;
        }
        return false;
    }

    @Override
    protected void initEvent() {
//        mIsrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (mIsrRefresh.isRefreshing()) {
//                    mIsrRefresh.setRefreshing(false);
//                }
//                mWvVedioDetail.reload();
//            }
//        });
    }

    /**
     * 在主线程拿到数据
     */
    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String) {
            mUrl = (String) event.getObject();
            Log.i(TAG, "onEventMainThread: " + mUrl);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        RelativeLayout view = (RelativeLayout) mWvVedioDetail.getParent();
        view.removeView(mWvVedioDetail);
        mWvVedioDetail.destroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消注册eventBus
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_share:
                societyShare();
                break;
        }
    }

    /**
     * 文章收藏
     *
     * @param vedio_id
     */
    public void postVideoCollect(String vedio_id) {
        AppObservable.bindActivity(this, _apiManager.getService().postVedioCollect(_mApplication.getUserInfo().getSessionID(), vedio_id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultVedioCollect, Throwable -> {
                    Throwable.printStackTrace();
                    ToastUtil.showShort(this, getString(R.string.no_net));
                });
    }

    private void resultVedioCollect(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals("success")) {
            LogUtils.i("ArticleDetailActivity:" + "请求成功");
            initData();
        } else {
            ToastUtil.showShort(this, returnValue.getData().getMessage());
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
