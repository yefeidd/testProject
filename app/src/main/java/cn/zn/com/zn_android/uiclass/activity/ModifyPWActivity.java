package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 修改密码
 */
public class ModifyPWActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.et_pw_now)
    EditText mEtPwNow;
    @Bind(R.id.et_pw_new)
    EditText mEtPwNew;
    @Bind(R.id.et_pw_new_confirm)
    EditText mEtPwNewConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_modify_pw);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.modify_pw));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ModifyPWActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ModifyPWActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                modifyPW();
                break;
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }

    private void modifyPW() {
        if (TextUtils.isEmpty(mEtPwNow.getText())) {
            ToastUtil.showShort(_mApplication, "当前密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(mEtPwNew.getText())) {
            ToastUtil.showShort(_mApplication, "新密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(mEtPwNewConfirm.getText())) {
            ToastUtil.showShort(_mApplication, "请确认新密码");
            return;
        }
        if (mEtPwNew.getText().length() < 6 || StringUtil.isNum(mEtPwNew.getText().toString()) || StringUtil.isEng(mEtPwNew.getText().toString())) {
            ToastUtil.showShort(_mApplication, getString(R.string.pw_len_limit));
            return;
        }
        if (!mEtPwNew.getText().toString().equals(mEtPwNewConfirm.getText().toString())) {
            mEtPwNew.setText("");
            mEtPwNewConfirm.setText("");
            ToastUtil.showShort(_mApplication, getString(R.string.pw_no_equals));
            return;
        }

        AppObservable.bindActivity(this, _apiManager.getService().modifyPW(_mApplication.getUserInfo().getSessionID(),
                mEtPwNow.getText().toString().trim(), mEtPwNew.getText().toString().trim(), mEtPwNewConfirm.getText().toString().trim()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::modifyResult, throwable -> {
                    Log.i(TAG, "modifyPW: 异常");
                    ToastUtil.showShort(this, getString(R.string.modify_fail));
                });
    }

    /**
     * 修改返回结果
     * @param returnValue
     */
    private void modifyResult(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            if (_mApplication.getUserInfo().getPassword() != null && !_mApplication.getUserInfo().getPassword().equals("")) {
                _mApplication.getUserInfo().setPassword(mEtPwNew.getText().toString());
            }
            ToastUtil.showShort(this, getString(R.string.modify_success));
            finish();
        } else {
            ToastUtil.showShort(this, returnValue.getData().getMessage());
            if (_mApplication.getResources().getString(R.string.please_login).equals(returnValue.getData().getMessage())) {
                _mApplication.getUserInfo().setIsLogin(Constants.NOT_LOGIN);
            }
        }

    }
}
