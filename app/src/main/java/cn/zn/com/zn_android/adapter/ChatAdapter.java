package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.ChatMsgModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/6/3 0003.
 */
public class ChatAdapter extends ArrayAdapter<ChatMsgModel> {

    private int resId = R.layout.item_chat;
    private ViewHolder viewHolder;
    private Context mContext;

    public ChatAdapter(Context context, int resource, List<ChatMsgModel> objects) {
        super(context, resource, objects);
        this.resId = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
//            Log.e("ChatAdapter", "convertView: " + convertView);
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
//            String str = viewHolder.mTvContent.getText().toString();
//            Log.e("getview", "getView: " + str);
        } else {
//            Log.e("ChatAdapter", "resId: " + resId);
            view = LayoutInflater.from(mContext).inflate(resId, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final ChatMsgModel msgModel = getItem(position);
        msgModel.showChatMsg(viewHolder, mContext);

        return view;
    }

    public class ViewHolder {
        public ProgressBar sending;

        @Bind(R.id.tv_role)
        public TextView mTvRole;
        @Bind(R.id.tv_name)
        public TextView mTvName;
        @Bind(R.id.tv_time)
        public TextView mTvTime;
        @Bind(R.id.tv_content)
        public TextView mTvContent;
        @Bind(R.id.rl_context)
        public RelativeLayout mRlContext;
        @Bind(R.id.tv_gift_time)
        public TextView mTvGiftTime;
        @Bind(R.id.tv_gift)
        public TextView mTvGift;
        @Bind(R.id.rl_gift)
        public RelativeLayout mRlGift;
        @Bind(R.id.sdv_avatar)
        public SimpleDraweeView mSdvAvatar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
