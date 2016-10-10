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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的收藏 视频 适配
 * Created by Jolly on 2016/4/6 0006.
 */
public class MyCollectVideoAdapter extends BaseAdapter {
    private Context mContext;
    private List<VideoBean> data = new ArrayList<>();

    public MyCollectVideoAdapter(Context mContext, List<VideoBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = UIUtil.inflate(R.layout.item_grid_video);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mSdvVideoImg.setImageURI(Uri.parse(data.get(position).getImage()));
        holder.mTvVideoTitle.setText(data.get(position).getTitle());

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.sdv_video_img)
        SimpleDraweeView mSdvVideoImg;
        @Bind(R.id.tv_video_title)
        TextView mTvVideoTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setData(List<VideoBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
