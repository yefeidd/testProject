package cn.zn.com.zn_android.model;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ChatAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.ChatMsgBean;
import cn.zn.com.zn_android.utils.DateUtils;
import cn.zn.com.zn_android.utils.StringUtil;

/**
 * 文本消息类
 * Created by Jolly on 2016/6/6 0006.
 */
public class ChatTextModel extends ChatMsgModel {

    private ChatMsgBean msgBean;

    public ChatTextModel(ChatMsgBean msgBean) {
        this.msgBean = msgBean;
    }

    @Override
    public void showChatMsg(ChatAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.mRlGift.setVisibility(View.GONE);
        viewHolder.mRlContext.setVisibility(View.VISIBLE);
        viewHolder.mTvName.setText(msgBean.getNickname());
        viewHolder.mTvRole.setText(msgBean.getLevel());
        viewHolder.mTvTime.setText(DateUtils.dateStrToDateStr("yyyy-MM-dd HH:mm", "MM-dd HH:mm", msgBean.getTimes()));
        if (null != msgBean.getMsg()) {
            viewHolder.mTvContent.setText(StringUtil.handlerReceiveMsg(viewHolder.mTvContent,
                    StringUtil.escapeString(msgBean.getMsg()), context));
        } else {
            viewHolder.mTvContent.setText(StringUtil.handlerReceiveMsg(viewHolder.mTvContent,
                    StringUtil.escapeString(msgBean.getContent()), context));
        }
        if (msgBean.getLevel().equals(Constants.ROLE_DEFAULT)) {
            viewHolder.mTvContent.setBackgroundColor(Color.TRANSPARENT);
        } else {
            viewHolder.mTvContent.setBackgroundResource(R.drawable.bg_chat_msg);
        }
        viewHolder.mSdvAvatar.setImageURI(Uri.parse(msgBean.getAvatars()));
    }
}
