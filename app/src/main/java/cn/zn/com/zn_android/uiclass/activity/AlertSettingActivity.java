package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/7/4 0004.
 */
public class AlertSettingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.cb_alert1)
    CheckBox mCbAlert1;
    @Bind(R.id.cb_alert2)
    CheckBox mCbAlert2;
    @Bind(R.id.cb_alert3)
    CheckBox mCbAlert3;
    @Bind(R.id.cb_alert4)
    CheckBox mCbAlert4;
    @Bind(R.id.cb_alert5)
    CheckBox mCbAlert5;
    @Bind(R.id.cb_alert6)
    CheckBox mCbAlert6;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_up_down)
    TextView mTvUpDown;
    @Bind(R.id.et_up)
    EditText mEtUp;
    @Bind(R.id.et_down)
    EditText mEtDown;
    @Bind(R.id.et_day_up)
    EditText mEtDayUp;
    @Bind(R.id.et_day_down)
    EditText mEtDayDown;
    @Bind(R.id.et_down_to)
    EditText mEtDownTo;

    private OptionalStockBean bean = new OptionalStockBean();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_alert_setting);
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
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof MarketImp) {
            bean = (OptionalStockBean) event.getObject();
        }
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.alert_setting);
        mTvSave.setText(R.string.commit);
        mTvName.setText(bean.getName());
        mTvPrice.setText(String.format(getString(R.string.alert_setting_new_price), new Object[] {bean.getPrice()}));
        String updown = String.format(getString(R.string.alert_setting_updown), new Object[] {bean.getUpDown()});
        SpannableString ss = new SpannableString(updown);
        ss.setSpan(new ForegroundColorSpan(UIUtil.getResources().getColor(R.color.app_bar_color)), 4, updown.length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        mTvUpDown.setText(ss);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.tv_save:
                ToastUtil.showShort(this, R.string.commit);
                break;
        }
    }
}
