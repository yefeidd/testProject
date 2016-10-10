package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/4/6 0006.
 */
public class RechargeAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, String>> data = new ArrayList<>();

    public RechargeAdapter(Context context, List<Map<String, String>> data) {
        this.data = data;
        this.mContext = context;
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
            convertView = UIUtil.inflate(R.layout.item_recharge_wealth);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvRechargeItem.setText(data.get(position).get(Constants.TITLE));
        if (data.get(position).get(Constants.INFO).equals("true")) {
            holder.mTvRechargeItem.setBackgroundResource(R.drawable.sp_rect_orange);
            holder.mTvRechargeItem.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.mTvRechargeItem.setBackgroundResource(R.drawable.sp_rect_corner_grey_border);
            holder.mTvRechargeItem.setTextColor(mContext.getResources().getColor(R.color.font_value_black));
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tv_recharge_item)
        TextView mTvRechargeItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
