package cn.zn.com.zn_android.model.factory;

import android.util.Log;

import cn.zn.com.zn_android.model.ChatGiftModel;
import cn.zn.com.zn_android.model.ChatMsgModel;
import cn.zn.com.zn_android.model.ChatTextModel;
import cn.zn.com.zn_android.model.bean.ChatMsgBean;
import cn.zn.com.zn_android.utils.JsonUtil;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息处理工厂类
 *
 * Created by Jolly on 2016/6/6 0006.
 */
public class MessageFactory {
    private static final String TAG = "MessageFactory";
    private MessageFactory() {}

    public static ChatMsgModel getMessage(MqttMessage msg) {
        String jsStr = new String(msg.getPayload());
        ChatMsgBean msgBean = null;
        try {
            msgBean = JsonUtil.fromJSON2Entity(jsStr, ChatMsgBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getMessage: ", e);
        }

        switch (msgBean.getType()) {
            case 1:
                return new ChatTextModel(msgBean);
            case 2:
                return new ChatGiftModel(msgBean);
            default:
                return null;
        }
    }

    public static List<ChatMsgModel> getMsgFromList(List<ChatMsgBean> msgList) {
        List<ChatMsgModel> msgModels = new ArrayList<>();

        for (ChatMsgBean msgBean : msgList) {
            switch (msgBean.getType()) {
                case 1:
                    msgModels.add(new ChatTextModel(msgBean)) ;
                    break;
                case 2:
                    msgModels.add(new ChatGiftModel(msgBean));
                    break;
            }
        }

        return msgModels;

    }
}
