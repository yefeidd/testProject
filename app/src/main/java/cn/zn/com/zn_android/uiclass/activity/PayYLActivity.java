package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * 银行支付
 */
public class PayYLActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_bank)
    TextView mTvBank;
    @Bind(R.id.tv_bank_card)
    TextView mTvBankCard;
    @Bind(R.id.tv_bank_use_name)
    TextView mTvBankUseName;
    @Bind(R.id.tv_buy_in)
    TextView mTvWarning;
    @Bind(R.id.tv_tips)
    TextView mTvTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_pay_yl);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.pay_bank));
        SpannableString tipSpan = new SpannableString(getString(R.string.bank_tips));
        tipSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)), 11, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvTips.setText(tipSpan);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PayYLActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PayYLActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }
}
