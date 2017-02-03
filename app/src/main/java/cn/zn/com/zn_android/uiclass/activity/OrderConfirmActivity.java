package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OrderInfoBean;
import cn.zn.com.zn_android.model.bean.PayOrderBean;
import cn.zn.com.zn_android.presenter.OrderConfirmPresenter;
import cn.zn.com.zn_android.presenter.requestType.OrderConfirmType;
import cn.zn.com.zn_android.uiclass.NoUnderlineSpan;
import cn.zn.com.zn_android.utils.DateUtils;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.OrderConfirmView;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/11/28.
 */

public class OrderConfirmActivity extends BaseMVPActivity<OrderConfirmView, OrderConfirmPresenter>
        implements OrderConfirmView, View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_time_warning)
    TextView mTvTimeWarning;
    @Bind(R.id.tv_question)
    TextView mTvQuestion;
    @Bind(R.id.tv_detail)
    TextView mTvDetail;
    @Bind(R.id.tv_cost)
    TextView mTvCost;
    @Bind(R.id.tv_diagnosed)
    TextView mTvDiagnosed;
    @Bind(R.id.tv_balance)
    TextView mTvBalance;
    @Bind(R.id.cb_use_znb)
    AppCompatCheckBox mCbUseZnb;
    @Bind(R.id.tv_order_time)
    TextView mTvOrderTime;
    @Bind(R.id.tv_order_no)
    TextView mTvOrderNo;
    @Bind(R.id.tv_real_cost)
    TextView mTvRealCost;
    @Bind(R.id.tv_pay_now)
    TextView mTvPayNow;
    @Bind(R.id.tv_not_enough)
    TextView mTvNotEnough;

    public static int _useTicCode = 0x1010;
    private int realCost = 0;
    private int wealth = 0;
    private String orderNo = "";
    private String zgTic = "";
    private List<OrderInfoBean.TicListBean> ticList = new ArrayList<>();
    private List<Integer> useTics = new ArrayList<>();
    private Subscription timerSubscription;    // 计时

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        useTics.clear();
        useTics.addAll(data.getIntegerArrayListExtra("USE_TIC_LIST"));
        zgTic = "";
        for (int i : useTics) {
            zgTic += ticList.get(i).getId() + ",";
        }
        if (zgTic.endsWith(",")) {
            zgTic = zgTic.substring(0, zgTic.length() - 1);
        }
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dettach();

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
        if (null != timerSubscription && !timerSubscription.isUnsubscribed()) {
            timerSubscription.unsubscribe();
            timerSubscription = null;
        }
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        orderNo = getIntent().getStringExtra("orderNum");

        presenter.attach(this);
        mToolbarTitle.setText(R.string.order_confirm);

        mTvOrderNo.setText(String.format(getString(R.string.order_no, orderNo), orderNo));

        SpannableString rechargeSs = new SpannableString(getString(R.string.balance_not_enough));
        rechargeSs.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(_mApplication, RechargeActivity.class);
                startActivity(intent);
            }
        }, 7, rechargeSs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rechargeSs.setSpan(new NoUnderlineSpan(), 7, rechargeSs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rechargeSs.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)),
                7, rechargeSs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvNotEnough.setText(rechargeSs);
        mTvNotEnough.setMovementMethod(LinkMovementMethod.getInstance());
        mTvNotEnough.setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mTvPayNow.setOnClickListener(this);
        mTvDiagnosed.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        presenter.queryOrderInfo(_mApplication.getUserInfo().getSessionID(), orderNo);
    }

    @Override
    public OrderConfirmPresenter initPresenter() {
        return new OrderConfirmPresenter(this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_order_confirm;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.tv_pay_now:
                if (realCost == 0) {
                    presenter.payOrder(_mApplication.getUserInfo().getSessionID(), orderNo, zgTic, "2");
                } else if (realCost > 0 && mCbUseZnb.isChecked() && realCost < wealth) {
                    presenter.payOrder(_mApplication.getUserInfo().getSessionID(), orderNo, zgTic, "2");
                } else {
                    ToastUtil.showShort(this, getString(R.string.balance_not_enough));
                }
                break;
            case R.id.tv_diagnosed:
                EventBus.getDefault().postSticky(new AnyEventType(ticList));
                startActivityForResult(new Intent(this, UseDiagnoseActivity.class), _useTicCode);
                break;
        }
    }

    /**
     * 显示余额
     *
     * @param txt
     */
    private void updateBalance(String txt) {
        SpannableString balanceSs = new SpannableString(txt);
        balanceSs.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_value_black)),
                0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvBalance.setText(balanceSs);
    }

    /**
     * 显示应付金额
     */
    private void updateRealCost() {
        int value = 0;
        for (int index : useTics) {
            value += ticList.get(index).getFace_value();
        }

        updateUseTic(value + "");

        if (realCost > value) {
            realCost = realCost - value;
        } else {
            realCost = 0;
        }

        if (realCost > 0) {
            mCbUseZnb.setChecked(true);
        } else {
            mCbUseZnb.setChecked(false);
        }

        SpannableString realCostSs = new SpannableString(String.format(getString(R.string.order_real_cost), String.valueOf(realCost)));
        realCostSs.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_value_black)),
                0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        realCostSs.setSpan(new AbsoluteSizeSpan(14, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvRealCost.setText(realCostSs);

        if (wealth < realCost) {
            mTvNotEnough.setVisibility(View.VISIBLE);
        } else {
            mTvNotEnough.setVisibility(View.GONE);
        }
    }

    private void updateUseTic(String ticTotalValue) {
        mTvDiagnosed.setText(String.format(getString(R.string.diagnosed_ticket), ticTotalValue));
    }

    @Override
    public void onSuccess(OrderConfirmType requestType, Object object) {
        switch (requestType) {
            case QUERY_ORDER_INFO:
                OrderInfoBean orderInfo = (OrderInfoBean) object;
                updateUI(orderInfo);
                break;
            case PAY_ORDER:
                PayOrderBean payOrder = (PayOrderBean) object;
                Intent intent = new Intent(this, PayResultActivity.class);
                if (!payOrder.getStatus().equals("error")) {
                    intent.putExtra("isSuccess", true);
                    ToastUtil.showShort(this, payOrder.getData());
                    startActivity(intent);
                } else {
                    intent.putExtra("isSuccess", false);
                    startActivity(intent);
                    ToastUtil.showShort(this, payOrder.getData());
                }
                break;
        }
    }

    @Override
    public void onError(OrderConfirmType requestType, Throwable t) {

    }

    private void updateUI(OrderInfoBean orderInfo) {
        startCountDown(orderInfo.getSy_time());
        realCost = (int) orderInfo.getMoney();
        wealth = (int) orderInfo.getWealth();
        mTvCost.setText(realCost + "币");
        updateBalance(String.format(getString(R.string.wealth_left), String.valueOf(wealth)));
        updateRealCost();
        mTvQuestion.setText(orderInfo.getOrder_title());
        mTvDetail.setText(orderInfo.getRemark());
        mTvOrderTime.setText(String.format(getString(R.string.order_time), DateUtils.getStringDate(orderInfo.getTimes() * 1000, "yyyy-MM-dd HH:mm:ss")));
        ticList.clear();
        ticList.addAll(orderInfo.getTic_list());
    }

    /**
     * 打开计时器
     */
    private void startCountDown(Long syTime) {
        timerSubscription = Observable.timer(1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
//                .toBlocking()
                .subscribe(aLong -> {
                    deelCountResult(syTime, aLong);
//                    Log.e("startCountDown", aLong +"");
                }, throwable -> {
                    Log.e("timer", "倒计时出错！\n" + throwable);
                    startCountDown(syTime);
                });
    }

    /**
     * 处理计时结果
     * @param aLong
     */
    private void deelCountResult(Long syTime, Long aLong) {
        if (aLong >= syTime) {
            timerSubscription.unsubscribe();
            timerSubscription = null;
            return;
        }
        Long countTime = syTime - aLong;
        mTvTimeWarning.setText(String.format(getString(R.string.order_time_warning), DateUtils.second2HourStr(countTime)));
    }


}
