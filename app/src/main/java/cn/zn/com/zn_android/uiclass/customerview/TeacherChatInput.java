package cn.zn.com.zn_android.uiclass.customerview;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.utils.LogUtils;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.ChatView;
import cn.zn.com.zn_android.viewfeatures.TeacherChatView;

/**
 * Created by Jolly on 2016/6/12 0012.
 */
public class TeacherChatInput extends ChatInput {
    private TeacherChatView mTeacherChatView;

    public TeacherChatInput(Context context) {
        this(context, null);
    }

    public TeacherChatInput(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TeacherChatInput(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void initView() {
        super.initView();
        mIbFen.setBackgroundResource(R.drawable.private_talk);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_fan) {
            showSendPrivateTalkDialog();
        } else {
            super.onClick(v);
        }
    }

    @Override
    public void setmChatView(ChatView mChatView) {
        super.setmChatView(mChatView);
        mTeacherChatView = (TeacherChatView) mChatView;
    }

    /**
     * 发送悄悄话的dialog
     */
    Dialog privateTalkDialog = null;

    private void showSendPrivateTalkDialog() {
        CusDownUpDialog.Builder builder = new CusDownUpDialog.Builder(_activity);
        View layout = LayoutInflater.from(_activity).inflate(R.layout.layout_send_private_talk, new ListView(_activity), false);
        EditText priMsg = (EditText) layout.findViewById(R.id.et_private_msg);
        TextView msgSend = (TextView) layout.findViewById(R.id.tv_send_msg);
        msgSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = priMsg.getText().toString();
                if (StringUtil.isEmpty(msg)) {
                    ToastUtil.showShort(_activity, _activity.getString(R.string.msg_not_empty));
                    return;
                }
                mTeacherChatView.sendPriConversion(msg);
                privateTalkDialog.dismiss();
                LogUtils.i("发送消息");
            }
        });
        builder.setLayoutView(layout);
        builder.setCancelable(true);
        privateTalkDialog = builder.create();
        privateTalkDialog.show();
    }
}
