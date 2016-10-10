package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.RefreshRateBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jolly on 2016/7/1 0001.
 */
public class RefreshRateAdapter extends ArrayAdapter<RefreshRateBean> {
    private int resId = android.R.layout.simple_list_item_1;
    private Context mContext;
    private List<RefreshRateBean> list = new ArrayList<>();

    public RefreshRateAdapter(Context context, int resource, List<RefreshRateBean> objects) {
        super(context, resource, objects);
        this.resId = resource;
        this.mContext = context;
        this.list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(resId, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(list.get(position).getTime());
        if (list.get(position).isSelected()) {
            viewHolder.textView.setCompoundDrawables(null, null, mContext.getResources().getDrawable(R.drawable.refresh_selected), null);
            viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.refresh_selected), null);
        } else {
            viewHolder.textView.setCompoundDrawables(null, null, null, null);
            viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        return view;
    }

    public class ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setTextColor(mContext.getResources().getColor(R.color.font_value_black));
            textView.setTextSize(14);
        }
    }
}
