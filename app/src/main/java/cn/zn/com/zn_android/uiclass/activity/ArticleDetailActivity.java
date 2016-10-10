package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.uiclass.customerview.InterceptSwpRefLayout;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import cn.zn.com.zn_android.utils.LogUtils;
import cn.zn.com.zn_android.utils.ToastUtil;
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
 * Created by zjs on 2016/4/7 0007.
 * explain:
 */
public class ArticleDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_history)
    ImageButton mIbHistory;
    @Bind(R.id.ib_search)
    ImageButton mIbShare;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.tv_click)
    TextView mTvClick;
    @Bind(R.id.tv_nickname)
    TextView mTvNickname;
    @Bind(R.id.wv_detail)
    X5WebView mWvDetail;
    @Bind(R.id.cb_collect)
    CheckBox mCbCollect;
    @Bind(R.id.rl_collect)
    RelativeLayout mRlCollect;
    @Bind(R.id.cb_like)
    CheckBox mCbLike;
    @Bind(R.id.rl_like)
    RelativeLayout mRlLike;
    //    @Bind(R.id.fl_content)
//    FrameLayout mFlContent;
    @Bind(R.id.isr_refresh)
    InterceptSwpRefLayout mIsrRefresh;
    @Bind(R.id.pb_web_load)
    ProgressBar mPbWebLoad;
    @Bind(R.id.sv_art_detail)
    ScrollView mSvArtDetail;
    private ArticleBean articleInfo;
    //初始化点赞状态
    private boolean likeStatus = false;
    private boolean collectStaus = false;
    private FrameLayout webViewParent;
    //    private View not_net_view;
    private boolean isArticle = true;

    /**
     * 分享的平台数组
     */
    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };
    private String shareContent = Constants.articleShareContent;
    private String shareTitle = Constants.articleShareTitle;
    private String mUrl = Constants.articleShareUrl;
    UMImage image = new UMImage(ArticleDetailActivity.this, Constants.iconResourece);
    private PresentScorePresenter sharepresenter;

    /**
     * 三方平台的分享
     */
    private void societyShare() {
        setDialog();
        new ShareAction(this).setDisplayList(displaylist)
                .withText(shareContent)
                .withTitle(shareTitle)
                .withTargetUrl(mUrl)
                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
        //关闭log和toast
        Config.OpenEditor = false;
//        Log.LOG = false;
        Config.IsToastTip = false;
        Config.dialog.dismiss();

    }

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
//            Toast.makeText(ArticleDetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            sharepresenter.sharePresentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(ArticleDetailActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(ArticleDetailActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_article_detail);

    }

    Handler handler = new Handler();

    @Override
    protected void initView() {
        sharepresenter = new PresentScorePresenter(this);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mIbShare.setImageResource(R.drawable.article_share);
        mIbShare.setVisibility(View.VISIBLE);
        mWvDetail.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        mUrl = articleInfo.getIosurl();
//        not_net_view = UIUtil.inflate(R.layout.layout_not_net);
//        mFlContent.addView(not_net_view);
        //setOntouchEvent(not_net_view);
        //没有网络的情况下显示一个提示界面
//        setShowView();

        mWvDetail.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                int len = mSvArtDetail.getHeight();
                mSvArtDetail.scrollTo(0, len);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mSvArtDetail.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }

        });
    }

//    /**
//     * 设置显示的view
//     */
//    private void setShowView() {
//        //没有网络的情况下显示一个提示界面
//        if (!(NetUtil.isMOBILEConnected(_mApplication) || NetUtil.isWIFIConnected(_mApplication))) {
//            mWvDetail.setVisibility(View.INVISIBLE);
//        } else {
//            not_net_view.setVisibility(View.INVISIBLE);
//            mWvDetail.setVisibility(View.VISIBLE);
//
//        }
//    }

