package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.VIPInfoBean;
import cn.zn.com.zn_android.model.bean.VipStateBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 牛人专区
 */
public class MemberAreaActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.wv_vip_area)
    X5WebView mWvVipArea;
    @Bind(R.id.btn_join)
    Button mBtnJoin;
    @Bind(R.id.pb_web_load)
    ProgressBar mPbWebLoad;

    private String tid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_member_area);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.member_area));
//        if (!(NetUtil.isMOBILEConnected(_mApplication) || NetUtil.isWIFIConnected(_mApplication))) {
//            View not_net_view = UIUtil.inflate(R.layout.layout_not_net);
//            mFlContent.addView(not_net_view);
//            mWvVipArea.setVisibility(View.GONE);
//        }
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void initEvent() {
        mWvVipArea.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
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

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String) {
            tid = (String) event.getObject();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_join:
                if (tid.equals("") || tid.equals("9898")) { // 特约讲堂,直接购买
                    if (_mApplication.getUserInfo().getIsLogin() == 1) {
//                        buyTactics();
                        confirmBuyDialog(getString(R.string.confirm_buy), true);
                    } else {
                        queryVipMemberIntru();
                    }
                } else { // 老师
                    startActivity(new Intent(this, MyTacticsActivity.class));
                    EventBus.getDefault().postSticky(new AnyEventType(tid));
                }
                break;
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("MemberAreaActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        if (tid.equals("") || tid.equals("9898")) {
            queryVipMemberIntru();
//            if (_mApplication.getUserInfo().getIsLogin() == 1) {
////                mBtnJoin.setVisibility(View.VISIBLE);
//                mBtnJoin.setBackgroundResource(R.drawable.sp_rect_corner_grey_light);
//                mBtnJoin.setText(getString(R.string.has_buy));
//                mBtnJoin.setClickable(false);
//            } else {
////                mBtnJoin.setVisibility(View.VISIBLE);
//                mBtnJoin.setBackgroundResource(R.drawable.sp_rect_orange);
//                mBtnJoin.setText(getString(R.string.buy_now));
//            }
            getVipState();
        } else {
            queryRoomVipPage();
            mBtnJoin.setVisibility(View.VISIBLE);
            mBtnJoin.setBackgroundResource(R.drawable.sp_rect_orange);
            mBtnJoin.setText(getString(R.string.buy_now));
        }


    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MemberAreaActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
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
        LinearLayout view = (LinearLayout) mWvVipArea.getParent();
        view.removeView(mWvVipArea);
        mWvVipArea.destroy();
        EventBus.getDefault().unregister(this);
    }

    private void queryVipMemberIntru() {
        _apiManager.getService().queryVipMemberIntru(_mApplication.getUserInfo().getSessionID(), "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultVipInstru, throwable -> {
                    Log.e(TAG, "queryVipMemberIntru: " + throwable, throwable);
                });
//        AppObservable.bindActivity(this, _apiManager.getService().queryVipMemberIntru(_mApplication.getUserInfo().getSessionID(), ""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::resultVipInstru, throwable -> {
//                    Log.e(TAG, "queryVipMemberIntru: " + throwable, throwable);
//                });
    }

    private void resultVipInstru(ReturnValue<VIPInfoBean> returnValue) {
        if (returnValue.getMsg().equals(Constants.ERROR)) {
            if (returnValue.getData().getMessage().contains("登录")) {
                ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
                startActivity(new Intent(this, LoginActivity.class));
            }
        } else {
            mWvVipArea.loadUrl(returnValue.getData().getVipurl());
        }
    }

    private void getVipState() {
        _apiManager.getService().getVipState(_mApplication.getUserInfo().getSessionID(), tid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultVipState, throwable -> {
                    Log.e(TAG, "getVipState: ", throwable);
                });
//        AppObservable.bindActivity(this, _apiManager.getService().getVipState(_mApplication.getUserInfo().getSessionID(), tid))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::resultVipState, throwable -> {
//                    Log.e(TAG, "getVipState: ", throwable);
//                });
    }

    private void resultVipState(ReturnValue<VipStateBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            VipStateBean vipState = returnValue.getData();
            if (vipState.getIsroomvip().equals(Constants.IS_VIP)) {
//                mBtnJoin.setVisibility(View.GONE);
                mBtnJoin.setVisibility(View.VISIBLE);
                mBtnJoin.setBackgroundResource(R.drawable.sp_rect_corner_grey_light);
                mBtnJoin.setText(getString(R.string.has_buy));
                mBtnJoin.setClickable(false);
            } else {
                mBtnJoin.setVisibility(View.VISIBLE);
                mBtnJoin.setBackgroundResource(R.drawable.sp_rect_orange);
                mBtnJoin.setText(getString(R.string.buy_now));
            }


        } else {
            ToastUtil.showShort(_mApplication, returnValue.getMsg());
        }
    }

    private void queryRoomVipPage() {
        _apiManager.getService().queryRoomVipPage(tid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultVipPage, throwable -> {
                    Log.e(TAG, "getVipState: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryRoomVipPage(tid))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::resultVipPage, throwable -> {
//                    Log.e(TAG, "getVipState: ", throwable);
//                });
    }

    private void resultVipPage(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            mWvVipArea.setVisibility(View.VISIBLE);
            mWvVipArea.loadUrl(returnValue.getData().getMessage());
        } else {
            ToastUtil.showShort(_mApplication, returnValue.getMsg());
            if (returnValue.getData().getMessage().contains("登录")) {
                ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    private void buyTactics() {
        String tacID;
        if (tid.equals("") || tid.equals("9898")) {
            tacID = "1";
        } else {
            tacID = tid;
        }
        _apiManager.getService().buyTactics(_mApplication.getUserInfo().getSessionID(), tacID, "2")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultBuyTactics, throwable -> {
                    Log.e(TAG, "buyTactics: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().buyTactics(_mApplication.getUserInfo().getSessionID(), tacID, "2"))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::resultBuyTactics, throwable -> {
//                    Log.e(TAG, "buyTactics: ", throwable);
//                });
    }

    private void resultBuyTactics(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
            finish();
        } else {
            if (returnValue.getData().getMessage().contains("余额不足")) {
                confirmBuyDialog(returnValue.getData().getMessage(), false);
                return;
            }
            ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
//            mBtnJoin.setVisibility(View.GONE);
            mBtnJoin.setBackgroundResource(R.drawable.sp_rect_corner_grey_light);
            mBtnJoin.setText(getString(R.string.has_buy));
            mBtnJoin.setClickable(false);
        }
    }

    private void confirmBuyDialog(String contentStr, boolean isBuy) {
        new JoDialog.Builder(this)
                .setStrTitle(R.string.tips)
                .setStrContent(contentStr)
                .setNegativeTextRes(R.string.cancel)
                .setPositiveTextRes(R.string.confirm)
                .setCallback(new JoDialog.ButtonCallback() {
                    @Override
                    public void onPositive(JoDialog dialog) {
                        if (isBuy) {
                            dialog.dismiss();
                            buyTactics();
                        } else {
                            startActivity(new Intent(getApplicationContext(), RechargeActivity.class));
                        }
                    }

                    @Override
                    public void onNegtive(JoDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show(true);
    }
}
