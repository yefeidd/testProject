package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.utils.AppUtil;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Scorpion on 2016/3/15.
 * 　　　___ __
 * 　　 {___{__}\
 * 　 {_}      `\)
 * 　{_}        `            _.-''''--.._
 * 　{_}                    //'.--.  \___`.
 * 　{ }__,_.--~~~-~~~-~~-::.---. `-.\  `.)
 * 　`-.{_{_{_{_{_{_{_{_//  -- 8;=- `
 * 　　`-:,_.:,_:,_:,.`\\._ ..'=- ,
 * 　　　　　// // // //`-.`\`   .-'/
 * 　　　　　<< << << <<    \ `--'  /----)
 * 　　　　　^  ^  ^  ^     `-.....--'''
 * Email:m15267280642@163.com
 */
public class RegisterActivity extends BaseActivity {
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.et_phone_num)
    EditText mEtPhoneNum;
    @Bind(R.id.et_sec_code)
    EditText mEtSecCode;
    @Bind(R.id.btn_code)
    Button mBtnCode;
    @Bind(R.id.register_pwd)
    EditText mRegisterPwd;
    @Bind(R.id.register_rpwd)
    EditText mRegisterRpwd;
    @Bind(R.id.cb_agreement)
    CheckBox mCbAgreement;
    @Bind(R.id.ll_register)
    LinearLayout mLlRegister;
    @Bind(R.id.tv_register_pro)
    TextView mTvRegisterPro;

    private Subscription timerSubscription;    // 倒计时
    private boolean isStartCountDown = false;
    private String mMobileNumber;
    private String mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_register);


    }

    @Override
    protected void initView() {
        mIvLeftmenu.setImageResource(R.drawable.register_back);

        if (getIntent().getStringExtra(Constants.FROM) != null && getIntent().getStringExtra(Constants.FROM).equals(Constants.FIND)) {
            mToolbarTitle.setText(getString(R.string.find_password));
            mLlRegister.setVisibility(View.GONE);
        } else {
            mToolbarTitle.setText("注册");
            mLlRegister.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void initEvent() {
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RegisterActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RegisterActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (AppUtil.getRunningTaskCount(this) == 1) {
            startActivity(new Intent(this, MainActivity.class));
        }
        if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
            timerSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.btn_code, R.id.btn_submit, R.id.iv_leftmenu, R.id.tv_register2login, R.id.tv_register_pro})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                String mobileNumber = mEtPhoneNum.getText().toString();
                if (StringUtil.isEmpty(mobileNumber)) {
                    ToastUtil.showShort(this, getString(R.string.phone_number_empty));
                    break;
                }
                if (!StringUtil.isMobile(mobileNumber)) {
                    ToastUtil.showShort(this, getString(R.string.phone_number_verify));
                    break;
                }
                if (getIntent().getStringExtra(Constants.FROM) != null && getIntent().getStringExtra(Constants.FROM).equals(Constants.REGISTER)) {
                    sendResCode();
                } else {
                    sendFindMsgCode();
                }
                break;
            case R.id.btn_submit:
                if (getIntent().getStringExtra(Constants.FROM) != null && getIntent().getStringExtra(Constants.FROM).equals(Constants.REGISTER)) {
                    sendResInfo();

                } else {
                    findPW();
                }
                break;
            case R.id.tv_register2login:
                finish();
                break;
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.tv_register_pro:
                startActivity(new Intent(RegisterActivity.this, RegisterProActivity.class));
                break;
        }
    }

    /**
     * 找回密码
     */
    private void findPW() {
        if (TextUtils.isEmpty(mRegisterPwd.getText())) { // 新密码为空
            ToastUtil.showShort(_mApplication, getString(R.string.new_pw_empty));
            return;
        }
        if (TextUtils.isEmpty(mRegisterRpwd.getText())) { // 确认密码为空
            ToastUtil.showShort(_mApplication, getString(R.string.confirm_pw_empty));
            return;
        }
        if (mRegisterPwd.getText().length() < 6
                || StringUtil.isNum(mRegisterPwd.getText().toString())
                || StringUtil.isEng(mRegisterPwd.getText().toString())) {
            ToastUtil.showShort(_mApplication, getString(R.string.pw_len_limit));
            return;
        }
        if (!mRegisterPwd.getText().toString().equals(mRegisterRpwd.getText().toString())) { // 两次输入不一样
            ToastUtil.showShort(_mApplication, getString(R.string.pw_no_equals));
            mRegisterPwd.setText("");
            mRegisterRpwd.setText("");
            return;
        }

        AppObservable.bindActivity(this, _apiManager.getService().findPW(mEtPhoneNum.getText().toString().trim(), mEtSecCode.getText().toString().trim(),
                mRegisterPwd.getText().toString().trim(), mRegisterRpwd.getText().toString().trim()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::findPWResult, throwable -> {
                    Log.e(TAG, getString(R.string.findpw_error));
                    ToastUtil.showShort(this, getString(R.string.findpw_error));
                });
    }

    /**
     * 处理找回密码结果
     *
     * @param returnValue 返回结果
     */
    private void findPWResult(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null) {
            if (returnValue.getMsg().equals(Constants.SUCCESS)) {
                // 找回密码成功
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else { // 失败
                ToastUtil.showShort(this, returnValue.getData().getMessage());
            }
        }
    }

    /**
     * 发送验证码
     */
    private void sendResCode() {

        if (isStartCountDown) { // 在计时中
            return;
        }
        isStartCountDown = true;
        AppObservable.bindActivity(this, _apiManager.getService().sendResCode(mEtPhoneNum.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendResCodeResult, throwable -> {
                    Log.e(TAG, getString(R.string.msg_regist_fail));
                    ToastUtil.showShort(_mApplication, getString(R.string.msg_regist_fail));
                });

    }

    /**
     * 发送找回密码验证码
     */
    private void sendFindMsgCode() {
        if (isStartCountDown) { // 在计时中
            return;
        }
        isStartCountDown = true;

        AppObservable.bindActivity(this, _apiManager.getService().findPWSendSms(mEtPhoneNum.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendMsgCodeResult, throwable -> {
                    Log.e(TAG, getString(R.string.msg_fail));
                    throwable.printStackTrace();
                    ToastUtil.showShort(_mApplication, getString(R.string.msg_fail));
                });
    }

    /**
     * 处理发送验证码返回结果
     *
     * @param result 结果
     */
    private void sendMsgCodeResult(ReturnValue<MessageBean> result) {
        if (result != null) {
            if (result.getMsg().equals(Constants.SUCCESS)) { // 成功
                startCountDown();
                ToastUtil.showShort(this, getString(R.string.msg_success));
            } else { // 失败
                ToastUtil.showShort(this, result.getData().getMessage());
                if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
                    timerSubscription.unsubscribe();
                }
            }
        }
    }

    /**
     * 打开计时器
     */
    private void startCountDown() {
        mBtnCode.setEnabled(false);
        mBtnCode.setText("60");
        mBtnCode.setBackgroundResource(R.drawable.sp_rect_corner_grey_light);
        timerSubscription = Observable.timer(1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong >= 60) {
                        timerSubscription.unsubscribe();
                        stopCountDown();
                        return;
                    }
                    mBtnCode.setText("" + (59 - aLong));
                }, throwable -> {
                    Log.e(TAG, "倒计时出错！");
                    stopCountDown();
                });
    }

    /**
     * 关闭计时器
     */
    private void stopCountDown() {
        mBtnCode.setText(getString(R.string.identify_code));
        isStartCountDown = false;
        mBtnCode.setEnabled(true);
        mBtnCode.setBackgroundResource(R.drawable.sp_rect_orange);
    }

    /**
     * 发送注册信息
     */
