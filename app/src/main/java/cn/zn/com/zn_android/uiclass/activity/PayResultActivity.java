package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 支付结果
 * Created by Jolly on 2016/11/29.
 */

public class PayResultActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_result)
    TextView mTvResult;
    @Bind(R.id.tv_success_tip)
    TextView mTvSuccessTip;
    @Bind(R.id.tv_fail_tip)
    TextView mTvFailTip;
    @Bind(R.id.ib_return)
    ImageButton mIbReturn;
    @Bind(R.id.ib_repay)
    ImageButton mIbRepay;
    @Bind(R.id.ll_option)
    LinearLayout mLlOption;

    private Subscription timerSubscription;
    private boolean isSuccess = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSuccess = getIntent().getBooleanExtra("isSuccess", true);
        _setLayoutRes(R.layout.activity_pay_result);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.pay_result);
        if (isSuccess) {
            String time = String.format(getString(R.string.pay_result_success_warning), 3);
            mTvSuccessTip.setText(time);
        } else {
            mTvResult.setText(R.string.pay_result_fail_tip);
            mTvResult.setTextColor(getResources().getColor(R.color.yellow_f39800));
            mTvResult.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_pay_fail), null, null, null);
            mTvSuccessTip.setVisibility(View.GONE);
            mTvFailTip.setVisibility(View.VISIBLE);
            mLlOption.setVisibility(View.VISIBLE);
            String time = String.format(getString(R.string.pay_result_fail_warning), 15);
            mTvSuccessTip.setText(time);
        }

        startCountDown();
    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.iv_leftmenu, R.id.ib_return, R.id.ib_repay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_leftmenu:
                if (isSuccess) {
                    EventBus.getDefault().post(new AnyEventType().setType(MainActivity.POS_PERSON));
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    finish();
                }
                break;
            case R.id.ib_return: // 去我的订单
                startActivity(new Intent(this, MyOrdersActivity.class));
                break;
            case R.id.ib_repay: // 返回上个页面
                finish();
                break;
        }
    }

    /**
     * 打开计时器
     */
    private void startCountDown() {
        timerSubscription = Observable.timer(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
//                .toBlocking()
                .subscribe(aLong -> {
                    deelCountResult(aLong);
//                    Log.e("startCountDown", aLong + "");
                }, throwable -> {
                    Log.e("timer", "倒计时出错！\n" + throwable);
                    startCountDown();
                });
    }

    /**
     * 处理计时结果
     *
     * @param aLong
     */
    private void deelCountResult(Long aLong) {

        if (isSuccess) {
            if (aLong > 3) {
                EventBus.getDefault().post(new AnyEventType().setType(MainActivity.POS_PERSON));
                startActivity(new Intent(this, MainActivity.class));
                return;
            }
            String time = String.format(getString(R.string.pay_result_success_warning), 3-aLong);
            mTvSuccessTip.setText(time);
        } else {
            if (aLong > 15) {
                EventBus.getDefault().post(new AnyEventType().setType(MainActivity.POS_PERSON));
                startActivity(new Intent(this, MainActivity.class));
                return;
            }
            String time = String.format(getString(R.string.pay_result_fail_warning), 15-aLong);
            mTvFailTip.setText(time);
        }


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
    protected void onDestroy() {
        super.onDestroy();
        if (!timerSubscription.isUnsubscribed()) {
            timerSubscription.unsubscribe();
        }
    }
}
