package cn.zn.com.zn_android.model;

import android.content.Context;

import cn.zn.com.zn_android.adapter.ChatAdapter;
import cn.zn.com.zn_android.model.bean.ChatMsgBean;

/**
 * 消息基础类
 * Created by Jolly on 2016/6/3 0003.
 */
public abstract class ChatMsgModel {
    private final String TAG = this.getClass().getSimpleName();

    ChatMsgBean msgBean;

    public ChatMsgBean getMsgBean() {
        return msgBean;
    }

    public abstract void showChatMsg(ChatAdapter.ViewHolder viewHolder, Context context);

}