//    //昵称前缀
//    private final String NICKNAME = "RongNiu";
//    //格式化字符串
//    final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private void sendResInfo() {

//        StringBuilder nickName = new StringBuilder();
        mMobileNumber = mEtPhoneNum.getText().toString();
        String registerCode = mEtSecCode.getText().toString();
        mPassword = mRegisterPwd.getText().toString();
        String password1 = mRegisterRpwd.getText().toString();

        //判断输入的是否合法
        if (userChecked(mMobileNumber, mPassword, password1)) return;

//        int index = mMobileNumber.length() - 4;
//        String laterNickName = mobileNumber111.substring(index);
        //拿到随机的昵称
//        nickName.append(NICKNAME).append(format.format(System.currentTimeMillis()));
//        nickName.append(laterNickName);

        if (RnApplication.isShowLog) {
            Log.i(TAG, "sendResInfo: " + mMobileNumber + "::" + "null" + "::" + mPassword + "::" + registerCode);
        }
        AppObservable.bindActivity(this, _apiManager.getService().sendResInfo(mMobileNumber, null, mPassword, registerCode, Constants.ANDROID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendResInfoResult, throwable -> {
                    Log.e(TAG, getString(R.string.register_fail));
                    ToastUtil.showShort(_mApplication, getString(R.string.register_fail));
                });

    }


    /**
     * 返回注册码提交结果
     *
     * @param result
     */
    private void sendResCodeResult(ReturnValue<MessageBean> result) {
        if (result != null) {
            if (result.getMsg().equals(Constants.SUCCESS)) {
                startCountDown();
                ToastUtil.showShort(this, getString(R.string.msg_success));
            } else {
                isStartCountDown = false;
                ToastUtil.showShort(this, result.getData().getMessage());
                if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
                    timerSubscription.unsubscribe();
                }
            }
        }
    }


    /**
     * 返回注册信息提交结果
     */
    private void sendResInfoResult(ReturnValue<MessageBean> result) {
        if (result != null) {
            if (result.getMsg().equals(Constants.SUCCESS)) {
                ToastUtil.showShort(this, getString(R.string.register_success));
                /* 登录 */
//                AppObservable.bindActivity(this, _apiManager.getService().login(mMobileNumber, mPassword, "2"))
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(this::loginResult, throwable -> {
//                            Log.e(TAG, "登录异常");
//                        });
                Callback<ReturnValue<MessageBean>> callback = new Callback<ReturnValue<MessageBean>>() {
                    @Override
                    public void success(ReturnValue<MessageBean> returnValue, Response response) {
                        List<Header> headerList = response.getHeaders();
                        for (Header header : headerList) {
                            Log.d(TAG, header.getName() + " " + header.getValue());
                            if (header.getName().equals(Constants.SET_COOKIE)) {
                                Log.i("Set-Cookie", "success: " + header.getValue());
                                _mApplication.getUserInfo().setSessionID(header.getValue());
                            }
                        }
                        if (returnValue.getMsg().equals(Constants.SUCCESS)) {
                            // 登录成功，保存数据
                            _mApplication.getUserInfo().setPhone(mMobileNumber); // 手机号
                            _mApplication.getUserInfo().setIsLogin(1);
                            startActivity(new Intent(_mApplication, MainActivity.class));
                            finish();
                        } else {
                            ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "sendResInfoResult: 异常");
                        NetUtil.errorTip(error.getKind());
                    }
                };

                _apiManager.getService().login(mMobileNumber, mPassword, "2", callback);
            } else {
                ToastUtil.showShort(this, result.getData().getMessage());
            }
        }
    }

    /**
     * 合格性校验
     */
    private boolean userChecked(String mobileNumber, String password, String password1) {
        //手机号不能为空
        if (StringUtil.isEmpty(mobileNumber)) {
            ToastUtil.showShort(this, getString(R.string.phone_number_empty));
            return true;
        }
        //手机号校验
        if (!StringUtil.isMobile(mobileNumber)) {
            ToastUtil.showShort(this, getString(R.string.phone_number_verify));
            return true;
        }
        //验证码不能为空
        if (StringUtil.isEmpty(mobileNumber)) {
            ToastUtil.showShort(this, getString(R.string.msgcode_empty));
            return true;
        }
        //密码不能为空
        if (StringUtil.isEmpty(password)) {
            ToastUtil.showShort(this, getString(R.string.pw_no_empty));
            return true;
        }
        //密码必须一致
        if (!password.equals(password1)) {
            ToastUtil.showShort(this, getString(R.string.pw_no_equals));
            return true;
        }
        if (password.length() < 6) {
            ToastUtil.showShort(_mApplication, getString(R.string.pw_len_limit));
            return true;
        }
        if (password1.length() < 6) {
            ToastUtil.showShort(_mApplication, getString(R.string.pw_len_limit));
            return true;
        }
        //注册协议需要同意
        if (!mCbAgreement.isChecked()) {
            ToastUtil.showShort(this, getString(R.string.register_agreement_checked));
            return true;
        }
        return false;
    }

}
