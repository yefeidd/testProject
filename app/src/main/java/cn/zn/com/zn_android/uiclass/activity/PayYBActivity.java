package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.utils.ToastUtil;

import butterknife.Bind;

/**
 * 暂时不用
 * 易宝支付
 */
public class PayYBActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_pay_yb);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.pay_yb));
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
            case R.id.btn_next:
                ToastUtil.showShort(_mApplication, getString(R.string.next_step));
                break;
        }
    }
}
