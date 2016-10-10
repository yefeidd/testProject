package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ChatAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.ChatMsgModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ChatMsgBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.VipStateBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.model.factory.MessageFactory;
import cn.zn.com.zn_android.presenter.ChatVipPresenter;
import cn.zn.com.zn_android.presenter.requestType.ChatRequestType;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.MemberAreaActivity;
import cn.zn.com.zn_android.uiclass.activity.RechargeActivity;
import cn.zn.com.zn_android.uiclass.customerview.ChatInput;
import cn.zn.com.zn_android.uiclass.customerview.ChatVIPInput;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.ChatVipView;
import com.umeng.analytics.MobclickAgent;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/6/8 0008.
 */
public class ChatVIPFragment extends BaseMVPFragment<ChatVipView, ChatVipPresenter> implements ChatVipView, View.OnClickListener {

    /**
     * 发送的消息类型
     * msg : 消息
     * gift：礼物
     */
    private static final String ARG_PARAM1 = "tid";
    private static final String ARG_PARAM2 = "msgUrl";
    private final String MSG = "1";
    private final String GIFT = "2";
    private static final String DEFAULT_TID = "9898";
    private final String DEFAULT_VIPFD = "1";

    @Bind(R.id.tv_tip_2)
    TextView mTvTip2;
    @Bind(R.id.tv_tip_3)
    TextView mTvTip3;
    @Bind(R.id.tv_join_vip)
    TextView mTvJoinVip;
    @Bind(R.id.rl_is_not_vip)
    RelativeLayout mRlIsNotVip;
    @Bind(R.id.chat_input)
    ChatVIPInput mChatInput;
    @Bind(R.id.ll_is_vip)
    LinearLayout mLlIsVip;
    @Bind(R.id.lv_chat)
    ListView mLvChat;

    private String vipfd = DEFAULT_VIPFD;
    protected static String tid = DEFAULT_TID;
    private boolean isRoomVip;//Vip状态

    private List<ChatMsgModel> msgList = new ArrayList<>();
    private ChatAdapter mAdapter;
    private String currentType = MSG;
    private VipStateBean vipState;

    public ChatVipView getChatVipView() {
        return this;
    }

    public void setVipState(boolean isRoomVip) {
        this.isRoomVip = isRoomVip;
        setVisible();
    }

    /**
     * 设置显示vip还是非vip的界面
     */
    public void setVisible() {
        if (isRoomVip) {
            mRlIsNotVip.setVisibility(View.GONE);
            mLlIsVip.setVisibility(View.VISIBLE);
        } else {
            mRlIsNotVip.setVisibility(View.VISIBLE);
            mLlIsVip.setVisibility(View.GONE);
        }
    }

    /**
     * 设置非vip页面的提示字体颜色显示
     */
    public void setNotVipTextColor() {
        SpannableString spannableString2 = new SpannableString(getString(R.string.tips_2));
        spannableString2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 4, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spannableString3 = new SpannableString(getString(R.string.tips_3));
        spannableString3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 6, spannableString3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvTip2.setText(spannableString2);
        mTvTip3.setText(spannableString3);
    }

