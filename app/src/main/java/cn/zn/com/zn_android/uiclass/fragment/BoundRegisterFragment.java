package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.activity.MainActivity;
import cn.zn.com.zn_android.uiclass.activity.RegisterProActivity;
import cn.zn.com.zn_android.utils.AppUtil;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
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
 * Created by zjs on 2016/5/13 0013.
 * explain:
 */
public class BoundRegisterFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "ucode";
    private static final String ARG_PARAM2 = "type";
    private Subscription timerSubscription;    // 倒计时
    private boolean isStartCountDown = false;
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
    @Bind(R.id.tv_register_pro)
    TextView mTvRegisterPro;
    @Bind(R.id.tv_register2login)
    TextView mTvRegister2login;
    @Bind(R.id.ll_register)
    LinearLayout mLlRegister;
    @Bind(R.id.btn_submit)
    Button mBtnSubmit;
    String ucode = "";
    private String mMobileNumber;
    private String mPassword;
    int type = Constants.WEI_XING;

    // TODO: Rename and change types and number of parameters
    public static BoundRegisterFragment newInstance(String ucode, int type) {
        BoundRegisterFragment fragment = new BoundRegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, ucode);
        args.putInt(ARG_PARAM2, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ucode = getArguments().getString(ARG_PARAM1);
            type = getArguments().getInt(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _setLayoutRes(R.layout.layout_register);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BoundRegisterFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BoundRegisterFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    protected void initEvent() {
        mBtnSubmit.setOnClickListener(this);
        mBtnCode.setOnClickListener(this);
        mTvRegister2login.setOnClickListener(this);
        mTvRegisterPro.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /**
     * 发送验证码
     */
    private void sendResCode() {

        if (isStartCountDown) { // 在计时中
            return;
        }
        isStartCountDown = true;
        AppObservable.bindFragment(this, _apiManager.getService().sendResCode(mEtPhoneNum.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendResCodeResult, throwable -> {
                    Log.e(TAG, getString(R.string.msg_regist_fail));
                    ToastUtil.showShort(_mApplication, getString(R.string.msg_regist_fail));
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
                ToastUtil.showShort(_mActivity, getString(R.string.msg_success));
            } else {
                ToastUtil.showShort(_mActivity, result.getData().getMessage());
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_code:
                String mobileNumber = mEtPhoneNum.getText().toString();
                if (StringUtil.isEmpty(mobileNumber)) {
                    ToastUtil.showShort(getActivity(), getString(R.string.phone_number_empty));
                    break;
                }
                if (!StringUtil.isMobile(mobileNumber)) {
                    ToastUtil.showShort(getActivity(), getString(R.string.phone_number_verify));
                    break;
                }
                sendResCode();
                break;
            case R.id.btn_submit:
                sendResInfo();
                break;
            case R.id.tv_register2login:
                getActivity().finish();
                break;
            case R.id.tv_register_pro:
                startActivity(new Intent(getActivity(), RegisterProActivity.class));
                break;
        }
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

        AppObservable.bindFragment(this, _apiManager.getService().bindRegister(type, ucode, mMobileNumber, mPassword, registerCode, Constants.ANDROID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendResInfoResult, throwable -> {
                    Log.e(TAG, getString(R.string.register_fail));
                    ToastUtil.showShort(_mApplication, getString(R.string.register_fail));
                });

    }

    /**
     * 返回注册信息提交结果
     */
    private void sendResInfoResult(ReturnValue<MessageBean> result) {
        if (result != null) {
            if (result.getMsg().equals(Constants.SUCCESS)) {
                ToastUtil.showShort(getActivity(), getString(R.string.register_success));
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
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        } else {
                            ToastUtil.showShort(getActivity(), returnValue.getData().getMessage());
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
                ToastUtil.showShort(getActivity(), result.getData().getMessage());
            }
        }
    }

    /**
     * 合格性校验
     */
    private boolean userChecked(String mobileNumber, String password, String password1) {
        //手机号不能为空
        if (StringUtil.isEmpty(mobileNumber)) {
            ToastUtil.showShort(getActivity(), getString(R.string.phone_number_empty));
            return true;
        }
        //手机号校验
        if (!StringUtil.isMobile(mobileNumber)) {
            ToastUtil.showShort(getActivity(), getString(R.string.phone_number_verify));
            return true;
        }
        //验证码不能为空
        if (StringUtil.isEmpty(mobileNumber)) {
            ToastUtil.showShort(getActivity(), getString(R.string.msgcode_empty));
            return true;
        }
        //密码不能为空
        if (StringUtil.isEmpty(password)) {
            ToastUtil.showShort(getActivity(), getString(R.string.pw_no_empty));
            return true;
        }
        //密码必须一致
        if (!password.equals(password1)) {
            ToastUtil.showShort(getActivity(), getString(R.string.pw_no_equals));
            return true;
        }
        //注册协议需要同意
        if (!mCbAgreement.isChecked()) {
            ToastUtil.showShort(getActivity(), getString(R.string.register_agreement_checked));
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        if (AppUtil.getRunningTaskCount(getActivity()) == 1) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
            timerSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
