package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个性签名
 */
public class SignatureActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.et_self_intru)
    EditText mEtSelfIntru;
    @Bind(R.id.tv_words_left)
    TextView mTvWordsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_signature);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.person_signature));
        mTvSave.setVisibility(View.VISIBLE);
        mTvSave.setText(getString(R.string.confirm));
        mEtSelfIntru.setText(getIntent().getStringExtra("signature"));
        mTvWordsLeft.setText(String.format(getString(R.string.modify_words_left),
                new Object[]{Math.round(50 - getIntent().getStringExtra("signature").length())}));

        Editable editable = mEtSelfIntru.getText();
        Selection.setSelection(editable, editable.length());
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mTvSave.setOnClickListener(this);

        mEtSelfIntru.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvWordsLeft.setText(String.format(getString(R.string.modify_words_left), new Object[]{Math.round(50 - s.length())}));
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SignatureActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SignatureActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.tv_save:
                modifySignature();
                break;
        }
    }

    /**
     * 调用修改昵称接口
     */
    private void modifySignature() {
        if (TextUtils.isEmpty(mEtSelfIntru.getText())) {
            ToastUtil.showShort(_mApplication, "没有输入任何字符哦");
            return;
        }

        AppObservable.bindActivity(this, _apiManager.getService().modifyMemberSignature(_mApplication.getUserInfo().getSessionID(), mEtSelfIntru.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::modifyResult, throwable -> {
                    Log.i(TAG, "modifySignature: 异常");
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
            _mApplication.getUserInfo().setSignature(mEtSelfIntru.getText().toString());
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