    // TODO: Rename and change types and number of parameters
    public static ChatVIPFragment newInstance(String id) {
        ChatVIPFragment fragment = new ChatVIPFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, tid);
//        fragment.setArguments(args);
        tid = id;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        presenter.setTi(this);
        presenter.queryImData("1");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
        presenter.getVipState(tid);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroyView() {
        try {
            if (presenter != null) {
                presenter.unsubscribe();
//                if (_mApplication.getUserInfo().isConnected()) {
//                    _mApplication.disconnect();
//
//                }
//                presenter.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        msgList.clear();

        super.onDestroyView();
    }

    @Override
    public void initView(View view) {
        mChatInput.setmChatView(this);
        mChatInput.set_activity(_mActivity);
        mChatInput.setContentView(mLvChat).initEmotionKeyBoard();

        mAdapter = new ChatAdapter(_mContext, R.layout.item_chat, msgList);
        mLvChat.setAdapter(mAdapter);
        mChatInput.setmChatView(this);
        mChatInput.set_activity(getActivity());
//        mChatInput.setContentView(mLlContentView).initEmotionKeyBoard();

        mChatInput.hideCsBtn();
        setNotVipTextColor();
        mLvChat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        mLvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mChatInput.updateView(ChatInput.InputMode.NONE);
                        break;
                }
                return false;
            }
        });
        getActivity().registerForContextMenu(mLvChat);
    }

    @Override
    protected void initEvent() {
        mTvJoinVip.setOnClickListener(this);
    }

    @Override
    public ChatVipPresenter initPresenter() {
        return new ChatVipPresenter(this, _mContext, tid);
    }

    @Override
    public int initResLayout() {
        return R.layout.fragment_chat_vip;
    }

    @Override
    public void showMessage(MqttMessage msg) {
        msgList.add(MessageFactory.getMessage(msg));
        mAdapter.notifyDataSetChanged();
        mLvChat.setSelection(mAdapter.getCount() - 1);
    }

    @Override
    public void showImData(List<ChatMsgBean> msgList) {
        this.msgList.addAll(MessageFactory.getMsgFromList(msgList));
        mAdapter.notifyDataSetChanged();
        mLvChat.setSelection(mAdapter.getCount() - 1);

//        connect();
    }

    @Override
    public void connectionLost() {

    }

    @Override
    public void sendMsgSucc(ReturnValue<MessageBean> returnValue) {
        if (returnValue.getMsg().equals(Constants.SUCCESS)) {
            if (currentType.equals(MSG)) {
                mChatInput.clearEdit();
                mChatInput.hideKeyBoard();
            }
        } else {
            ToastUtil.showShort(_mContext, returnValue.getData().getMessage());
            if (currentType.equals(MSG)) {
                mChatInput.hideKeyBoard();
            }
            if (_mContext.getResources().getString(R.string.please_login).equals(returnValue.getData().getMessage())) {
                _mApplication.getUserInfo().setIsLogin(Constants.NOT_LOGIN);
                startActivity(new Intent(_mContext, LoginActivity.class));
            }
        }


    }

    @Override
    public void sendMsg() {
        currentType = MSG;
        presenter.sendMsg(_mApplication.getUserInfo().getSessionID(), MSG, mChatInput.getMsg().toString(), "", "");


    }

    @Override
    public void sendGift(String giftIndex, String giftNum) {
        currentType = GIFT;
        presenter.sendMsg(_mApplication.getUserInfo().getSessionID(), GIFT, "", giftIndex, giftNum);


    }

    @Override
    public void showRanking() {

    }

    @Override
    public void showVip() {
        EventBus.getDefault().

                postSticky(new AnyEventType(tid)

                );

        startActivity(new Intent(_mActivity, MemberAreaActivity.class)

        );


    }

    @Override
    public void showRecharge() {
        if (_mApplication.getUserInfo().getIsLogin() != Constants.IS_LOGIN) {
            ToastUtil.showShort(_mActivity, getString(R.string.please_login));
            startActivity(new Intent(_mActivity, LoginActivity.class));
            return;
        }

        EventBus.getDefault().postSticky(new AnyEventType(tid));
        startActivity(new Intent(_mActivity, RechargeActivity.class));
    }

    @Override
    public void addCollectRoom() {

    }

    @Override
    public void onSuccess(ChatRequestType type, Object object) {
        switch (type) {
            case IM_CONECT:
                try {
                    presenter.subscribe();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case IS_VIP:
                vipState = ((ReturnValue<VipStateBean>) object).getData();
                if (vipState.getIsroomvip().equals(Constants.IS_VIP)) {
                    isRoomVip = true;
                } else {
                    isRoomVip = false;
                }
                setVipState(isRoomVip);
                break;
            case PRI_CHAT:
                MessageBean msgBean = (MessageBean) object;
                ToastUtil.showShort(_mContext, msgBean.getMessage());
                break;
//            default:
//                break;
        }
    }

    @Override
    public void onError(ChatRequestType type, Object object) {
        switch (type) {
            case IM_DATA:
                try {
                    presenter.subscribe();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
//            case :
//                break;
//            default:
//                break;

        }
    }

    @Override
    public void sendPriConversion(String msg) {
        presenter.postPrivateConversation(msg);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_join_vip:
                startActivity(new Intent(getActivity(), MemberAreaActivity.class));
                EventBus.getDefault().postSticky(new AnyEventType(tid));
                break;
        }
    }

}
