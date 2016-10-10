package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 修改昵称
 */
public class ModifyNickActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.et_nickname)
    EditText mEvNickname; // 昵称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_modify_nick);
    }

    @Override
    protected void initView() {
        mTvSave.setVisibility(View.VISIBLE);
        mToolbarTitle.setText(getString(R.string.modify_nick));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        mEvNickname.setText(_mApplication.getUserInfo().getName());
        Editable editable = mEvNickname.getText();
        mEvNickname.setSelection(editable.length());

        mEvNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                char[] changeChars = s.toString().toCharArray();
                s = mEvNickname.getText();
                for (int i = 0; i < changeChars.length; i++) {
                    if (!StringUtil.isNickNameLegal(changeChars[i])) { // 字符合法
                        Log.d("isNickNameLegal", "afterTextChanged: " + s + "\nlength: " + s.length());
                        if (s.length() > i) {
                            s.delete(i, i + 1);
                            mEvNickname.setSelection(s.length());
                        }
                    }
                }
                Log.d("isNickNameLegal", "afterTextChanged: " + s + "\nlength: " + s.length());

            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ModifyNickActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ModifyNickActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.tv_save:
                if (TextUtils.isEmpty(mEvNickname.getText())) {
                    ToastUtil.showShort(_mApplication, "还没有输入昵称哦");
                } else if (mEvNickname.getText().length() < 2) {
                    ToastUtil.showShort(getApplicationContext(), getString(R.string.nick_min_len));
                } else {
                    modifyNick();
                }
                break;
        }
    }

    /**
     * 调用修改昵称接口
     */
    private void modifyNick() {
        AppObservable.bindActivity(this, _apiManager.getService().modifyMemberNick(_mApplication.getUserInfo().getSessionID(), mEvNickname.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::modifyResult, throwable -> {
                    Log.i(TAG, "modifyNick: 异常");
                    ToastUtil.showShort(this, getString(R.string.modify_fail));
                });
    }

    /**
     * 修改返回结果
     *
     * @param returnValue
     */
    private void modifyResult(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            _mApplication.getUserInfo().setName(mEvNickname.getText().toString());
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
