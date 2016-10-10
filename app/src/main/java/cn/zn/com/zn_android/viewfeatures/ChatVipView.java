package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.model.bean.ChatMsgBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.requestType.ChatRequestType;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

/**
 * Created by Jolly on 2016/6/8 0008.
 */
public interface ChatVipView extends BaseView {

    // 显示消息
    void showMessage(MqttMessage msg);

    void showImData(List<ChatMsgBean> msgList);

    // 连接中断
    void connectionLost();

    //发送消息
    void sendMsgSucc(ReturnValue<MessageBean> returnValue);

    void sendMsg();

    //赠送礼物、
    void sendGift(String giftIndex, String giftNum);

    //显示排行榜
    void showRanking();

    //显示vip界面
    void showVip();

    //充值财富币页
    void showRecharge();

    //收藏页面
    void addCollectRoom();

    //返回请求成功
    void onSuccess(ChatRequestType type, Object object);

    //返回请求失败
//    void onError(Enum type, String code);
    void onError(ChatRequestType type, Object object);

    void sendPriConversion(String msg);

}
