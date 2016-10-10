package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.TacticsBean;
import cn.zn.com.zn_android.utils.UIUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/4/7 0007.
 */
public class TacticsAdapter extends BaseAdapter {
    private Context mContext;
    private List<TacticsBean> data;

    public TacticsAdapter(Context mContext, List<TacticsBean> data) {
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
            convertView = UIUtil.inflate(R.layout.item_my_tactics);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvTitle.setText(data.get(position).getTitle());
        if (data.get(position).getNickname() != null && !data.get(position).getNickname().equals("")) {
            holder.mTvTeacher.setText(data.get(position).getNickname());
            holder.mTvBuy.setVisibility(View.GONE);
        } else {
            holder.mTvTeacher.setText(data.get(position).getContent());
            holder.mTvBuy.setVisibility(View.VISIBLE);
        }
        if (data.get(position).getTimes() == null) {
            holder.mTvTime.setVisibility(View.GONE);
        } else if (data.get(position).getTimes().length() > 10) {
            holder.mTvTime.setVisibility(View.VISIBLE);
            holder.mTvTime.setText(data.get(position).getTimes().substring(0, 10));
        } else {
            holder.mTvTime.setVisibility(View.VISIBLE);
            holder.mTvTime.setText(data.get(position).getTimes());
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_teacher)
        TextView mTvTeacher;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_buy)
        TextView mTvBuy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
