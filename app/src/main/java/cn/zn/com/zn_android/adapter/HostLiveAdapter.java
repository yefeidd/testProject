package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.uiclass.activity.TeacherLiveActivity;
import cn.zn.com.zn_android.utils.UIUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/3/31 0031.
 * explain:this is a list adapter for the hot live
 */
public class HostLiveAdapter extends BaseAdapter {

    private List<?> hostLiveList;
    private Context mContext;


    public void setHostLiveList(List<?> hostLiveList) {
        this.hostLiveList = hostLiveList;
        notifyDataSetChanged();
    }

    public HostLiveAdapter(List<?> hostLiveList, Context context) {
        this.hostLiveList = hostLiveList;
        mContext = context;
    }

    ViewHolder holder = null;

    @Override
    public int getCount() {

        if (hostLiveList != null) {
            return hostLiveList.size();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = UIUtil.inflate(R.layout.item_hot_live);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HotLiveBean hotlive = (HotLiveBean) hostLiveList.get(position);
        if (hotlive.getAvatars()!="") {
            holder.mIvHeadAvatars.setImageURI(Uri.parse(hotlive.getAvatars()));
        }
        holder.mTvNameTitle.setText(hotlive.getTitle());
        String roomNumber = mContext.getString(R.string.room_number) + hotlive.getRoom_number();
        holder.mTvRoomNumber.setText(roomNumber);
        String operation = mContext.getString(R.string.room_operation);
        if (null == hotlive.getOperation()) {
            operation = operation + "无";
        } else {
            operation = operation + hotlive.getOperation();
        }
        holder.mTvOperation.setText(operation);
        String click = mContext.getString(R.string.room_click) + hotlive.getClick();
        holder.mTvClick.setText(click);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, TeacherLiveActivity.class));
                EventBus.getDefault().postSticky(new AnyEventType(hotlive));
            }
        });
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


    class ViewHolder {
        @Bind(R.id.iv_head_avatars)
        ImageView mIvHeadAvatars;
        @Bind(R.id.tv_name_title)
        TextView mTvNameTitle;
        @Bind(R.id.tv_room_number)
        TextView mTvRoomNumber;
        @Bind(R.id.tv_operation)
        TextView mTvOperation;
        @Bind(R.id.tv_click)
        TextView mTvClick;
        @Bind(R.id.tv_room_enter)
        TextView mBtnHomeEnter;
        @Bind(R.id.ll_hot_tv)
        LinearLayout mLlHotTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
