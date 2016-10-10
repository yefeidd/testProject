package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/9/27 0027.
 */

public class CalenderAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private int resId = R.layout.item_calender;
    private List<String> data = new ArrayList<>();

    public CalenderAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resId = resource;
        this.data = objects;
    }

    @NonNull
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

        String day = data.get(position);
        if (!TextUtils.isEmpty(day) && position > 6) {
            if (DateUtils.getCurrentDay() == Integer.valueOf(day)) {
                holder.mTvCalender.setBackgroundColor(mContext.getResources().getColor(R.color.yellow_facc72));
            }
        }
        holder.mTvCalender.setText(day);

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_calender)
        TextView mTvCalender;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
