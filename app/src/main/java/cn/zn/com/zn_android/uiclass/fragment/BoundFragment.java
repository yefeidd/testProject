package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.activity.MainActivity;
import cn.zn.com.zn_android.uiclass.activity.RegisterActivity;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Created by zjs on 2016/5/13 0013.
 * explain:
 */
public class BoundFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "ucode";
    private static final String ARG_PARAM2 = "type";
    @Bind(R.id.et_phone_num)
    EditText mEtPhoneNum;
    @Bind(R.id.tv_forget_pwd)
    TextView mTvForgetPwd;
    @Bind(R.id.btn_submit)
    Button mBtnSubmit;
    @Bind(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    private String ucode = "";
    int type = Constants.WEI_XING;

    // TODO: Rename and change types and number of parameters
    public static BoundFragment newInstance(String ucode, int type) {
        BoundFragment fragment = new BoundFragment();
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
        _setLayoutRes(R.layout.layout_bound);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        mBtnSubmit.setOnClickListener(this);
        mTvForgetPwd.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BoundFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BoundFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    /**
     * 绑定登录
     */
    private void bindLogin() {

        String loginPassword = mEtLoginPwd.getText().toString();
        String loginPhone = mEtPhoneNum.getText().toString();
        /* 用户名为空 */
        if (TextUtils.isEmpty(loginPhone)) {
            ToastUtil.showShort(_mActivity, getString(R.string.login_user_empty));
            return;
        }
        /* 密码为空 */
        if (TextUtils.isEmpty(loginPassword)) {
            ToastUtil.showShort(_mActivity, getString(R.string.login_pw_empty));
            return;
        }

        Callback<ReturnValue<MessageBean>> callback = new Callback<ReturnValue<MessageBean>>() {
            @Override
            public void success(ReturnValue<MessageBean> returnValue, Response response) {
                //保存sessionID
                List<Header> headerList = response.getHeaders();
                for (Header header : headerList) {
                    Log.d(TAG, header.getName() + " " + header.getValue());
                    if (header.getName().equals(Constants.SET_COOKIE)) {
                        String phpSessId = header.getValue().split(";")[0];
                        _mApplication.getUserInfo().setSessionID(phpSessId);
                        break;
                    }
                }
                if (returnValue.getMsg().equals(Constants.SUCCESS)) {
                    // 登录成功，保存数据
                    _mApplication.getUserInfo().setIsLogin(Constants.IS_LOGIN);
                    startActivity(new Intent(_mActivity, MainActivity.class));
                } else {
                    ToastUtil.showShort(_mActivity, returnValue.getData().getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "failure: " + error);
                NetUtil.errorTip(error.getKind());

            }
        };

        _apiManager.getService().bindLogin(type, ucode, loginPhone, loginPassword, callback);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_pwd:
                startActivity(new Intent(_mActivity, RegisterActivity.class));
                _mActivity.finish();
                break;
            case R.id.btn_submit:
                bindLogin();
                break;
        }
    }
}
