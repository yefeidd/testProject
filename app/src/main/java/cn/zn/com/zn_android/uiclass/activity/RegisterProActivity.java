package cn.zn.com.zn_android.uiclass.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * Created by zjs on 2016/4/19 0019.
 * explain:
 */
public class RegisterProActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.wv_register)
    X5WebView mWvRegister;
    @Bind(R.id.pb_web_load)
    ProgressBar mPbWebLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_register_protocol);
    }

    @Override
    protected void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mWvRegister.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        mToolbarTitle.setText(getString(R.string.register_agreement));
//        if (!(NetUtil.isMOBILEConnected(_mApplication) || NetUtil.isWIFIConnected(_mApplication))) {
//            View not_net_view = UIUtil.inflate(R.layout.layout_not_net);
//            mFlContent.addView(not_net_view);
//            mWvRegister.setVisibility(View.GONE);
//        }
        mWvRegister.loadUrl(Constants_api.BASE_URL + Constants_api.REGISTER_MEMRA);

    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mWvRegister.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
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
        MobclickAgent.onPageStart("RegisterProActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RegisterProActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        webViewParent = (FrameLayout) mWvRegister.getParent();
//        webViewParent.removeView(mWvRegister);
        LinearLayout view = (LinearLayout) mWvRegister.getParent();
        view.removeView(mWvRegister);
        mWvRegister.destroy();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