//    /**
//     * 为一个view设置触摸点击事件
//     *
//     * @param view
//     */
//    private void setOntouchEvent(View view) {
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mWvDetail.clearHistory();
//                        mWvDetail.clearCache(true);
//                        mWvDetail.reload();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                SystemClock.sleep(3000);
//                                UIUtil.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        setShowView();
//                                    }
//                                });
//                            }
//                        }).start();
//                        break;
//                }
//                return true;
//            }
//        });
//    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mRlLike.setOnClickListener(this);
        mRlCollect.setOnClickListener(this);
        mIbShare.setOnClickListener(this);
        //设置下拉刷新
        mIsrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mIsrRefresh.isRefreshing()) {
                    mIsrRefresh.setRefreshing(false);
                }
                mWvDetail.reload();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("ArticleDetailActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        //初始化收藏状态
        if (_mApplication.getUserInfo().getIsLogin() == Constants.NOT_LOGIN) {
            collectStaus = false;
            initData();
        } else {
            postArtIsCollect(articleInfo.getId());
        }

        mWvDetail.getSettings().setJavaScriptEnabled(true);
        mTvTitle.setText(articleInfo.getTitle());
        mTvTime.setText(articleInfo.getTimes());
        mTvClick.setText("阅: " + articleInfo.getClick());
        mTvNickname.setText(articleInfo.getSource());

        mWvDetail.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                mPbWebLoad.setProgress(newProgress);
                if (mPbWebLoad != null && newProgress != 100) {
                    mPbWebLoad.setVisibility(View.VISIBLE);
                } else if (mPbWebLoad != null) {
                    mPbWebLoad.setVisibility(View.GONE);
                }
            }

        });

//        mWvDetail.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                setShowView();
//                mIsrRefresh.setRefreshing(false);
//                super.onPageFinished(view, url);
//            }
//        });
        mWvDetail.loadUrl(articleInfo.getUrl());

    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof ArticleBean) {
            articleInfo = (ArticleBean) event.getObject();
            //是否是博文列表
            isArticle = event.getState();
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ArticleDetailActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        mWvDetail.stopLoading();
//        webViewParent = (FrameLayout) mWvDetail.getParent();
//        webViewParent.removeView(mWvDetail);
        FrameLayout view = (FrameLayout) mWvDetail.getParent();
        view.removeView(mWvDetail);
        mWvDetail.destroy();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 更新页面数据，按钮的状态
     */
    protected void initData() {
        if (isArticle) {
            mToolbarTitle.setText(getString(R.string.article));
        } else {
            mToolbarTitle.setText(getString(R.string.news));
        }
        mCbLike.setChecked(likeStatus);
        mCbCollect.setChecked(collectStaus);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.rl_like:
                postArtLikes(articleInfo.getId());
                break;
            case R.id.rl_collect:
                postArtCollect(articleInfo.getId());
                break;
            case R.id.ib_search:
                societyShare();
                break;
//            default:
//                break;
        }
    }


    /**
     * 文章点赞
     *
     * @param art_id
     */
    public void postArtLikes(String art_id) {
        AppObservable.bindActivity(this, _apiManager.getService().postArtLikes(art_id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultArtLikes, Throwable -> {
                    Throwable.printStackTrace();
                    ToastUtil.showShort(this, getString(R.string.no_net));
                });
    }

    private void resultArtLikes(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals("success")) {
            LogUtils.i("ArticleDetailActivity:" + "请求成功");
            likeStatus = !likeStatus;
            initData();
        } else {
            ToastUtil.showShort(this, getString(R.string.no_net));
        }
    }


    /**
     * 文章收藏
     *
     * @param art_id
     */
    public void postArtCollect(String art_id) {
        AppObservable.bindActivity(this, _apiManager.getService().postArtCollect(_mApplication.getUserInfo().getSessionID(), art_id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultArtCollect, Throwable -> {
                    Throwable.printStackTrace();
                    ToastUtil.showShort(this, getString(R.string.no_net));
                });
    }

    private void resultArtCollect(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals("success")) {
            LogUtils.i("ArticleDetailActivity:" + "请求成功");
            collectStaus = !collectStaus;
            initData();
        } else {
            ToastUtil.showShort(this, returnValue.getData().getMessage());
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    /**
     * 文章是否被收藏
     *
     * @param art_id
     */
    public void postArtIsCollect(String art_id) {
        AppObservable.bindActivity(this, _apiManager.getService().postArtISCollect(_mApplication.getUserInfo().getSessionID(), art_id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultArtIsCollect, Throwable -> {
                    Throwable.printStackTrace();
                    ToastUtil.showShort(this, getString(R.string.no_net));
                });
    }

    private void resultArtIsCollect(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null) {
            LogUtils.i("ArticleDetailActivity:" + "请求成功");
            if (returnValue.getData().getMessage().equals("未收藏")) {
                collectStaus = false;
            } else {
                collectStaus = true;
            }
            initData();
        } else {
            collectStaus = false;
            initData();
        }
    }


}
