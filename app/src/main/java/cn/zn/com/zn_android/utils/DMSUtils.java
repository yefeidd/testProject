package cn.zn.com.zn_android.utils;

import android.content.Context;
import android.util.Log;

import com.aodianyun.dms.android.DMS;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.presenter.requestType.ChatRequestType;
import cn.zn.com.zn_android.viewfeatures.ChatView;
import cn.zn.com.zn_android.viewfeatures.ChatVipView;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Jolly on 2016/6/13 0013.
 */
public class DMSUtils {
    private final String TAG = "DMSUtils";

    private Context mContext;
    private ChatVipView mChatVipView;
    private ChatView mChatView;

    public DMSUtils(Context mContext, ChatVipView mChatVipView, ChatView mChatView) {
        this.mContext = mContext;
        this.mChatVipView = mChatVipView;
        this.mChatView = mChatView;
    }

    /**
     * 使用DMS的pubkey 和subkey 进行初始化
     */
    public void initDMS(String tid) {
        //使用DMS的pubkey 和subkey 进行初始化
        DMS.init(mContext, Constants.PUB_KEY, Constants.SUB_KEY, new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage msg) throws Exception {
                //收到话题消息
                Log.e(TAG, "messageArrived: " + msg.toString());
                if (topic.contains("vip")) {
                    if (null != mChatVipView) {
                        mChatVipView.showMessage(msg);
                    }
                } else if (topic.equals(Constants.TOPIC_HEAD + tid)){
                    if (null != mChatView) {
                        mChatView.showMessage(msg);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //传送完成
                Log.e(TAG, "deliveryComplete: ");
            }

            @Override
            public void connectionLost(Throwable t) {
                //连接中断
                Log.e(TAG, "connectionLost: ", t);
            }
        });
    }

    public void disconnect() throws MqttException {
        DMS.disconnect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
//                userInfo.setConnected(false);
                Log.d(TAG, "onSuccess: disconnect");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, "onFailure: ", exception);
            }
        });
    }

    /**
     * 连接到DMS服务器，连接成功后才能关注和推送消息
     * @throws MqttException
     */
    public void connect() throws MqttException {
        // 连接到DMS服务器，连接成功后才能关注和推送消息
        DMS.connect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                if (null != mChatView) {
                    mChatView.onSuccess(ChatRequestType.IM_CONECT, asyncActionToken);
                }

                if (null != mChatVipView) {
                    mChatVipView.onSuccess(ChatRequestType.IM_CONECT, asyncActionToken);
                }

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, "onFailure: ", exception);
            }
        });
    }
}
