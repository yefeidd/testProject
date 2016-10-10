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
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.NoUnderlineSpan;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener, JoDialog.ButtonCallback {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_about)
    TextView mTvAbout;
    @Bind(R.id.tv_phone)
    TextView mTvPhone;
    @Bind(R.id.tv_official)
    TextView mTvOfficial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_about_us);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.about_us));
        SpannableString ssPhone = new SpannableString(getString(R.string.about_us_phone));
        ssPhone.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startCallTelDialog();
            }
        }, 5, ssPhone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssPhone.setSpan(new NoUnderlineSpan(), 5, ssPhone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssPhone.setSpan(new ForegroundColorSpan(0xffd83d21), 5, ssPhone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvPhone.setText(ssPhone);
        mTvPhone.setMovementMethod(LinkMovementMethod.getInstance());
        mTvPhone.setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }

    private void startCallTelDialog() {
        String phone = getString(R.string.about_us_phone).substring(5); //.replace("-", "");
//        String content = String.format(getString(R.string.about_us_call), new Object[]{phone});

        new JoDialog.Builder(this)
                .setGravity(Gravity.CENTER)
                .setStrTitle(R.string.call)
                .setStrContent(String.format(getString(R.string.about_us_call), new Object[]{phone}))
                .setNegativeTextRes(R.string.cancel)
                .setPositiveTextRes(R.string.confirm)
                .setCallback(this)
                .show(true);
    }

    @Override
    public void onPositive(JoDialog dialog) {
        dialog.dismiss();
        Log.i(TAG, "onPositive: " + getString(R.string.about_us_phone).substring(5).replace("-", ""));
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.about_us_phone).substring(5).replace("-", "")));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onNegtive(JoDialog dialog) {
        dialog.dismiss();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AboutUsActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AboutUsActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
