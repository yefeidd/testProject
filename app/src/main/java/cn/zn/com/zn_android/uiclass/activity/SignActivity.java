package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.CalenderAdapter;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.SignInfoBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.SignPresenter;
import cn.zn.com.zn_android.presenter.requestType.SignRequestType;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.uiclass.NoScrollGridView;
import cn.zn.com.zn_android.utils.DateUtils;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.SignView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 签到
 * Created by Jolly on 2016/9/26 0026.
 */

public class SignActivity extends BaseMVPActivity<SignView, SignPresenter> implements SignView {
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_serial_sign)
    TextView mTvSerialSign;
    @Bind(R.id.btn_sign)
    ImageButton mBtnSign;
    @Bind(R.id.tv_total_sign)
    TextView mTvTotalSign;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_day_sign)
    TextView mTvDaySign;
    @Bind(R.id.tv_day7)
    TextView mTvDay7;
    @Bind(R.id.tv_day15)
    TextView mTvDay15;
    @Bind(R.id.tv_day30)
    TextView mTvDay30;
    @Bind(R.id.tv_month_more)
    TextView mTvMonthMore;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.gv_calender)
    NoScrollGridView mGvCalender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public SignPresenter initPresenter() {
        return new SignPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.past));
        mIbSearch.setImageResource(R.mipmap.ic_share);

        CalenderAdapter adapter = new CalenderAdapter(this, R.layout.item_calender, getCalenderDays());
        mGvCalender.setAdapter(adapter);

        SpannableString daySignSS = new SpannableString(getString(R.string.day_sign));
        daySignSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)),
                5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvDaySign.setText(daySignSS);

        SpannableString day7SignSS = new SpannableString(getString(R.string.day_7_sign));
        day7SignSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_facc72)),
                4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        day7SignSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)),
                9, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvDay7.setText(day7SignSS);

        SpannableString day15SignSS = new SpannableString(getString(R.string.day_15_sign));
        day15SignSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_facc72)),
                4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        day15SignSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)),
                10, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvDay15.setText(day15SignSS);

        SpannableString day30SignSS = new SpannableString(getString(R.string.day_30_sign));
        day30SignSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_facc72)),
                4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        day30SignSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)),
                10, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvDay30.setText(day30SignSS);

    }

    private List<String> getCalenderDays() {
        List<String> data = new ArrayList<>();

        int inweek = DateUtils.getCurrentDayOfWeek();
        int today = DateUtils.getCurrentDay();
        int dayCount = DateUtils.getCurrentMonthDay();
        int startSpace = 0;
        if (today > inweek) {
            startSpace = 7 - (today - inweek) % 7;
        } else {
            startSpace = inweek - today;
        }
        int endSpace = 7 - (dayCount - today - (7 - inweek)) % 7;
        int calenderCount = startSpace + endSpace + dayCount + 7;
        String[] weeks = getResources().getStringArray(R.array.week);
        for (int i = 0; i < calenderCount; i ++) {
            String day = "";
            if (i < 7) {
                day = weeks[i];
            } else if (i < startSpace + 7) {
                day = "";
            } else if (i > (startSpace + dayCount + 6)) {
                day = "";
            } else {
                day = (i - startSpace - 6) + "";
            }
            data.add(day);
        }

        return data;
    }

    @Override
    protected void initData() {
        presenter.queryUserSign(_mApplication.getUserInfo().getSessionID());
    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.iv_leftmenu, R.id.ib_search, R.id.btn_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.ib_search:

                break;
            case R.id.btn_sign:
                presenter.userSign(_mApplication.getUserInfo().getSessionID());
                break;
        }
    }

    private void resultUserSign(SignInfoBean bean) {

        String serial_day = String.format(getString(R.string.serial_sign_days), bean.getContinuity_day());
        SpannableString serialSs = new SpannableString(serial_day);
        serialSs.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_0a78b8)),
                0, bean.getContinuity_day().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvSerialSign.setText(serialSs);

        String total_day = String.format(getString(R.string.total_sign_days), bean.getTotal_day());
        SpannableString totalSs = new SpannableString(total_day);
        totalSs.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_0a78b8)),
                0, bean.getTotal_day().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvTotalSign.setText(totalSs);

        mTvDate.setText(bean.getToday());

        if (bean.getSign_state() == 1) {
            mBtnSign.setBackgroundResource(R.mipmap.ic_signed);
        } else {
            mBtnSign.setBackgroundResource(R.mipmap.ic_sign);
        }
    }

    @Override
    public void onSuccess(SignRequestType requestType, Object object) {
        switch (requestType) {
            case SIGN_INFO:
                resultUserSign((SignInfoBean) object);
                break;
            case USER_SIGN:
                MessageBean messageBean = (MessageBean) object;
                ToastUtil.showShort(this, messageBean.getMessage());
                presenter.queryUserSign(_mApplication.getUserInfo().getSessionID());
                break;
        }
    }

    @Override
    public void onError(SignRequestType requestType, Throwable t) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
