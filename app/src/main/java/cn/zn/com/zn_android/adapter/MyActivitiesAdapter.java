package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.ActivityBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/7/5 0005.
 */
public class MyActivitiesAdapter extends ArrayAdapter<ActivityBean> {
    private List<ActivityBean> list = new ArrayList<>();
    private Context mContext;
    private int resId = R.layout.item_my_activities;

    public MyActivitiesAdapter(Context context, int resource, List<ActivityBean> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resId = resource;
        this.list = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(resId, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ActivityBean bean = list.get(position);
        viewHolder.mTvName.setText(bean.getName());
        viewHolder.mTvEndTime.setText(new StringBuilder("结束时间：").append(bean.getEndTime()));
        viewHolder.mTvStatus.setText(bean.getStatus());
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.sdv_img)
        SimpleDraweeView mSdvImg;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_end_time)
        TextView mTvEndTime;
        @Bind(R.id.tv_status)
        TextView mTvStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
