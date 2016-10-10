package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.uiclass.NoUnderlineSpan;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * 支付宝支付
 */
public class PayZFBActivity extends BaseActivity implements View.OnClickListener, JoDialog.ButtonCallback {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_zfb_account)
    TextView mTvZfbAccount;
    @Bind(R.id.tv_bank_card)
    TextView mTvBankCard;
//    @Bind(R.id.tv_zfb_mobile)
//    TextView mTvZfbMobile;
    @Bind(R.id.tv_zfb_tel)
    TextView mTvZfbTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_pay_zfb);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.pay_zfb));

//        SpannableString mobileSpan = new SpannableString(getString(R.string.pay_zfb_mobile));
//        mobileSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)), 5, mobileSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mobileSpan.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                startCallTelDialog(mTvZfbMobile.getText().toString().substring(5));
//            }
//        }, 5, mobileSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mobileSpan.setSpan(new NoUnderlineSpan(), 5, mobileSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mTvZfbMobile.setText(mobileSpan);
//        mTvZfbMobile.setMovementMethod(LinkMovementMethod.getInstance());
//        mTvZfbMobile.setHighlightColor(getResources().getColor(android.R.color.transparent));

        SpannableString telSpan = new SpannableString(getString(R.string.pay_zfb_tel));
        telSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)), 5, telSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        telSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startCallTelDialog(mTvZfbTel.getText().toString().substring(5));
            }
        }, 5, telSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        telSpan.setSpan(new NoUnderlineSpan(), 5, telSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvZfbTel.setText(telSpan);
        mTvZfbTel.setMovementMethod(LinkMovementMethod.getInstance());
        mTvZfbTel.setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PayZFBActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PayZFBActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
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

    @Override
    public void onPositive(JoDialog dialog) {
        dialog.dismiss();
        Log.i(TAG, "onPositive: " + dialog.getContent().getText().toString().substring(7).replace("-", ""));
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dialog.getContent().getText().toString().substring(7).replace("-", "")));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onNegtive(JoDialog dialog) {
        dialog.dismiss();
    }

    private void startCallTelDialog(String phone) {

        new JoDialog.Builder(this)
                .setGravity(Gravity.CENTER)
                .setStrTitle(R.string.call)
                .setStrContent(String.format(getString(R.string.about_us_call), new Object[]{phone}))
                .setNegativeTextRes(R.string.cancel)
                .setPositiveTextRes(R.string.confirm)
                .setCallback(this)
                .show(true);
    }

}
