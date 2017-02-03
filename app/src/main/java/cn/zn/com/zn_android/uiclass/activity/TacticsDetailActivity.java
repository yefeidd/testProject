package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.TacticsBean;
import cn.zn.com.zn_android.model.bean.VipStateBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TacticsDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.wv_tactics_detail)
    X5WebView mWvTacticsDetail;
    @Bind(R.id.btn_buy)
    Button mBtnBuy;
    @Bind(R.id.pb_web_load)
    ProgressBar mPbWebLoad;

    private TacticsBean tacticsBean;
    private boolean isShow = false;
//    private FrameLayout mParentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_tactics_detail);
    }

    public void onEventMainThread(AnyEventType event) {
        tacticsBean = (TacticsBean) event.getObject();
        isShow = event.getState();
    }

    @Override
    protected void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mWvTacticsDetail.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        //没有网络的情况下显示一个提示界面
//        if (!(NetUtil.isMOBILEConnected(_mApplication) || NetUtil.isWIFIConnected(_mApplication))) {
//            View not_net_view = UIUtil.inflate(R.layout.layout_not_net);
//            mFlContent.addView(not_net_view);
//            mWvTacticsDetail.setVisibility(View.GONE);
//        }
        mToolbarTitle.setText(getString(R.string.tactics));
        if (tacticsBean != null && tacticsBean.getUrl() != null) {
            mWvTacticsDetail.loadUrl(tacticsBean.getUrl());
            //设置缓存模式
            mWvTacticsDetail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            //设置Dom仓库可用
            mWvTacticsDetail.getSettings().setDomStorageEnabled(true);
            mWvTacticsDetail.getSettings().setDatabaseEnabled(true);
            mWvTacticsDetail.getSettings().setJavaScriptEnabled(true);
        }
        if (!isShow) {
            getVipState();
        } else {
            mBtnBuy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);

        mWvTacticsDetail.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("TacticsDetailActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("TacticsDetailActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
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
        //将webview注销掉，防止内存溢出
        LinearLayout view = (LinearLayout) mWvTacticsDetail.getParent();
        view.removeView(mWvTacticsDetail);
        mWvTacticsDetail.destroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.btn_buy:
//                tacticsBean
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
//                    buyTactics();
                    startBuyTacticsDialog();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
        }
    }

    private void getVipState() {
        _apiManager.getService().getVipState(_mApplication.getUserInfo().getSessionID(), "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultVipState, throwable -> {
                    Log.e(TAG, "getVipState: ", throwable);
                });
//        AppObservable.bindActivity(this, _apiManager.getService().getVipState(_mApplication.getUserInfo().getSessionID(), ""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::resultVipState, throwable -> {
//                    Log.e(TAG, "getVipState: ", throwable);
//                });
    }

    private void resultVipState(ReturnValue<VipStateBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            VipStateBean vipState = returnValue.getData();
            if (vipState.getIsroomvip().equals("0"))
                mBtnBuy.setVisibility(View.VISIBLE);
            else
                mBtnBuy.setVisibility(View.GONE);

        } else {
            ToastUtil.showShort(_mApplication, returnValue.getMsg());
        }
    }

    private void buyTactics() {
        _apiManager.getService().buyTactics(_mApplication.getUserInfo().getSessionID(), tacticsBean.getSumid(), "2")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultBuyTactics, throwable -> {
                    Log.e(TAG, "buyTactics: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().buyTactics(_mApplication.getUserInfo().getSessionID(), tacticsBean.getSumid(), "2"))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::resultBuyTactics, throwable -> {
//                    Log.e(TAG, "buyTactics: ", throwable);
//                });
    }

    private void resultBuyTactics(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
            mBtnBuy.setVisibility(View.GONE);
        } else {
            if (returnValue.getData().getMessage().contains("余额不足")) {
                startActivity(new Intent(this, RechargeActivity.class));
            }
            if (returnValue.getData().getMessage().contains("购买过")) {
                finish();
            }
            ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
        }
    }

    private void startBuyTacticsDialog() {
        new JoDialog.Builder(this)
                .setStrTitle(R.string.tips)
                .setStrContent(getString(R.string.confirm_buy))
                .setPositiveTextRes(R.string.confirm)
                .setNegativeTextRes(R.string.cancel)
                .setCallback(new JoDialog.ButtonCallback() {
                    @Override
                    public void onPositive(JoDialog dialog) {
                        buyTactics();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegtive(JoDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show(true);
    }
}
