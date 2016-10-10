package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.PrivateMsgBean;
import cn.zn.com.zn_android.utils.UIUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjs on 2016/4/1 0001.
 * explain: 悄悄话的适配器
 */
public class PrivateTalkAdapter extends BaseAdapter {
    private Context mContext;
    private List<?> list;
    private boolean isRoomVip = false;
    ViewHolder holder = null;
    private PrivateMsgBean msg;
    private String nickName;
    private String content;
    private String times;
    private final String USER = "1";
    private final String TEACHER = "2";
    private String identity;
    private SpannableString spanString;

    public void setList(List<?> list) {
        this.mContext = RnApplication.getInstance();
        this.list = list;
    }

    public PrivateTalkAdapter(List<?> list) {
        this.list = list;
        this.mContext = RnApplication.getInstance();
    }

    public void setRoomVip(boolean roomVip) {
        isRoomVip = roomVip;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
//        return 10;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = UIUtil.inflate(R.layout.item_private_talk);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        msg = (PrivateMsgBean) list.get(position);
        nickName = msg.getNickname();
        content = msg.getQacontent();
        times = msg.getTimes();
        /**
         * 根据身份不同设置颜色和字体显示
         */
        if (msg.getStatus().equals(TEACHER)) {
            identity = "老师";
            holder.mTvContent.setBackgroundColor(mContext.getResources().getColor(R.color.teacher_color));
        } else {
//            if (isRoomVip) {
                identity = "VIP会员";
//            } else {
//                identity = "小白";
//            }
            holder.mTvContent.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        String title = nickName + "【" + identity + "】";
        spanString = new SpannableString(title);
        spanString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, nickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (msg.getStatus().equals(TEACHER)) {
            spanString.setSpan(new ForegroundColorSpan(UIUtil.getResources().getColor(R.color.red)),
                    nickName.length(), spanString.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
//            if (isRoomVip) {
                spanString.setSpan(new ForegroundColorSpan(UIUtil.getResources().getColor(R.color.vip_color)),
                        nickName.length(), spanString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            } else {
//                spanString.setSpan(new ForegroundColorSpan(UIUtil.getResources().getColor(R.color.gray)),
//                        nickName.length(), spanString.length(),
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
        }
        holder.mTvTitle.setText(spanString);
        holder.mTvContent.setText(content);
        holder.mTvTime.setText("  "+msg.getTimes());
        return view;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_content)
        TextView mTvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
