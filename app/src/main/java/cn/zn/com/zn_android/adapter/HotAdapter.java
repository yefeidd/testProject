package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.HotConceptBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/8/18 0018.
 */
public class HotAdapter extends ArrayAdapter<HotConceptBean> {
    private Context mContext;
    private List<HotConceptBean> data = new ArrayList<>();
    private int resId = R.layout.item_hot_concept;

    public HotAdapter(Context context, int resource, List<HotConceptBean> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.data.addAll(objects);
        this.resId = resource;
    }

    public void setData(List<HotConceptBean> beanList) {
        data.clear();
        data.addAll(beanList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public HotConceptBean getItem(int position) {
        return data.get(position);
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

        HotConceptBean bean = data.get(position);
        holder.mTvIndustryName.setText(bean.getHyname());
        if (bean.getUpdownrate().startsWith("-")) {
            holder.mTvUpDown.setTextColor(mContext.getResources().getColor(R.color.green_down));
            holder.mTvUpDown.setText(bean.getUpdownrate() + "%");
        } else if (Float.valueOf(bean.getUpdownrate()) == 0) {
            holder.mTvUpDown.setTextColor(mContext.getResources().getColor(R.color.font_value));
            holder.mTvUpDown.setText(bean.getUpdownrate() + " %");
        } else {
            holder.mTvUpDown.setTextColor(mContext.getResources().getColor(R.color.app_bar_color));
            holder.mTvUpDown.setText("+" + bean.getUpdownrate() + "%");
        }
        holder.mTvName.setText(bean.getTic_name());
        return convertView;
    }

    public class ViewHolder {
        @Bind(R.id.tv_industry_name)
        TextView mTvIndustryName;
        @Bind(R.id.tv_up_down)
        TextView mTvUpDown;
        @Bind(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
