package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ChatAdapter;
import cn.zn.com.zn_android.model.bean.ChatMsgBean;
import cn.zn.com.zn_android.utils.DateUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jolly on 2016/6/7 0007.
 */
public class ChatGiftModel extends ChatMsgModel {
    private final String TAG = getClass().getSimpleName();

    private ChatMsgBean msgBean;

    public ChatGiftModel(ChatMsgBean msgBean) {
        this.msgBean = msgBean;
    }

    @Override
    public void showChatMsg(ChatAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.mRlContext.setVisibility(View.GONE);
        viewHolder.mRlGift.setVisibility(View.VISIBLE);
        viewHolder.mTvRole.setText(msgBean.getLevel());
        viewHolder.mSdvAvatar.setImageURI(Uri.parse(msgBean.getAvatars()));
        viewHolder.mTvGiftTime.setText(DateUtils.dateStrToDateStr("yyyy-MM-dd HH:mm", "MM-dd HH:mm", msgBean.getTimes()));
        String name = msgBean.getNickname();
        String numStr = null;
        String giftID = null;
        if (null != msgBean.getGift()) {
            numStr = msgBean.getGift().getNum();
            giftID = msgBean.getGift().getIndex();
        } else {
            numStr = msgBean.getGiftnum();
            giftID = msgBean.getGiftid();
        }
        StringBuilder sb = new StringBuilder(name).append("送老师").append(numStr).append("个");
        SpannableString ss = new SpannableString(sb);
        ss.setSpan(
                new ForegroundColorSpan(context.getResources().getColor(R.color.app_bar_color)),
                name.length(), name.length() + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(
                new ForegroundColorSpan(context.getResources().getColor(R.color.app_bar_color)),
                name.length() + 3, ss.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        viewHolder.mTvGift.setText("");
        viewHolder.mTvGift.append(ss);

        SpannableString str = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open(String.format("gifts/ic_gift%d.png", Integer.valueOf(giftID)));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Matrix matrix = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            matrix.postScale(1.0f, 1.0f);
            final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    width, height, matrix, true);
            String content = String.valueOf(Integer.valueOf(giftID));
            str = new SpannableString(String.valueOf(Integer.valueOf(giftID)));
            ImageSpan span = new ImageSpan(context, resizedBitmap, ImageSpan.ALIGN_BASELINE);
            str.setSpan(span, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "showChatMsg: ", e);
        }
        viewHolder.mTvGift.append(str);
    }
}
