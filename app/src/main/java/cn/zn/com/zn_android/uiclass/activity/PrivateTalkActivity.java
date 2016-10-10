package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.PrivateTalkAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.PrivateMsgBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.uiclass.customerview.InterceptSwpRefLayout;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/3/30 0030.
 * explain:
 */
public class PrivateTalkActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_history)
    ImageButton mIbHistory;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_refresh_tips)
    TextView mTvRefreshTips;
    @Bind(R.id.ll_refresh_tips)
    LinearLayout mLlRefreshTips;
    @Bind(R.id.lv_private_talk)
    ListView mLvPrivateTalk;
    @Bind(R.id.isr_refresh)
    InterceptSwpRefLayout mIsrRefresh;
    @Bind(R.id.et_msg)
    EditText mEtMsg;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    @Bind(R.id.ll_send)
    LinearLayout mLlSend;
    @Bind(R.id.ib_clean)
    ImageButton mIbClean;
    @Bind(R.id.ibn_talk_history)
    ImageButton mIbnTalkHistory;
    @Bind(R.id.rl_second_button)
    RelativeLayout mRlSecondButton;
    @Bind(R.id.rl_btns)
    RelativeLayout mRlBtns;
    @Bind(R.id.rl_data)
    RelativeLayout mRlData;
    private ArrayList<?> mPrivateMsgList;
    private boolean isRoomVip;
    private PrivateTalkAdapter mPrivateAdapter;
    private String msg;
    private String tid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        _setLayoutRes(R.layout.activity_private_talk);
        UIUtil.hidekeyBoard(mEtMsg);
        super.onCreate(savedInstanceState);
    }

    public void onEventMainThread(AnyEventType event) {
        mPrivateMsgList = (ArrayList<?>) event.getObject();
        isRoomVip = event.getState();
        tid = event.getTid();
    }

    @Override
    protected void initView() {
        if (mPrivateMsgList == null || mPrivateMsgList.size() == 0) {
            mLlRefreshTips.setVisibility(View.VISIBLE);
            mLvPrivateTalk.setVisibility(View.GONE);
        } else {
            mLlRefreshTips.setVisibility(View.GONE);
            mLvPrivateTalk.setVisibility(View.VISIBLE);
        }
        mPrivateAdapter = new PrivateTalkAdapter(mPrivateMsgList);
        mPrivateAdapter.setRoomVip(isRoomVip);
        mLvPrivateTalk.setAdapter(mPrivateAdapter);
        mEtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mTvSend.setBackgroundResource(R.drawable.sp_rect_orange);
                } else {
                    mTvSend.setBackgroundResource(R.drawable.sp_rect_corner_grey);
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        mIbnTalkHistory.setOnClickListener(this);
        mIvLeftmenu.setOnClickListener(this);
        mIsrRefresh.setOnRefreshListener(this);
        mIbClean.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
        //设置tips的点击事件和颜色
//        setTipsColor();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("PrivateTalkActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        mToolbarTitle.setText(getString(R.string.member_private_msg));
        setData();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PrivateTalkActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    /**
     * 为页面设置数据
     */
    private void setData() {
        mPrivateAdapter.setRoomVip(isRoomVip);
        mPrivateAdapter.setList(mPrivateMsgList);
        mPrivateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibn_talk_history:
                if (_mApplication.getUserInfo().getIsLogin() == Constants.IS_LOGIN) {
                    Intent intent = new Intent(this, HistoryPrivateActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.showShort(this, getString(R.string.please_login));
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.ib_clean:
                mPrivateMsgList.clear();
                mPrivateAdapter.setList(mPrivateMsgList);
                mPrivateAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_send:
                msg = mEtMsg.getText().toString();
                if (StringUtil.isEmpty(msg)) {
                    ToastUtil.showShort(this, getString(R.string.msg_not_empty));
                    return;
                }
                postMsg(msg);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 下拉刷新的回调接口
     */
    @Override
    public void onRefresh() {
        getMsgList();
    }


    /**
     * 获取消息列表
     */
    private void getMsgList() {
        AppObservable.bindActivity(this, _apiManager.getService().getPrivateMsg(_mApplication.getUserInfo().getSessionID(), ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultPrivateMsg, Throwable -> {
                    Throwable.printStackTrace();
                    showTips();
                });
    }

    private void resultPrivateMsg(ReturnListValue<PrivateMsgBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            if (mPrivateMsgList != null) mPrivateMsgList.clear();
            mPrivateMsgList = (ArrayList<?>) returnValue.getData();
            showTips();
        } else {
            ToastUtil.showShort(this, returnValue.getMsg());
            showTips();
        }

    }


    /**
     * 发送悄悄话
     */
    private void postMsg(String msg) {
        AppObservable.bindActivity(this, _apiManager.getService().postPrivateMsg(_mApplication.getUserInfo().getSessionID(), msg, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultMessage, Throwable -> {
                    Throwable.printStackTrace();
                });
    }

    private void resultMessage(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null) {
            if (returnValue.getData().getMessage().equals(getString(R.string.please_login))) {
                startActivity(new Intent(this, LoginActivity.class));
            } else if (returnValue.getData().getMessage().equals(getString(R.string.vip_tips))) {
                ToastUtil.showShort(this, returnValue.getData().getMessage());
                EventBus.getDefault().postSticky(new AnyEventType(tid));
                startActivity(new Intent(this, MemberAreaActivity.class));
            } else {
                mEtMsg.setText("");
                UIUtil.hidekeyBoard(mEtMsg);
                ToastUtil.showShort(this, returnValue.getData().getMessage());
            }
        }
    }


    /**
     *
     */

    /**
     * 是否显示提示
     */
    public void showTips() {
        if (mPrivateMsgList == null || mPrivateMsgList.size() == 0) {
            mIsrRefresh.setRefreshing(false);
            mLlRefreshTips.setVisibility(View.VISIBLE);
            mLvPrivateTalk.setVisibility(View.GONE);
        } else {
            mIsrRefresh.setRefreshing(false);
            mLlRefreshTips.setVisibility(View.GONE);
            mLvPrivateTalk.setVisibility(View.VISIBLE);
            setData();
        }
    }

}
