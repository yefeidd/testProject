package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.HotHyGnModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/8/15 0015.
 */
public class IndustryAdapter extends ArrayAdapter<HotHyGnModel> {
    private Context mContext;
    private List<HotHyGnModel> data;
    private int resId = R.layout.item_gv_hot_stocks;

    public IndustryAdapter(Context context, int resource, List<HotHyGnModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
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
        data.get(position).showHotUi(holder, mContext);
        return convertView;
    }

    public class ViewHolder {
        @Bind(android.R.id.title)
        public TextView mTitle;
        @Bind(R.id.tv_up_down)
        public TextView mTvUpDown;
        @Bind(R.id.tv_name)
        public TextView mTvName;
        @Bind(R.id.tv_price)
        public TextView mTvPrice;
        @Bind(R.id.tv_rate)
        public TextView mTvRate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
