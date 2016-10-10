package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aodianyun.dms.android.DMS;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.ChatMsgBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.VipStateBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.requestType.ChatRequestType;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.utils.LogUtils;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.ChatVipView;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/6/8 0008.
 */
public class ChatVipPresenter extends BasePresenter<ChatVipView> {
    private final String TAG = this.getClass().getSimpleName();

    private RnApplication _mApplication = RnApplication.getInstance();
    private ChatVipView mChatView;
    private Context mContext;
    private Activity _activity;
    private String tid = Constants.DEFAULT_TID;
    private ApiManager _apiManager;
    private String topic = Constants.TOPIC_HEAD + tid + "vip";

    public void setTid(String tid) {
        this.tid = tid;
        topic = Constants.TOPIC_HEAD + tid + "vip";
    }

    public ChatVipPresenter(ChatVipView chatView, Context context, String tid) {
        this.mChatView = chatView;
        this.mContext = context;
        this._apiManager = ApiManager.getInstance();
        this._activity = (Activity) context;
        this.tid = tid;
        topic = Constants.TOPIC_HEAD + tid + "vip";
    }

    public void publish(String topic, byte[] payload) throws MqttException, MqttPersistenceException {
        //推送消息到对应话题
        DMS.publish(topic, payload, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }

    public void subscribe() throws MqttException {
        //关注某个话题的消息和批量关注
        //将从init设置的callback中收到pubish到这个话题的消息
        DMS.subscribe(topic, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.e(TAG, "onSuccess: "  );

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, "onFailure: ", exception);
            }
        });
    }

    public void unsubscribe() throws MqttException  {
        // 取消关注的话题，将不会收到该话题的消息
        DMS.unsubscribe(topic, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.e(TAG, "onSuccess: unsubscribe");

            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, "onFailure: unsubscribe", exception);
            }
        });
    }

    /**
     * 发送消息到服务器进行推送
     * vipfd(1:VIP聊天,0:普通[默认0，特约讲堂此参数为空])
     */
    public void sendMsg(String sessionID, String type, String msg, String giftid, String giftnum) {
        String vipfd = "";
        if (tid.equals(Constants.DEFAULT_TID)) {
            vipfd = "";
        } else {
            vipfd = (topic.contains("vip")) ? "1" : "0";
        }

        AppObservable.bindActivity(_activity, _apiManager.getService().sendMsg(sessionID, type, tid, StringUtil.handlerSendMsg(msg), vipfd, giftid, giftnum, Constants.ANDROID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendMsgResult, throwable -> {
                    Log.e(TAG, mContext.getString(R.string.register_fail));
                    ToastUtil.showShort(mContext, mContext.getString(R.string.register_fail));
                });
    }

    /**
     * 返回发送结果
     */
    private void sendMsgResult(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null) {
            mChatView.sendMsgSucc(returnValue);
        }
    }

    /**
     * 获取房间历史聊天记录
     * @param isvip 0:普通房间1:VIP房间,默认0
     */
    public void queryImData(String isvip) {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryImData(tid, isvip))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::imDataResult, throwable -> {
                    Log.e(TAG, "queryImData", throwable);
                    ToastUtil.showShort(mContext, mContext.getString(R.string.no_net));
                    mChatView.onError(ChatRequestType.IM_DATA, throwable);
                });
    }


    private void imDataResult(ReturnListValue<ChatMsgBean> returnValue) {
        if (null != returnValue) {
            mChatView.showImData(returnValue.getData());
        }
    }

    /**
     * 发送悄悄话
     */
    public void postPrivateConversation(String msg) {
        if (_mApplication.getUserInfo().getIsLogin() != Constants.IS_LOGIN) {
            _activity.startActivity(new Intent(_activity, LoginActivity.class));
            return;
        }
        AppObservable.bindActivity(_activity, _apiManager.getService().postPrivateMsg(_mApplication.getUserInfo().getSessionID(), msg, tid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultMessage, Throwable -> {
                    mChatView.onError(ChatRequestType.PRI_CHAT, Throwable);
                });
    }
    private void resultMessage(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null) {
            mChatView.onSuccess(ChatRequestType.PRI_CHAT, returnValue.getData());
        } else {
            mChatView.onError(ChatRequestType.PRI_CHAT, "error");
        }
    }

    /**
     * 获取VIP状态
     *
     * @param tid
     */
    public void getVipState(String tid) {
        AppObservable.bindActivity(_activity, _apiManager.getService().getVipState(_mApplication.getUserInfo().getSessionID(), tid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultVipState, Throwable -> {
                    Throwable.printStackTrace();
                });
    }

    private void resultVipState(ReturnValue<VipStateBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
//            vipState = returnValue.getData();
//            if (vipState.getIsroomvip().equals(Constants.IS_VIP)) {
//                isRoomVip = true;
//            } else {
//                isRoomVip = false;
//            }
            mChatView.onSuccess(ChatRequestType.IS_VIP, returnValue);
        } else {
            LogUtils.i(returnValue.getMsg());
//            mChatView.onError(ChatRequestType.IS_VIP, returnValue);
        }
    }

}