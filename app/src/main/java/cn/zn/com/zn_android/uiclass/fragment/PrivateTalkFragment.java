package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.PrivateTalkAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.PrivateMsgBean;
import cn.zn.com.zn_android.model.bean.VipStateBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.MemberAreaActivity;
import cn.zn.com.zn_android.uiclass.activity.PrivateTalkActivity;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/3/30 0030.
 * explain:
 */
public class PrivateTalkFragment extends BaseFragment implements View.OnClickListener {

    private boolean isRoomVip = false;
    private static final String TAG = "PrivateTalkFragment";
    @Bind(R.id.tv_hint)
    TextView mTvHint;
    @Bind(R.id.lv_msg_list)
    ListView mLvMsgList;
    @Bind(R.id.btn_send_msg)
    TextView mBtnSendMsg;
    private List<PrivateMsgBean> mPrivateMsgList;
    private PrivateTalkAdapter mPrivateAdapter;
    private VipStateBean vipState;
    protected static String tid = "";

    // TODO: Rename and change types and number of parameters
    public static PrivateTalkFragment newInstance(String id) {
        PrivateTalkFragment fragment = new PrivateTalkFragment();
        tid = id;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _setLayoutRes(R.layout.fragment_private_talk);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 隐藏发送按钮和表情
     */
    private void hinitSendView() {
    }

    @Override
    protected void initView(View view) {
        initTips();
        mPrivateMsgList = new ArrayList<>();
        mPrivateAdapter = new PrivateTalkAdapter(mPrivateMsgList);
        mLvMsgList.setAdapter(mPrivateAdapter);
//        getData();
        hinitSendView();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PrivateTalkFragment"); //统计页面，"MainScreen"为页面名称，可自定义
        getData();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PrivateTalkFragment");
    }

    /**
     * 获取数据，设置数据
     */
    private void getData() {
        getMsgList();
        getVipState("");
    }

    private void getVipState(String tid) {
        _apiManager.getService().getVipState(_mApplication.getUserInfo().getSessionID(), tid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultVipState, Throwable -> {
                    Throwable.printStackTrace();
                });
//        AppObservable.bindFragment(this, _apiManager.getService().getVipState(_mApplication.getUserInfo().getSessionID(), tid))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultVipState, Throwable -> {
//                    Throwable.printStackTrace();
//                });
    }

    private void resultVipState(ReturnValue<VipStateBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            vipState = returnValue.getData();
            if (vipState.getIsroomvip().equals(Constants.IS_VIP)) {
                mTvHint.setVisibility(View.GONE);
                isRoomVip = true;
            } else {
                isRoomVip = false;
                mTvHint.setVisibility(View.VISIBLE);
            }
        } else {
            ToastUtil.showShort(_mContext, returnValue.getMsg());
        }
    }


    /**
     * 获取消息列表
     */
    private void getMsgList() {
        if (_mApplication.getUserInfo().getIsLogin() == 0) {
            return;
        }
        _apiManager.getService().getPrivateMsg(_mApplication.getUserInfo().getSessionID(), "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultPrivateMsg, Throwable -> {
                    Throwable.printStackTrace();
                });
//        AppObservable.bindFragment(this, _apiManager.getService().getPrivateMsg(_mApplication.getUserInfo().getSessionID(), ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultPrivateMsg, Throwable -> {
//                    Throwable.printStackTrace();
//                });
    }

    private void resultPrivateMsg(ReturnListValue<PrivateMsgBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            mPrivateMsgList = returnValue.getData();
            if (mPrivateMsgList != null) {
                mPrivateAdapter.setList(mPrivateMsgList);
                mPrivateAdapter.setRoomVip(isRoomVip);
                mPrivateAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtil.showShort(_mContext, returnValue.getMsg());
        }

    }


    /**
     * 初始化提示的字体
     */
    private void initTips() {
        SpannableString privateTalk = new SpannableString(getString(R.string.private_talk_tips));
        privateTalk.setSpan(new ForegroundColorSpan(Color.RED), 15, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privateTalk.setSpan(new ForegroundColorSpan(Color.RED), 19, privateTalk.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privateTalk.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(_mContext, 12)), 19, privateTalk.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privateTalk.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(_mContext, 15)), 15, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvHint.setText(privateTalk);
    }


    @Override
    protected void initEvent() {
        mBtnSendMsg.setOnClickListener(this);
        mTvHint.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_msg:
                Intent intent = new Intent(_mContext, PrivateTalkActivity.class);
                startActivity(intent);
                AnyEventType type = new AnyEventType(mPrivateMsgList);
                type.setState(isRoomVip).setTid(tid);
                EventBus.getDefault().postSticky(type);
                break;
            case R.id.tv_hint:
                //判断是否已经登陆,未登录跳转至登陆页面,否则跳转至会员介绍页面
                if (_mApplication.getUserInfo().getIsLogin() != Constants.IS_LOGIN) {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                } else {
                    startActivity(new Intent(_mContext, MemberAreaActivity.class));
                    EventBus.getDefault().postSticky(new AnyEventType(""));
                }
                break;
        }

    }
}
