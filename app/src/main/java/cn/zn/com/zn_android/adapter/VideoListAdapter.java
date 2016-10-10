package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.VideoBean;
import cn.zn.com.zn_android.utils.UIUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/4/5 0005.
 */
public class VideoListAdapter extends BaseAdapter {
    private Context mContext;

    private List<VideoBean> data;
    private VideoBean videoInfo;

    public VideoListAdapter(Context mContext, List<VideoBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public void setData(List<VideoBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = UIUtil.inflate(R.layout.item_video);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        videoInfo = data.get(position);
        holder.mTvTitle.setText(videoInfo.getTitle());
        if (videoInfo.getClick_num() != null) {
            holder.mTvPlayTimes.setText(String.format(mContext.getString(R.string.video_play_num), new Object[] {videoInfo.getClick_num()}));
            holder.mTvPlayTimes.setVisibility(View.VISIBLE);
        } else {
            holder.mTvPlayTimes.setVisibility(View.GONE);
        }
        holder.mTvInfo.setText(videoInfo.getRemark());
        if (null != videoInfo.getImage()) {
            holder.mSdvImg.setImageURI(Uri.parse(videoInfo.getImage()));
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.sdv_img)
        SimpleDraweeView mSdvImg;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_play_times)
        TextView mTvPlayTimes;
        @Bind(R.id.tv_info)
        TextView mTvInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
