package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ChatAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.ChatMsgModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ChatMsgBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.model.factory.MessageFactory;
import cn.zn.com.zn_android.presenter.ChatPresenter;
import cn.zn.com.zn_android.presenter.requestType.ChatRequestType;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.MemberAreaActivity;
import cn.zn.com.zn_android.uiclass.activity.RankingListActivity;
import cn.zn.com.zn_android.uiclass.activity.RechargeActivity;
import cn.zn.com.zn_android.uiclass.customerview.ChatInput;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import cn.zn.com.zn_android.viewfeatures.ChatView;
import cn.zn.com.zn_android.viewfeatures.TeacherChatView;
import com.umeng.analytics.MobclickAgent;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/6/3 0003.
 */
public class TeacherChatFragment extends BaseMVPFragment<ChatView, ChatPresenter> implements TeacherChatView {
    /**
     * 发送的消息类型
     * msg : 消息
     * gift：礼物
     */
    private static final String ARG_PARAM1 = "tid";
    private final String MSG = "1";
    private final String GIFT = "2";
    private static final String DEFAULT_TID = "9898";
    private final String DEFAULT_VIPFD = "";
    private String vipfd = DEFAULT_VIPFD;
    protected static String tid = DEFAULT_TID;
    private List<ChatMsgModel> msgList = new ArrayList<>();
    private List<String> qqlist = new ArrayList<>();
    private ChatAdapter mAdapter;
    private String currentType = MSG;

    @Bind(R.id.lv_chat)
    ListView mLvChat;
    @Bind(R.id.chat_input)
    ChatInput mChatInput;

    public TeacherChatView getChatVipView() {
        return this;
    }

    // TODO: Rename and change types and number of parameters
    public static TeacherChatFragment newInstance(String id) {
        TeacherChatFragment fragment = new TeacherChatFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, tid);
//        fragment.setArguments(args);
        tid = id;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            tid = getArguments().getString(ARG_PARAM1);
//        }
        presenter.queryImData("0");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroyView() {
        try {
            if (presenter != null) {
                presenter.unsubscribe(tid);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        msgList.clear();

        super.onDestroyView();
    }

    @Override
    public ChatPresenter initPresenter() {
        return new ChatPresenter(this, _mContext, tid);
    }

    @Override
    public int initResLayout() {
        return R.layout.fragment_chat_teacher;
    }

    @Override
    public void initView(View view) {
//        mChatInput = (ChatInput) view.findViewById(R.id.chat_input);
        mChatInput.setmChatView(this);
        mChatInput.set_activity(_mActivity);
        mChatInput.setContentView(mLvChat).initEmotionKeyBoard();

        presenter.queryQQByTid(tid);
        mAdapter = new ChatAdapter(_mContext, R.layout.item_chat, msgList);
        mLvChat.setAdapter(mAdapter);
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
    public void showImData(List<ChatMsgBean> msgList) {

        this.msgList.addAll(MessageFactory.getMsgFromList(msgList));
        mAdapter.notifyDataSetChanged();
        mLvChat.setSelection(mAdapter.getCount() - 1);

    }

    @Override
    public void showMessage(MqttMessage msg) {
        Log.d(TAG, "showMessage: " + msg.toString());

        msgList.add(MessageFactory.getMessage(msg));
        mAdapter.notifyDataSetChanged();
        mLvChat.setSelection(mAdapter.getCount() - 1);

    }


    @Override
    public void connectionLost() {
//        ToastUtil.showShort(_mContext, getString(R.string.no_net));

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
        EventBus.getDefault().postSticky(new AnyEventType(tid));
        startActivity(new Intent(_mActivity, RankingListActivity.class));
    }


    @Override
    public void showVip() {
        EventBus.getDefault().postSticky(new AnyEventType(tid));
        startActivity(new Intent(_mActivity, MemberAreaActivity.class));
    }

    //
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
    public void sendPriConversion(String msg) {
        presenter.postPrivateConversation(msg);
    }

    /**
     * 查询QQ客服列表，vip状态的回调
     *
     * @param type
     * @param object
     */
    @Override
    public void onSuccess(ChatRequestType type, Object object) {
        ReturnValue<MessageBean> returnValue;
        switch (type) {
            case CSQQ:
                qqlist = (ArrayList<String>) object;
                mChatInput.setQqList(qqlist); //设置QQ客服列表
                break;
            case IM_CONECT:
                try {
                    presenter.subscribe();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case FANS:
                returnValue = (ReturnValue<MessageBean>) object;
                if (returnValue.getMsg().equals(Constants.ERROR)) {
                    mChatInput.showFanDialog(returnValue.getData().getMessage());
                } else {
                    mChatInput.showFanDialog(getString(R.string.had_fan));
                }
                break;
            case PRI_CHAT:
                returnValue = (ReturnValue<MessageBean>) object;
                if (returnValue.getData().getMessage().equals(getString(R.string.please_login))) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else if (returnValue.getData().getMessage().equals(getString(R.string.vip_tips))) {
                    ToastUtil.showShort(getActivity(), returnValue.getData().getMessage());
                } else {
                    mChatInput.mEtMsg.setText("");
                    UIUtil.hidekeyBoard(mChatInput.mEtMsg);
                    ToastUtil.showShort(_mContext, returnValue.getData().getMessage());
                }
                break;
        }
    }

    /**
     * 接口调用错误的时候回调
     *
     * @param type
     * @param object
     */
    @Override
    public void onError(ChatRequestType type, Object object) {
        switch (type) {
            case CSQQ:
                break;
            case IM_DATA:
                try {
                    presenter.subscribe();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case FANS:
                break;
            case PRI_CHAT:
                break;
//            default:
//                break;
        }
    }

    @Override
    public void addCollectRoom() {
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

}
