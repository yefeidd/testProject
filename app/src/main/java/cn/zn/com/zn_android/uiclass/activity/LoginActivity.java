package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.uiclass.NoUnderlineSpan;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.utils.AppUtil;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登录页面
 * <p>
 * Created by WJL on 2016/3/11 0011 17:57.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_register)
    TextView tv_register;
    @Bind(R.id.tv_find_pw)
    TextView tv_find_pw;
    @Bind(R.id.cb_remenber_pw)
    CheckBox cb_remenber_pw;
    @Bind(R.id.et_username)
    EditText mEtUsername; //用户名
    @Bind(R.id.et_password)
    EditText mEtPassword; //密码
    @Bind(R.id.ibn_wx_login)
    ImageButton mIbnWxLogin;
    @Bind(R.id.ibn_qq_login)
    ImageButton mIbnQqLogin;
    @Bind(R.id.ibn_wb_login)
    ImageButton mIbnWbLogin;

    //获取UMShareAPI
    private UMShareAPI mShareAPI;
    private SHARE_MEDIA platform;
    private int type = Constants.WEI_XING;
    private String ucode = "";

    private final String BOUND_SUCCESS = "登录成功";
    private final String NOT_BOUND = "0";

    private boolean isFromGuide = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(true);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_login);
        //获取UMShareAPI
        mShareAPI = UMShareAPI.get(this);
        if (_mApplication.getUserInfo().getIsLogin() == Constants.IS_LOGIN) finish();
    }

    public void onEventMainThread(AnyEventType event) {
        if (null != event && event.getState()) {
            isFromGuide = event.getState();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 社会化组件登陆
     */
    private void societyLogin(SHARE_MEDIA platform) {
        setDialog();
        //授权登陆
        mShareAPI.doOauthVerify(this, platform, umAuthListener);
        mShareAPI.getPlatformInfo(this, platform, umAuthListener);
    }

    /**
     * 请求回调监听器
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            if (action == 0) {
                switch (type) {
                    case Constants.WEI_XING:
                        ucode = data.get("unionid");
                        break;
                    case Constants.WEI_BO:
                        ucode = data.get("uid");
                        break;
                    case Constants.QQ:
                        ucode = data.get("openid");
                        break;
                }
                if (StringUtil.isEmpty(ucode)) {
                    Toast.makeText(getApplicationContext(), "授权登陆失败，请联系客服人员", Toast.LENGTH_SHORT).show();
                } else {
                    checkSocietyLogin();
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "授权登陆失败，请联系客服人员", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "取消授权", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 重写
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 第三方登陆校验接口
     */
    private void checkSocietyLogin() {
        if (StringUtil.isEmpty(ucode)) {
            ToastUtil.showShort(this, getString(R.string.authorization_fail));
            return;
        }

        /**
         * 三方openid提交到服务器的回调
         */
        _apiManager.getService().tripartiteLogin(type, ucode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (returnValue != null) {
                        String bound_status = returnValue.getData().getMessage();
                        // 登录成功，保存数据  //如果绑定成功
                        if (bound_status.equals(BOUND_SUCCESS)) {
                            // 登录成功，设置登陆状态为已经登陆
                            _mApplication.getUserInfo().setIsTeacher(returnValue.getData().getIs_teacher());
                            _mApplication.getUserInfo().setIsLogin(Constants.IS_LOGIN);
                            finish();
                        } else if (bound_status.equals(NOT_BOUND)) {
                            EventBus.getDefault().postSticky(new AnyEventType(ucode).setType(type));
                            startActivity(new Intent(LoginActivity.this, BoundLoginActivity.class));
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "sendResInfoResult: 异常", throwable);
                    ToastUtil.show(this, getString(R.string.no_net), Toast.LENGTH_SHORT);
                });
    }

    @Override
    protected void initView() {
        SpannableString register = new SpannableString(getString(R.string.register));
        register.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(_mApplication, RegisterActivity.class);
                intent.putExtra(Constants.FROM, Constants.REGISTER);
                startActivity(intent);
            }
        }, 5, register.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setSpan(new NoUnderlineSpan(), 5, register.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(this, 16)), 5, register.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setSpan(new ForegroundColorSpan(Color.WHITE), 5, register.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_register.setText(register);
        tv_register.setMovementMethod(LinkMovementMethod.getInstance());
        tv_register.setHighlightColor(getResources().getColor(android.R.color.transparent));

        if (_spfHelper.getData(Constants.SPF_KEY_PHONE) != null && !_spfHelper.getData(Constants.SPF_KEY_PHONE).equals("")) {
            String phoneStr = _spfHelper.getData(Constants.SPF_KEY_PHONE);
            phoneStr = phoneStr.substring(0, 3) + " " + phoneStr.substring(3, 7) + " " + phoneStr.substring(7, phoneStr.length());
            mEtUsername.setText(phoneStr);
            Editable editable = mEtUsername.getText();
            Selection.setSelection(editable, editable.length());
        }
        if (_spfHelper.getData(Constants.SPF_KEY_PWD) != null && !_spfHelper.getData(Constants.SPF_KEY_PWD).equals("")) {
            mEtPassword.setText(_spfHelper.getData(Constants.SPF_KEY_PWD));
            Editable editable = mEtPassword.getText();
            Selection.setSelection(editable, editable.length());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("LoginActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        if (_mApplication.getUserInfo().getIsLogin() == 1) {
            finish();
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LoginActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initEvent() {
        tv_find_pw.setOnClickListener(this);
        mIbnWxLogin.setOnClickListener(this);
        mIbnWbLogin.setOnClickListener(this);
        mIbnQqLogin.setOnClickListener(this);
        mEtUsername.addTextChangedListener(new TextWatcher() {

            int c = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                c = count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mEtPassword.setText("");
                }
                if (s.length() == 3 && c > 0) {
                    s.append(" ");
                }
                if (s.length() == 8 && c > 0) {
                    s.append(" ");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_find_pw:
                Intent intent = new Intent(_mApplication, RegisterActivity.class);
                intent.putExtra(Constants.FROM, Constants.FIND);
                startActivity(intent);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.ib_back:
                if (AppUtil.getRunningTaskCount(this) == 1) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                break;
            //三方微信登陆
            case R.id.ibn_wx_login:
                //判断是否快速点击多次
                if (UIUtil.isFastClick()) {
                    return;
                }
                type = Constants.WEI_XING;
                platform = SHARE_MEDIA.WEIXIN;
                if (mShareAPI.isInstall(this, platform)) {
                    societyLogin(platform);
                } else {
                    notInstallTips();
                }
                break;
            case R.id.ibn_qq_login:
                type = Constants.QQ;
                platform = SHARE_MEDIA.QQ;
                if (mShareAPI.isInstall(this, platform)) {
                    societyLogin(platform);
                } else {
                    notInstallTips();
                }
                break;
            case R.id.ibn_wb_login:
                type = Constants.WEI_BO;
                platform = SHARE_MEDIA.SINA;
                societyLogin(platform);
                break;
        }
    }

    /**
     * 设置三方跳转的时候的dialog
     */
    private void setDialog() {
        JoDialog dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);
        Config.dialog = dialog;
    }


    /**
     * 未安装QQ或者微信的时候给出提示
     */
    private void notInstallTips() {
        switch (type) {
            case Constants.WEI_XING:
                Config.dialog.dismiss();
                ToastUtil.show(this, "微信客户端未安装，请先安装微信客户端", 0);
                break;
            case Constants.QQ:
                Config.dialog.dismiss();
                ToastUtil.show(this, "QQ客户端未安装，请先安装QQ客户端", 0);
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        /* 用户名为空 */
        if (TextUtils.isEmpty(mEtUsername.getText())) {
            ToastUtil.showShort(this, getString(R.string.login_user_empty));
            return;
        }
        /* 密码为空 */
        if (TextUtils.isEmpty(mEtPassword.getText())) {
            ToastUtil.showShort(this, getString(R.string.login_pw_empty));
            return;
        }
        final String phoneStr = mEtUsername.getText().toString().trim().replace(" ", "");

        _apiManager.getService().login(phoneStr, mEtPassword.getText().toString(), "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (returnValue != null) {
                        if (returnValue.getMsg().equals(Constants.SUCCESS)) {
                            // 登录成功，保存数据
//                            _mApplication.getUserInfo().setPhone(phoneStr); // 手机号
//                            _mApplication.getUserInfo().setIsLogin(1);
//                            _mApplication.getUserInfo().setIsTeacher(returnValue.getData().getIs_teacher());
                            _spfHelper.saveData(Constants.SPF_KEY_PHONE, phoneStr);
                            _mApplication.getUserInfo().setPhone(phoneStr); // 手机号
                            _mApplication.getUserInfo().setIsLogin(1);
                            _mApplication.getUserInfo().setIsTeacher(returnValue.getData().getIs_teacher());
                            if (cb_remenber_pw.isChecked()) { // 记住密码
                                _spfHelper.saveData(Constants.SPF_KEY_PWD, mEtPassword.getText().toString().trim());
                                _mApplication.getUserInfo().setPassword(mEtPassword.getText().toString().trim());
                            } else {
                                _spfHelper.saveData(Constants.SPF_KEY_PWD, "");
                                _mApplication.getUserInfo().setPassword("");
                            }
//                    startActivity(new Intent(_mApplication, MainActivity.class));
                            if (!isFromGuide) {
                                finish();
                            } else {
                                startActivity(new Intent(_mApplication, MainActivity.class));
                            }
                            startActivity(new Intent(_mApplication, MainActivity.class));
                            finish();
                        } else {
                            ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
                        }

//                if (returnValue.getMsg().equals(Constants.SUCCESS)) {
//                    // 登录成功，保存数据
//                    // 登录成功，保存数据
//                    _spfHelper.saveData(Constants.SPF_KEY_PHONE, mEtUsername.getText().toString().trim());
//                    _mApplication.getUserInfo().setPhone(mEtUsername.getText().toString().trim()); // 手机号
//                    _mApplication.getUserInfo().setIsLogin(1);
//                    _mApplication.getUserInfo().setIsTeacher(returnValue.getData().getIs_teacher());
//                    if (cb_remenber_pw.isChecked()) { // 记住密码
//                        _spfHelper.saveData(Constants.SPF_KEY_PWD, mEtPassword.getText().toString().trim());
//                        _mApplication.getUserInfo().setPassword(mEtPassword.getText().toString().trim());
//                    } else {
//                        _spfHelper.saveData(Constants.SPF_KEY_PWD, "");
//                        _mApplication.getUserInfo().setPassword("");
//                    }
////                    startActivity(new Intent(_mApplication, MainActivity.class));
//                    if (!isFromGuide) {
//                        finish();
//                    } else {
//                        startActivity(new Intent(_mApplication, MainActivity.class));
//                    }
//                } else {
//                    ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
//                }
                    }
                }, throwable -> {
                    Log.e(TAG, "sendResInfoResult: 异常");
                    ToastUtil.show(this, getString(R.string.no_net), Toast.LENGTH_SHORT);
                });

    }

}
