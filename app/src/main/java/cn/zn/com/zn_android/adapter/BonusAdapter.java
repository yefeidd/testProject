package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.BonusBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class BonusAdapter extends ArrayAdapter<BonusBean> {
    private Context mContext;
    private int resId = R.layout.item_bonus;
    private List<BonusBean> data = new ArrayList<>();

    public BonusAdapter(Context context, int resource, List<BonusBean> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resId = resource;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(resId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTvYear.setText(data.get(position).getYear());
        holder.mTvRemark.setText(data.get(position).getRemark());
        holder.mTvDate.setText(data.get(position).getDate());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_year)
        TextView mTvYear;
        @Bind(R.id.tv_remark)
        TextView mTvRemark;
        @Bind(R.id.tv_date)
        TextView mTvDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
